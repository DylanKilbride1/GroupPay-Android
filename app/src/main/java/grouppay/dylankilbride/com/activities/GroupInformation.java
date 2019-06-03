package grouppay.dylankilbride.com.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import grouppay.dylankilbride.com.adapters.GroupInfoParticipantsRVAdapter;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.DeletionSuccess;
import grouppay.dylankilbride.com.models.ImageUploadResponse;
import grouppay.dylankilbride.com.models.User;
import grouppay.dylankilbride.com.retrofit_interfaces.GroupAccountAPI;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class GroupInformation extends AppCompatActivity {

  private String groupName, groupAccountId, groupImageUrl, groupDescription, userId;
  private List<User> participantsList = new ArrayList<>();
  private ImageView groupImage;
  private LinearLayout descriptionContainer;
  private TextView description;
  private TextView changeGroupImage, numberOfParticipants;
  private RecyclerView groupParticipantsRv;
  private GroupInfoParticipantsRVAdapter adapter;
  private LinearLayoutManager groupParticipantsRvLayoutManager;
  private GroupAccountAPI apiInterface;
  private RequestBody filename;
  private MultipartBody.Part fileToUpload;
  private static final int GALLERY_REQUEST_CODE = 290;
  private TextView leaveGroup;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_group_information);

    groupName = getIntent().getStringExtra("groupName");
    groupAccountId = getIntent().getStringExtra("groupAccountId");
    groupDescription = getIntent().getStringExtra("groupDescription");
    groupImageUrl = getIntent().getStringExtra("groupImageUrl");
    userId = getIntent().getStringExtra("userId");

    groupImage = findViewById(R.id.groupInfoGroupImage);
    changeGroupImage = findViewById(R.id.groupInfoChangeImage);
    numberOfParticipants = findViewById(R.id.groupInfoNumberOfParticipants);
    changeGroupImage = findViewById(R.id.groupInfoChangeImage);
    leaveGroup = findViewById(R.id.groupInfoLeaveGroupButton);
    descriptionContainer = findViewById(R.id.groupInfoGroupDescriptionContainer);
    description = findViewById(R.id.groupDescriptionTV);

    checkForDescription();

    leaveGroup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        confirmLeaveDialog();
      }
    });

    changeGroupImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(ContextCompat.checkSelfPermission(GroupInformation.this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
          requestStoragePermission();
        }
        pickImage();
      }
    });

    setUpActionBar(groupName);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    loadGroupImage(groupImageUrl);

  }

  private void checkForDescription() {
    if (groupDescription != null) {
      descriptionContainer.setVisibility(View.VISIBLE);
      description.setText(groupDescription);
    }
  }

  public void setUpActionBar(String groupName) {
    Toolbar toolbar = (Toolbar) findViewById(R.id.groupInfoToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(groupName);
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }

  public void loadGroupImage(String imageUrl) {
    Glide.with(getApplicationContext())
        .load(imageUrl)
        .into(groupImage);
  }

  public void setUpGroupParticipantsRecyclerView(List<User> participantsList) {
    // set up the RecyclerView
    groupParticipantsRv = (RecyclerView) findViewById(R.id.groupInfoParticipantsRv);
    groupParticipantsRv.setNestedScrollingEnabled(false);
    groupParticipantsRvLayoutManager = new LinearLayoutManager(this);
    groupParticipantsRv.setLayoutManager(groupParticipantsRvLayoutManager);
    adapter = new GroupInfoParticipantsRVAdapter(participantsList, R.layout.group_participants_list_item, this);
    groupParticipantsRv.setAdapter(adapter);
    groupParticipantsRv.addItemDecoration(new DividerItemDecoration(groupParticipantsRv.getContext(), DividerItemDecoration.VERTICAL));
  }

  public void setUpGroupParticipantsCall(String groupAccountId) {
    Retrofit getGroupParticipants = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiInterface = getGroupParticipants.create(GroupAccountAPI.class);
    getGroupParticipantsCall(groupAccountId);
  }

  public void getGroupParticipantsCall(String groupAccountId){
    Call<List<User>> call = apiInterface.getAllGroupParticipants(groupAccountId);
    call.enqueue(new Callback<List<User>>() {
      @Override
      public void onResponse(Call<List<User>> call, Response<List<User>> response) {
        if(!response.isSuccessful()) {
        } else {
          if (response.body().size() > 0 && !response.body().equals("null")) {
                participantsList.clear();
            for (int i = 0; i < response.body().size(); i++) {
              User groupParticipant = new User(response.body().get(i).getFirstName(),
                  response.body().get(i).getLastName(),
                  response.body().get(i).getEmailAddress(),
                  response.body().get(i).getMobileNumber(),
                  response.body().get(i).getProfileImage());
              participantsList.add(groupParticipant);
            }
            String participantsString = participantsList.size() + " Participants";
            setUpGroupParticipantsRecyclerView(participantsList);
            numberOfParticipants.setText(participantsString);
          }
        }
      }

      @Override
      public void onFailure(Call<List<User>> call, Throwable t) {

      }
    });
  }

  private void requestStoragePermission() {
    if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
      alertDialogBuilder.setTitle("Gallery Access Needed");
      alertDialogBuilder.setMessage("The following permission is needed to allow you to select a group photo from your gallery");

      alertDialogBuilder.setPositiveButton("Cool!", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          ActivityCompat.requestPermissions(GroupInformation.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
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

  public void pickImage() {
    String[] mimeTypes = {"image/jpeg", "image/png"};
    if (ContextCompat.checkSelfPermission(GroupInformation.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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
          String filePath = getRealPathFromURIPath(selectedImage, GroupInformation.this);
          File file = new File(filePath);
          RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
          fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
          filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
          Picasso.get().load(selectedImage).into(groupImage);
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
    Retrofit groupImageUpload = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiInterface = groupImageUpload.create(GroupAccountAPI.class);
    handleImageUploadResponse(file, filename);
  }

  public void handleImageUploadResponse(MultipartBody.Part file, RequestBody filename){
    Call<ImageUploadResponse> fileUpload = apiInterface.uploadGroupProfileImage(groupAccountId, file, filename);
    fileUpload.enqueue(new Callback<ImageUploadResponse>() {
      @Override
      public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
        if(!response.isSuccessful()){
          //Handle
        } else {
          Glide.with(getApplicationContext())
              .load(response.body().getFileUrl())
              .into(groupImage);
        }
      }

      @Override
      public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
        Log.d("Upload Error", "Error " + t.getMessage());
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void confirmLeaveDialog() {
    AlertDialog.Builder adb = new AlertDialog.Builder(this);
    adb.setTitle("Are you sure?");
    adb.setMessage("You will not be refunded any money you have deposited to " + groupName);
    //adb.setIcon(android.R.drawable.);
    adb.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        setUpDeleteParticipantRequest(groupAccountId, userId);
      }
    });
    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {

      }
    });
    AlertDialog alert = adb.create();
    alert.show();
    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
    nbutton.setTextColor(getResources().getColor(R.color.colorAccent));
    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
    pbutton.setTextColor(getResources().getColor(R.color.incorrectField));
  }

  @Override
  protected void onResume() {
    super.onResume();
    setUpGroupParticipantsCall(groupAccountId);
  }

  public void setUpDeleteParticipantRequest(String groupAccountId, String userId){
    Retrofit deleteParticipant = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiInterface = deleteParticipant.create(GroupAccountAPI.class);
    deleteParticipantResponse(groupAccountId, userId);
  }

  public void deleteParticipantResponse(String groupAccountId, String userId){
    Call<DeletionSuccess> participantDelete = apiInterface.deleteUserFromGroup(groupAccountId, userId);
    participantDelete.enqueue(new Callback<DeletionSuccess>() {
      @Override
      public void onResponse(Call<DeletionSuccess> call, Response<DeletionSuccess> response) {
        if(!response.isSuccessful()){
          Toast.makeText(getApplicationContext(), "Oops! Something went wrong..", Toast.LENGTH_SHORT).show();
        } else {
          Intent backToHome = new Intent(getApplicationContext(), Home.class);
          backToHome.putExtra("userId", userId);
          backToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(backToHome);
        }
      }

      @Override
      public void onFailure(Call<DeletionSuccess> call, Throwable t) {
        Toast.makeText(getApplicationContext(), "Oops! Something went wrong..", Toast.LENGTH_SHORT).show();
        Log.d("Upload Error", "Error " + t.getMessage());
      }
    });
  }
}