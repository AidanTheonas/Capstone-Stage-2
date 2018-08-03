package tz.co.dfm.dfmradio.Ui.Fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import tz.co.dfm.dfmradio.R;
import tz.co.dfm.dfmradio.Ui.Activities.AboutUsActivity;
import tz.co.dfm.dfmradio.Ui.Activities.SettingsActivity;

public class SettingsPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notification_sound)));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference(getString(R.string.key_app_language)));

        Preference appVersionPreference = findPreference(getString(R.string.key_app_version));
        try {
            appVersionPreference.setSummary(getAppVersion());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Preference aboutUsPreference = findPreference(getString(R.string.key_about));
        aboutUsPreference.setOnPreferenceClickListener(preference -> {
            Intent settingsIntent = new Intent(getActivity(), AboutUsActivity.class);
            startActivity(settingsIntent);
            getActivity().overridePendingTransition(0, 0);
            return true;
        });
    }

    public String getAppVersion() throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        return packageInfo.versionName;
    }
}
