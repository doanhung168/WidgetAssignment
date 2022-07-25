package com.example.baseproject;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.baseproject.data.local_database.AppDatabase;
import com.example.baseproject.data.model.Reminder;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Reminder>> mReminderList = new MutableLiveData<>();

    public LiveData<List<Reminder>> getReminderList() {
        return mReminderList;
    }

    public ReminderViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadAllReminder() {
        mReminderList.setValue(
                AppDatabase.getInstance(getApplication())
                        .getReminderDAO()
                        .getReminders()
        );
    }

    public boolean addReminder(Reminder reminder) {
        long id = AppDatabase.getInstance(getApplication().getApplicationContext())
                .getReminderDAO()
                .insert(reminder);
        return id > 0;
    }

    public void updateReminder(Reminder reminder) {
        AppDatabase.getInstance(getApplication().getApplicationContext())
                .getReminderDAO()
                .update(reminder);
    }

    public void deleteReminderCompletely() {
        AppDatabase.getInstance(getApplication().getApplicationContext())
                .getReminderDAO()
                .deleteReminderCompletely();
    }


}
