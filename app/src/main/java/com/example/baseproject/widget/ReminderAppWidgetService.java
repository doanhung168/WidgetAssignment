package com.example.baseproject.widget;

import static com.example.baseproject.widget.ReminderAppWidget.EXTRA_ITEM;
import static com.example.baseproject.widget.ReminderAppWidget.MY_EXTRA_CHECKED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.baseproject.R;
import com.example.baseproject.data.local_database.AppDatabase;
import com.example.baseproject.data.model.Reminder;
import com.example.baseproject.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ReminderAppWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ReminderRemoteViewsFactory();
    }

    class ReminderRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private List<Reminder> widgetItems;

        @Override
        public void onCreate() {
            widgetItems = new ArrayList<>();
        }

        @Override
        public void onDataSetChanged() {
            widgetItems = AppDatabase.getInstance(getApplicationContext())
                    .getReminderDAO()
                    .getReminders();
        }

        @Override
        public void onDestroy() {
            widgetItems = null;
        }

        @Override
        public int getCount() {
            return widgetItems.size();
        }


        @SuppressLint("RemoteViewLayout")
        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = null;

            Reminder reminder = widgetItems.get(position);
            if (reminder != null) {

                remoteViews = new RemoteViews(getPackageName(), R.layout.reminder_item);
                remoteViews.setTextViewText(R.id.tvReminderTime, Util.formatTime(reminder.getReminderTime()));

                if (reminder.getDone()) {
                    SpannableString spannableString = new SpannableString(reminder.getContent());
                    spannableString.setSpan(new StrikethroughSpan(), 0, reminder.getContent().length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, reminder.getContent().length(), 0);
                    remoteViews.setTextViewText(R.id.checkBox, spannableString);
                } else {
                    remoteViews.setTextViewText(R.id.checkBox, reminder.getContent());
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    remoteViews.setCompoundButtonChecked(R.id.checkBox, reminder.getDone());
                }

                Intent fillIntent = new Intent();
                fillIntent.putExtra(EXTRA_ITEM, reminder.getId());
                fillIntent.putExtra(MY_EXTRA_CHECKED, reminder.getDone());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    remoteViews.setOnCheckedChangeResponse(R.id.checkBox,
                            RemoteViews.RemoteResponse.fromFillInIntent(fillIntent));
                } else {
                    remoteViews.setOnClickFillInIntent(R.id.rootReminder, fillIntent);
                }

            }
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
