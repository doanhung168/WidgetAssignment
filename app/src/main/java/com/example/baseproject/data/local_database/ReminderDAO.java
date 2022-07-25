package com.example.baseproject.data.local_database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.baseproject.data.model.Reminder;

import java.util.List;

@Dao
public interface ReminderDAO {

    @Query("SELECT * FROM Reminder")
    List<Reminder> getReminders();

    @Query("UPDATE Reminder SET done = :done WHERE id = :id")
    void update(int done, long id);

    @Insert
    long insert(Reminder reminder);

    @Update
    int update(Reminder reminder);

    @Query("DELETE FROM Reminder WHERE done = 1")
    void deleteReminderCompletely();

}
