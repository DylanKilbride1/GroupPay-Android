package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import grouppay.dylankilbride.com.retrofit_interfaces.ProfileAPI;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class ProfileEditPhoneNumber extends AppCompatActivity {

  String userIdStr;
  Button updatePhoneNumber;
  EditText newPhoneNumber;
  ProfileAPI apiInterface;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_edit_phone_number);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    userIdStr = getIntent().getStringExtra("userId");

    updatePhoneNumber = (Button) findViewById(R.id.profileEditPhoneBTN);
    newPhoneNumber = (EditText) findViewById(R.id.profileNewPhoneNumberET);

    updatePhoneNumber.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Retrofit updatePhoneNumberRequest = new Retrofit.Builder()
            .baseUrl(LOCALHOST_SERVER_BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        apiInterface = updatePhoneNumberRequest.create(ProfileAPI.class);

        updatePhoneNumber();
      }
    });
  }

  private void updatePhoneNumber() {
    User user = new User(Long.valueOf(userIdStr), null, null, null, null, newPhoneNumber.getText().toString(), null);

    Call<User> call = apiInterface.updateUserPhoneNumber(userIdStr, user);

    call.enqueue(new Callback<User>() {
      @Override
      public void onResponse(Call<User> call, Response<User> response) {
        if(!response.isSuccessful()) {
          //Handle
        } else {
          Intent intent = new Intent(ProfileEditPhoneNumber.this, Profile.class);
          intent.putExtra("userId", userIdStr);
          startActivity(intent);
          finish();
        }
      }

      @Override
      public void onFailure(Call<User> call, Throwable t) {

      }
    });
  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.editPhoneNumberToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_edit_phone_number_title);
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
