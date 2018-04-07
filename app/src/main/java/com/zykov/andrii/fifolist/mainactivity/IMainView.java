package com.zykov.andrii.fifolist.mainactivity;

public interface IMainView {
    void startReservationListView();

    void processBackAction();

    void openReservationDetailedView(Long reservationId);

    interface IMainViewContract {
        void onResume();

        void onHomeItemSelected();

        void onAddReservationItemSelected();

        void onReservationItemSelected(Long reservationId);

    }
}
