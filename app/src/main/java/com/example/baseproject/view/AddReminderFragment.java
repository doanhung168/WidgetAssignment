package com.example.baseproject.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.baseproject.R;
import com.example.baseproject.ReminderViewModel;
import com.example.baseproject.data.model.Reminder;
import com.example.baseproject.databinding.FragmentAddReminderBinding;
import com.example.baseproject.util.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddReminderFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private FragmentAddReminderBinding mBinding;
    private ReminderViewModel mReminderViewModel;

    private int mDay, mMonth, mYear, mHour, mMinute;
    private int mSavedDay, mSavedMonth, mSavedYear, mSavedHour, mSavedMinute;
    private Date mDate;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddReminderBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();
        actionClickBtnChooseTime();
        actionClickBtnAddNewRemind();

    }

    private void initViewModel() {
        mReminderViewModel = new ViewModelProvider(requireActivity()).get(ReminderViewModel.class);
    }

    private void actionClickBtnAddNewRemind() {
        mBinding.btnAddNewRemind.setOnClickListener(v -> {
            Util.hideKeyboard(requireActivity());
            String content = Objects.requireNonNull(mBinding.edtRemind.getText()).toString().trim();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(getContext(), getString(R.string.remind_empty_message), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mDate == null) {
                Toast.makeText(getContext(), getString(R.string.time_empty_message), Toast.LENGTH_SHORT).show();
                return;
            }

            boolean result = mReminderViewModel.addReminder(new Reminder(content, mDate.getTime()));

            if (result) {
                Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .popBackStack();
                Util.notifyWidget(requireContext());
                mReminderViewModel.loadAllReminder();
            } else {
                Toast.makeText(requireContext(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void actionClickBtnChooseTime() {

        mBinding.btnTimePicker.setOnClickListener(v -> {
            Util.hideKeyboard(requireActivity());
            getDateTimeCalendar();
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    this, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    private void getDateTimeCalendar() {
        Calendar calendar = Calendar.getInstance();
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMonth = calendar.get(Calendar.MONTH);
        mYear = calendar.get(Calendar.YEAR);
        mHour = calendar.get(Calendar.HOUR);
        mMinute = calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mSavedDay = dayOfMonth;
        mSavedMonth = month;
        mSavedYear = year;

        getDateTimeCalendar();

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), this, mHour, mMinute, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mSavedHour = hourOfDay;
        mSavedMinute = minute;

        mDate = new Date(mSavedYear, mSavedMonth, mSavedDay, mSavedHour, mSavedMinute);
        mBinding.btnTimePicker.setText(Util.formatTime(mDate.getTime()));
    }
}