package grouppay.dylankilbride.com.text_watchers;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

import grouppay.dylankilbride.com.grouppay.R;

public class DepositAmountTextWatcher implements TextWatcher {

  private DecimalFormat decimalFormatWithDecimal;
  private DecimalFormat decimalFormatWithoutDecimal;
  private boolean hasFractionalPart;
  private EditText amountToPay;
  private TextView feeWarning;
  private Context context;
  private int trailingZeroCount;
  private String feeWarningStringPart1 = "GroupPay charges 3.9% + .25c per transaction. To cover our fees, we will debit €";
  private String feeWarningStringPart2 = " from your account.";

  public DepositAmountTextWatcher(EditText amountToPay, Context context) {
    decimalFormatWithDecimal = new DecimalFormat("#,###.##");
    decimalFormatWithDecimal.setDecimalSeparatorAlwaysShown(true);
    decimalFormatWithoutDecimal = new DecimalFormat("#,###");
    this.amountToPay = amountToPay;
    this.context = context;
    hasFractionalPart = false;
  }

  @SuppressWarnings("unused")
  private static final String TAG = "NumberTextWatcher";

  public void afterTextChanged(Editable inputtedValue) {
    amountToPay.removeTextChangedListener(this);
    feeWarning = (TextView) ((Activity) context).findViewById(R.id.depositMoneyToGroupFeeTV);

    try {
      String cleanAmount = amountToPay.getText().toString().replaceAll("[^\\d.]", "");
      if ((Double.parseDouble(cleanAmount)) > 0) {
        feeWarning.setText(feeWarningStringPart1 + calculateFee(new BigDecimal((Double.parseDouble(cleanAmount)))) + feeWarningStringPart2);
      }
    } catch (Exception e) {
      feeWarning.setText(feeWarningStringPart1 + "0" + feeWarningStringPart2);
    }
    try {
      int initialLength, endLength;
      initialLength = amountToPay.getText().length();

      String v = inputtedValue.toString().replace(String.valueOf(decimalFormatWithDecimal.getDecimalFormatSymbols().getGroupingSeparator()), "");
      Number n = decimalFormatWithDecimal.parse(v);
      int cp = amountToPay.getSelectionStart();
      if (hasFractionalPart) {
        //amountToPay.setText(decimalFormatWithDecimal.format(n));
        StringBuilder trailingZeros = new StringBuilder();
        while (trailingZeroCount-- > 0) {
          trailingZeros.append('0');
          amountToPay.setText(decimalFormatWithDecimal.format(n) + trailingZeros.toString());
        }
      } else {
        amountToPay.setText(decimalFormatWithoutDecimal.format(n));
      }
      endLength = amountToPay.getText().length();
      int sel = (cp + (endLength - initialLength));
      if (sel > 0 && sel <= amountToPay.getText().length()) {
        amountToPay.setSelection(sel);
      } else {
        amountToPay.setSelection(amountToPay.getText().length() - 1);
      }
    } catch (NumberFormatException numberFormatException) {

    } catch (ParseException parseException) {

    }

    amountToPay.addTextChangedListener(this);
  }

  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  public void onTextChanged(CharSequence s, int start, int before, int count) {

    int index = s.toString().indexOf(String.valueOf(decimalFormatWithDecimal.getDecimalFormatSymbols().getDecimalSeparator()));
    trailingZeroCount = 0;
    if (index > -1) {
      for (index++; index < s.length(); index++) {
        if (s.charAt(index) == '0') {
          trailingZeroCount++;
        } else {
          trailingZeroCount = 0;
        }
      }
      hasFractionalPart = true;
    } else {
      hasFractionalPart = false;
    }
  }

  public String calculateFee(BigDecimal amountForGroup) {
    BigDecimal amountToBeDebited;
    BigDecimal adder = new BigDecimal(0.24375);
    BigDecimal divisor = new BigDecimal(0.96135);
    amountToBeDebited = (amountForGroup.add(adder)).divide(divisor, RoundingMode.HALF_UP);
    return amountToBeDebited.setScale(2, RoundingMode.HALF_UP).toString();
  }
}