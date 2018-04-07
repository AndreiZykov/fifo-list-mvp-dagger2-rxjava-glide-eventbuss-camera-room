package com.zykov.andrii.fifolist.mainactivity;

import android.content.Context;

import javax.inject.Inject;

public class MainViewPresenter implements IMainView.IMainViewContract {

    private final IMainView view;

    private Context context;

    @Inject
    public MainViewPresenter(IMainView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onResume() {
        view.startReservationListView();
    }

    @Override
    public void onHomeItemSelected() {
        view.processBackAction();
    }

    @Override
    public void onAddReservationItemSelected() {
        view.openReservationDetailedView(null);
    }

    @Override
    public void onReservationItemSelected(Long reservationId) {
        view.openReservationDetailedView(reservationId);
    }
}
