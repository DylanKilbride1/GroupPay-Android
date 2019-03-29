package grouppay.dylankilbride.com.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Transaction;

public class ActiveAccountPaymentLogRVAdapter extends RecyclerView.Adapter<ActiveAccountPaymentLogRVAdapter.ViewHolder> {

  public List<Transaction> transactionsList;
  private int itemLayout;
  private Context context;
  private static ItemClickListener clickInterface;

  public ActiveAccountPaymentLogRVAdapter(List<Transaction> transactionsList, int itemLayout) {
    this.transactionsList = transactionsList;
    this.itemLayout = itemLayout;
  }

  public ActiveAccountPaymentLogRVAdapter(List<Transaction> transactionsList, Context context) {
    this.transactionsList = transactionsList;
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
    final Transaction transaction = transactionsList.get(position);
    String paymentTypeStr = "Paid";
    String paymentAmountEuro = "€" + transaction.getAmountPaidStr();
    viewHolder.userInitials.setText(transaction.getUser().getInitials());
    viewHolder.userFullName.setText(transaction.getUser().getFullName());
    viewHolder.paymentTime.setText(transaction.getFormattedPaymentDateAndTime(transaction.getPaymentDateAndTime()));
    viewHolder.paymentType.setText(paymentTypeStr);
    viewHolder.paymentAmount.setText(paymentAmountEuro);

    viewHolder.userFullName.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        transactionsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, transactionsList.size());
      }
    });

//    viewHolder.contactName.setOnClickListener(new View.OnClickListener() {
//
//      @Override
//      public void onClick(View view) {
//        Intent intent = new Intent(view.getContext(), ViewContact.class);
//        intent.putExtra("fullName", contactsList.get(position).getFullName());
//        intent.putExtra("email", contactsList.get(position).getEmailAddress());
//        intent.putExtra("phone", contactsList.get(position).getPhoneNumber());
//        view.getContext().startActivity(intent);
//      }
//    });
  }

  @Override
  public int getItemCount() {
    return transactionsList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView userInitials, userFullName, paymentTime, paymentType, paymentAmount;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      userInitials = itemView.findViewById(R.id.paymentLogUserInitials);
      userFullName = itemView.findViewById(R.id.paymentLogUserFullName);
      paymentTime = itemView.findViewById(R.id.paymentLogPaymentTime);
      paymentType = itemView.findViewById(R.id.paymentLogPaymentType);
      paymentAmount = itemView.findViewById(R.id.paymentLogPaymentAmount);
    }

    @Override
    public void onClick(View view) {
      if (view.equals(view)) {
        removeAt(getAdapterPosition());
      }
    }

    public void removeAt(int position) {
      transactionsList.remove(position);
      notifyItemRemoved(position);
      notifyItemRangeChanged(position, transactionsList.size());
    }
  }
}
