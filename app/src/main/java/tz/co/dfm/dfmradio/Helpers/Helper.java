package tz.co.dfm.dfmradio.Helpers;

import android.content.Context;

import tz.co.dfm.dfmradio.R;

@SuppressWarnings("StringBufferReplaceableByString")
public class Helper {
    public static String buildEpisodeSubTitle(String episodeDate, String episodeHostName, Context context) {
        return new StringBuilder()
                .append(episodeDate)
                .append(" ")
                .append(context.getString(R.string.by_string))
                .append(" ")
                .append(episodeHostName).toString();
    }
}
