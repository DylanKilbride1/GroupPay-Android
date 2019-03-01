package grouppay.dylankilbride.com.adapters;

import android.view.View;

import grouppay.dylankilbride.com.models.Contact;
import grouppay.dylankilbride.com.models.GroupAccount;

public interface ItemClickListener {

  void onItemClick(Contact contact);

  void onItemClick(GroupAccount groupAccount);
}