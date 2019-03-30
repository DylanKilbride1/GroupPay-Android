package grouppay.dylankilbride.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Transaction;

public class AllTransactionsRVAdapter extends RecyclerView.Adapter<AllTransactionsRVAdapter.ViewHolder>{

  public List<Transaction> transactionList;
  private int itemLayout;
  private Context context;
  private static ItemClickListener clickInterface;

  public AllTransactionsRVAdapter(List<Transaction> transactionList, int itemLayout) {
    this.transactionList = transactionList;
    this.itemLayout = itemLayout;
  }

  public AllTransactionsRVAdapter(List<Transaction> transactionList, int itemLayout, Context context) {
    this.transactionList = transactionList;
    this.itemLayout = itemLayout;
    this.context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
    final Transaction transaction = transactionList.get(position);
    viewHolder.depositedToGroup.setText(transaction.returnFundsDepositedToGroup());
    viewHolder.paymentType.setText(transaction.getPaymentType());
    viewHolder.paymentDateTime.setText(transaction.getPaymentDateAndTime());
    viewHolder.paymentAmount.setText(transaction.getAmountPaidStr());
  }

  @Override
  public int getItemCount() {
    return transactionList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView depositedToGroup, paymentDateTime, paymentAmount, paymentType;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      depositedToGroup = itemView.findViewById(R.id.allTransactionsGroupNameTV);
      paymentDateTime = itemView.findViewById(R.id.allTransactionsPaymentTimeTV);
      paymentAmount = itemView.findViewById(R.id.allTransactionsPaymentAmountTV);
      paymentType = itemView.findViewById(R.id.allTransactionsPaymentTypeTV);
    }

    @Override
    public void onClick(View view) {
    }

    public void removeAt(int position) {
      transactionList.remove(position);
      notifyItemRemoved(position);
      notifyItemRangeChanged(position, transactionList.size());
    }
  }
}