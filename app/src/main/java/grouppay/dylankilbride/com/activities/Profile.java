package grouppay.dylankilbride.com.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import grouppay.dylankilbride.com.models.ImageUploadResponse;
import grouppay.dylankilbride.com.retrofit_interfaces.ProfileAPI;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class Profile extends AppCompatActivity {

  String userIdStr, profileUrl;
  TextView editFullNameTV, editEmailTV, editPhoneNumberTV, mainEmailTV, mainNameTV;
  ImageView profileImage;
  private LinearLayout photoContainer;
  RelativeLayout fullNameRL, emailAddressRL, phoneNumberRL;
  private static final int GALLERY_REQUEST_CODE = 1234;
  ProfileAPI profileAPI;
  RequestOptions noProfileImageDefault = new RequestOptions().error(R.drawable.no_profile_photo);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    userIdStr = getIntent().getStringExtra("userId");

    profileImage = (ImageView) findViewById(R.id.profile_image);
    photoContainer = findViewById(R.id.profilePhotoContainer);
    fullNameRL = (RelativeLayout) findViewById(R.id.profileFullNameRL);
    emailAddressRL = (RelativeLayout) findViewById(R.id.profileEmailAddressRL);
    phoneNumberRL = (RelativeLayout) findViewById(R.id.profilePhoneNumberRL);
    mainEmailTV = (TextView) findViewById(R.id.profileMainEmailAddressTV);
    mainNameTV = (TextView) findViewById(R.id.profileMainFullNameTV);

    photoContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(ContextCompat.checkSelfPermission(Profile.this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
          requestStoragePermission();
        }
        pickImage();
      }
    });

    emailAddressRL.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent editEmail = new Intent(Profile.this, ProfileEditEmail.class);
        editEmail.putExtra("userId", userIdStr);
        startActivity(editEmail);
        finish();
      }
    });

    phoneNumberRL.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent editPhoneNumber = new Intent(Profile.this, ProfileEditPhoneNumber.class);
        editPhoneNumber.putExtra("userId", userIdStr);
        startActivity(editPhoneNumber);
        finish();
      }
    });

    fullNameRL.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent editName = new Intent(Profile.this, ProfileEditFullName.class);
        editName.putExtra("userId", userIdStr);
        startActivity(editName);
        finish();
      }
    });

    //checkCameraPermissions();
  }

  private void requestStoragePermission() {
    if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
      alertDialogBuilder.setTitle("Gallery Access Needed");
      alertDialogBuilder.setMessage("The following permission is needed to allow you to select a profile photo from your gallery");

      alertDialogBuilder.setPositiveButton("Cool!", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          ActivityCompat.requestPermissions(Profile.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        }
      });

      alertDialogBuilder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          dialogInterface.dismiss();
        }
      });

      AlertDialog alert = alertDialogBuilder.create();
      alert.show();

      Button negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
      negativeButton.setTextColor(getResources().getColor(R.color.colorAccent));
      Button positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
      positiveButton.setTextColor(getResources().getColor(R.color.colorAccent));

    } else {
      ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if(requestCode == GALLERY_REQUEST_CODE) {
      if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "Permission granted, sweet!", Toast.LENGTH_SHORT);
      } else {
        Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
      }
    }
  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_profile_title);
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }

  public void pickImage() {
    String[] mimeTypes = {"image/jpeg", "image/png"};
    if (ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
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
          String filePath = getRealPathFromURIPath(selectedImage, Profile.this);
          File file = new File(filePath);
          RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
          MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
          RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
          setUpImageUploadRequest(fileToUpload, filename);
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
    Retrofit profileImageUpload = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    profileAPI = profileImageUpload.create(ProfileAPI.class);
    handleImageUploadResponse(file, filename);
  }

  public void handleImageUploadResponse(MultipartBody.Part file, RequestBody filename){
    Call<ImageUploadResponse> fileUpload = profileAPI.uploadUserProfileImage(userIdStr, file, filename);
    fileUpload.enqueue(new Callback<ImageUploadResponse>() {
      @Override
      public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
        if(!response.isSuccessful()){
          //Handle
        } else {
          profileUrl = response.body().getFileUrl();
          Glide.with(profileImage.getContext())
              .load(response.body().getFileUrl())
              .apply(noProfileImageDefault)
              .into(profileImage);
        }
      }

      @Override
      public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
        Log.d("Upload Error", "Error " + t.getMessage());
      }
    });
  }

  public void getProifleDetails() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    final ProfileAPI profileAPI = retrofit.create(ProfileAPI.class); //Creates model for JSON
    Call<User> call = profileAPI.getUserDetails(userIdStr);
    call.enqueue(new Callback<User>() { //Don't use execute as it will execute on main thread
      @Override
      public void onResponse(Call<User> call, Response<User> response) {
        if (!response.isSuccessful()) {
          //TODO Add error display message for user
          return;
        } else {
          String fullName = response.body().getFirstName() + " " + response.body().getLastName();
          editFullNameTV = (TextView) findViewById(R.id.profileEditFullNameTV);
          editEmailTV = (TextView) findViewById(R.id.profileEditEmailAddressTV);
          editPhoneNumberTV = (TextView) findViewById(R.id.profileEditPhoneNumberTV);
          editFullNameTV.setText(fullName);
          editEmailTV.setText(response.body().getEmailAddress());
          editPhoneNumberTV.setText(response.body().getMobileNumber());
          mainNameTV.setText(response.body().getFullName());
          mainEmailTV.setText(response.body().getEmailAddress());
          profileUrl = response.body().getProfileUrl();
          if(response.body().getProfileUrl() == null) {
            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.no_profile_photo));
          } else {
            Glide.with(profileImage.getContext())
                .load(response.body().getProfileUrl())
                .apply(noProfileImageDefault)
                .into(profileImage);
          }
        }
      }

      @Override
      public void onFailure(Call<User> call, Throwable t) {
        //TODO Do something here
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        Intent backToHome = new Intent(Profile.this, Home.class);
        backToHome.putExtra("profileUrl", profileUrl);
        setResult(RESULT_OK, backToHome);
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    getProifleDetails();
  }

//  private void checkCameraPermissions() {
//    if (ContextCompat.checkSelfPermission(Profile.this,
//        Manifest.permission.CAMERA)
//        != PackageManager.PERMISSION_GRANTED) {
//
//      if (ActivityCompat.shouldShowRequestPermissionRationale(Profile.this,
//          Manifest.permission.CAMERA)) {
//      } else {
//        ActivityCompat.requestPermissions(Profile.this,
//            new String[]{Manifest.permission.CAMERA},
//            CAMERA_PERMISSIONS_REQUEST_CODE);
//
//        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//        // app-defined int constant. The callback method gets the
//        // result of the request.
//      }
//    } else {
//      // Permission has already been granted
//    }
 // }
}