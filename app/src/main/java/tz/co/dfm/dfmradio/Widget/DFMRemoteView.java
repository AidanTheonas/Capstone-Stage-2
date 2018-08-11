package tz.co.dfm.dfmradio.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import tz.co.dfm.dfmradio.Models.FavoriteEpisodesColumns;
import tz.co.dfm.dfmradio.Models.FavoriteEpisodesProvider;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;

import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_MODEL;

/**
 * Capstone-Stage-2 Created by aidan on 11/08/2018.
 */
public class DFMRemoteView extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteView(this.getApplicationContext());
    }

    class WidgetRemoteView implements RemoteViewsService.RemoteViewsFactory{
        private Cursor cursor;
        private Context context;

        WidgetRemoteView(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() { }

        @Override
        public void onDataSetChanged() {
            if (cursor != null) cursor.close();
            cursor = getContentResolver().query(FavoriteEpisodesProvider.FavoriteEpisodes.CONTENT_URI,
                    null,
                    null,
                    null,
                    FavoriteEpisodesColumns.COLUMN_TIMESTAMP);
        }

        @Override
        public void onDestroy() {
            if (cursor != null) cursor.close();
        }

        @Override
        public int getCount() {
            if (cursor == null) {
                return 0;
            } else {
                return cursor.getCount();
            }
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (cursor == null || cursor.getCount() == 0)return null;

            cursor.moveToPosition(position);
            Shows shows = new Shows(
                    cursor.getInt(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_EPISODE_ID)),
                    cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_SHOW_NAME)),
                    cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_EPISODE_TITLE)),
                    cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_EPISODE_DATE)),
                    cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_EPISODE_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_THUMBNAIL_FILE)),
                    cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_MEDIA_FILE)),
                    cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_SHOW_HOST)),
                    cursor.getInt(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_MEDIA_TYPE))
            );

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.dfmradio_widget);
            remoteViews.setTextViewText(R.id.tv_widget_episode_title, shows.getEpisodeTitle());
            remoteViews.setTextViewText(R.id.tv_widget_episode_sub_title, shows.getEpisodeDate());

            Bitmap bitmap;
            try {
                bitmap = Picasso.get().load(shows.getEpisodeThumbnail()).get();
                remoteViews.setImageViewBitmap(R.id.iv_widget_episode_thumbnail,bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bundle extras = new Bundle();
            extras.putParcelable(SHOW_MODEL, shows);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            remoteViews.setOnClickFillInIntent(R.id.rl_widget_view, fillInIntent);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
