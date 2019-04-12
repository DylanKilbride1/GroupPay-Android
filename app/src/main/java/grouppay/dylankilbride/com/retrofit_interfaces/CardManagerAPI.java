package grouppay.dylankilbride.com.retrofit_interfaces;



import grouppay.dylankilbride.com.models.StripeCharge;
import grouppay.dylankilbride.com.models.StripeChargeReceipt;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CardManagerAPI {

  @POST("/payments/charge")
  Call<StripeChargeReceipt> sendStripeTokenToServer(@Body StripeCharge stripeCharge);

  @POST("/payments/charge/save")
  Call<StripeChargeReceipt> sendStripeTokenToServerAndSave(@Body StripeCharge stripeCharge);

  @GET("/users/user/getAllPaymentMethods/{userId}")
  Call<ResponseBody> getUsersPaymentMethods(@Path("userId") String userId);
}