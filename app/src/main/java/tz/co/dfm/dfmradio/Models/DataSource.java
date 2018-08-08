package tz.co.dfm.dfmradio.Models;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(
        version = DataSource.DATABASE_VERSION
)
public class DataSource {
    @Table(FavoriteEpisodesColumns.class)
    public static final String FAVORITE_EPISODES_TABLE = "tbl_favorite_episodes";
    static final int DATABASE_VERSION = 1;
}
