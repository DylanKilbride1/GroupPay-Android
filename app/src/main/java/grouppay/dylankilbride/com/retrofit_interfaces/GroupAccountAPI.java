package grouppay.dylankilbride.com.retrofit_interfaces;

import java.util.List;

import grouppay.dylankilbride.com.models.DeletionSuccess;
import grouppay.dylankilbride.com.models.GroupAccount;
import grouppay.dylankilbride.com.models.ImageUploadResponse;
import grouppay.dylankilbride.com.models.Transaction;
import grouppay.dylankilbride.com.models.User;
import grouppay.dylankilbride.com.models.VirtualCard;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface GroupAccountAPI {

  @POST("groupAccounts/createBasicAccount")
  Call<GroupAccount> createBasicAccount(@Body GroupAccount groupAccount);

  @PUT("groupAccounts/addParticipants/{groupAccountId}")
  Call<GroupAccount> addContactsToAccount(@Path("groupAccountId") String groupId, @Body List<User> contactsList);

  @GET("groupAccounts/getDetailedGroupInfo/{groupAccountId}")
  Call<GroupAccount> getDetailedGroupInfo(@Path("groupAccountId") String groupId);

  @GET("groupAccounts/getAllUserAssociatedAccounts/{userId}")
  Call<List<GroupAccount>> getUserAssociatedAccounts(@Path("userId") String userId);

  @POST("groupAccounts/getAllContactsWithGrouppayAccounts")
  Call<List<User>> getAllContactsWithGrouppayAccounts(@Body List<String> contactsPhoneNumbers);

  @GET("groupAccounts/getGroupTransactions/{groupAccountId}")
  Call<List<Transaction>> getAllAccountTransactions(@Path("groupAccountId") String groupAccountId);

  @Multipart
  @POST("groupAccounts/uploadGroupProfileImage/{groupAccountId}")
  Call<ImageUploadResponse> uploadGroupProfileImage(@Path("groupAccountId") String groupAccountId,
                                                   @Part MultipartBody.Part file,
                                                   @Part("name") RequestBody name);

  @GET("groupAccounts/getAllUsersInGroup/{groupAccountId}")
  Call<List<User>> getAllGroupParticipants(@Path("groupAccountId") String groupAccountId);

  @DELETE("groupAccounts/deleteUserFromGroup/{groupAccountId}/{userId}")
  Call<DeletionSuccess> deleteUserFromGroup(@Path("groupAccountId") String groupAccountId,
                                            @Path("userId") String userId);

  @GET("groupAccounts/getVirtualCardDetails/{groupAccountId}")
  Call<VirtualCard> getGroupPaymentDetails(@Path("groupAccountId") String groupAccountId);
}
