package grouppay.dylankilbride.com.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import grouppay.dylankilbride.com.adapters.CreateGroupAccountStage2RVAdapter;
import grouppay.dylankilbride.com.adapters.ItemClickListener;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Cards;
import grouppay.dylankilbride.com.models.Contact;
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
  private RecyclerView contactsRecyclerView, contactsNotOnGPRV;
  private RecyclerView.LayoutManager contactsRecyclerViewLayoutManager;
  private Button addContactsButton;
  private ArrayList<User> selectedContacts = new ArrayList<>();
  private ArrayList<User> contactList;
  private ArrayList<User> contactsNotOnGPList;
  private TextView numberOfContactsOnGP, numberOfContactsNotOnGP;
  private String groupAccountIdStr;
  private static final int CONTACTS_PERMISSIONS_REQUEST_CODE = 151;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_group_stage2);
    checkContactsPermissions();

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    groupAccountIdStr = getIntent().getStringExtra("groupAccountId");
    addContactsButton = (Button) findViewById(R.id.createGroupAccountStage2ContinueBTN);
    numberOfContactsOnGP = findViewById(R.id.numberOfContactsOnGPTV);
    numberOfContactsNotOnGP = findViewById(R.id.numberOfContactsNotOnGPTV);

    showHideContinueButton(selectedContacts);

    contactList = new ArrayList<>();
    contactsNotOnGPList = new ArrayList<>();
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

  public void setUpContactsOnGroupPayRecyclerView(List<User> contactList) {
    contactsRecyclerView = (RecyclerView) findViewById(R.id.createGroupAccountStage2RV);
    contactsRecyclerViewLayoutManager = new LinearLayoutManager(this);
    contactsRecyclerView.setLayoutManager(contactsRecyclerViewLayoutManager);
    adapter = new CreateGroupAccountStage2RVAdapter(contactList, R.layout.activity_create_group_stage2_list_item, this);
    contactsRecyclerView.setAdapter(adapter);
    adapter.setOnClick(CreateGroupAccountStage2.this);
    //contactsRecyclerView.addItemDecoration(new DividerItemDecoration(contactsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
  }

  public void setUpContactsNotOnGPRecyclerView(List<User> contactNotOnGPList) {
    contactsRecyclerView = (RecyclerView) findViewById(R.id.contactsNotOnGPStage2RV);
    contactsRecyclerViewLayoutManager = new LinearLayoutManager(this);
    contactsRecyclerView.setLayoutManager(contactsRecyclerViewLayoutManager);
    adapter = new CreateGroupAccountStage2RVAdapter(contactNotOnGPList, R.layout.activity_create_group_stage2_list_item, this);
    contactsRecyclerView.setAdapter(adapter);
    adapter.setOnClick(CreateGroupAccountStage2.this);
    //contactsRecyclerView.addItemDecoration(new DividerItemDecoration(contactsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
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
      //String numberOfContactsSelected = "Added - " + selectedContacts.size() + " Contacts";
    } else {
      selectedContacts.remove(contact);
      showHideContinueButton(selectedContacts);
      //String numberOfContactsSelected = "Removed - " + selectedContacts.size() + " Contacts";
    }
  }

  public void showHideContinueButton(List<User> selectedContacts) {
    if (selectedContacts.size() == 0) {
      addContactsButton.setVisibility(View.INVISIBLE);
    } else {
      String numberOfContactsSelected = "Continue \u2022 " + selectedContacts.size() + " Contacts";
      addContactsButton.setVisibility(View.VISIBLE);
      addContactsButton.setText(numberOfContactsSelected);
    }
  }

  @Override
  public void onItemClick(GroupAccount groupAccount) {

  }

  @Override
  public void onItemClick(Cards card) {

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
      String contactName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
      if(contactName.contains(" ")) {
        String[] contactNames = contactName.split(" ", 2);
        contactsPhoneNumbers.add(phoneNumber);
        contactsNotOnGPList.add(new User(contactNames[0], contactNames[1], phoneNumber));
      } else {
        contactsNotOnGPList.add(new User(contactName, " ", phoneNumber));
      }
    }
    setUpContactsNotOnGPRecyclerView(contactsNotOnGPList);
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
          numberOfContactsOnGP.setText("\u2022 " + contactList.size());
          setUpContactsOnGroupPayRecyclerView(contactList);
          setUpContactsNotOnGPRecyclerView(contactsNotOnGPList);
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

  private void checkContactsPermissions() {
    if (ContextCompat.checkSelfPermission(CreateGroupAccountStage2.this,
        Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED) {

      if (ActivityCompat.shouldShowRequestPermissionRationale(CreateGroupAccountStage2.this,
          Manifest.permission.READ_CONTACTS)) {
      } else {
        ActivityCompat.requestPermissions(CreateGroupAccountStage2.this,
            new String[]{Manifest.permission.READ_CONTACTS},
            CONTACTS_PERMISSIONS_REQUEST_CODE);

        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
      }
    } else {
      // Permission has already been granted
    }
  }
}