package tz.co.dfm.dfmradio.Models;

import android.provider.BaseColumns;

public class FavoriteEpisodesContract {
  private FavoriteEpisodesContract(){}

  public static final class FavoriteEpisodesEntry implements BaseColumns{
      public static final String TABLE_NAME = "favorite_episodes";
      public static final String COLUMN_EPISODE_ID = "episode_id";
      public static final String COLUMN_SHOW_NAME = "show_name";
      public static final String COLUMN_EPISODE_TITLE = "episode_title";
      public static final String COLUMN_EPISODE_DATE = "episode_date";
      public static final String COLUMN_EPISODE_DESCRIPTION = "episode_description";
      public static final String COLUMN_THUMBNAIL_FILE = "thumbnail_file";
      public static final String COLUMN_MEDIA_FILE = "media_file";
      public static final String COLUMN_SHOW_HOST = "show_host";
      public static final String COLUMN_MEDIA_TYPE = "media_type";
      public static final String COLUMN_TIMESTAMP = "timestamp";
  }
}
