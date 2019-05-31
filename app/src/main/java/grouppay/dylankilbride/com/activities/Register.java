package grouppay.dylankilbride.com.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.service.GroupPayMessagingService;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class Register extends AppCompatActivity {
  EditText firstNameBox, lastNameBox, emailBox, passwordBox, mobileNumberBox;
  Button registerButton;
  TextView loginLink;
  String URL = LOCALHOST_SERVER_BASEURL + "/users/register";
  RequestQueue requestQueue;
  private String token;
  private String tokenFromSharedPrefs;
  private FirebaseAuth firebaseAuth;
  private Spinner countryCodes;
  private ArrayList<String> codes;
  private ArrayList<String> countries;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    if(GroupPayMessagingService.getToken(getApplicationContext()) == null) {
      generateFirstFirebaseToken();
    } else {
      tokenFromSharedPrefs = getApplicationContext().getSharedPreferences("_", MODE_PRIVATE).getString("FirebaseDeviceToken", null);
    }

    codes = new ArrayList<>();
    countries = new ArrayList<>();
    parseCountryCodesJson();

    firstNameBox = (EditText)findViewById(R.id.firstNameBox);
    lastNameBox = (EditText)findViewById(R.id.lastNameBox);
    emailBox = (EditText)findViewById(R.id.emailBox);
    passwordBox = (EditText)findViewById(R.id.passwordBox);
    mobileNumberBox = (EditText)findViewById(R.id.mobileNumberBox);
    registerButton = (Button)findViewById(R.id.registerButton);
    loginLink = (TextView)findViewById(R.id.loginLink);
    countryCodes = findViewById(R.id.registrationCountryCode);

    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_dropdown_item, countries);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    countryCodes.setAdapter(dataAdapter);

    requestQueue = Volley.newRequestQueue(this);

    registerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String countryCodeDigits = codes.get(countryCodes.getSelectedItemPosition());
        String phoneNumber = mobileNumberBox.getText().toString();
        if(phoneNumber.isEmpty() || phoneNumber.length() < 8) {
          mobileNumberBox.setError("Phone number is required");
          mobileNumberBox.requestFocus();
        }

        JSONObject registrationRequestDetails = new JSONObject();
        try {
          registrationRequestDetails.put("email_address", emailBox.getText().toString());
          registrationRequestDetails.put("first_name", firstNameBox.getText().toString());
          registrationRequestDetails.put("last_name", lastNameBox.getText().toString());
          registrationRequestDetails.put("password", passwordBox.getText().toString());
          registrationRequestDetails.put("mobile_number", mobileNumberBox.getText().toString());
          registrationRequestDetails.put("device_token", tokenFromSharedPrefs);
        } catch (JSONException e) {
          Log.e("Couldn't create JSON: ", e.toString());
        }
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
            URL,
            registrationRequestDetails,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  if (response.get("result").equals("registered")) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(emailBox.getText().toString(),
                        passwordBox.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                          Intent intent = new Intent(Register.this, VerifyPhoneEmail.class);
                          intent.putExtra("registrationEmail", emailBox.getText().toString());
                          intent.putExtra("registrationPhone", mobileNumberBox.getText().toString());
                          intent.putExtra("registrationPassword", phoneNumber);
                          intent.putExtra("countryCode", countryCodeDigits);
                          startActivity(intent);
                        } else {
                          Intent backToLoin = new Intent(Register.this, Login.class);
                          backToLoin.putExtra("registrationEmail", emailBox.getText().toString());
                          backToLoin.putExtra("registrationPassword", passwordBox.getText().toString());
                          startActivity(backToLoin);
                        }
                      }
                    });
                  } else {
                    Snackbar.make(findViewById(R.id.registerLinearLayout), "There's already an account with that email",
                        Snackbar.LENGTH_LONG)
                        .show();
                  }
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                Log.e("Rest Error", error.toString());
              }
            }){
        };
        requestQueue.add(objectRequest);
      }
    });

    loginLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(Register.this, Login.class));
      }
    });
  }

  private void generateFirstFirebaseToken() {
    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
      token = instanceIdResult.getToken();
      putTokenInSharedPrefs(token);
    });
  }

  private void putTokenInSharedPrefs(String token) {
    getSharedPreferences("_", MODE_PRIVATE).edit().putString("FirebaseDeviceToken", token).apply();
  }

  private String getCountriesJsonFile() {
    InputStream is = getResources().openRawResource(R.raw.country_codes);
    Writer writer = new StringWriter();
    char[] buffer = new char[1024];
    try {
      Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      int n;
      while ((n = reader.read(buffer)) != -1) {
        writer.write(buffer, 0, n);
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    String jsonString = writer.toString();
    return jsonString;
  }

  private void parseCountryCodesJson() {
    try {
      JSONObject jsonObject = new JSONObject(getCountriesJsonFile());
      JSONArray jsonArray = jsonObject.getJSONArray("countries");
      int size = jsonArray.length();
      for(int i = 0; i < size; i++) {
        JSONObject country = jsonArray.getJSONObject(i);
        codes.add(country.getString("code"));
        countries.add(country.getString("name"));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}

