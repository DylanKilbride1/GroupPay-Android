package grouppay.dylankilbride.com.retrofit_interfaces;

import java.util.List;

import grouppay.dylankilbride.com.models.Contact;
import grouppay.dylankilbride.com.models.GroupAccount;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GroupAccountAPI {

  @PUT("groupAccounts/createBasicAccount")
  Call<GroupAccount> createBasicAccount(@Body GroupAccount groupAccount);

  @PUT("groupAccounts/addParticipants/{groupAccountId}")
  Call<GroupAccount> addContactsToAccount(@Path("groupAccountId") String groupId, @Body List<Contact> contactsList);

  @GET("groupAccounts/getDetailedGroupInfo/{groupAccountId}")
  Call<GroupAccount> getDetailedGroupInfo(@Path("groupAccountId") String groupId);

  @GET("groupAccounts/getAllUserAssociatedAccounts/{userId}")
  Call<List<GroupAccount>> getUserAssociatedAccounts(@Path("userId") String userId);
}
