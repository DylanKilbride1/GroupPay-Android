package grouppay.dylankilbride.com.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import grouppay.dylankilbride.com.grouppay.R;

public class DepositMoneyToGroup extends AppCompatActivity {

  String groupAccountId, groupAccountName, userId, activityHeader;
  TextView depositMoneyToGroupHeader;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_deposit_money_to_group);

    groupAccountId = getIntent().getStringExtra("groupAccountIdStr");
    groupAccountName = getIntent().getStringExtra("groupAccountName");
    userId = getIntent().getStringExtra("userIdStr");

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    depositMoneyToGroupHeader = (TextView) findViewById(R.id.depositMoneyToGroupTitle);
    activityHeader = getResources().getString(R.string.deposit_amount_to_group_header) + " " + groupAccountName;
    depositMoneyToGroupHeader.setText(activityHeader);
  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.depositMoneyToGroupToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_deposit_money_to_group_title);
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }

}
