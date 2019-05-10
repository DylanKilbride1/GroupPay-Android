package grouppay.dylankilbride.com.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.math.BigDecimal;

import androidx.core.content.ContextCompat;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.GroupAccount;
import grouppay.dylankilbride.com.models.ImageUploadResponse;
import grouppay.dylankilbride.com.retrofit_interfaces.GroupAccountAPI;
import grouppay.dylankilbride.com.retrofit_interfaces.ProfileAPI;
import grouppay.dylankilbride.com.text_watchers.CurrencyTextWatcher;
import grouppay.dylankilbride.com.text_watchers.DecimalDigitsInputFilter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class CreateGroupAccountStage1 extends AppCompatActivity {

  Button createStage1AccountBTN;
  GroupAccountAPI apiInterface;
  EditText groupName, groupDescription, amountNeeded;
  ImageView groupImage;
  String userIdStr, groupAccountIdStr;
  String groupAccountPhotoUrl;
  MultipartBody.Part fileToUpload;
  RequestBody filename;
  long userId;
  private static final int GALLERY_REQUEST_CODE = 234;
  RequestOptions noProfileImageDefault = new RequestOptions().error(R.drawable.no_profile_photo);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_group_stage1);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    userIdStr = getIntent().getStringExtra("userId");
    userId = Long.valueOf(userIdStr);

    createStage1AccountBTN = (Button) findViewById(R.id.createGroupAccountStage1ContinueBTN);
    groupName = (EditText) findViewById(R.id.createGroupAccountStage1NameET);
    groupDescription = (EditText) findViewById(R.id.createGroupAccountStage1DescriptionET);
    amountNeeded = (EditText) findViewById(R.id.createGroupAccountStage1AmtNeededET);
    groupImage = (ImageView) findViewById(R.id.createGroupAccountStage1Image);

    groupImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        pickImage();
      }
    });

    createStage1AccountBTN.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Retrofit createBasicAccount = new Retrofit.Builder()
            .baseUrl(LOCALHOST_SERVER_BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        apiInterface = createBasicAccount.create(GroupAccountAPI.class);
        createBasicAccount();
      }
    });

    amountNeeded.addTextChangedListener(new CurrencyTextWatcher(amountNeeded));
    amountNeeded.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
    setAmountMaxLength(18);
  }

  public void setAmountMaxLength(int length) {
    InputFilter[] filterArray = new InputFilter[1];
    filterArray[0] = new InputFilter.LengthFilter(length);
    amountNeeded.setFilters(filterArray);
  }

  private void validateGroupFieldEntries() {
    if (groupName.getText().toString().equals("")) {
    }
  }

  public void pickImage() {
    String[] mimeTypes = {"image/jpeg", "image/png"};
    if (ContextCompat.checkSelfPermission(CreateGroupAccountStage1.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      Toast.makeText(CreateGroupAccountStage1.this, "Permission Denied", Toast.LENGTH_LONG).show();
    } else {
      Intent imageSelection = new Intent(Intent.ACTION_PICK);
      imageSelection.setType("image/*");
      imageSelection.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
      startActivityForResult(imageSelection, GALLERY_REQUEST_CODE);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == Activity.RESULT_OK){
      switch (requestCode) {
        case GALLERY_REQUEST_CODE:
          Uri selectedImage = data.getData();
          String filePath = getRealPathFromURIPath(selectedImage, CreateGroupAccountStage1.this);
          File file = new File(filePath);
          RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
          fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
          filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
          Picasso.get().load(selectedImage).into(groupImage);
      }
    }
  }

  private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
    Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
    if (cursor == null) {
      return contentURI.getPath();
    } else {
      cursor.moveToFirst();
      int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
      return cursor.getString(idx);
    }
  }

  public void setUpImageUploadRequest(MultipartBody.Part file, RequestBody filename){
    Retrofit groupImageUpload = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiInterface = groupImageUpload.create(GroupAccountAPI.class);
    handleImageUploadResponse(file, filename);
  }

  public void handleImageUploadResponse(MultipartBody.Part file, RequestBody filename){
    Call<ImageUploadResponse> fileUpload = apiInterface.uploadGroupProfileImage(groupAccountIdStr, file, filename);
    fileUpload.enqueue(new Callback<ImageUploadResponse>() {
      @Override
      public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
        if(!response.isSuccessful()){
          //Handle
        } else {

        }
      }

      @Override
      public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
        Log.d("Upload Error", "Error " + t.getMessage());
      }
    });
  }

  public void createBasicAccount() {
    GroupAccount groupAccount = new GroupAccount(userId,
        groupName.getText().toString(),
        groupDescription.getText().toString(),
        new BigDecimal(amountNeeded.getText().toString()));

    Call<GroupAccount> call = apiInterface.createBasicAccount(groupAccount);

    call.enqueue(new Callback<GroupAccount>() {
      @Override
      public void onResponse(Call<GroupAccount> call, Response<GroupAccount> response) {
        if(!response.isSuccessful()) {
          //Handle
        } else {
          groupAccountIdStr = String.valueOf(response.body().getGroupAccountId());
          setUpImageUploadRequest(fileToUpload, filename);
          Intent intent = new Intent(CreateGroupAccountStage1.this, CreateGroupAccountStage2.class);
          String groupId = String.valueOf(response.body().getGroupAccountId());
          intent.putExtra("groupAccountId", groupId);
          startActivity(intent);
          finish();
        }
      }

      @Override
      public void onFailure(Call<GroupAccount> call, Throwable t) {

      }
    });
  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.createGroupNameToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_create_group_stage1);
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
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
}
