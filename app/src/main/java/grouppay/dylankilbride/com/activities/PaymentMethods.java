package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import grouppay.dylankilbride.com.adapters.PaymentMethodsRVAdapter;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Cards;
import grouppay.dylankilbride.com.retrofit_interfaces.CardManagerAPI;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class PaymentMethods extends AppCompatActivity {

  PaymentMethodsRVAdapter adapter;
  private RecyclerView paymentMethodsRecyclerView;
  private RecyclerView.LayoutManager paymentMethodsRecyclerViewLayoutManager;
  private ImageView noPaymentMethodsImage;
  private TextView noPaymentMethodsText;
  private String userId;
  private CardManagerAPI apiInterface;
  private ArrayList<Cards> paymentMethodsList;
  private ProgressBar loadingPB;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment_methods);

    userId = getIntent().getStringExtra("userIdStr");

    noPaymentMethodsImage = findViewById(R.id.noPaymentMethodsImageView);
    noPaymentMethodsText = findViewById(R.id.noPaymentMethodsTextView);
    loadingPB = findViewById(R.id.loadingPaymentCardsPB);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    paymentMethodsList = new ArrayList<>();

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddPaymentMethod);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent addPaymentMethod = new Intent(PaymentMethods.this, AddPaymentMethod.class);
        addPaymentMethod.putExtra("userIdStr", userId);
        startActivity(addPaymentMethod);
      }
    });
  }

  public void setUpAccountPreviewRecyclerView(List<Cards> cardsList) {
    // set up the RecyclerView
    paymentMethodsRecyclerView = (RecyclerView) findViewById(R.id.paymentMethodsRV);
    paymentMethodsRecyclerViewLayoutManager = new LinearLayoutManager(this);
    paymentMethodsRecyclerView.setLayoutManager(paymentMethodsRecyclerViewLayoutManager);
    paymentMethodsRecyclerView.setAdapter(new PaymentMethodsRVAdapter(cardsList, R.layout.activity_payment_methods_list_item));
    paymentMethodsRecyclerView.addItemDecoration(new DividerItemDecoration(paymentMethodsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.paymentMethodsToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_payment_methods_title);
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }

  private void paymentMethodsRequestSetUp(String userId) {
    loadingPB.setVisibility(View.VISIBLE);
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
              setUpAccountPreviewRecyclerView(paymentMethodsList);
              checkForEmptyPaymentMethodList();
              loadingPB.setVisibility(View.INVISIBLE);
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else {
            paymentMethodsList.clear();
            setUpAccountPreviewRecyclerView(paymentMethodsList);
            checkForEmptyPaymentMethodList();
            loadingPB.setVisibility(View.INVISIBLE);
          }
        }
      }
      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        loadingPB.setVisibility(View.INVISIBLE);
        Toast.makeText(PaymentMethods.this,
            "Sorry, we can't retrieve your cards right now..",
            Toast.LENGTH_SHORT
        ).show();
      }
    });
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

  public void checkForEmptyPaymentMethodList() {
    if (paymentMethodsList.isEmpty()) {
      paymentMethodsRecyclerView.setVisibility(View.GONE);
      noPaymentMethodsText.setVisibility(View.VISIBLE);
      noPaymentMethodsImage.setVisibility(View.VISIBLE);
    } else {
      paymentMethodsRecyclerView.setVisibility(View.VISIBLE);
      noPaymentMethodsText.setVisibility(View.GONE);
      noPaymentMethodsImage.setVisibility(View.GONE);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    paymentMethodsRequestSetUp(userId);
  }
}
