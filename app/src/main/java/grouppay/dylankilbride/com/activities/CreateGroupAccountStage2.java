package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import grouppay.dylankilbride.com.adapters.CreateGroupAccountStage2RVAdapter;
import grouppay.dylankilbride.com.adapters.ItemClickListener;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.User;
import grouppay.dylankilbride.com.retrofit_interfaces.GroupAccountAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class CreateGroupAccountStage2 extends AppCompatActivity implements ItemClickListener{

    private CreateGroupAccountStage2RVAdapter adapter;
    private GroupAccountAPI apiInterface;
    private RecyclerView usersRecyclerView;
    private RecyclerView.LayoutManager usersRecyclerViewLayoutManager;
    private Button addUsersButton;
    private ArrayList<User> selectedUsers = new ArrayList<>();
    String groupAccountIdStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_stage2);

        groupAccountIdStr = getIntent().getStringExtra("groupAccountId");
        addUsersButton = (Button)findViewById(R.id.createGroupAccountStage2ContinueBTN);

        ArrayList<User> usersList = new ArrayList<>();
        usersList.add(new User("Chigo", "Benz", "0861524605"));
        usersList.add(new User("Cian", "Hend", "0812345677"));
        setUpAccountPreviewRecyclerView(usersList);

        addUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit createBasicAccount = new Retrofit.Builder()
                    .baseUrl(LOCALHOST_SERVER_BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

                apiInterface = createBasicAccount.create(GroupAccountAPI.class);
                //addUsersToGroupAccount();
            }
        });
    }

    public void setUpAccountPreviewRecyclerView(List<User> usersList) {
        usersRecyclerView = (RecyclerView) findViewById(R.id.createGroupAccountStage2RV);
        usersRecyclerViewLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersRecyclerViewLayoutManager);
        adapter = new CreateGroupAccountStage2RVAdapter(usersList, R.layout.activity_create_group_stage2_list_item, this);
        usersRecyclerView.setAdapter(adapter);
        adapter.setOnClick(CreateGroupAccountStage2.this);
        usersRecyclerView.addItemDecoration(new DividerItemDecoration(usersRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    public void addUsersToGroupAccount(List<User> usersToAdd) {
        Call<List<User>> call = apiInterface.addUsersToAccount(groupAccountIdStr, usersToAdd);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()) {
                    //Handle
                } else {
                    Intent intent = new Intent(CreateGroupAccountStage2.this, GroupAccountDetailed.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(User user) {
        if(!user.isPressed()) {

            selectedUsers.add(user);
        }
    }
}
