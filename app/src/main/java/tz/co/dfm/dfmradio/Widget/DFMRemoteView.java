package tz.co.dfm.dfmradio.Widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import tz.co.dfm.dfmradio.Models.FavoriteEpisodesContract;
import tz.co.dfm.dfmradio.Models.FavoriteEpisodesProvider;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;

import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_MODEL;

/** Capstone-Stage-2 Created by aidan on 11/08/2018. */
public class DFMRemoteView extends RemoteViewsService {
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new WidgetRemoteView(this.getApplicationContext());
  }

  class WidgetRemoteView implements RemoteViewsService.RemoteViewsFactory {
    private Cursor cursor;
    private Context context;
    private FirebaseAnalytics firebaseAnalytics;
    static final String APP_WIDGET_CONTENT_PROVIDER_READING_ERROR = "APP_WIDGET_ERROR";
    static final int APP_WIDGET_ERROR_ID = 107;

    WidgetRemoteView(Context context) {
      this.context = context;
    }

    @Override
    public void onCreate() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        firebaseAnalytics.setMinimumSessionDuration(20000);
        firebaseAnalytics.setSessionTimeoutDuration(600000);
    }

    @Override
    public void onDataSetChanged() {
      if (cursor != null) cursor.close();
      try {
          Thread thread = new Thread() {
              public void run() {
                  cursor =
                          getContentResolver()
                                  .query(
                                          FavoriteEpisodesProvider.CONTENT_URI,
                                          null,
                                          null,
                                          null,
                                          FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_TIMESTAMP);
              }
          };
          thread.start();
        thread.join();
      } catch (InterruptedException e) {
          Bundle bundle = new Bundle();
          bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, APP_WIDGET_ERROR_ID);
          bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, e.getMessage());
          firebaseAnalytics.logEvent(APP_WIDGET_CONTENT_PROVIDER_READING_ERROR, bundle);
      }
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
      if (cursor == null || cursor.getCount() == 0) return null;

      cursor.moveToPosition(position);
      Shows shows =
          new Shows(
              cursor.getInt(cursor.getColumnIndex(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_ID)),
              cursor.getString(cursor.getColumnIndex(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_SHOW_NAME)),
              cursor.getString(cursor.getColumnIndex(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_TITLE)),
              cursor.getString(cursor.getColumnIndex(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_DATE)),
              cursor.getString(
                  cursor.getColumnIndex(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_DESCRIPTION)),
              cursor.getString(
                  cursor.getColumnIndex(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_THUMBNAIL_FILE)),
              cursor.getString(cursor.getColumnIndex(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_MEDIA_FILE)),
              cursor.getString(cursor.getColumnIndex(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_SHOW_HOST)),
              cursor.getInt(cursor.getColumnIndex(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_MEDIA_TYPE)));

      RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.dfmradio_widget);
      remoteViews.setTextViewText(R.id.tv_widget_episode_title, shows.getEpisodeTitle());
      remoteViews.setTextViewText(R.id.tv_widget_episode_sub_title, shows.getEpisodeDate());

      Bitmap bitmap;
      try {
        bitmap = Picasso.get().load(shows.getEpisodeThumbnail()).get();
        remoteViews.setImageViewBitmap(R.id.iv_widget_episode_thumbnail, bitmap);
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
