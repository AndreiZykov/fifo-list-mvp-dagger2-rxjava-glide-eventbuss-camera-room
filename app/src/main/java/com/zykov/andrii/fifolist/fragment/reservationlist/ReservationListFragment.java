package com.zykov.andrii.fifolist.fragment.reservationlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zykov.andrii.fifolist.R;
import com.zykov.andrii.fifolist.db.entity.ReservationEntity;
import com.zykov.andrii.fifolist.fragment.reservationlist.view.ReservationListAdapter;
import com.zykov.andrii.fifolist.mainactivity.MainActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ReservationListFragment extends Fragment implements IReservationListView {

    public static String TAG = ReservationListFragment.class.getSimpleName();

    public static Fragment getInstance() {
        return new ReservationListFragment();
    }

    @Inject
    IReservationListContract presenter;

    @BindView(R.id.fragment_reservation_list_recycler_view)
    RecyclerView reservationListRecyclerView;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_reservation_list_layout, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Reservation List");
        reservationListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reservationListRecyclerView.setAdapter(new ReservationListAdapter(id -> presenter.onReservationItemSelected(id)));
        return view;
    }

    @Override
    public void showReservationItems(List<ReservationEntity> data) {
        ((ReservationListAdapter) reservationListRecyclerView.getAdapter()).getData().clear();
        ((ReservationListAdapter) reservationListRecyclerView.getAdapter()).getData().addAll(data);
        reservationListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void openReservationItemDetailsView(Long reservationId) {
        Activity activity;
        if ((activity = getActivity()) != null && !activity.isFinishing()) {
            ((ReservationListFragmentListener) activity).onReservationItemSelected(reservationId);
        }
    }

    @Override
    public void showError(String errorMessage) {
        Activity activity;
        if ((activity = getActivity()) != null && !activity.isFinishing()) {
            new AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.fragment_reservation_details_remove_error_dialog_title))
                    .setMessage(errorMessage)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }

    public interface ReservationListFragmentListener {
        void onReservationItemSelected(Long id);
    }

}
