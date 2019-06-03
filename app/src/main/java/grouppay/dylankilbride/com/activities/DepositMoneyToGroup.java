package grouppay.dylankilbride.com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.text_watchers.CurrencyTextWatcher;
import grouppay.dylankilbride.com.text_watchers.DecimalDigitsInputFilter;
import grouppay.dylankilbride.com.text_watchers.DepositAmountTextWatcher;

public class DepositMoneyToGroup extends AppCompatActivity {

  String groupAccountId, groupAccountName, userId, activityHeader, amountToDebit;
  EditText amountToPay;
  Button continueBtn;
  TextView depositMoneyToGroupHeader, feeWarning, exceedsAmount, euroSymbol;
  private String numberOfParticipants, totalAmountOwed, totalAmountPaid;
  private long amountOwed, amountPaid;
  private BigDecimal maxDepositAmount, enteredAmount;
  private String feeWarningStringPart1 = "We charge 3.9% + .25c per transaction. To cover our fees, we will debit €";
  private String feeWarningStringPart2 = " from your account.";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_deposit_money_to_group);

    groupAccountId = getIntent().getStringExtra("groupAccountIdStr");
    groupAccountName = getIntent().getStringExtra("groupAccountName");
    userId = getIntent().getStringExtra("userIdStr");
    numberOfParticipants = getIntent().getStringExtra("numberOfParticipants");
    totalAmountOwed = getIntent().getStringExtra("totalOwed");
    totalAmountPaid = getIntent().getStringExtra("totalPaid");

    amountOwed = Long.valueOf(totalAmountOwed);
    amountPaid = Long.valueOf(totalAmountPaid);
    maxDepositAmount = new BigDecimal(amountOwed - amountPaid);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    continueBtn = (Button) findViewById(R.id.depositMoneyToGroupBTN);
    continueBtn.setEnabled(false);
    continueBtn.setAlpha(0.5f);
    depositMoneyToGroupHeader = (TextView) findViewById(R.id.depositMoneyToGroupTitle);
    activityHeader = getResources().getString(R.string.deposit_amount_to_group_header) + " " + groupAccountName;
    depositMoneyToGroupHeader.setText(activityHeader);
    amountToPay = (EditText) findViewById(R.id.depositMoneyToGroupAmountET);
    feeWarning = findViewById(R.id.depositMoneyToGroupFeeTV);
    exceedsAmount = findViewById(R.id.depositMoneyToGroupExceedsAmtTV);
    euroSymbol = findViewById(R.id.depositMoneyToGroupEuroSymbolTV);

    continueBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent enterPaymentDetails = new Intent(DepositMoneyToGroup.this, EnterPaymentMethodDetails.class);
        enterPaymentDetails.putExtra("amountToDebitStr", calculateFee(new BigDecimal(amountToPay.getText().toString().replaceAll("[^\\d\\.]", ""))));
        enterPaymentDetails.putExtra("amountForGroupStr", amountToPay.getText().toString().replaceAll("[^\\d\\.]", ""));
        enterPaymentDetails.putExtra("userIdStr", userId);
        enterPaymentDetails.putExtra("groupAccountIdStr", groupAccountId);
        startActivity(enterPaymentDetails);
        finish();
      }
    });

    feeWarning.setText(feeWarningStringPart1 + "0" + feeWarningStringPart2);
    amountToPay.addTextChangedListener(new DepositAmountTextWatcher(amountToPay, this));
    setAmountMaxLength(18);
    amountToPay.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        if (amountToPay.length() > 0) {
          euroSymbol.setVisibility(View.VISIBLE);
          enteredAmount = new BigDecimal(amountToPay.getText().toString().replaceAll("[^\\d.]", ""));
          if (enteredAmount.compareTo(maxDepositAmount) > 0) {
            exceedsAmount.setVisibility(View.VISIBLE);
            continueBtn.setEnabled(false);
            continueBtn.setAlpha(0.5f);
          } else {
            continueBtn.setEnabled(true);
            continueBtn.setAlpha(1);
            exceedsAmount.setVisibility(View.INVISIBLE);
          }
        } else {
          continueBtn.setEnabled(false);
          continueBtn.setAlpha(0.5f);
          euroSymbol.setVisibility(View.INVISIBLE);
        }
      }
    });
  }

  public void setAmountMaxLength(int length) {
    InputFilter[] filterArray = new InputFilter[1];
    filterArray[0] = new InputFilter.LengthFilter(length);
    amountToPay.setFilters(filterArray);
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

  @Override
  protected void onResume() {
    super.onResume();
    //amountToPay.setText(calculateAmountToPay(numberOfParticipants, totalAmountOwed));
  }

  public String calculateFee(BigDecimal amountForGroup) {
    BigDecimal amountToBeDebited;
    BigDecimal adder = new BigDecimal(0.24375);
    BigDecimal divisor = new BigDecimal(0.96135);
    amountToBeDebited = (amountForGroup.add(adder)).divide(divisor, RoundingMode.HALF_UP);
    return amountToBeDebited.setScale(2, RoundingMode.HALF_UP).toString();
  }
}
