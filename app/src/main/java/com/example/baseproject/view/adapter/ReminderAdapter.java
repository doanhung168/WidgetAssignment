package com.example.baseproject.view.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseproject.data.model.Reminder;
import com.example.baseproject.databinding.ReminderAdapterItemBinding;
import com.example.baseproject.util.Util;

public class ReminderAdapter extends ListAdapter<Reminder, ReminderAdapter.ReminderViewHolder> {

    private final OnClickReminderItemListener mOnClickListener;


    public ReminderAdapter(@NonNull DiffUtil.ItemCallback<Reminder> diffCallback, OnClickReminderItemListener onClickListener) {
        super(diffCallback);
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReminderAdapterItemBinding binding = ReminderAdapterItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ReminderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = getItem(position);
        if (reminder != null) {
            holder.mBinding.chkReminder.setText(reminder.getContent());
            holder.mBinding.tvReminderTime.setText(Util.formatTime(reminder.getReminderTime()));
            holder.mBinding.chkReminder.setChecked(reminder.getDone());

            if (reminder.getDone()) {
                holder.mBinding.chkReminder.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.mBinding.chkReminder.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            }

            holder.mBinding.chkReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    holder.mBinding.chkReminder.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.mBinding.chkReminder.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                }

                reminder.setDone(isChecked);
                mOnClickListener.onUpdateItem(reminder);
            });

        }
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {

        private final ReminderAdapterItemBinding mBinding;

        public ReminderViewHolder(@NonNull ReminderAdapterItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public interface OnClickReminderItemListener {
        void onUpdateItem(Reminder reminder);
    }
}
