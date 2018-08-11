package tz.co.dfm.dfmradio.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import tz.co.dfm.dfmradio.R;

/**
 * Capstone-Stage-2 Created by aidan on 11/08/2018.
 */
public class DFMRadioWidgetService extends IntentService {
    public static final String UPDATE_WIDGET_ACTION = "tz.co.dfm.dfmradio.update_widget_action";

    public DFMRadioWidgetService(){
        super("DFMRadioWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String widgetAction = intent.getAction();
            if (UPDATE_WIDGET_ACTION.equals(widgetAction)) {
                updateWidgetAction();
            }
        }
    }
    private void updateWidgetAction() {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int widgetsIds[] = widgetManager.getAppWidgetIds(new ComponentName(this, DFMRadioWidget.class));
        widgetManager.notifyAppWidgetViewDataChanged(widgetsIds, R.id.gv_widget_favorite_episodes);
        DFMRadioWidget.updateDFMRadioWidget(this, widgetManager, widgetsIds);
    }

    public static void updateDFMWidget(Context context) {
        Intent intent = new Intent(context, DFMRadioWidgetService.class);
        intent.setAction(UPDATE_WIDGET_ACTION);
        context.startService(intent);
    }
}
