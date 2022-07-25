package com.example.baseproject.data.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity
public class Reminder {


    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long mId;

    @ColumnInfo(name = "content")
    private String mContent;

    @ColumnInfo(name = "done")
    private boolean mDone;

    @ColumnInfo(name = "reminderTime")
    private long mReminderTime;

    public Reminder() {
    }

    @Ignore
    public Reminder(String content, long reminderTime) {
        this.mContent = content;
        this.mDone = false;
        this.mReminderTime = reminderTime;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public boolean getDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public long getReminderTime() {
        return mReminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.mReminderTime = reminderTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return mId == reminder.mId && mDone == reminder.mDone && mReminderTime == reminder.mReminderTime && Objects.equals(mContent, reminder.mContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mContent);
    }

    public final static DiffUtil.ItemCallback<Reminder> DIFF_CALLBACK = new DiffUtil.ItemCallback<Reminder>() {
        @Override
        public boolean areItemsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.mId == newItem.mId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.equals(newItem);
        }
    };
}
