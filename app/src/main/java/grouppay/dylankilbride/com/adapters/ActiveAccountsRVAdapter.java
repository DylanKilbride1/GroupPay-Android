package grouppay.dylankilbride.com.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.GroupAccount;

public class ActiveAccountsRVAdapter extends RecyclerView.Adapter<ActiveAccountsRVAdapter.ViewHolder>{

  public List<GroupAccount> accountsList;
  private int itemLayout;
  private Context context;
  private static ItemClickListener onItemClick;
  private RequestOptions noGroupImageDefault = new RequestOptions().error(R.drawable.no_group_icon);

  public ActiveAccountsRVAdapter(List<GroupAccount> accountsList, int itemLayout, Context context) {
    this.accountsList = accountsList;
    this.itemLayout = itemLayout;
    this.context = context;
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
    Glide.with(context.getApplicationContext())
        .load(accountsList.get(position).getGroupImage().getGroupImageLocation())
        .apply(noGroupImageDefault)
        .into(viewHolder.groupImage);
    viewHolder.groupName.setText(groupAccount.getAccountName());
    viewHolder.accountValues.setText(groupAccount.getAccountFinanceString());
    viewHolder.numberOfMembers.setText(groupAccount.getNumberOfMembersString());

    viewHolder.listItem.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {
        onItemClick.onItemClick(accountsList.get(position));
      }
    });
  }

  public void updateData(final List<GroupAccount> groupAccounts) {
    List<GroupAccount> updatedGroupAccounts = new ArrayList<>();
    updatedGroupAccounts.addAll(groupAccounts);
  }

  public void setOnClick(ItemClickListener onClick) {
    this.onItemClick = onClick;
  }

  @Override
  public int getItemCount() {
    return accountsList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView groupName, accountValues, numberOfMembers;
    public LinearLayout listItem;
    public ImageView groupImage;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      groupName = itemView.findViewById(R.id.groupNameView);
      groupImage = itemView.findViewById(R.id.accountPreviewImage);
      accountValues = itemView.findViewById(R.id.accountValuesView);
      numberOfMembers = itemView.findViewById(R.id.numberOfMembers);
      listItem = itemView.findViewById(R.id.homeAccountPreviewItemLL);
    }

    public void removeAt(int position) {
      accountsList.remove(position);
      notifyItemRemoved(position);
      notifyItemRangeChanged(position, accountsList.size());
    }
  }
}