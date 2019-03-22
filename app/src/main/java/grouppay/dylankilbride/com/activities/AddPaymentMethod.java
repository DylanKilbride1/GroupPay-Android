package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import grouppay.dylankilbride.com.grouppay.R;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class AddPaymentMethod extends AppCompatActivity {

  public final int MY_SCAN_REQUEST_CODE = 1234;
  private EditText cardholderName, cardNumber, expiryDate, cvv;
  private Button addPaymentMethodContinueBTN;
  private String expiryMonth, expiryYear;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_payment_method);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    cardholderName = (EditText) findViewById(R.id.addCardCardholderNameET);
    cardNumber = (EditText) findViewById(R.id.addCardNumberET);
    expiryDate = (EditText) findViewById(R.id.addCardExpiryET);
    cvv = (EditText) findViewById(R.id.addCardCvvET);
    addPaymentMethodContinueBTN = (Button) findViewById(R.id.addPaymentMethodContinueBTN);

   // addTextWatchers(cardNumber, expiryDate, cvv);

    addPaymentMethodContinueBTN.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        parseCardExpiryDate();
      }
    });


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

  private void parseCardExpiryDate() {
    String[] expiryDateSegments = expiryDate.getText().toString().split("/");
    expiryMonth = expiryDateSegments[0];
    expiryYear = "20" + expiryDateSegments[1];
  }

  private void addTextWatchers(EditText cardNumber, EditText expiryDate, EditText cvv) {

  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.addPaymentMethodToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_add_payment_method_title);
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
