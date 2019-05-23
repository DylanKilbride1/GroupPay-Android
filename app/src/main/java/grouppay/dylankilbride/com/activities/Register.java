package grouppay.dylankilbride.com.activities;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    if(GroupPayMessagingService.getToken(getApplicationContext()) == null) {
      generateFirstFirebaseToken();
    } else {
      tokenFromSharedPrefs = getApplicationContext().getSharedPreferences("_", MODE_PRIVATE).getString("FirebaseDeviceToken", null);
    }

    firstNameBox = (EditText)findViewById(R.id.firstNameBox);
    lastNameBox = (EditText)findViewById(R.id.lastNameBox);
    emailBox = (EditText)findViewById(R.id.emailBox);
    passwordBox = (EditText)findViewById(R.id.passwordBox);
    mobileNumberBox = (EditText)findViewById(R.id.mobileNumberBox);
    registerButton = (Button)findViewById(R.id.registerButton);
    loginLink = (TextView)findViewById(R.id.loginLink);

    requestQueue = Volley.newRequestQueue(this);

    registerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

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
                          intent.putExtra("registrationPassword", passwordBox.getText().toString());
                          startActivity(intent);
                        } else {
                          FirebaseAuthException e = (FirebaseAuthException) task.getException();
                          Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
}

