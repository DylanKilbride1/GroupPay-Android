package grouppay.dylankilbride.com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import grouppay.dylankilbride.com.adapters.GroupInfoParticipantsRVAdapter;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.User;
import grouppay.dylankilbride.com.retrofit_interfaces.GroupAccountAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class GroupInformation extends AppCompatActivity {

  private String groupName, groupAccountId, groupImageUrl;
  private List<User> participantsList = new ArrayList<>();
  private ImageView groupImage;
  private TextView changeGroupImage, numberOfParticipants;
  private RecyclerView groupParticipantsRv;
  private GroupInfoParticipantsRVAdapter adapter;
  private LinearLayoutManager groupParticipantsRvLayoutManager;
  private GroupAccountAPI apiInterface;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_group_information);

    groupName = getIntent().getStringExtra("groupName");
    groupAccountId = getIntent().getStringExtra("groupAccountId");
    groupImageUrl = getIntent().getStringExtra("groupImageUrl");
    groupImage = findViewById(R.id.groupInfoGroupImage);
    changeGroupImage = findViewById(R.id.groupInfoChangeImage);
    numberOfParticipants = findViewById(R.id.groupInfoNumberOfParticipants);

    setUpActionBar(groupName);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    loadGroupImage(groupImageUrl);

  }

  public void setUpActionBar(String groupName) {
    Toolbar toolbar = (Toolbar) findViewById(R.id.groupInfoToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(groupName);
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }

  public void loadGroupImage(String imageUrl) {
    Glide.with(getApplicationContext())
        .load(imageUrl)
        .into(groupImage);
  }

  public void setUpGroupParticipantsRecyclerView(List<User> participantsList) {
    // set up the RecyclerView
    groupParticipantsRv = (RecyclerView) findViewById(R.id.groupInfoParticipantsRv);
    groupParticipantsRvLayoutManager = new LinearLayoutManager(this);
    groupParticipantsRv.setLayoutManager(groupParticipantsRvLayoutManager);
    adapter = new GroupInfoParticipantsRVAdapter(participantsList, R.layout.group_participants_list_item, this);
    groupParticipantsRv.setAdapter(adapter);
    groupParticipantsRv.addItemDecoration(new DividerItemDecoration(groupParticipantsRv.getContext(), DividerItemDecoration.VERTICAL));
  }

  public void setUpGroupParticipantsCall(String groupAccountId) {
    Retrofit getGroupParticipants = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    apiInterface = getGroupParticipants.create(GroupAccountAPI.class);
    getGroupParticipantsCall(groupAccountId);
  }

  public void getGroupParticipantsCall(String groupAccountId){
    Call<List<User>> call = apiInterface.getAllGroupParticipants(groupAccountId);
    call.enqueue(new Callback<List<User>>() {
      @Override
      public void onResponse(Call<List<User>> call, Response<List<User>> response) {
        if(!response.isSuccessful()) {
        } else {
          if (response.body().size() > 0 && !response.body().equals("null")) {
                participantsList.clear();
            for (int i = 0; i < response.body().size(); i++) {
              User groupParticipant = new User(response.body().get(i).getFirstName(),
                  response.body().get(i).getLastName(),
                  response.body().get(i).getEmailAddress(),
                  response.body().get(i).getMobileNumber(),
                  response.body().get(i).getProfileImage());
              participantsList.add(groupParticipant);
            }
            String participantsString = participantsList.size() + " Participants";
            setUpGroupParticipantsRecyclerView(participantsList);
            numberOfParticipants.setText(participantsString);
          }
        }
      }

      @Override
      public void onFailure(Call<List<User>> call, Throwable t) {

      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    setUpGroupParticipantsCall(groupAccountId);
  }
}