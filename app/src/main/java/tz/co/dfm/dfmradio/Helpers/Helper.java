package tz.co.dfm.dfmradio.Helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Date;
import java.util.Locale;

import static tz.co.dfm.dfmradio.Helpers.Constants.AUDIO_PATH;
import static tz.co.dfm.dfmradio.Helpers.Constants.MEDIA_TYPE_AUDIO;
import static tz.co.dfm.dfmradio.Helpers.Constants.THUMBNAILS_PATH;
import static tz.co.dfm.dfmradio.Helpers.Constants.VIDEO_PATH;

@SuppressWarnings("StringBufferReplaceableByString")
public class Helper {
  public static String buildEpisodeSubTitle(
      String episodeDate, String episodeHostName, String showName) {
    return new StringBuilder()
        .append(episodeDate)
        .append(" | ")
        .append(episodeHostName)
        .append(" | ")
        .append(showName)
        .toString();
  }

  public static String buildThumbnailUrl(String thumbnailFile) {
    return new StringBuilder().append(THUMBNAILS_PATH).append(thumbnailFile).toString();
  }

  public static String buildMediaUrl(String mediaUrl, int mediaType) {
    if (mediaType == MEDIA_TYPE_AUDIO) {
      return new StringBuilder().append(AUDIO_PATH).append(mediaUrl).toString();
    } else {
      return new StringBuilder().append(VIDEO_PATH).append(mediaUrl).toString();
    }
  }

  public static int getCurrentTimeAsInteger() {
    return (int) (new Date().getTime() / 1000);
  }

  public static ContextWrapper updateLocale(Context context, Locale lang) {
    Resources resources = context.getApplicationContext().getResources();
    Configuration overrideConfiguration = resources.getConfiguration();
    Locale.setDefault(lang);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      overrideConfiguration.setLocale(lang);
      LocaleList localeList = new LocaleList(lang);
      LocaleList.setDefault(localeList);
      overrideConfiguration.setLocales(localeList);
      context = context.createConfigurationContext(overrideConfiguration);

    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      overrideConfiguration.setLocale(lang);
    } else {
      overrideConfiguration.locale = lang;
    }
    resources.updateConfiguration(
        overrideConfiguration, context.getResources().getDisplayMetrics());

    return new ContextWrapper(context);
  }
}
