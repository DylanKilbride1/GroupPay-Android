package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ProfileEditFullName extends AppCompatActivity {

  String userIdStr;
  Button updateFullName;
  EditText newFirstName, newLastName;
  ProfileAPIInterface apiInterface;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_edit_name);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    userIdStr = getIntent().getStringExtra("userId");

    updateFullName = (Button) findViewById(R.id.profileEditFullNameBTN);
    newFirstName = (EditText) findViewById(R.id.profileNewFirstNameET);
    newLastName = (EditText) findViewById(R.id.profileNewLastNameET);

    updateFullName.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Retrofit updateEmailRequest = new Retrofit.Builder()
            .baseUrl(LOCALHOST_SERVER_BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        apiInterface = updateEmailRequest.create(ProfileAPIInterface.class);

        updateFullName();
      }
    });
  }

  private void updateFullName() {
    Users user = new Users(Long.valueOf(userIdStr), newFirstName.getText().toString(), newLastName.getText().toString(), null, null, null);

    Call<Users> call = apiInterface.updateUserFullName(userIdStr, user);

    call.enqueue(new Callback<Users>() {
      @Override
      public void onResponse(Call<Users> call, Response<Users> response) {
        if(!response.isSuccessful()) {
          //Handle
        } else {
          Intent intent = new Intent(ProfileEditFullName.this, Profile.class);
          intent.putExtra("userId", userIdStr);
          startActivity(intent);
          finish();
        }
      }

      @Override
      public void onFailure(Call<Users> call, Throwable t) {

      }
    });
  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.editFullNameToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_edit_full_name_title);
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }
}
