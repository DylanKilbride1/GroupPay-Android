package grouppay.dylankilbride.com.text_watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;

public class CurrencyTextWatcher implements TextWatcher {

  private DecimalFormat decimalFormatWithDecimal;
  private DecimalFormat decimalFormatWithoutDecimal;
  private boolean hasFractionalPart;
  private EditText amountToPay;
  private int trailingZeroCount;

  public CurrencyTextWatcher(EditText amountToPay) {
    decimalFormatWithDecimal = new DecimalFormat("#,###.##");
    decimalFormatWithDecimal.setDecimalSeparatorAlwaysShown(true);
    decimalFormatWithoutDecimal = new DecimalFormat("#,###");
    this.amountToPay = amountToPay;
    hasFractionalPart = false;
  }

  @SuppressWarnings("unused")
  private static final String TAG = "NumberTextWatcher";

  public void afterTextChanged(Editable editText) {
    amountToPay.removeTextChangedListener(this);

    try {
      int initialLength, endLength;
      initialLength = amountToPay.getText().length();

      String v = editText.toString().replace(String.valueOf(decimalFormatWithDecimal.getDecimalFormatSymbols().getGroupingSeparator()), "");
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

}
