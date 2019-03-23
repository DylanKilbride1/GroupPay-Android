package grouppay.dylankilbride.com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import grouppay.dylankilbride.com.grouppay.R;

public class DepositMoneyToGroup extends AppCompatActivity {

  String groupAccountId, groupAccountName, userId, activityHeader;
  EditText amountToPay;
  Button continueBtn;
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

    continueBtn = (Button) findViewById(R.id.depositMoneyToGroupBTN);
    depositMoneyToGroupHeader = (TextView) findViewById(R.id.depositMoneyToGroupTitle);
    activityHeader = getResources().getString(R.string.deposit_amount_to_group_header) + " " + groupAccountName;
    depositMoneyToGroupHeader.setText(activityHeader);
    amountToPay = (EditText) findViewById(R.id.depositMoneyToGroupAmountET);

    continueBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent enterPaymentDetails = new Intent(DepositMoneyToGroup.this, EnterPaymentMethodDetails.class);
        enterPaymentDetails.putExtra("amountToDepositStr", amountToPay.getText().toString());
        enterPaymentDetails.putExtra("userIdStr", userId);
        enterPaymentDetails.putExtra("groupAccountIdStr", groupAccountId);
        startActivity(enterPaymentDetails);
      }
    });
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
