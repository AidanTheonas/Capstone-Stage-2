package tz.co.dfm.dfmradio.Ui.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;

import java.util.Locale;

import tz.co.dfm.dfmradio.R;
import tz.co.dfm.dfmradio.Ui.Fragments.SettingsPreferenceFragment;

import static tz.co.dfm.dfmradio.Helpers.Constants.ENG_LOCALE;
import static tz.co.dfm.dfmradio.Helpers.Constants.SW_LOCALE;
import static tz.co.dfm.dfmradio.Helpers.Helper.updateLocale;

public class SettingsActivity extends AppCompatPreferenceActivity
    implements SharedPreferences.OnSharedPreferenceChangeListener {
  public static final String LANGUAGE_CHANGED = "LANGUAGE_CHANGED";
  public static boolean languageChanged = false;
  /**
   * A preference value change listener that updates the preference's summary to reflect its new
   * value.
   */
  private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
      (preference, newValue) -> {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
          // For list preferences, look up the correct display value in
          // the preference's 'entries' list.
          ListPreference listPreference = (ListPreference) preference;
          int index = listPreference.findIndexOfValue(stringValue);

          // Set the summary to reflect the new value.
          preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

        } else if (preference instanceof RingtonePreference) {
          // For ringtone preferences, look up the correct display value
          // using RingtoneManager.
          if (TextUtils.isEmpty(stringValue)) {
            // Empty values correspond to 'silent' (no ringtone).
            preference.setSummary(R.string.pref_ringtone_silent);

          } else {
            Ringtone ringtone =
                RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));

            if (ringtone == null) {
              // Clear the summary if there was a lookup error.
              preference.setSummary(R.string.choose_notification_alert_sound);
            } else {
              // Set the summary to reflect the new ringtone display
              // name.
              String name = ringtone.getTitle(preference.getContext());
              preference.setSummary(name);
            }
          }

        } else {
          preference.setSummary(stringValue);
        }
        return true;
      };

  public static void bindPreferenceSummaryToValue(Preference preference) {
    preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

    sBindPreferenceSummaryToValueListener.onPreferenceChange(
        preference,
        PreferenceManager.getDefaultSharedPreferences(preference.getContext())
            .getString(preference.getKey(), ""));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getFragmentManager()
        .beginTransaction()
        .replace(android.R.id.content, new SettingsPreferenceFragment())
        .commit();
    PreferenceManager.getDefaultSharedPreferences(this)
        .registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.trim().equals(getResources().getString(R.string.key_app_language).trim())) {
      languageChanged = true;
      if (sharedPreferences
          .getString(key, getResources().getString(R.string.english_lang))
          .trim()
          .equals(getResources().getString(R.string.english_lang))) {
        updateLocale(this, new Locale(ENG_LOCALE));
      } else {
        updateLocale(this, new Locale(SW_LOCALE));
      }
      recreate();
    }
  }

  @Override
  public void onBackPressed() {
    Intent intent = new Intent();
    intent.putExtra(LANGUAGE_CHANGED, languageChanged);
    setResult(RESULT_OK, intent);
    finish();
  }
}
