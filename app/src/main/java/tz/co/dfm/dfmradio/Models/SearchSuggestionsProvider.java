package tz.co.dfm.dfmradio.Models;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "tz.co.dfm.dfmradio.content.android.searchSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @SuppressWarnings("ConstantConditions,Recycle")
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor defaultCursor = super.query(uri, projection, selection, selectionArgs, sortOrder);
        Uri searchResultLeftIcon = Uri.parse("android.resource://" + getContext().getPackageName() + "/drawable/ic_search_black_24dp");
        MatrixCursor generatedCursor = new MatrixCursor(defaultCursor.getColumnNames());
       for(int i = 0;i < defaultCursor.getCount() ; i++) {
           defaultCursor.moveToPosition(i);
           generatedCursor.addRow(new Object[]{
                   defaultCursor.getInt(defaultCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_FORMAT)),
                   searchResultLeftIcon,
                   defaultCursor.getString(defaultCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)),
                   defaultCursor.getString(defaultCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_QUERY)),
                   defaultCursor.getInt(defaultCursor.getColumnIndex(BaseColumns._ID))
           });
       }
        return generatedCursor;
    }
}
