package grouppay.dylankilbride.com.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.User;

public class GroupInfoParticipantsRVAdapter extends RecyclerView.Adapter<GroupInfoParticipantsRVAdapter.ViewHolder>{

  public List<User> participantsList;
  private int itemLayout;
  private Context context;
  private static ItemClickListener onItemClick;
  private RequestOptions noProfileImageDefault = new RequestOptions().error(R.drawable.no_profile_photo);


  public GroupInfoParticipantsRVAdapter(List<User> participantsList, int itemLayout, Context context) {
    this.participantsList = participantsList;
    this.itemLayout = itemLayout;
    this.context = context;
  }

  public GroupInfoParticipantsRVAdapter(List<User> participantsList, Context context) {
    this.participantsList = participantsList;
    this.context = context;
  }

  @NonNull
  @Override
  public GroupInfoParticipantsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
    return new GroupInfoParticipantsRVAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final GroupInfoParticipantsRVAdapter.ViewHolder viewHolder, final int position) {
    final User participant = participantsList.get(position);
    viewHolder.contactName.setText(participant.getFullName());
    Glide.with(context.getApplicationContext())
        .load(participant.getProfileImage().getProfileImageLocation())
        .apply(noProfileImageDefault)
        .into(viewHolder.contactPhoto);
  }

  public void setOnClick(ItemClickListener onClick) {
    this.onItemClick = onClick;
  }

  @Override
  public int getItemCount() {
    return participantsList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout viewLayout;
    public TextView contactName;
    public ImageView contactPhoto;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      viewLayout = itemView.findViewById(R.id.groupInfoParticipantsLL);
      contactName = itemView.findViewById(R.id.groupInfoGroupParticipantNameTV);
      contactPhoto = itemView.findViewById(R.id.groupInfoGroupParticipantIV);
    }

    public void removeAt(int position) {
      participantsList.remove(position);
      notifyItemRemoved(position);
      notifyItemRangeChanged(position, participantsList.size());
    }
  }
}
