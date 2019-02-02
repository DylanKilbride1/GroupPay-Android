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
import android.widget.GridLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import grouppay.dylankilbride.com.constants.Constants;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.GroupAccount;
import grouppay.dylankilbride.com.retrofit_interfaces.GroupAccountAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class CreateGroupAccountStage1 extends AppCompatActivity {

  Button createStage1AccountBTN;
  GroupAccountAPI apiInterface;
  EditText groupName, groupDescription, amountNeeded;
  String userIdStr;
  long userId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_group_stage1);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    userIdStr = getIntent().getStringExtra("userId");
    userId = Long.valueOf(userIdStr);

    createStage1AccountBTN = (Button) findViewById(R.id.createGroupAccountStage1ContinueBTN);
    groupName = (EditText) findViewById(R.id.createGroupAccountStage1NameET);
    groupDescription = (EditText) findViewById(R.id.createGroupAccountStage1DescriptionET);
    amountNeeded = (EditText) findViewById(R.id.createGroupAccountStage1AmtNeededET);

    createStage1AccountBTN.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Retrofit createBasicAccount = new Retrofit.Builder()
            .baseUrl(LOCALHOST_SERVER_BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        apiInterface = createBasicAccount.create(GroupAccountAPI.class);

        createBasicAccount();
      }
    });
  }

  public void createBasicAccount() {
    GroupAccount groupAccount = new GroupAccount(userId,
        groupName.getText().toString(),
        groupDescription.getText().toString(),
        new BigDecimal(amountNeeded.getText().toString()));

    Call<GroupAccount> call = apiInterface.createBasicAccount(groupAccount);

    call.enqueue(new Callback<GroupAccount>() {
      @Override
      public void onResponse(Call<GroupAccount> call, Response<GroupAccount> response) {
        if(!response.isSuccessful()) {
          //Handle
        } else {
//          Intent intent = new Intent(CreateGroupAccountStage1.this, Profile.class);
//          intent.putExtra("userId", userIdStr);
//          startActivity(intent);
//          finish();
        }
      }

      @Override
      public void onFailure(Call<GroupAccount> call, Throwable t) {

      }
    });
  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.createGroupNameToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_create_group_stage1);
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
