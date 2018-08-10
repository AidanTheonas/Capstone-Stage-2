package tz.co.dfm.dfmradio.Helpers;

import java.util.Date;

import static tz.co.dfm.dfmradio.Helpers.Constants.AUDIO_PATH;
import static tz.co.dfm.dfmradio.Helpers.Constants.MEDIA_TYPE_AUDIO;
import static tz.co.dfm.dfmradio.Helpers.Constants.THUMBNAILS_PATH;
import static tz.co.dfm.dfmradio.Helpers.Constants.VIDEO_PATH;

@SuppressWarnings("StringBufferReplaceableByString")
public class Helper {
    public static String buildEpisodeSubTitle(String episodeDate, String episodeHostName, String showName) {
        return new StringBuilder()
                .append(episodeDate)
                .append(" | ")
                .append(episodeHostName)
                .append(" | ")
                .append(showName).toString();
    }

    public static String buildThumbnailUrl(String thumbnailFile) {
        return new StringBuilder()
                .append(THUMBNAILS_PATH)
                .append(thumbnailFile)
                .toString();
    }

    public static String buildMediaUrl(String mediaUrl, int mediaType) {
        if (mediaType == MEDIA_TYPE_AUDIO) {
            return new StringBuilder()
                    .append(AUDIO_PATH)
                    .append(mediaUrl)
                    .toString();
        } else {
            return new StringBuilder()
                    .append(VIDEO_PATH)
                    .append(mediaUrl)
                    .toString();
        }

    }

    public static int getCurrentTimeAsInteger() {
        return (int) (new Date().getTime() / 1000);
    }
}
