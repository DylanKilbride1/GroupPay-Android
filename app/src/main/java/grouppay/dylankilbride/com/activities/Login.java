package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONException;
import org.json.JSONObject;

import grouppay.dylankilbride.com.grouppay.R;

public class Login extends AppCompatActivity {
  EditText emailBox, passwordBox;
  TextView invalidCredentials;
  Button loginButton;
  TextView registerLink;
  String URL = "http://10.0.2.2:8080/users/login";

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

    Intent intent = getIntent();
    String registrationEmail = intent.getStringExtra("registrationEmail");
    String registrationPassword = intent.getStringExtra("registrationPassword");

    emailBox.setText(registrationEmail);
    passwordBox.setText(registrationPassword);

    final RequestQueue loginRequestQueue = Volley.newRequestQueue(Login.this);

    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        JSONObject loginRequestDetails = new JSONObject();
        try {
          loginRequestDetails.put("email", emailBox.getText().toString());
          loginRequestDetails.put("password", passwordBox.getText().toString());
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

  @Override
  public void onBackPressed() {
    moveTaskToBack(true);
  }
}