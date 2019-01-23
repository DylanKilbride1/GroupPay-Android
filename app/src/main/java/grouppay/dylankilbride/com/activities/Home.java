package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import grouppay.dylankilbride.com.adapters.ActiveAccountsRVAdapter;
import grouppay.dylankilbride.com.adapters.ActiveAccountsRVAdapterNew;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.GroupAccount;

public class Home extends AppCompatActivity {

  ActiveAccountsRVAdapterNew adapter;
  private RecyclerView accountsRecyclerView;
  private RecyclerView.LayoutManager accountsRecyclerViewLayoutManager;
  private TextView noAccountsTextView, navName, navEmail;
  private DrawerLayout drawerLayout;
  private ActionBarDrawerToggle actionBarDrawerToggle;
  private NavigationView navigationView;
  private String userId, userName, userEmail;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    noAccountsTextView= (TextView) findViewById(R.id.noAccountPreviewsTextView);

    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_close);

    drawerLayout.addDrawerListener(actionBarDrawerToggle);
    actionBarDrawerToggle.syncState();

    ArrayList<GroupAccount> groupAccounts = new ArrayList<>();
    groupAccounts.add(new GroupAccount(1, "Pas De Casa", "Quick Hol", 3, new BigDecimal("47.23"), new BigDecimal("2500")));
    groupAccounts.add(new GroupAccount(2, "Dinner Today", "Quick Hol", 14, new BigDecimal("4"), new BigDecimal("25")));
    groupAccounts.add(new GroupAccount(3, "Monday", "Quick Hol", 5, new BigDecimal("56.70"), new BigDecimal("314")));
    groupAccounts.add(new GroupAccount(4, "Car", "Quick Hol", 2, new BigDecimal("0"), new BigDecimal("100")));

    setUpAccountPreviewRecyclerView(groupAccounts);

    emptyRVTextViewSetUp(checkIfListIsEmpty(groupAccounts));

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    userId = getIntent().getStringExtra("userId");
    userName = getIntent().getStringExtra("name");
    userEmail = getIntent().getStringExtra("email");

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
//        Intent intent = new Intent(Home.this, CreateAccount.class);
//        intent.putExtra("userId", userId);
//        startActivity(intent);
      }
    });

    navigationView = (NavigationView) findViewById(R.id.navView);
    View headerView = navigationView.getHeaderView(0);
    navName = (TextView) headerView.findViewById(R.id.navName);
    navEmail = (TextView) headerView.findViewById(R.id.navEmail);
    navName.setText(userName);
    navEmail.setText(userEmail);
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
          case R.id.nav_profile:
            //Intent intentMaps = new Intent(Home.this, ContactsLocation.class);
            //startActivity(intentMaps);
            break;
          case R.id.nav_cards:
            //TODO Add intent for cards activity
            break;
          case R.id.nav_logout:
            Intent intentLogin = new Intent(Home.this, Login.class);
            startActivity(intentLogin);
            break;
          default:
            return true;
        }
        return true;
      }
    });
  }

  public void setUpAccountPreviewRecyclerView(List<GroupAccount> accountList) {
    // set up the RecyclerView
    accountsRecyclerView = (RecyclerView) findViewById(R.id.rvAccountsPreview);
    accountsRecyclerViewLayoutManager = new LinearLayoutManager(this);
    accountsRecyclerView.setLayoutManager(accountsRecyclerViewLayoutManager);
    accountsRecyclerView.setAdapter(new ActiveAccountsRVAdapterNew(accountList, R.layout.accounts_preview_list_item));
  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.titleview_main_activity, null);

      ((TextView) v.findViewById(R.id.title)).setText(this.getTitle());
      ((TextView) v.findViewById(R.id.title)).setTextSize(20);

      this.getSupportActionBar().setCustomView(v);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if(actionBarDrawerToggle.onOptionsItemSelected(item))
      return true;
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    actionBarDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    actionBarDrawerToggle.onConfigurationChanged(newConfig);
  }

  public boolean checkIfListIsEmpty(List<GroupAccount> list) {
    if (list.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

  public void emptyRVTextViewSetUp(boolean listEmpty) {
    if (listEmpty == true) {
      accountsRecyclerView.setVisibility(View.GONE);
      noAccountsTextView.setVisibility(View.VISIBLE);
    } else {
      accountsRecyclerView.setVisibility(View.VISIBLE);
      noAccountsTextView.setVisibility(View.GONE);
    }
  }

  @Override
  public void onBackPressed() {
    moveTaskToBack(true);
  }

  //TODO Add nav drawer to this activity
}