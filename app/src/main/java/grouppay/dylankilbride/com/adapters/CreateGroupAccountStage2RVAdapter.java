package grouppay.dylankilbride.com.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.User;

public class CreateGroupAccountStage2RVAdapter extends RecyclerView.Adapter<CreateGroupAccountStage2RVAdapter.ViewHolder>{

    public List<User> contactList;
    private int itemLayout;
    private Context context;
    private static ItemClickListener onItemClick;
    private RequestOptions noProfileImageDefault = new RequestOptions().error(R.drawable.no_profile_photo);

    public CreateGroupAccountStage2RVAdapter(List<User> contactList, int itemLayout, Context context) {
        this.contactList = contactList;
        this.itemLayout = itemLayout;
        this.context = context;
    }

    public CreateGroupAccountStage2RVAdapter(List<User> contactList, Context context) {
        this.contactList = contactList;
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
        final User contact = contactList.get(position);
        viewHolder.contactName.setText(contact.getFullName());
        Glide.with(context.getApplicationContext())
            .load(contact.getProfileImage().getProfileImageLocation())
            .apply(noProfileImageDefault)
            .into(viewHolder.contactPhoto);
        viewHolder.contactNumber.setText(contact.getMobileNumber());
        viewHolder.viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contact.getIsPressedValue()) {
                    contact.setPressedTrue();
                    viewHolder.viewLayout.setBackgroundResource(R.color.createGroupAccountContactItemPressed);
                } else {
                    viewHolder.viewLayout.setBackgroundResource(R.color.addContactsUnselectedBg);
                    contact.setPressedFalse();
                }
                onItemClick.onItemClick(contactList.get(position));
            }
        });
    }

    public void setOnClick(ItemClickListener onClick) {
        this.onItemClick = onClick;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout viewLayout;
        public TextView contactName;
        public ImageView contactPhoto;
        private TextView contactNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewLayout = itemView.findViewById(R.id.createGroupAccountStage2MainLL);
            contactName = itemView.findViewById(R.id.createGroupAccountStage2ContactNameTV);
            contactPhoto = itemView.findViewById(R.id.createGroupStage2ContactPhoto);
            contactNumber = itemView.findViewById(R.id.createGroupAccountStage2ContactNumberTV);
        }

        public void removeAt(int position) {
            contactList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, contactList.size());
        }
    }
}