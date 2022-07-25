package com.example.baseproject.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.baseproject.R;
import com.example.baseproject.ReminderViewModel;
import com.example.baseproject.data.model.Reminder;
import com.example.baseproject.databinding.FragmentReminderListBinding;
import com.example.baseproject.util.Util;
import com.example.baseproject.view.adapter.ReminderAdapter;


public class ReminderListFragment extends Fragment implements ReminderAdapter.OnClickReminderItemListener {

    private FragmentReminderListBinding mBinding;
    private ReminderViewModel mReminderViewModel;
    private ReminderAdapter mReminderAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentReminderListBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        setUpRcvReminderList();
        setUpBtnToAddItemFragment();
        setUpToolBarAction();
    }

    private void initViewModel() {
        mReminderViewModel = new ViewModelProvider(requireActivity()).get(ReminderViewModel.class);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpRcvReminderList() {
        mBinding.rcvReminderList.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        mReminderAdapter = new ReminderAdapter(Reminder.DIFF_CALLBACK, this);
        mBinding.rcvReminderList.setAdapter(mReminderAdapter);

        mReminderViewModel.loadAllReminder();
        mReminderViewModel.getReminderList().observe(getViewLifecycleOwner(), reminders -> {
            mReminderAdapter.submitList(reminders);
            mReminderAdapter.notifyDataSetChanged();
        });

    }

    private void setUpBtnToAddItemFragment() {
        mBinding.btnToAddItemFragment.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_reminderListFragment_to_addReminderFragment)
        );
    }

    private void setUpToolBarAction() {
        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.deleteReminder) {
                mReminderViewModel.deleteReminderCompletely();
                mReminderViewModel.loadAllReminder();
                Util.notifyWidget(requireContext());
            }
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mReminderViewModel.loadAllReminder();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onUpdateItem(Reminder reminder) {
        mReminderViewModel.updateReminder(reminder);
        Util.notifyWidget(requireContext());
    }
}