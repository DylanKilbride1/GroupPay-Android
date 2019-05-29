package grouppay.dylankilbride.com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.retrofit_interfaces.ProfileAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class VerifyPhoneEmail extends AppCompatActivity {

  private Button verifyPhone, verifyEmail, backToLogin, verifyCodeManually;
  private String phoneNumber, emailAddress;
  private EditText verificationCode;
  private FirebaseAuth firebaseAuth;
  private TextView option, verifyCodePrompt, verificationPurposeText;
  private View parentLayout;
  private String verificationId, countryCode;
  private ProgressBar verificationProgress;
  private ProfileAPI apiInterface;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_verify_phone_email);

    phoneNumber = getIntent().getStringExtra("registrationPhone");
    emailAddress = getIntent().getStringExtra("registrationEmail");
    countryCode = getIntent().getStringExtra("countryCode");

    firebaseAuth = FirebaseAuth.getInstance();

    verifyPhone = findViewById(R.id.verifyAccountPhone);
    verifyEmail = findViewById(R.id.verifyAccountEmail);
    backToLogin = findViewById(R.id.verifyAccountPhoneBackToLogin);
    parentLayout = findViewById(android.R.id.content);
    verificationCode = findViewById(R.id.enterVerificationCodeET);
    verificationPurposeText = findViewById(R.id.verificationPurposeText);
    verifyCodePrompt = findViewById(R.id.verifyPhoneCodePrompt);
    verifyCodeManually = findViewById(R.id.verifyPhoneNumberManually);
    verificationProgress = findViewById(R.id.verificationProgressBar);
    option = findViewById(R.id.verifyOptionText);

    verifyPhone.setOnClickListener(view -> {
      fadeForPhoneVerification();
      sendVerificationCode();
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

    verifyCodeManually.setOnClickListener(view -> {
      String code = verificationCode.getText().toString();
      if(code != null) {
        verifyCode(code);
      }
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

  public void fadeForPhoneVerification() {
    YoYo.with(Techniques.SlideOutLeft)
        .duration(1000)
        .repeat(0)
        .playOn(verifyPhone);
    YoYo.with(Techniques.SlideOutLeft)
        .duration(1000)
        .repeat(0)
        .delay(200)
        .playOn(verifyEmail);
    verificationPurposeText.setVisibility(View.GONE);
    option.setVisibility(View.GONE);
    verifyCodeManually.setVisibility(View.VISIBLE);
    backToLogin.setVisibility(View.GONE);
    YoYo.with(Techniques.FadeOut)
        .duration(1)
        .repeat(0)
        .playOn(verifyCodeManually);
    YoYo.with(Techniques.FadeIn)
        .duration(2000)
        .repeat(0)
        .delay(400)
        .playOn(verifyCodeManually);
    verifyCodePrompt.setVisibility(View.VISIBLE);
    verificationCode.setVisibility(View.VISIBLE);
  }

  private void sendVerificationCode() {
    Toast.makeText(VerifyPhoneEmail.this, "Code Sent!", Toast.LENGTH_SHORT).show();
    PhoneAuthProvider.getInstance().verifyPhoneNumber(countryCode + phoneNumber,
        60,
        TimeUnit.SECONDS,
        TaskExecutors.MAIN_THREAD,
        callback);
  }

  private void verifyCode(String code) {
    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
    signInWithCredential(phoneAuthCredential);
  }

  private void signInWithCredential(PhoneAuthCredential credential) {
    firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              Intent loginActivity = new Intent(VerifyPhoneEmail.this, Login.class);
              loginActivity.putExtra("registrationEmail", emailAddress);
              startActivity(loginActivity);
            } else {
              Toast.makeText(VerifyPhoneEmail.this, "Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
              Log.e("Phone Auth Error:", task.getException().getMessage());
            }
          }
        });
  }

  private PhoneAuthProvider.OnVerificationStateChangedCallbacks callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    @Override
    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
      super.onCodeSent(s, forceResendingToken);
      verificationId = s;
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
      String code = phoneAuthCredential.getSmsCode();
      if (code != null) {
        verificationCode.setText(code);
        verificationProgress.setVisibility(View.VISIBLE);
        verifyCode(code);
      }
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
      Toast.makeText(VerifyPhoneEmail.this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("Phone Auth Error:", e.getMessage());
    }
  };
}