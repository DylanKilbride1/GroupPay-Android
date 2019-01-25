package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import grouppay.dylankilbride.com.grouppay.R;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class AddPaymentMethod extends AppCompatActivity {

  public final int MY_SCAN_REQUEST_CODE = 1234;
  private EditText cardholderName, cardNumber, expiryDate, cvv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_payment_method);

    cardholderName = (EditText) findViewById(R.id.addCardCardholderNameET);
    cardNumber = (EditText) findViewById(R.id.addCardNumberET);
    expiryDate = (EditText) findViewById(R.id.addCardExpiryET);
    cvv = (EditText) findViewById(R.id.addCardCvvET);
  }

  public void onScanPress(View v) {
    Intent scanIntent = new Intent(this, CardIOActivity.class);

    // customize these values to suit your needs.
    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

    // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
    startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == MY_SCAN_REQUEST_CODE) {
      String cardNumberResultStr, cardExpiryResultStr;
      if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
        CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

        // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
        cardNumberResultStr = scanResult.getRedactedCardNumber();

        //TODO Perform Luhn Algorithm check here

        cardNumber.setText(cardNumberResultStr);
        if(scanResult.getCardType().toString().equals("American Express")) {
          cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_amex_icon, 0, 0, 0);
        } else if(scanResult.getCardType().toString().equals("Discover")) {
          cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_discover_icon, 0, 0, 0);
        } else if(scanResult.getCardType().toString().equals("Visa")) {
          cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_visa_icon, 0, 0, 0);
        } else if(scanResult.getCardType().toString().equals("MasterCard")) {
          cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_mastercard_icon, 0, 0, 0);
        } else if(scanResult.getCardType().toString().equals("Maestro")) {
          cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_maestro_icon, 0, 0, 0);
        } else {
          cardNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_card_cardtype_generic_icon, 0, 0, 0);
        }

        if (scanResult.isExpiryValid()) {
          cardExpiryResultStr = scanResult.expiryMonth + "/" + scanResult.expiryYear;
          expiryDate.setText(cardExpiryResultStr);
        }

        if (scanResult.cvv != null) {
          // Never log or display a CVV
          //resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
        }

        if (scanResult.postalCode != null) {
          //resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
        }
      }
      else {
        //resultDisplayStr = "Scan was canceled.";
      }
      // do something with resultDisplayStr, maybe display it in a textView
      // resultTextView.setText(resultDisplayStr);
    }
    // else handle other activity results
  }
}
