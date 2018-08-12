package tz.co.dfm.dfmradio.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import tz.co.dfm.dfmradio.MainActivity;
import tz.co.dfm.dfmradio.R;
import tz.co.dfm.dfmradio.Ui.Activities.EpisodeDetails;

/** Implementation of App Widget functionality. */
public class DFMRadioWidget extends AppWidgetProvider {
  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    appWidgetManager.updateAppWidget(appWidgetId, generateViews(context));
  }

  private static RemoteViews generateViews(Context context) {
    RemoteViews remoteViews =
        new RemoteViews(context.getPackageName(), R.layout.widget_default_view);
    Intent remoteViewIntent = new Intent(context, DFMRemoteView.class);
    remoteViews.setRemoteAdapter(R.id.gv_widget_favorite_episodes, remoteViewIntent);

    // Loads episode details activity on play button click
    Intent playEpisodeIntent = new Intent(context, EpisodeDetails.class);
    PendingIntent playEpisodePendingIntent =
        PendingIntent.getActivity(context, 0, playEpisodeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setPendingIntentTemplate(
        R.id.gv_widget_favorite_episodes, playEpisodePendingIntent);

    // Open main activity so user can choose episodes to add
    Intent addEpisodesIntent = new Intent(context, MainActivity.class);
    PendingIntent addEpisodesPendingIntent =
        PendingIntent.getActivity(context, 0, addEpisodesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setOnClickPendingIntent(R.id.iv_add_more_episodes, addEpisodesPendingIntent);

    return remoteViews;
  }

  public static void updateDFMRadioWidget(
      Context context, AppWidgetManager manager, int[] widgetsIds) {
    for (int appWidgetId : widgetsIds) {
      updateAppWidget(context, manager, appWidgetId);
    }
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    DFMRadioWidgetService.updateDFMWidget(context);
  }

  @Override
  public void onEnabled(Context context) {}

  @Override
  public void onDisabled(Context context) {}
}
