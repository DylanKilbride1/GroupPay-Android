package grouppay.dylankilbride.com.adapters;

import grouppay.dylankilbride.com.models.Cards;
import grouppay.dylankilbride.com.models.GroupAccount;
import grouppay.dylankilbride.com.models.User;

public interface ItemClickListener {

  void onItemClick(User contact);

  void onItemClick(GroupAccount groupAccount);

  void onItemClick(Cards card);
}