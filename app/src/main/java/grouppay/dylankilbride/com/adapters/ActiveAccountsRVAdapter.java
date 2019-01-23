package grouppay.dylankilbride.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.List;

import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.GroupAccount;

public class ActiveAccountsRVAdapter extends RecyclerView.Adapter<ActiveAccountsRVAdapter.ViewHolder> {

  private List<GroupAccount> groupAccountList;
  private LayoutInflater layoutInflater;
  private ItemClickListener itemClickListener;

  public ActiveAccountsRVAdapter(Context context, List<GroupAccount> data){
    this.layoutInflater = LayoutInflater.from(context);
    this.groupAccountList = data;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    View view = layoutInflater.inflate(R.layout.accounts_preview_list_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    //TODO Add image
    String groupNameStr = groupAccountList.get(i).getAccountName();
    BigDecimal amountPaid = groupAccountList.get(i).getTotalAmountPaid();
    String amountPaidStr = String.valueOf(amountPaid);
    BigDecimal amountOwed = groupAccountList.get(i).getTotalAmountOwed();
    String amountOwedStr = String.valueOf(amountOwed);
    int members = groupAccountList.get(i).getNumberOfMembers();
    String membersStr = String.valueOf(members) + " members";
    String financialValues = "€" + amountPaidStr + " of €" + amountOwedStr;

    viewHolder.groupName.setText(groupNameStr);
    viewHolder.accountValues.setText(financialValues);
    viewHolder.numberOfMembers.setText(membersStr);
  }

  @Override
  public int getItemCount() {
    return 0;
  }

  // stores and recycles views as they are scrolled off screen
  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //ImageView groupPhotoImageView;
    TextView groupName, accountValues, numberOfMembers;

    ViewHolder(View itemView) {
      super(itemView);
      //TODO Add Image
      groupName = itemView.findViewById(R.id.groupNameView);
      accountValues = itemView.findViewById(R.id.accountValuesView);
      numberOfMembers = itemView.findViewById(R.id.numberOfMembers);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
    }
  }

  // convenience method for getting data at click position
  GroupAccount getItem(int id) {
    return groupAccountList.get(id);
  }

  // allows clicks events to be caught
  void setClickListener(ItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

}