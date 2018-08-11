package tz.co.dfm.dfmradio.FirebaseCloudMessaging;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Capstone-Stage-2 Created by aidan on 11/08/2018.
 */
public class DFMPodcastMessagingService extends FirebaseMessagingService {
    public static final String EPISODE_ID_KEY = "episode_id";
    public static final String EPISODE_THUMBNAIL_KEY = "episode_thumbnail";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> receivedData = remoteMessage.getData();
        String episodeId = receivedData.get("episode_title");
        Log.e("Received",remoteMessage.getData().toString());
    }
}
