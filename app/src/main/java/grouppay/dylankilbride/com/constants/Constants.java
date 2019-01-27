package grouppay.dylankilbride.com.constants;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import grouppay.dylankilbride.com.grouppay.R;

public class Constants {

  Toolbar toolbar;

  public static final String LOCALHOST_SERVER_BASEURL = "http://10.0.2.2:8080";

//  public void setUpActionBar(Activity activity, int toolbarResource, int toolbarTitleString) {
//    Toolbar toolbar = (Toolbar) activity.findViewById(toolbarResource);
//    setSupportActionBar(toolbar);
//
//    if (getSupportActionBar() != null) {
//      getSupportActionBar().setDisplayShowCustomEnabled(true);
//      getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//      LayoutInflater inflator = LayoutInflater.from(this);
//      View v = inflator.inflate(R.layout.generic_titleview, null);
//
//      ((TextView) v.findViewById(R.id.title)).setText(R.string.toolbar_edit_email_address_title);
//      ((TextView) v.findViewById(R.id.title)).setTextSize(20);
//
//      this.getSupportActionBar().setCustomView(v);
//    }
//  }

}
