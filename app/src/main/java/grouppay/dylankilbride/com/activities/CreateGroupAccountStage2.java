package grouppay.dylankilbride.com.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import grouppay.dylankilbride.com.adapters.CreateGroupAccountStage2RVAdapter;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.User;

public class CreateGroupAccountStage2 extends AppCompatActivity {

    CreateGroupAccountStage2RVAdapter adapter;
    private RecyclerView usersRecyclerView;
    private RecyclerView.LayoutManager usersRecyclerViewLayoutManager;
    String groupAccountIdStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_stage2);

        groupAccountIdStr = getIntent().getStringExtra("groupAccountId");

        ArrayList<User> usersList = new ArrayList<>();
        usersList.add(new User("Chigo", "Benz", "0861524605"));
        usersList.add(new User("Cian", "Hend", "0812345677"));
        setUpAccountPreviewRecyclerView(usersList);
    }

    public void setUpAccountPreviewRecyclerView(List<User> usersList) {
        // set up the RecyclerView
        usersRecyclerView = (RecyclerView) findViewById(R.id.createGroupAccountStage2RV);
        usersRecyclerViewLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersRecyclerViewLayoutManager);
        usersRecyclerView.setAdapter(new CreateGroupAccountStage2RVAdapter(usersList, R.layout.activity_create_group_stage2_list_item));
        usersRecyclerView.addItemDecoration(new DividerItemDecoration(usersRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }
}
