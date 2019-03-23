package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import grouppay.dylankilbride.com.adapters.ActiveAccountPaymentLogRVAdapter;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.GroupAccount;
import grouppay.dylankilbride.com.models.Payments;
import grouppay.dylankilbride.com.models.User;
import grouppay.dylankilbride.com.retrofit_interfaces.GroupAccountAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class GroupAccountDetailed extends AppCompatActivity {

  ProgressBar paymentProgress;
  ImageView groupImage;
  TextView progressStartAmount, progressFinalAmount;
  private RecyclerView paymentsLogRecyclerView;
  private RecyclerView.LayoutManager paymentsLogRecyclerViewLayoutManager;
  String groupAccountIdStr, userIdStr, groupAccountName;
  GroupAccountAPI apiInterface;

  GroupAccount intentReceivedGroupAccount = new GroupAccount();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_active_account_detailed);

    groupAccountIdStr = getIntent().getStringExtra("groupAccountId");
    userIdStr = getIntent().getStringExtra("userIdStr");

    setUpActionBar("GroupName");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    paymentProgress = (ProgressBar) findViewById(R.id.detailedAccountPaymentProgress);
    groupImage = (ImageView) findViewById(R.id.activeAccountDetailedGroupImage);
    progressStartAmount = (TextView) findViewById(R.id.activeAccountProgressStartTV);
    progressFinalAmount = (TextView) findViewById(R.id.activeAccountProgressEndTV);

    User userTest = new User(4, "Dylan", "Kilbride", "blah", "blah", "blah", null);
    Calendar calendar = Calendar.getInstance();

    ArrayList<Payments> testpaymentLog = new ArrayList<>();
    testpaymentLog.add(new Payments(userTest, new BigDecimal("30.45"), calendar));
    testpaymentLog.add(new Payments(userTest, new BigDecimal("20"), calendar));
    testpaymentLog.add(new Payments(userTest, new BigDecimal("40"), calendar));
    testpaymentLog.add(new Payments(userTest, new BigDecimal("2"), calendar));
    testpaymentLog.add(new Payments(userTest, new BigDecimal("40"), calendar));
    testpaymentLog.add(new Payments(userTest, new BigDecimal("2"), calendar));

    setUpAccountPreviewRecyclerView(testpaymentLog);
    setUpFAB();
  }

  private void setUpFAB() {
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabDepositMoney);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(GroupAccountDetailed.this, DepositMoneyToGroup.class);
        intent.putExtra("userIdStr", userIdStr);
        intent.putExtra("groupAccountIdStr", groupAccountIdStr);
        intent.putExtra("groupAccountName", groupAccountName);
        startActivity(intent);
      }
    });
  }

  private void getInfoRequestSetUp() {
    Retrofit getDetailedAccountsInfo = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiInterface = getDetailedAccountsInfo.create(GroupAccountAPI.class);
    getDetailedGroupInfo(groupAccountIdStr);
  }

  public void setUpAccountPreviewRecyclerView(List<Payments> tempList) {
    // set up the RecyclerView
    paymentsLogRecyclerView = (RecyclerView) findViewById(R.id.groupAccountsPaymentLogRV);
    paymentsLogRecyclerViewLayoutManager = new LinearLayoutManager(this);
    paymentsLogRecyclerView.setLayoutManager(paymentsLogRecyclerViewLayoutManager);
    paymentsLogRecyclerView.setAdapter(new ActiveAccountPaymentLogRVAdapter(tempList, R.layout.activity_active_account_detailed_payment_log));
    paymentsLogRecyclerView.addItemDecoration(new DividerItemDecoration(paymentsLogRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
  }

  public void setUpActionBar(String groupName) {
    Toolbar toolbar = (Toolbar) findViewById(R.id.detailedAccountsToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(groupName);
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  public void getDetailedGroupInfo(String groupAccountIdStr) {
    Call<GroupAccount> call = apiInterface.getDetailedGroupInfo(groupAccountIdStr);
    call.enqueue(new Callback<GroupAccount>() {
      @Override
      public void onResponse(Call<GroupAccount> call, Response<GroupAccount> response) {
        if(!response.isSuccessful()) {
          //Handle
        } else {
          paymentProgress.setMax(roundBigDecimalUp(response.body().getTotalAmountOwed()));
          paymentProgress.setProgress(roundBigDecimalUp(response.body().getTotalAmountPaid()));
          if(response.body().getTotalAmountPaid().compareTo(BigDecimal.ZERO) == 0) {
            String totalAmountPaid = "€" + Integer.toString(roundBigDecimalUp(response.body().getTotalAmountPaid()));
            progressStartAmount.setText(totalAmountPaid);
          } else {
            String totalAmountPaid = "€" + response.body().getTotalAmountPaid().stripTrailingZeros().toPlainString();
            progressStartAmount.setText(totalAmountPaid);
          }
          String totalAmountOwed = "€" + response.body().getTotalAmountOwed().stripTrailingZeros().toPlainString();
          progressFinalAmount.setText(totalAmountOwed);
          groupAccountName = response.body().getAccountName();
        }
      }
      @Override
      public void onFailure(Call<GroupAccount> call, Throwable t) {

      }
    });
  }

  public int roundBigDecimalUp(BigDecimal amount){
    BigDecimal roundedBigDecimal = amount.setScale(0, RoundingMode.CEILING);
    return roundedBigDecimal.intValueExact();
  }

  @Override
  protected void onResume() {
    super.onResume();
    getInfoRequestSetUp();
  }
}
