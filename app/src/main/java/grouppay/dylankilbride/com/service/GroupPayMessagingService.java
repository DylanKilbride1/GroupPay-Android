package grouppay.dylankilbride.com.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import grouppay.dylankilbride.com.activities.Home;
import grouppay.dylankilbride.com.grouppay.R;

public class GroupPayMessagingService extends FirebaseMessagingService {

  @Override
  public void onNewToken(String token) {
    super.onNewToken(token);
    Log.e("newToken", token);
    getSharedPreferences("_", MODE_PRIVATE).edit().putString("FirebaseDeviceToken", token).apply();
  }

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
//
//    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
//        .setContentTitle(remoteMessage.getNotification().getTitle())
//        .setContentText(remoteMessage.getNotification().getBody())
//        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        .setStyle(new NotificationCompat.BigTextStyle())
//        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//        .setSmallIcon(R.mipmap.ic_grouppay_launcher)
//        .setAutoCancel(true);
//
//    NotificationManager notificationManager =
//        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//    notificationManager.notify(0, notificationBuilder.build());
    Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
    Intent intent = new Intent(this, Home.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    String channelId = "Default";
    NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(remoteMessage.getNotification().getTitle())
        .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
      manager.createNotificationChannel(channel);
    }
    manager.notify(0, builder.build());
  }

  public static String getToken(Context context) {
    return context.getSharedPreferences("_", MODE_PRIVATE).getString("FirebaseDeviceToken", null);
  }
}