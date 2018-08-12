package tz.co.dfm.dfmradio.Models;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import static tz.co.dfm.dfmradio.Models.FavoriteEpisodesProvider.AUTHORITY;

@ContentProvider(authority = AUTHORITY, database = DataSource.class)
public class FavoriteEpisodesProvider {
  static final String AUTHORITY = "tz.co.dfm.dfmradio.content.android.provider";

  @TableEndpoint(table = DataSource.FAVORITE_EPISODES_TABLE)
  public static class FavoriteEpisodes {
    @ContentUri(
        path = "favoriteEpisodes",
        type = "vnd.android.cursor.dir/favoriteEpisodes",
        defaultSort = FavoriteEpisodesColumns.COLUMN_TIMESTAMP + " DESC")
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favoriteEpisodes");
  }
}
