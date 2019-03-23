package grouppay.dylankilbride.com.retrofit_interfaces;

import grouppay.dylankilbride.com.models.StripeCharge;
import grouppay.dylankilbride.com.models.StripeChargeReceipt;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CardManagerAPI {

  @POST("/payments/charge")
  Call<StripeChargeReceipt> sendStripeTokenToServer(@Body StripeCharge stripeCharge);
}