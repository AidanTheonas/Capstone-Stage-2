package tz.co.dfm.dfmradio.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Capstone-Stage-2 Created by aidan on 13/08/2018.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dfm_radio_podcast.db";
    private static final int DATABASE_VERSION = 10;

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_FAVORITE_EPISODES_TABLE =
                "CREATE TABLE " +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.TABLE_NAME + " ("
                        + FavoriteEpisodesContract.FavoriteEpisodesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_ID + " INTEGER UNIQUE NOT NULL," +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_TITLE + " TEXT NOT NULL," +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_DATE + " TEXT NOT NULL," +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_DESCRIPTION + " TEXT NOT NULL," +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_SHOW_NAME + " TEXT NOT NULL," +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_SHOW_HOST + " TEXT NOT NULL," +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_THUMBNAIL_FILE + " TEXT NOT NULL," +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_MEDIA_FILE + " TEXT NOT NULL," +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_MEDIA_TYPE + " INTEGER NOT NULL," +
                        FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL" +
                        ");";
        db.execSQL(SQL_FAVORITE_EPISODES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ FavoriteEpisodesContract.FavoriteEpisodesEntry.TABLE_NAME);
        onCreate(db);
    }
}
