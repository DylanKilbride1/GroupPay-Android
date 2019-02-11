package grouppay.dylankilbride.com.retrofit_interfaces;

import grouppay.dylankilbride.com.models.GroupAccount;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface GroupAccountAPI {

  @PUT("groupAccounts/createBasicAccount")
  Call<GroupAccount> createBasicAccount(@Body GroupAccount groupAccount);
}
