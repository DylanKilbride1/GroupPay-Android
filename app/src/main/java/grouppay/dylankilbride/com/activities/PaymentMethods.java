package grouppay.dylankilbride.com.activities;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import grouppay.dylankilbride.com.adapters.PaymentMethodsRVAdapter;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Cards;

public class PaymentMethods extends AppCompatActivity {

  PaymentMethodsRVAdapter adapter;
  private RecyclerView paymentMethodsRecyclerView;
  private RecyclerView.LayoutManager paymentMethodsRecyclerViewLayoutManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment_methods);

    setUpActionBar();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    ArrayList<Cards> cardsTempList = new ArrayList<>();
    cardsTempList.add(new Cards(1, "1234 2345 3456 4567", "02/21", "Dylan Kilbride", "MASTERCARD"));
    cardsTempList.add(new Cards(2, "0987 6545 6789 3454", "02/21", "Dylan Kilbride", "VISA"));
    cardsTempList.add(new Cards(3, "8888 6546 3456 8765", "02/21", "Dylan Kilbride", "AMERICAN EXPRESS"));
    cardsTempList.add(new Cards(4, "1234 2345 3456 4567", "02/21", "Dylan Kilbride", "MAESTRO"));
    cardsTempList.add(new Cards(2, "0987 6545 6789 3454", "02/21", "Dylan Kilbride", "VISA"));
    cardsTempList.add(new Cards(3, "8888 6546 3456 8765", "02/21", "Dylan Kilbride", "DISCOVER"));
    cardsTempList.add(new Cards(4, "1234 2345 3456 4567", "02/21", "Dylan Kilbride", "REVOLUT"));

    setUpAccountPreviewRecyclerView(cardsTempList);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddPaymentMethod);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(PaymentMethods.this, AddPaymentMethod.class);
        startActivity(intent);
      }
    });
  }

  public void setUpAccountPreviewRecyclerView(List<Cards> cardsList) {
    // set up the RecyclerView
    paymentMethodsRecyclerView = (RecyclerView) findViewById(R.id.paymentMethodsRV);
    paymentMethodsRecyclerViewLayoutManager = new LinearLayoutManager(this);
    paymentMethodsRecyclerView.setLayoutManager(paymentMethodsRecyclerViewLayoutManager);
    paymentMethodsRecyclerView.setAdapter(new PaymentMethodsRVAdapter(cardsList, R.layout.activity_payment_methods_list_item));
    paymentMethodsRecyclerView.addItemDecoration(new DividerItemDecoration(paymentMethodsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

  }

  public void setUpActionBar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.paymentMethodsToolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowCustomEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);

      LayoutInflater inflator = LayoutInflater.from(this);
      View v = inflator.inflate(R.layout.generic_titleview, null);

      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_payment_methods_title);
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
