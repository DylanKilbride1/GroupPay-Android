package grouppay.dylankilbride.com.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import grouppay.dylankilbride.com.adapters.PaymentMethodsRVAdapter;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Cards;
import grouppay.dylankilbride.com.models.StripeCharge;
import grouppay.dylankilbride.com.models.StripeChargeReceipt;
import grouppay.dylankilbride.com.retrofit_interfaces.CardManagerAPI;
import grouppay.dylankilbride.com.text_watchers.CardExpiryDateTextWatcher;
import grouppay.dylankilbride.com.text_watchers.CardNumberTextWatcher;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;
import static java.security.AccessController.getContext;

public class EnterPaymentMethodDetails extends AppCompatActivity {

  public final int MY_SCAN_REQUEST_CODE = 1234;
  private EditText cardholderName, cardNumber, expiryDate, cvv;
  private Button usePaymentDetailsBTN;
  private TextView selectSavedPaymentMethod;
  private String expiryMonth, expiryYear, amountToDebitStr, amountForGroupStr, userId, groupAccountId;
  private double amountToDebit, amountForGroup;
  private ProgressWheel paymentProgressSpinner;
  private FrameLayout progressOverlay;
  private CardManagerAPI apiInterface;
  private ArrayList<Cards> paymentMethodsList;
  private RecyclerView paymentMethodsRecyclerView;
  private RecyclerView.LayoutManager paymentMethodsRecyclerViewLayoutManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_enter_payment_method_details);

    userId = getIntent().getStringExtra("userIdStr");
    groupAccountId = getIntent().getStringExtra("groupAccountIdStr");
    amountToDebitStr = getIntent().getStringExtra("amountToDebitStr");
    amountForGroupStr = getIntent().getStringExtra("amountForGroupStr");
    amountToDebit = Double.parseDouble(amountToDebitStr);
    amountForGroup = Double.parseDouble(amountForGroupStr);

    paymentMethodsList = new ArrayList<>();

    paymentMethodsRequestSetUp(userId);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    cardholderName = (EditText) findViewById(R.id.enterPaymentMethodDetailsCardholderNameET);
    cardNumber = (EditText) findViewById(R.id.enterPaymentMethodDetailsNumberET);
    expiryDate = (EditText) findViewById(R.id.enterPaymentMethodDetailsExpiryET);
    cvv = (EditText) findViewById(R.id.enterPaymentMethodDetailsCvvET);
    usePaymentDetailsBTN = (Button) findViewById(R.id.enterPaymentMethodDetailsContinueBTN);
    paymentProgressSpinner = (ProgressWheel) findViewById(R.id.progressWheel);
    progressOverlay = (FrameLayout) findViewById(R.id.progress_overlay);
    selectSavedPaymentMethod = findViewById(R.id.enterPaymentDetailsSelectSavedDetails);

    if(checkPaymentMethodsListSize() > 0) {
      selectSavedPaymentMethod.setVisibility(View.VISIBLE);

      selectSavedPaymentMethod.setOnClickListener(view -> {
        showSavedCardsDialog();
      });
    }

    usePaymentDetailsBTN.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        createPaymentCardToken();
      }
    });

    cardNumber.addTextChangedListener(new CardNumberTextWatcher(cardNumber));
    expiryDate.addTextChangedListener(new CardExpiryDateTextWatcher());
  }

  public void setUpSavedCardsRecyclerView(List<Cards> cardsList) {
    // set up the RecyclerView
    paymentMethodsRecyclerView = (RecyclerView) findViewById(R.id.paymentMethodsRV);
    paymentMethodsRecyclerViewLayoutManager = new LinearLayoutManager(this);
    paymentMethodsRecyclerView.setLayoutManager(paymentMethodsRecyclerViewLayoutManager);
    paymentMethodsRecyclerView.setAdapter(new PaymentMethodsRVAdapter(cardsList, R.layout.saved_cards_dialog_list_item));
    paymentMethodsRecyclerView.addItemDecoration(new DividerItemDecoration(paymentMethodsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

  }

  private int checkPaymentMethodsListSize() {
    return paymentMethodsList.size();
  }

  private void createPaymentCardToken(){
    Stripe stripe = new Stripe(this, "pk_test_kwfy65ynBeJFLDiklvYHV2tI00fUxcehhP");
    stripe.createToken(
        createCardFromFieldEntries(),
        new TokenCallback() {
          public void onSuccess(Token token) {
            setUpTokenToServerCall(new StripeCharge(token.getId(), amountForGroup, amountToDebit, userId, groupAccountId));
            startSpinnerOverlay();
          }
          public void onError(Exception error) {
            Toast.makeText(EnterPaymentMethodDetails.this,
                "Error tokenizing card details",
                Toast.LENGTH_SHORT
            ).show();
            Log.e("Stripe Error on Token: ", error.getLocalizedMessage());
          }
        }
    );
  }

  private Card createCardFromFieldEntries() {
    parseCardExpiryDate();
    return new Card(cardNumber.getText().toString(),
            Integer.valueOf(expiryMonth),
            Integer.valueOf(expiryYear),
            cvv.getText().toString());
  }

  private void startSpinnerOverlay() {
    progressOverlay.setBackground(new ColorDrawable(Color.TRANSPARENT));
    progressOverlay.setVisibility(View.VISIBLE);
    paymentProgressSpinner.spin();
    paymentProgressSpinner.setSpinSpeed(0.75f);
  }

  private void stopSpinnerOverlay() {
    progressOverlay.setVisibility(View.INVISIBLE);
    paymentProgressSpinner.stopSpinning();
  }

  private void setUpTokenToServerCall(StripeCharge stripeCharge){
    Retrofit sendToken = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
      apiInterface = sendToken.create(CardManagerAPI.class);
    sendTokenToServer(stripeCharge);
  }

  private void sendTokenToServer(StripeCharge stripeCharge) {
    Call<Void> call;
    call = apiInterface.sendStripeTokenToServer(stripeCharge);
    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        stopSpinnerOverlay();
        if(response.code() == 200) {
          finish();
        } else {
          Toast.makeText(getApplicationContext(), "Foook!", Toast.LENGTH_LONG).show();
          finish();
        }
      }
      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
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

  private void parseCardExpiryDate() {
    String[] expiryDateSegments = expiryDate.getText().toString().split("/"); //App will crash if no / exists
    expiryMonth = expiryDateSegments[0];
    expiryYear = "20" + expiryDateSegments[1];
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
        if (scanResult.isExpiryValid()) {
          cardExpiryResultStr = scanResult.expiryMonth + "/" + scanResult.expiryYear; //TODO Remove this to stop incorrect exp date
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

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.enterPaymentMethodDetailsToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_enter_payment_details_title);
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }

  private void showSavedCardsDialog() {
//    setUpSavedCardsRecyclerView(paymentMethodsList);
    final Dialog dialog = new Dialog(EnterPaymentMethodDetails.this);
    dialog.setContentView(R.layout.saved_cards_dialog);

    LinearLayout card = dialog.findViewById(R.id.savedCardLL);
  }

//  private void optionalCardSavingDialog() {
//    AlertDialog.Builder adb = new AlertDialog.Builder(this);
//    adb.setTitle("Pay and save card details?");
//    adb.setMessage(R.string.save_card_details_dialog);
//    //adb.setIcon(android.R.drawable.);
//    adb.setPositiveButton("Pay and Save", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialog, int which) {
//        parseCardExpiryDate();
//        Card cardToAdd = new Card(cardNumber.getText().toString(),
//            Integer.valueOf(expiryMonth),
//            Integer.valueOf(expiryYear),
//            cvv.getText().toString());
//        if(!cardToAdd.validateCard()) {
//          Toast.makeText(getApplicationContext(), "Card Details Invalid!", Toast.LENGTH_LONG).show();
//        }
//        stripeProcess(cardToAdd, true);
//      }
//    });
//    adb.setNegativeButton("Just Pay", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialog, int which) {
//        parseCardExpiryDate();
//        Card cardToAdd = new Card(cardNumber.getText().toString(),
//            Integer.valueOf(expiryMonth),
//            Integer.valueOf(expiryYear),
//            cvv.getText().toString());
//        if(!cardToAdd.validateCard()) {
//          Toast.makeText(getApplicationContext(), "Card Details Invalid!", Toast.LENGTH_LONG).show();
//        }
//        stripeProcess(cardToAdd, false);
//      }
//    });
//    AlertDialog alert = adb.create();
//    alert.show();
//    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
//    nbutton.setTextColor(getResources().getColor(R.color.colorAccent));
//    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
//    pbutton.setTextColor(getResources().getColor(R.color.colorAccent));
//  }

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

  private void parsePaymentDetails(String stripeCardList) {
    String hiddenDigits = "\u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 ";
    try {
      JSONObject scl = new JSONObject(stripeCardList);
      JSONArray dataArray = scl.getJSONArray("data");
      for(int i = 0; i < dataArray.length(); i++) {
        JSONObject paymentObject = dataArray.getJSONObject(i);
        String protectedCardNumber = hiddenDigits + paymentObject.getString("last4");
        paymentMethodsList.add(new Cards(protectedCardNumber,
            paymentObject.getInt("exp_month"),
            paymentObject.getInt("exp_year"),
            paymentObject.getString("brand")));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void paymentMethodsRequestSetUp(String userId) {
    Retrofit getPaymentMethods = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiInterface = getPaymentMethods.create(CardManagerAPI.class);
    getPaymentMethodsCall(userId); //TODO Change!
  }

  public void getPaymentMethodsCall(String userId) {
    Call<ResponseBody> call = apiInterface.getUsersPaymentMethods(userId);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(!response.isSuccessful()) {
          //Handle
        } else {
          if(!response.body().toString().isEmpty()) {
            paymentMethodsList.clear();
            try {
              parsePaymentDetails(response.body().string());
              setUpSavedCardsRecyclerView(paymentMethodsList);
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else {
            paymentMethodsList.clear();
            setUpSavedCardsRecyclerView(paymentMethodsList);
          }
        }
      }
      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

      }
    });
  }
}
