package com.example.baseproject.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.navigation.NavDeepLinkBuilder;

import com.example.baseproject.MainActivity;
import com.example.baseproject.R;
import com.example.baseproject.data.local_database.AppDatabase;
import com.example.baseproject.util.Util;
import com.example.baseproject.view.ReminderListFragment;


public class ReminderAppWidget extends AppWidgetProvider {

    public static final String CHECKED_ACTION = "com.doanhung.widget_assignment.CHECKED_ACTION";
    public static final String EXTRA_ITEM = "com.doanhung.widget_assignment.EXTRA_ITEM";
    public static final String MY_EXTRA_CHECKED = "MY_EXTRA_CHECKED";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, ReminderAppWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.reminder_app_widget);
            remoteViews.setRemoteAdapter(R.id.list_view, intent);
            remoteViews.setEmptyView(R.id.list_view, R.id.empty_view);

            Intent checkedIntent = new Intent(context, ReminderAppWidget.class);
            checkedIntent.setAction(CHECKED_ACTION);
            PendingIntent checkedPendingIntent = PendingIntent.getBroadcast(context, 0, checkedIntent, 0 | PendingIntent.FLAG_MUTABLE);
            remoteViews.setPendingIntentTemplate(R.id.list_view, checkedPendingIntent);

            PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.addReminderFragment)
                    .createPendingIntent();
            remoteViews.setOnClickPendingIntent(R.id.imvAddReminder, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(CHECKED_ACTION)) {
            long reminderId = intent.getExtras().getLong(EXTRA_ITEM, -1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                boolean isChecked = intent.getExtras().getBoolean(RemoteViews.EXTRA_CHECKED, false);
                AppDatabase.getInstance(context.getApplicationContext())
                        .getReminderDAO()
                        .update(isChecked ? 1 : 0, reminderId);
                Util.notifyWidget(context);
            } else {
                boolean isChecked = intent.getExtras().getBoolean(MY_EXTRA_CHECKED, false);
                boolean newIsChecked = !isChecked;

                AppDatabase.getInstance(context.getApplicationContext())
                        .getReminderDAO()
                        .update(newIsChecked ? 1 : 0, reminderId);
                Util.notifyWidget(context);
            }

        }
        super.onReceive(context, intent);

    }
}