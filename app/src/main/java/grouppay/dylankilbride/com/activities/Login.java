package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.retrofit_interfaces.ProfileAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class Login extends AppCompatActivity {
  EditText emailBox, passwordBox;
  TextView invalidCredentials;
  Button loginButton;
  TextView registerLink;
  String URL = LOCALHOST_SERVER_BASEURL + "/users/login";
  private String token;
  private ProfileAPI apiInterface;
  private FirebaseAuth firebaseAuth;
  private View parentLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    Toolbar toolbar = (Toolbar) findViewById(R.id.appBar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");

    emailBox = (EditText) findViewById(R.id.emailBox);
    passwordBox = (EditText) findViewById(R.id.passwordBox);
    loginButton = (Button) findViewById(R.id.loginButton);
    registerLink = (TextView) findViewById(R.id.registerLink);
    invalidCredentials = (TextView) findViewById(R.id.invalidCredentials);
    parentLayout = findViewById(android.R.id.content);

    Intent intent = getIntent();
    String registrationEmail = intent.getStringExtra("registrationEmail");
    String registrationPassword = intent.getStringExtra("registrationPassword");

    emailBox.setText(registrationEmail);
    passwordBox.setText(registrationPassword);

    firebaseAuth = FirebaseAuth.getInstance();
    firebaseAuth.getCurrentUser().reload();

    final RequestQueue loginRequestQueue = Volley.newRequestQueue(Login.this);

    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

          JSONObject loginRequestDetails = new JSONObject();
          try {
            if (firebaseAuth.getCurrentUser().isEmailVerified()) {
              loginRequestDetails.put("email", emailBox.getText().toString());
              loginRequestDetails.put("password", passwordBox.getText().toString());
              loginRequestDetails.put("isVerified", "TRUE");
            } else {
              loginRequestDetails.put("email", emailBox.getText().toString());
              loginRequestDetails.put("password", passwordBox.getText().toString());
              loginRequestDetails.put("isVerified", "FALSE");
            }
          } catch (JSONException e) {
            Log.e("Couldn't create JSON: ", e.toString());
          }

          JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,
              URL,
              loginRequestDetails,
              new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                  try {
                    if (response.get("result").equals("1")) {
                      Intent intent = new Intent(Login.this, Home.class);
                      intent.putExtra("userId", response.get("userId").toString());
                      intent.putExtra("email", emailBox.getText().toString());
                      intent.putExtra("name", response.get("name").toString());
                      intent.putExtra("profileImgUrl", response.get("profileImageUrl").toString());
                      token = response.get("deviceToken").toString();
                      compareDeviceTokens(token);
                      startActivity(intent);
                    } else {
                      ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.incorrectField));
                      ViewCompat.setBackgroundTintList(emailBox, colorStateList);
                      ViewCompat.setBackgroundTintList(passwordBox, colorStateList);
                      invalidCredentials.setVisibility(View.VISIBLE);
                    }
                  } catch (JSONException e) {
                    e.printStackTrace();
                  }
                }
              }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.e("Something: ", error.toString());
            }
          });
          loginRequestQueue.add(loginRequest);
    }
    });


    registerLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(Login.this, Register.class));
      }
    });
  }

  private void compareDeviceTokens(String token)  {
    String tokenFromSharedPrefs = getApplicationContext().getSharedPreferences("_", MODE_PRIVATE).getString("FirebaseDeviceToken", null);
    if (!token.equals(tokenFromSharedPrefs)) {
      Map<String, String> tokenMap = new HashMap<>();
      tokenMap.put("newToken", tokenFromSharedPrefs);
      setUpNewTokenCall(token, tokenMap);
    }
  }

  private void setUpNewTokenCall(String oldToken, Map<String, String> newToken) {
    Retrofit updateTokenRequest = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    apiInterface = updateTokenRequest.create(ProfileAPI.class);
    updateDeviceToken(oldToken, newToken);
  }

  private void updateDeviceToken(String oldToken, Map<String, String> newToken) {
    Call<Void> call = apiInterface.updateUsersDeviceToken(oldToken, newToken);
    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
        if(!response.isSuccessful()) {
          //Handle
        } else {
          Log.e("TOKEN UPDATED", newToken.get("newToken"));
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        Log.e("TOKEN UPDATED", newToken.get("newToken"));
      }
    });
  }

  @Override
  public void onBackPressed() {
    moveTaskToBack(true);
  }
}