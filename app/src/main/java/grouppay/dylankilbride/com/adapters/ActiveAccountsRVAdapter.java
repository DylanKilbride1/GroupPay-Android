package grouppay.dylankilbride.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import grouppay.dylankilbride.com.activities.GroupAccountDetailed;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.GroupAccount;

public class ActiveAccountsRVAdapter extends RecyclerView.Adapter<ActiveAccountsRVAdapter.ViewHolder>{

  public List<GroupAccount> accountsList;
  private int itemLayout;
  private Context context;
  private static ItemClickListener clickInterface;

  public ActiveAccountsRVAdapter(List<GroupAccount> accountsList, int itemLayout) {
    this.accountsList = accountsList;
    this.itemLayout = itemLayout;
  }

  public ActiveAccountsRVAdapter(List<GroupAccount> accountsList, Context context) {
    this.accountsList = accountsList;
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
    final GroupAccount groupAccount = accountsList.get(position);
    viewHolder.groupName.setText(groupAccount.getAccountName());
    viewHolder.accountValues.setText(groupAccount.getAccountFinanceString());
    viewHolder.numberOfMembers.setText(groupAccount.getNumberOfMembersString());

    viewHolder.groupName.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        accountsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, accountsList.size());
      }
    });

    viewHolder.accountValues.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), GroupAccountDetailed.class);
        view.getContext().startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return accountsList.size();
  }


  public void setAccounts(List<GroupAccount> accountsList) {
    this.accountsList = accountsList;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView groupName, accountValues, numberOfMembers;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      groupName = itemView.findViewById(R.id.groupNameView);
      accountValues = itemView.findViewById(R.id.accountValuesView);
      numberOfMembers = itemView.findViewById(R.id.numberOfMembers);
    }

    @Override
    public void onClick(View view) {
      if(view.equals(groupName)){
        removeAt(getAdapterPosition());
      }
    }

    public void removeAt(int position) {
      accountsList.remove(position);
      notifyItemRemoved(position);
      notifyItemRangeChanged(position, accountsList.size());
    }
  }
}