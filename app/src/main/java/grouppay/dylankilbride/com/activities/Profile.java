package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import grouppay.dylankilbride.com.retrofit_interfaces.ProfileAPIInterface;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Users;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class Profile extends AppCompatActivity {

  String userIdStr;
  TextView editFullNameTV, editEmailTV, editPhoneNumberTV, mainEmailTV, mainNameTV;
  RelativeLayout fullNameRL, emailAddressRL, phoneNumberRL;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    userIdStr = getIntent().getStringExtra("userId");

    fullNameRL = (RelativeLayout) findViewById(R.id.profileFullNameRL);
    emailAddressRL = (RelativeLayout) findViewById(R.id.profileEmailAddressRL);
    phoneNumberRL = (RelativeLayout) findViewById(R.id.profilePhoneNumberRL);
    mainEmailTV = (TextView) findViewById(R.id.profileMainEmailAddressTV);
    mainNameTV = (TextView) findViewById(R.id.profileMainFullNameTV);

    getProifleDetails();

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

  public void getProifleDetails() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    ProfileAPIInterface profileAPIInterface = retrofit.create(ProfileAPIInterface.class); //Creates model for JSON
    Call<Users> call = profileAPIInterface.getUserDetails(userIdStr);
    call.enqueue(new Callback<Users>() { //Don't use execute as it will execute on main thread
      @Override
      public void onResponse(Call<Users> call, Response<Users> response) {
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
        }
      }

      @Override
      public void onFailure(Call<Users> call, Throwable t) {
        //TODO Do something here
      }
    });
  }
}
