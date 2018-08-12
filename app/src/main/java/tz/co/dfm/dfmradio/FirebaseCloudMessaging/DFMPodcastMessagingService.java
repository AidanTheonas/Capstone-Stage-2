package tz.co.dfm.dfmradio.FirebaseCloudMessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.util.Map;

import tz.co.dfm.dfmradio.Helpers.Constants;
import tz.co.dfm.dfmradio.Helpers.Helper;
import tz.co.dfm.dfmradio.R;
import tz.co.dfm.dfmradio.Ui.Activities.LoadSharedMedia;

/** Capstone-Stage-2 Created by aidan on 11/08/2018. */
public class DFMPodcastMessagingService extends FirebaseMessagingService {
  public static final String EPISODE_ID_KEY = "episode_id";
  public static final String EPISODE_THUMBNAIL_KEY = "episode_thumbnail";
  public static final String EPISODE_TITLE = "episode_title";
  public static final String EPISODE_BODY = "episode_body";
  public static final String NOTIFICATION_CHANNEL_ID = "DFM_RADIO_NOTIFICATION_CHANNEL";
  public static final int PENDING_INTENT_REQUEST_CODE = 107;
  public static final int NOTIFICATION_ID = 108;
  public static final int MAX_BODY_LENGTH = 50;

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    Map<String, String> receivedData = remoteMessage.getData();
    String episodeId = receivedData.get(EPISODE_ID_KEY);
    String episodeThumbnail = receivedData.get(EPISODE_THUMBNAIL_KEY);
    String episodeTitle = receivedData.get(EPISODE_TITLE);
    String episodeBody = receivedData.get(EPISODE_BODY);
    displayNotification(episodeId, episodeThumbnail, episodeTitle, episodeBody);
  }

  private void displayNotification(
      String episodeID, String episodeThumbnail, String episodeTitle, String episodeBody) {

    if (episodeBody.length() > MAX_BODY_LENGTH) {
      episodeBody = episodeBody.substring(0, MAX_BODY_LENGTH).concat("\u2026");
    }

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    if (!sharedPreferences.getBoolean(
        this.getResources().getString(R.string.key_notifications), true)) return;

    Uri mediaUri = Uri.parse(Constants.WATCH_EPISODE_PATH.concat(episodeID));
    Intent intent = new Intent(this, LoadSharedMedia.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.setData(mediaUri);

    PendingIntent pendingIntent =
        PendingIntent.getActivity(
            this, PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT);

    String notificationPreference =
        sharedPreferences.getString(
            this.getResources().getString(R.string.key_notification_sound), "DEFAULT_SOUND");
    Uri alertNotification = Uri.parse(notificationPreference);

    Bitmap bitmap;
    try {
      bitmap = Picasso.get().load(Helper.buildThumbnailUrl(episodeThumbnail)).get();
      NotificationCompat.Builder notificationBuilder =
          new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
              .setSmallIcon(R.drawable.ic_notification_icon)
              .setLargeIcon(bitmap)
              .setContentTitle(episodeTitle)
              .setContentText(episodeBody)
              .setAutoCancel(true)
              .setSound(alertNotification)
              .setContentIntent(pendingIntent);

      NotificationManager notificationManager =
          (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      if (notificationManager != null)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
