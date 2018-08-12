package tz.co.dfm.dfmradio.Models;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

public interface FavoriteEpisodesColumns {
  @DataType(DataType.Type.INTEGER)
  @PrimaryKey
  @NotNull
  String COLUMN_ID = "_id";

  @DataType(DataType.Type.INTEGER)
  @Unique(onConflict = ConflictResolutionType.REPLACE)
  @NotNull
  String COLUMN_EPISODE_ID = "episode_id";

  @DataType(DataType.Type.TEXT)
  @NotNull
  String COLUMN_SHOW_NAME = "show_name";

  @DataType(DataType.Type.TEXT)
  @NotNull
  String COLUMN_EPISODE_TITLE = "episode_title";

  @DataType(DataType.Type.TEXT)
  @NotNull
  String COLUMN_EPISODE_DATE = "episode_date";

  @DataType(DataType.Type.TEXT)
  @NotNull
  String COLUMN_EPISODE_DESCRIPTION = "episode_description";

  @DataType(DataType.Type.TEXT)
  @NotNull
  String COLUMN_THUMBNAIL_FILE = "thumbnail_file";

  @DataType(DataType.Type.TEXT)
  @NotNull
  String COLUMN_MEDIA_FILE = "media_file";

  @DataType(DataType.Type.TEXT)
  @NotNull
  String COLUMN_SHOW_HOST = "show_host";

  @DataType(DataType.Type.INTEGER)
  @NotNull
  String COLUMN_MEDIA_TYPE = "media_type";

  @DataType(DataType.Type.INTEGER)
  String COLUMN_TIMESTAMP = "timestamp";
}
