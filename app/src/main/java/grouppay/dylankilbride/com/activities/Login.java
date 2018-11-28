package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import grouppay.dylankilbride.com.grouppay.R;

public class Login extends AppCompatActivity {
  EditText emailBox, passwordBox;
  Button loginButton;
  TextView registerLink;
  String URL = "http://10.0.2.2:8080/login";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    Toolbar toolbar = (Toolbar) findViewById(R.id.appBar);
    setSupportActionBar(toolbar);

    emailBox = (EditText) findViewById(R.id.emailBox);
    passwordBox = (EditText) findViewById(R.id.passwordBox);
    loginButton = (Button) findViewById(R.id.loginButton);
    registerLink = (TextView) findViewById(R.id.registerLink);

    final RequestQueue loginRequestQueue = Volley.newRequestQueue(Login.this);

//    loginButton.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
//          @Override
//          public void onResponse(String s) {
//            if(s.equals("Logged In")){
//              Intent intent = new Intent(Login.this, Home.class);
//              startActivity(intent);
//            }
//            else{
//              Toast.makeText(Login.this, "Incorrect Details", Toast.LENGTH_LONG).show();
//            }
//          }
//        }, new Response.ErrorListener(){
//          @Override
//          public void onErrorResponse(VolleyError volleyError) {
//            Toast.makeText(Login.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
//          }
//        }) {
//          @Override
//          protected Map<String, String> getParams() throws AuthFailureError {
//            Map<String, String> parameters = new HashMap<String, String>();
//            parameters.put("email", emailBox.getText().toString());
//            parameters.put("password", passwordBox.getText().toString());
//            return parameters;
//          }
//        };
//
//        RequestQueue rQueue = Volley.newRequestQueue(Login.this);
//        rQueue.add(request);
//      }
//    });

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
                    startActivity(intent);
                  } else {
                    Toast.makeText(Login.this, "Incorrect Details", Toast.LENGTH_LONG).show();
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
}