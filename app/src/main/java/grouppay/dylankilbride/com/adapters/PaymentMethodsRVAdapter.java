package grouppay.dylankilbride.com.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import grouppay.dylankilbride.com.grouppay.R;
import grouppay.dylankilbride.com.models.Cards;

import static androidx.core.content.res.TypedArrayUtils.getString;

public class PaymentMethodsRVAdapter extends RecyclerView.Adapter<PaymentMethodsRVAdapter.ViewHolder>{

  public List<Cards> cardsList;
  private int itemLayout;
  private Context context;
  private static ItemClickListener clickInterface;

  public PaymentMethodsRVAdapter(List<Cards> cardsList, int itemLayout) {
    this.cardsList = cardsList;
    this.itemLayout = itemLayout;
  }

  public PaymentMethodsRVAdapter(List<Cards> cardsList, Context context) {
    this.cardsList = cardsList;
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
    final Cards cards = cardsList.get(position);
    viewHolder.cardType.setText(cards.getCardType());
    viewHolder.cardNumberPreview.setText(cards.getLastFour());
    if(cards.getCardType().equals("Visa")) {
      viewHolder.cardTypeIcon.setBackgroundResource(R.drawable.visa_icon);
    } else if(cards.getCardType().equals("MasterCard")) {
      viewHolder.cardTypeIcon.setBackgroundResource(R.drawable.mastercard_icon);
    } else if(cards.getCardType().equals("Maestro")) {
      viewHolder.cardTypeIcon.setBackgroundResource(R.drawable.maestro_icon);
    } else if(cards.getCardType().equals("Discover")) {
      viewHolder.cardTypeIcon.setBackgroundResource(R.drawable.discover_icon);
    } else if(cards.getCardType().equals("American Express")) {
      viewHolder.cardTypeIcon.setBackgroundResource(R.drawable.amex_icon);
    } else {
      viewHolder.cardTypeIcon.setBackgroundResource(R.drawable.nav_drawer_card_icon);
    }
  }

  @Override
  public int getItemCount() {
    return cardsList.size();
  }


  public void setAccounts(List<Cards> cardsList) {
    this.cardsList = cardsList;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cardType, cardNumberPreview;
    public ImageView cardTypeIcon;


    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      cardType = itemView.findViewById(R.id.cardTypeText);
      cardNumberPreview = itemView.findViewById(R.id.cardNumberPreview);
      cardTypeIcon = itemView.findViewById(R.id.cardTypeImg);
    }

    @Override
    public void onClick(View view) {
      if(view.equals(cardType)){
        removeAt(getAdapterPosition());
      }
    }

    public void removeAt(int position) {
      cardsList.remove(position);
      notifyItemRemoved(position);
      notifyItemRangeChanged(position, cardsList.size());
    }
  }
}

