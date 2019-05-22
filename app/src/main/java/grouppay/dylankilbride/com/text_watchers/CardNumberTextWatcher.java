package grouppay.dylankilbride.com.text_watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import grouppay.dylankilbride.com.grouppay.R;

public class CardNumberTextWatcher implements TextWatcher{

  private static final char SPACE = ' ';
  private ArrayList<String> patterns = new ArrayList<>();
  private EditText cardNumber;
  String ptVisa = "^4[0-9]{6,}$";
  String ptMasterCard = "^5[1-5][0-9]{5,}$";
  String ptAmeExp = "^3[47][0-9]{5,}$";
  String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";

  public CardNumberTextWatcher(EditText numberField) {
    patterns.add(ptVisa);
    patterns.add(ptMasterCard);
    patterns.add(ptAmeExp);
    patterns.add(ptDiscover);
    this.cardNumber = numberField;
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    String ccNum = s.toString();
    String ccNumNoSpaces = ccNum.replaceAll("\\s", "");
      for(String p : patterns){
        if(ccNumNoSpaces.matches(p)){
          if(p.equals(ptAmeExp)) {
            cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_amex_icon, 0, 0, 0);
          } else if(p.equals(ptDiscover)) {
            cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_discover_icon, 0, 0, 0);
          } else if(p.equals(ptVisa)) {
            cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_visa_icon, 0, 0, 0);
          } else if(p.equals(ptMasterCard)) {
            cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_mastercard_icon, 0, 0, 0);
          } else {
            cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_generic_icon, 0, 0, 0);
          }
        }
      }

    int pos = 0;
    while (true) {
      if (pos >= s.length()) break;
      if (SPACE == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
        s.delete(pos, pos + 1);
      } else {
        pos++;
      }
    }

    pos = 4;
    while (true) {
      if (pos >= s.length()) break;
      final char c = s.charAt(pos);
      if ("0123456789".indexOf(c) >= 0) {
        s.insert(pos, "" + SPACE);
      }
      pos += 5;
    }
  }
}