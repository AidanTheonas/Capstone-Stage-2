package tz.co.dfm.dfmradio.Helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constants {
    private static final String FAVORITE_SHOWS = "Favorites";
    private static final String ALL_SHOWS = "All";
    private static final String D_YOUTH_SHOW = "D-Youth";
    private static final String D_HITS_SHOW = "D-Hits";
    private static final String D_BUSINESS_SHOW = "D-Business";
    private static final String D_IBADA_SHOW = "D-Ibada";
    private static final String D_KIDS_SHOW = "D-Kids";
    private static final String D_LOVE_SHOW = "D-Love";
    private static final String SERMONS_TEACHINGS_SHOW = "Sermons and Teachings";

    public static final String shows[] = {
            FAVORITE_SHOWS,
            ALL_SHOWS,
            D_YOUTH_SHOW,
            D_HITS_SHOW,
            D_BUSINESS_SHOW,
            D_IBADA_SHOW,
            D_KIDS_SHOW,
            D_LOVE_SHOW,
            SERMONS_TEACHINGS_SHOW
    };

    public static final Map<String, Integer> showsId = Collections.unmodifiableMap(
            new HashMap<String, Integer>() {{
                put(FAVORITE_SHOWS, -1);
                put(ALL_SHOWS, 0);
                put(D_YOUTH_SHOW, 1);
                put(D_HITS_SHOW, 2);
                put(D_BUSINESS_SHOW, 3);
                put(D_IBADA_SHOW, 4);
                put(D_KIDS_SHOW, 5);
                put(D_LOVE_SHOW, 6);
                put(SERMONS_TEACHINGS_SHOW, 7);
            }});
}
