package tz.co.dfm.dfmradio;

import android.app.Application;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Capstone-Stage-2 Created by aidan on 11/08/2018.
 */
public class DFMRadioApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }
}
