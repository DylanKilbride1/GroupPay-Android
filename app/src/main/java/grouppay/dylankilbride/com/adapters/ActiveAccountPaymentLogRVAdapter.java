package grouppay.dylankilbride.com.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Payments;

public class ActiveAccountPaymentLogRVAdapter extends RecyclerView.Adapter<ActiveAccountPaymentLogRVAdapter.ViewHolder> {

  public List<Payments> paymentsList;
  private int itemLayout;
  private Context context;
  private static ItemClickListener clickInterface;

  public ActiveAccountPaymentLogRVAdapter(List<Payments> paymentsList, int itemLayout) {
    this.paymentsList = paymentsList;
    this.itemLayout = itemLayout;
  }

  public ActiveAccountPaymentLogRVAdapter(List<Payments> paymentsList, Context context) {
    this.paymentsList = paymentsList;
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
    final Payments payments = paymentsList.get(position);
    String paymentTypeStr = "Paid";
    String paymentAmountEuro = "€" + payments.getAmountPaidStr();
    viewHolder.userInitials.setText(payments.getUser().getInitials());
    viewHolder.userFullName.setText(payments.getUser().getFullName());
    viewHolder.paymentTime.setText(payments.getPaymentDateAndTime().toString());
    viewHolder.paymentType.setText(paymentTypeStr);
    viewHolder.paymentAmount.setText(paymentAmountEuro);

    viewHolder.userFullName.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        paymentsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, paymentsList.size());
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
    return paymentsList.size();
  }


  public void setAccounts(List<Payments> paymentsList) {
    this.paymentsList = paymentsList;
    notifyDataSetChanged();
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
      paymentsList.remove(position);
      notifyItemRemoved(position);
      notifyItemRangeChanged(position, paymentsList.size());
    }
  }
}
