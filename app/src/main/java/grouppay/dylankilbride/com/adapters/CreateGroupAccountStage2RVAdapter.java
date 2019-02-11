package grouppay.dylankilbride.com.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.User;

public class CreateGroupAccountStage2RVAdapter extends RecyclerView.Adapter<CreateGroupAccountStage2RVAdapter.ViewHolder>{

    public List<User> userList;
    private int itemLayout;
    private Context context;
    private static ItemClickListener clickInterface;

    public CreateGroupAccountStage2RVAdapter(List<User> userList, int itemLayout) {
        this.userList = userList;
        this.itemLayout = itemLayout;
    }

    public CreateGroupAccountStage2RVAdapter(List<User> userList, Context context) {
        this.userList = userList;
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
        final User user = userList.get(position);
        viewHolder.username.setText(user.getFullName());
        viewHolder.userImage.setBackgroundResource(R.drawable.human_photo);

        viewHolder.viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout viewLayout;
        public TextView username;
        public ImageView userImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewLayout = itemView.findViewById(R.id.createGroupAccountStage2MainLL);
            username = itemView.findViewById(R.id.createGroupAccountStage2UserNameTV);
            userImage = itemView.findViewById(R.id.createGroupAccountStage2UserImage);
        }

        @Override
        public void onClick(View view) {
        }

        public void removeAt(int position) {
            userList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, userList.size());
        }
    }
}