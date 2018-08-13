package tz.co.dfm.dfmradio.Models;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

import tz.co.dfm.dfmradio.R;

@SuppressWarnings({"ConstantConditions", "StringBufferReplaceableByString"})
public class FavoriteEpisodesProvider extends ContentProvider {
    static final String AUTHORITY = "tz.co.dfm.dfmradio.content.android.provider";

    static final String FAVORITE_EPISODES = "favoriteEpisodes";

    static final String URL = "content://".concat(AUTHORITY).concat("/").concat(FAVORITE_EPISODES);
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static final int EPISODES = 1;
    static final int EPISODES_ID = 2;
    static final UriMatcher uriMatcher;
    @SuppressWarnings("unused")
    private static HashMap<String, String> FAVORITE_EPISODES_PROJECTION_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, FAVORITE_EPISODES, EPISODES);
        uriMatcher.addURI(AUTHORITY, FAVORITE_EPISODES+"/#", EPISODES_ID);
    }

    SQLiteDatabase sqLiteDatabase;

    public FavoriteEpisodesProvider() {
    }

    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return sqLiteDatabase != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FavoriteEpisodesContract.FavoriteEpisodesEntry.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case EPISODES:
                queryBuilder.setProjectionMap(FAVORITE_EPISODES_PROJECTION_MAP);
                break;

            case EPISODES_ID:
                queryBuilder.appendWhere(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_ID + " = " + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder.trim().equals("")) {
            sortOrder = FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_TIMESTAMP;
        }

        Cursor cursor = queryBuilder.query(sqLiteDatabase, projection, selection,
                selectionArgs, null, null, sortOrder);
        if (getContext() != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {

            case EPISODES:
                return "vnd.android.cursor.dir/vnd.dfm.favoriteepisodes";

            case EPISODES_ID:
                return "vnd.android.cursor.item/vnd.dfm.favoriteepisodes";
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.unsupported_uri).concat(uri.toString()));
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = sqLiteDatabase.insert(FavoriteEpisodesContract.FavoriteEpisodesEntry.TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            if (getContext() != null)
                getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException(getContext().getResources().getString(R.string.failed_to_add_record).concat(uri.toString()));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case EPISODES:
                count = sqLiteDatabase.delete(FavoriteEpisodesContract.FavoriteEpisodesEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case EPISODES_ID:
                String id = uri.getPathSegments().get(1);
                StringBuilder stringBuilder =new StringBuilder()
                .append(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_ID)
                        .append(" = ")
                        .append(id)
                        .append((!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""));
                count = sqLiteDatabase.delete(FavoriteEpisodesContract.FavoriteEpisodesEntry.TABLE_NAME,stringBuilder.toString(), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.unknown_uri).concat(uri.toString()));
        }

        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int count;
        switch (uriMatcher.match(uri)) {
            case EPISODES:
                count = sqLiteDatabase.update(FavoriteEpisodesContract.FavoriteEpisodesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case EPISODES_ID:
                StringBuilder stringBuilder = new StringBuilder()
                        .append(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_ID)
                        .append(" = ")
                        .append(uri.getPathSegments().get(1))
                        .append((!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""));
                count = sqLiteDatabase.update(FavoriteEpisodesContract.FavoriteEpisodesEntry.TABLE_NAME, values,stringBuilder.toString()
                        , selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.unknown_uri).concat(uri.toString()));
        }

        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
