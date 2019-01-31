package grouppay.dylankilbride.com.activities;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import grouppay.dylankilbride.com.adapters.ActiveAccountPaymentLogRVAdapter;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Payments;
import grouppay.dylankilbride.com.models.User;

public class GroupAccountDetailed extends AppCompatActivity {

  ProgressBar paymentProgress;
  ImageView groupImage;
  TextView progressStartAmount, progressFinalAmount;
  private RecyclerView paymentsLogRecyclerView;
  private RecyclerView.LayoutManager paymentsLogRecyclerViewLayoutManager;
  String paymentStartAmountStr = "€0";
  String getPaymentEndAmountStr = "€500";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_active_account_detailed);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    paymentProgress = (ProgressBar) findViewById(R.id.detailedAccountPaymentProgress);
    groupImage = (ImageView) findViewById(R.id.activeAccountDetailedGroupImage);
    progressStartAmount = (TextView) findViewById(R.id.activeAccountProgressStartTV);
    progressFinalAmount = (TextView) findViewById(R.id.activeAccountProgressEndTV);

    progressStartAmount.setText(paymentStartAmountStr);
    progressFinalAmount.setText(getPaymentEndAmountStr);

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

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.detailedAccountsToolbar);
    setSupportActionBar(toolbar);

    String groupName = "Group Account Name";

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
}
