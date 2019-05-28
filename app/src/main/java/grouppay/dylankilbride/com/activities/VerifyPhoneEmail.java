package grouppay.dylankilbride.com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import grouppay.dylankilbride.com.grouppay.R;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyPhoneEmail extends AppCompatActivity {

  private Button verifyPhone, verifyEmail, backToLogin;
  private String phoneNumber, emailAddress;
  private FirebaseAuth firebaseAuth;
  private TextView option;
  private View parentLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_verify_phone_email);

    phoneNumber = getIntent().getStringExtra("registrationPhone");
    emailAddress = getIntent().getStringExtra("registrationEmail");

    verifyPhone = findViewById(R.id.verifyAccountPhone);
    verifyEmail = findViewById(R.id.verifyAccountEmail);
    backToLogin = findViewById(R.id.verifyAccountPhoneBackToLogin);
    parentLayout = findViewById(android.R.id.content);
    option = findViewById(R.id.verifyOptionText);

    verifyPhone.setOnClickListener(view -> {

    });

    verifyEmail.setOnClickListener(view -> {

      firebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(task -> {
          if(task.isSuccessful()) {
            String emailSent = "An email has been sent to " + emailAddress;
            Snackbar.make(parentLayout, emailSent, Snackbar.LENGTH_LONG).show();
          }
      });
      fadeForEmailVerification(view);
    });

    backToLogin.setOnClickListener(view -> {
      Intent login = new Intent(VerifyPhoneEmail.this, Login.class);
      login.putExtra("registrationEmail", emailAddress);
      startActivity(login);
    });
  }

  public void fadeForEmailVerification(View view) {
    YoYo.with(Techniques.SlideOutLeft)
        .duration(1000)
        .repeat(0)
        .playOn(verifyPhone);
    YoYo.with(Techniques.SlideOutLeft)
        .duration(1000)
        .repeat(0)
        .delay(200)
        .playOn(verifyEmail);
    option.setTextSize(15l);
    option.setText("Click on the link that was sent to your email address to verify your account");
    YoYo.with(Techniques.FadeOut)
        .duration(1)
        .repeat(0)
        .playOn(backToLogin);
    YoYo.with(Techniques.FadeIn)
        .duration(2000)
        .repeat(0)
        .delay(400)
        .playOn(backToLogin);
  }
}
