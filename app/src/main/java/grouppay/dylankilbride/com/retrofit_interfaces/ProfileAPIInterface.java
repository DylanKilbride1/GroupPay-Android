package grouppay.dylankilbride.com.retrofit_interfaces;

import grouppay.dylankilbride.com.models.Users;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ProfileAPIInterface {

  @Headers({"Accept: application/json"})
  @GET("/users/user/{userId}")
  Call<Users> getUserDetails(@Path("userId") String userId);

  @PATCH("/users/user/updateEmail/{userId}")
  Call<Users> updateUserEmail(@Path("userId") String userId, @Body Users user);

  @PATCH("/users/user/updateMobileNumber/{userId}")
  Call<Users> updateUserPhoneNumber(@Path("userId") String userId, @Body Users user);

  @PATCH("/users/user/updateFullName/{userId}")
  Call<Users> updateUserFullName(@Path("userId") String userId, @Body Users user);

}

