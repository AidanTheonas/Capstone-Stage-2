package tz.co.dfm.dfmradio.Helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static  final String BASE_URL = "http://192.168.137.1/capstone-project/public/";

    static  final String VIDEO_PATH = "http://192.168.137.1/capstone-project/storage/app/public/media/video/";

    static  final String AUDIO_PATH = "http://192.168.137.1/capstone-project/storage/app/public/media/audio/";

    static  final String THUMBNAILS_PATH = "http://192.168.137.1/capstone-project/storage/app/public/thumbnails/";

    public static final String WATCH_EPISODE_PATH = "http://192.168.137.1/capstone-project/public/watchepisode/";

    public static final String SEARCH_EPISODE_PATH = "http://192.168.137.1/capstone-project/public/searchEpisode/";

    public static final int MEDIA_TYPE_AUDIO = 1;

    public static final String FAVORITE_SHOWS = "Favorites";
    public static final String ALL_SHOWS = "All";
    private static final String D_YOUTH_SHOW = "D-Youth";
    private static final String D_HITS_SHOW = "D-Hits";
    private static final String D_TESTIFY_SHOW = "D-Testify";
    private static final String D_LOVE_SHOW = "D-Love";

    public static final String shows[] = {
            D_YOUTH_SHOW,
            D_HITS_SHOW,
            D_TESTIFY_SHOW,
            D_LOVE_SHOW,
    };

    public static final Map<String, Integer> showsId = Collections.unmodifiableMap(
            new HashMap<String, Integer>() {{
                put(ALL_SHOWS, 0);
                put(D_YOUTH_SHOW, 9);
                put(D_HITS_SHOW, 8);
                put(D_TESTIFY_SHOW, 7);
                put(D_LOVE_SHOW, 10);
            }});
}
