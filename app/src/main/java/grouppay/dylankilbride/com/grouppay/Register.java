package grouppay.dylankilbride.com.grouppay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
  EditText firstNameBox, lastNameBox, emailBox, passwordBox, mobileNumberBox;
  Button registerButton;
  TextView loginLink;
  String URL = "http://10.0.2.2/add";

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

    registerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
          @Override
          public void onResponse(String s) {
            if(s.equals("true")){
              Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
            }
            else{
              Toast.makeText(Register.this, "Can't Register", Toast.LENGTH_LONG).show();
            }
          }
        },new Response.ErrorListener(){
          @Override
          public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(Register.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
          }
        }) {
          @Override
          protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("firstName", firstNameBox.getText().toString());
            parameters.put("lastName", lastNameBox.getText().toString());
            parameters.put("email", emailBox.getText().toString());
            parameters.put("password", passwordBox.getText().toString());
            parameters.put("mobileNumber", mobileNumberBox.getText().toString());
            return parameters;
          }
        };

        RequestQueue rQueue = Volley.newRequestQueue(Register.this);
        rQueue.add(request);
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

