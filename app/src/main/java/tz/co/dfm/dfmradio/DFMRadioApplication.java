package tz.co.dfm.dfmradio;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

import static tz.co.dfm.dfmradio.Helpers.Constants.ENG_LOCALE;
import static tz.co.dfm.dfmradio.Helpers.Constants.SW_LOCALE;
import static tz.co.dfm.dfmradio.Helpers.Helper.updateLocale;

/** Capstone-Stage-2 Created by aidan on 11/08/2018. */
public class DFMRadioApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    FirebaseMessaging.getInstance().subscribeToTopic("all");
    updateLanguage();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    updateLanguage();
    super.onConfigurationChanged(newConfig);
  }

  public void updateLanguage() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    if (sharedPreferences
        .getString(
            getResources().getString(R.string.key_app_language),
            getResources().getString(R.string.english_lang))
        .trim()
        .equals(getResources().getString(R.string.english_lang))) {
      updateLocale(this, new Locale(ENG_LOCALE));
    } else {
      updateLocale(this, new Locale(SW_LOCALE));
    }
  }
}
