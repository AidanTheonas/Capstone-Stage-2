package tz.co.dfm.dfmradio.Helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import tz.co.dfm.dfmradio.R;

public class Constants {
  public static final String BASE_URL = "https://www.capstone-stage-2.aidantheonas.com/public/";
  public static final String WATCH_EPISODE_PATH =
      "https://www.capstone-stage-2.aidantheonas.com/public/watchepisode/";
  public static final String SEARCH_EPISODE_PATH =
      "https://www.capstone-stage-2.aidantheonas.com/public/searchEpisode/";
  public static final int MEDIA_TYPE_AUDIO = 1;
  static final String THUMBNAILS_PATH =
      "https://www.capstone-stage-2.aidantheonas.com/public/storage/thumbnails/";
  public static final String ENG_LOCALE = "en";
  public static final String SW_LOCALE = "sw";
  public static final int FAVORITE_SHOWS = R.string.favorite_shows;
  public static final int ALL_SHOWS = R.string.all_shows;
  static final String VIDEO_PATH =
      "https://www.capstone-stage-2.aidantheonas.com/public/storage/media/video/";
  static final String AUDIO_PATH =
      "https://www.capstone-stage-2.aidantheonas.com/public/storage/media/audio/";
  private static final int D_YOUTH_SHOW = R.string.d_youth_show;
  private static final int D_HITS_SHOW = R.string.d_hits_show;
  private static final int D_TESTIFY_SHOW = R.string.d_testify_show;
  private static final int D_LOVE_SHOW = R.string.d_love_show;

  public static final int shows[] = {
    D_YOUTH_SHOW, D_HITS_SHOW, D_TESTIFY_SHOW, D_LOVE_SHOW,
  };

  public static final Map<Integer, Integer> showsId =
      Collections.unmodifiableMap(
          new HashMap<Integer, Integer>() {
            {
              put(ALL_SHOWS, 0);
              put(D_YOUTH_SHOW, 9);
              put(D_HITS_SHOW, 8);
              put(D_TESTIFY_SHOW, 7);
              put(D_LOVE_SHOW, 10);
            }
          });
}
