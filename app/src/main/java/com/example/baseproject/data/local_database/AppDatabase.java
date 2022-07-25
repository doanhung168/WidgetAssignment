package com.example.baseproject.data.local_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.baseproject.data.model.Reminder;

@Database(entities = {Reminder.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "reminderDB";

    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        synchronized (AppDatabase.class) {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(
                                context.getApplicationContext(),
                                AppDatabase.class,
                                DATABASE_NAME
                        )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    protected AppDatabase() {
    }

    public abstract ReminderDAO getReminderDAO();

}
