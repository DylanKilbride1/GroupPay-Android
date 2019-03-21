package grouppay.dylankilbride.com.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import grouppay.dylankilbride.com.adapters.CreateGroupAccountStage2RVAdapter;
import grouppay.dylankilbride.com.adapters.ItemClickListener;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.GroupAccount;
import grouppay.dylankilbride.com.models.User;
import grouppay.dylankilbride.com.retrofit_interfaces.GroupAccountAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static grouppay.dylankilbride.com.constants.Constants.LOCALHOST_SERVER_BASEURL;

public class CreateGroupAccountStage2 extends AppCompatActivity implements ItemClickListener {

  private CreateGroupAccountStage2RVAdapter adapter;
  private GroupAccountAPI apiInterface;
  private RecyclerView contactsRecyclerView;
  private RecyclerView.LayoutManager contactsRecyclerViewLayoutManager;
  private Button addContactsButton;
  private ArrayList<User> selectedContacts = new ArrayList<>();
  private ArrayList<User> contactList;
  String groupAccountIdStr;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_group_stage2);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    groupAccountIdStr = getIntent().getStringExtra("groupAccountId");
    addContactsButton = (Button) findViewById(R.id.createGroupAccountStage2ContinueBTN);

    showHideContinueButton(selectedContacts);

    contactList = new ArrayList<>();
    getContactsPhoneNumbers();

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

  public void setUpAccountPreviewRecyclerView(List<User> contactList) {
    contactsRecyclerView = (RecyclerView) findViewById(R.id.createGroupAccountStage2RV);
    contactsRecyclerViewLayoutManager = new LinearLayoutManager(this);
    contactsRecyclerView.setLayoutManager(contactsRecyclerViewLayoutManager);
    adapter = new CreateGroupAccountStage2RVAdapter(contactList, R.layout.activity_create_group_stage2_list_item, this);
    contactsRecyclerView.setAdapter(adapter);
    adapter.setOnClick(CreateGroupAccountStage2.this);
    contactsRecyclerView.addItemDecoration(new DividerItemDecoration(contactsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
  }

  public void addContactsToGroupAccount(List<User> contactsToAdd) {
    Call<GroupAccount> call = apiInterface.addContactsToAccount(groupAccountIdStr, contactsToAdd);
    call.enqueue(new Callback<GroupAccount>() {
      @Override
      public void onResponse(Call<GroupAccount> call, Response<GroupAccount> response) {
        if (!response.isSuccessful()) {
          //Handle
        } else {
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
  public void onItemClick(User contact) {
    if (contact.getIsPressedValue()) {
      selectedContacts.add(contact);
      showHideContinueButton(selectedContacts);
    } else {
      showHideContinueButton(selectedContacts);
      selectedContacts.remove(contact);
    }
  }

  @Override
  public void onItemClick(GroupAccount groupAccount) {

  }

  public void showHideContinueButton(List<User> selectedContacts) {
    if (selectedContacts.size() == 0) {
      addContactsButton.setVisibility(View.INVISIBLE);
    } else {
      String numberOfContactsSelected = "Continue - " + selectedContacts.size() + " Contacts";
      addContactsButton.setVisibility(View.VISIBLE);
      addContactsButton.setText(numberOfContactsSelected);
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

  public void getContactsPhoneNumbers() {
    List<String> contactsPhoneNumbers = new ArrayList<>();
    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        null);
    while (phones.moveToNext()) {
      String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
      contactsPhoneNumbers.add(phoneNumber);
    }
    setUpContactsWithGPAccountsCall(contactsPhoneNumbers);
  }

  public void setUpContactsWithGPAccountsCall(List<String> contactsPhoneNumbers) {
    Retrofit getContactsWithGPAccounts = new Retrofit.Builder()
        .baseUrl(LOCALHOST_SERVER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    apiInterface = getContactsWithGPAccounts.create(GroupAccountAPI.class);
    getAllContactsWithGPAccounts(contactsPhoneNumbers);
  }

  public void getAllContactsWithGPAccounts(List<String> contactsPhoneNumbers) {
    Call<List<User>> call = apiInterface.getAllContactsWithGrouppayAccounts(contactsPhoneNumbers);
    call.enqueue(new Callback<List<User>>() {
      @Override
      public void onResponse(Call<List<User>> call, Response<List<User>> response) {
        if (!response.isSuccessful()) {
          //Handle
        } else {
          for (int i = 0; i < response.body().size(); i++) {
            contactList.add(response.body().get(i));
          }
          setUpAccountPreviewRecyclerView(contactList);
        }
      }

      @Override
      public void onFailure(Call<List<User>> call, Throwable t) {
        Toast.makeText(getApplicationContext(), "Unable to retreive contacts", Toast.LENGTH_LONG).show();
      }
    });
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