package grouppay.dylankilbride.com.retrofit_interfaces;

import grouppay.dylankilbride.com.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ProfileAPI {

  @Headers({"Accept: application/json"})
  @GET("/users/user/{userId}")
  Call<User> getUserDetails(@Path("userId") String userId);

  @PATCH("/users/user/updateEmail/{userId}")
  Call<User> updateUserEmail(@Path("userId") String userId, @Body User user);

  @PATCH("/users/user/updateMobileNumber/{userId}")
  Call<User> updateUserPhoneNumber(@Path("userId") String userId, @Body User user);

  @PATCH("/users/user/updateFullName/{userId}")
  Call<User> updateUserFullName(@Path("userId") String userId, @Body User user);

}

