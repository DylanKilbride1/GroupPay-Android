package grouppay.dylankilbride.com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.GroupAccount;
import grouppay.dylankilbride.com.models.VirtualCard;
import grouppay.dylankilbride.com.retrofit_interfaces.GroupAccountAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vinaygaba.creditcardview.CardNumberFormat;
import com.vinaygaba.creditcardview.CreditCardView;

import java.math.BigDecimal;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class VirtualCardDetails extends AppCompatActivity {

  private String groupAccountIdStr, groupAccountName, valueOfCard;
  private GroupAccountAPI apiInterface;
  private CreditCardView groupPaymentDetails;
  private TextView cvcView;
  private String cvcStr = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_virtual_card_details);

    groupAccountIdStr = getIntent().getStringExtra("groupId");
    groupAccountName = getIntent().getStringExtra("groupName");
    valueOfCard = getIntent().getStringExtra("cardValue");

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    groupPaymentDetails = findViewById(R.id.groupPaymentCard);
    groupPaymentDetails.setCardNumberFormat(CardNumberFormat.MASKED_ALL_BUT_LAST_FOUR);

    cvcView = findViewById(R.id.virtualPaymentMethodCVC);

    groupPaymentDetails.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
          cvcView.setText(cvcStr.replace("", " ").trim());
        }
        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
          cvcView.setText("X X X"); //finger was lifted
        }
        return true;
      }
    });
  }

  private void setUpPaymentDetailsCall() {
    Retrofit getPaymentDetails = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiInterface = getPaymentDetails.create(GroupAccountAPI.class);
    getPaymentDetails(groupAccountIdStr); //TODO Change!
  }

  private void getPaymentDetails(final String groupAccountIdStr) {
    Call<VirtualCard> call = apiInterface.getGroupPaymentDetails(groupAccountIdStr);
    call.enqueue(new Callback<VirtualCard>() {
      @Override
      public void onResponse(Call<VirtualCard> call, Response<VirtualCard> response) {
        if (!response.isSuccessful()) {
          //Handle
        } else {
          try {
            groupPaymentDetails.setCardName(groupAccountName);
            groupPaymentDetails.setCardNumber(response.body().getPan());
            groupPaymentDetails.setExpiryDate(response.body().getCardExpiry());
            cvcStr = response.body().getCvv();
          } catch (Exception e) {
            System.out.println("FAIL");
          }
        }
      }
      @Override
      public void onFailure(Call<VirtualCard> call, Throwable t) {

      }
    });
  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.virtualPaymentMethodToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText("Virtual Payment Details");
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    setUpPaymentDetailsCall();
  }
}
