package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import grouppay.dylankilbride.com.adapters.ActiveAccountPaymentLogRVAdapter;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Contact;
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
  String groupAccountIdStr;
  GroupAccountAPI apiInterface;

  GroupAccount intentReceivedGroupAccount = new GroupAccount();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_active_account_detailed);

    groupAccountIdStr = getIntent().getStringExtra("groupAccountId");

    Retrofit getDetailedAccountsInfo = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiInterface = getDetailedAccountsInfo.create(GroupAccountAPI.class);
    getDetailedGroupInfo(groupAccountIdStr);

    setUpActionBar("GroupName");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    paymentProgress = (ProgressBar) findViewById(R.id.detailedAccountPaymentProgress);
    groupImage = (ImageView) findViewById(R.id.activeAccountDetailedGroupImage);
    progressStartAmount = (TextView) findViewById(R.id.activeAccountProgressStartTV);
    progressFinalAmount = (TextView) findViewById(R.id.activeAccountProgressEndTV);

    User userTest = new User(4, "Dylan", "Kilbride", "blah", "blah", "blah");
    Date date = new Date();
    String strDateFormat = "hh:mm";
    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
    String formattedDate = dateFormat.format(date);

    ArrayList<Payments> testpaymentLog = new ArrayList<>();
    testpaymentLog.add(new Payments(userTest, new BigDecimal("30.45"), new Date()));
    testpaymentLog.add(new Payments(userTest, new BigDecimal("20"), new Date()));
    testpaymentLog.add(new Payments(userTest, new BigDecimal("40"), new Date()));
    testpaymentLog.add(new Payments(userTest, new BigDecimal("2"), new Date()));
    testpaymentLog.add(new Payments(userTest, new BigDecimal("40"), new Date()));
    testpaymentLog.add(new Payments(userTest, new BigDecimal("2"), new Date()));

    setUpAccountPreviewRecyclerView(testpaymentLog);
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
          if(response.body().getTotalAmountPaid().compareTo(BigDecimal.ZERO) == 0) {
            String totalAmountPaid = "€" + Integer.toString(roundBigDecimalUp(response.body().getTotalAmountPaid()));
            progressStartAmount.setText(totalAmountPaid);
          } else {
            String totalAmountPaid = "€" + response.body().getTotalAmountPaid().toString();
            progressStartAmount.setText(totalAmountPaid);
          }
          String totalAmountOwed = "€" + response.body().getTotalAmountOwed().stripTrailingZeros().toString();
          progressFinalAmount.setText(totalAmountOwed);
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
}
