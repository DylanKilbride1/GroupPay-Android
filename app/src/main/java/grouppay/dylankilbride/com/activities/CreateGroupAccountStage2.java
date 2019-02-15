package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import grouppay.dylankilbride.com.adapters.CreateGroupAccountStage2RVAdapter;
import grouppay.dylankilbride.com.adapters.ItemClickListener;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Contact;
import grouppay.dylankilbride.com.models.GroupAccount;
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
    private RecyclerView contactsRecyclerView;
    private RecyclerView.LayoutManager contactsRecyclerViewLayoutManager;
    private Button addContactsButton;
    private ArrayList<Contact> selectedContacts = new ArrayList<>();
    String groupAccountIdStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_stage2);

        setUpActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        groupAccountIdStr = getIntent().getStringExtra("groupAccountId");
        addContactsButton = (Button)findViewById(R.id.createGroupAccountStage2ContinueBTN);

        ArrayList<Contact> contactList = new ArrayList<>();
        contactList.add(new Contact("FN1", "LN1", "testcontact1"));
        contactList.add(new Contact("FN2", "LN2", "testcontact2"));
        setUpAccountPreviewRecyclerView(contactList);

        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit addContactsToAccount = new Retrofit.Builder()
                    .baseUrl(LOCALHOST_SERVER_BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

                apiInterface = addContactsToAccount.create(GroupAccountAPI.class);
                addContactsToGroupAccount(selectedContacts);
            }
        });
    }

    public void setUpAccountPreviewRecyclerView(List<Contact> contactList) {
        contactsRecyclerView = (RecyclerView) findViewById(R.id.createGroupAccountStage2RV);
        contactsRecyclerViewLayoutManager = new LinearLayoutManager(this);
        contactsRecyclerView.setLayoutManager(contactsRecyclerViewLayoutManager);
        adapter = new CreateGroupAccountStage2RVAdapter(contactList, R.layout.activity_create_group_stage2_list_item, this);
        contactsRecyclerView.setAdapter(adapter);
        adapter.setOnClick(CreateGroupAccountStage2.this);
        contactsRecyclerView.addItemDecoration(new DividerItemDecoration(contactsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    public void addContactsToGroupAccount(List<Contact> contactsToAdd) {
        Call<GroupAccount> call = apiInterface.addContactsToAccount(groupAccountIdStr, contactsToAdd);
        call.enqueue(new Callback<GroupAccount>() {
            @Override
            public void onResponse(Call<GroupAccount> call, Response<GroupAccount> response) {
                if(!response.isSuccessful()) {
                    //Handle
                } else {
//                    GroupAccount newGroupAccount = new GroupAccount(response.body().getGroupAccountId(),
//                        response.body().getAdminId(),
//                        response.body().getAccountName(),
//                        response.body().getAccountDescription(),
//                        response.body().getNumberOfMembers(),
//                        response.body().getTotalAmountPaid(),
//                        response.body().getTotalAmountOwed(),
//                        response.body().getTestResourceId());
                    Intent intent = new Intent(CreateGroupAccountStage2.this, GroupAccountDetailed.class);
                    intent.putExtra("groupAccountId", groupAccountIdStr);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<GroupAccount> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(Contact contact) {
        if(contact.getIsPressedValue()) {
            selectedContacts.add(contact);
        } else {
            selectedContacts.remove(contact);
        }
    }

    public void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.createGroupNameToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.generic_titleview, null);

            ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_create_group_stage2);
            ((TextView) v.findViewById(R.id.title)).setTextSize(20);

            this.getSupportActionBar().setCustomView(v);
        }
    }
}
