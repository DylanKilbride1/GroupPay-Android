package grouppay.dylankilbride.com.text_watchers;

import android.text.Editable;
import android.text.TextWatcher;

public class CardNumberTextWatcher implements TextWatcher {

  private static final char SPACE = ' ';

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    // Remove all spacing char
    int pos = 0;
    while (true) {
      if (pos >= s.length()) break;
      if (SPACE == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
        s.delete(pos, pos + 1);
      } else {
        pos++;
      }
    }

    // Insert char where needed.
    pos = 4;
    while (true) {
      if (pos >= s.length()) break;
      final char c = s.charAt(pos);
      // Only if its a digit where there should be a space we insert a space
      if ("0123456789".indexOf(c) >= 0) {
        s.insert(pos, "" + SPACE);
      }
      pos += 5;
    }
  }
}