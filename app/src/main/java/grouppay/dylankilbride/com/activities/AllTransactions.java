package grouppay.dylankilbride.com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import grouppay.dylankilbride.com.adapters.ActiveAccountsRVAdapter;
import grouppay.dylankilbride.com.adapters.AllTransactionsRVAdapter;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.GroupAccount;
import grouppay.dylankilbride.com.models.Transaction;
import grouppay.dylankilbride.com.models.User;
import grouppay.dylankilbride.com.retrofit_interfaces.TransactonAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class AllTransactions extends AppCompatActivity {

  private String userIdStr;
  private String numberOfActiveGroupsStr;
  private TransactonAPI apiInterface;
  ArrayList<Transaction> transactionsList;
  private AllTransactionsRVAdapter adapter;
  private RecyclerView transactionHistoryRecylerView;
  private RecyclerView.LayoutManager transactionHistoryLayoutManager;
  private TextView numberOfActiveGroups, totalAmountDeposited;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_all_transactions);

    userIdStr = getIntent().getStringExtra("userIdStr");
    numberOfActiveGroupsStr = getIntent().getStringExtra("numberOfGroups");

    transactionsList = new ArrayList<>();

    numberOfActiveGroups = (TextView) findViewById(R.id.allTransactionsActiveGroupsTV);
    totalAmountDeposited = (TextView) findViewById(R.id.allTransactionsTotalDepositedTV);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

  public void setUpTransactionHistoryRecyclerView() {
    // set up the RecyclerView
    transactionHistoryRecylerView = (RecyclerView) findViewById(R.id.allTransactionsRV);
    transactionHistoryLayoutManager = new LinearLayoutManager(this);
    transactionHistoryRecylerView.setLayoutManager(transactionHistoryLayoutManager);
    adapter = new AllTransactionsRVAdapter(transactionsList, R.layout.activity_all_transactions_list_item, this);
    transactionHistoryRecylerView.setAdapter(adapter);
    transactionHistoryRecylerView.addItemDecoration(new DividerItemDecoration(transactionHistoryRecylerView.getContext(), DividerItemDecoration.VERTICAL));
  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.allTransactionsToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_all_transactions_title);
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }

  public void populateActiveGroupLL() {
    String activeAccounts = numberOfActiveGroupsStr + " Group(s)";
    numberOfActiveGroups.setText(activeAccounts);
  }

  public void populateAmountDepositedLL() {
    BigDecimal total = new BigDecimal(0);
    for(Transaction transactions: transactionsList){
      total = total.add(transactions.getAmountPaid());
    }
    String totalAmountSpent = "€" + total;
    totalAmountDeposited.setText(totalAmountSpent);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        Intent backToHome = new Intent(AllTransactions.this, Home.class);
        backToHome.putExtra("userIdStr", userIdStr);
        setResult(RESULT_OK, backToHome);
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void setUpTransactionHistoryCall(String userId) {
    Retrofit getUserTransactionHistory = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiInterface = getUserTransactionHistory.create(TransactonAPI.class);
    getUserTransactionHistory(userId);
  }

  public void getUserTransactionHistory(String userId){
    Call<List<Transaction>> call = apiInterface.getUsersTransactionHistory(userId);
    call.enqueue(new Callback<List<Transaction>>() {
      @Override
      public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
        if(!response.isSuccessful()) {
        } else {
          transactionsList.clear();
          if (response.body().size() > 0 && !response.body().equals("null")) {
            for (int i = 0; i < response.body().size(); i++) {
              transactionsList.add(new Transaction(response.body().get(i).getAmountPaid(),
                  response.body().get(i).getPaymentDateAndTime(),
                  response.body().get(i).getGroupName(),
                  response.body().get(i).getTransactionOwner()));
            }
          }
          Collections.reverse(transactionsList);
          setUpTransactionHistoryRecyclerView();
          populateAmountDepositedLL();
        }
      }

      @Override
      public void onFailure(Call<List<Transaction>> call, Throwable t) {

      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    setUpTransactionHistoryCall(userIdStr);
    populateActiveGroupLL();
  }
}