package grouppay.dylankilbride.com.retrofit_interfaces;

import java.util.List;

import grouppay.dylankilbride.com.models.Transaction;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TransactonAPI {

  @GET("/transactions/getUsersTransactionHistory/{userId}")
  Call<List<Transaction>> getUsersTransactionHistory(@Path("userId") String userId);
}
