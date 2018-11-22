package grouppay.dylankilbride.com.activities;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import grouppay.dylankilbride.com.grouppay.R;

public class Register extends AppCompatActivity {
  EditText firstNameBox, lastNameBox, emailBox, passwordBox, mobileNumberBox;
  Button registerButton;
  TextView loginLink;
  String URL = "http://10.0.2.2:8080/register";
  RequestQueue requestQueue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    firstNameBox = (EditText)findViewById(R.id.firstNameBox);
    lastNameBox = (EditText)findViewById(R.id.lastNameBox);
    emailBox = (EditText)findViewById(R.id.emailBox);
    passwordBox = (EditText)findViewById(R.id.passwordBox);
    mobileNumberBox = (EditText)findViewById(R.id.mobileNumberBox);
    registerButton = (Button)findViewById(R.id.registerButton);
    loginLink = (TextView)findViewById(R.id.loginLink);

    final String firstName = firstNameBox.getText().toString();

    requestQueue = Volley.newRequestQueue(this);


    registerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        StringRequest objectRequest = new StringRequest(Request.Method.POST, URL,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                Log.e("Server Response", response);
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                Log.e("Rest Error", error.toString());
              }
            }){
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("email_address", emailBox.getText().toString());
            params.put("first_name", firstNameBox.getText().toString());
            params.put("last_name", lastNameBox.getText().toString());
            params.put("password", passwordBox.getText().toString());
            params.put("mobile_number", mobileNumberBox.getText().toString());

            return params;
          }
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
}

