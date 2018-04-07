package com.zykov.andrii.fifolist.fragment.reservationlist;

import com.zykov.andrii.fifolist.db.entity.ReservationEntity;

import java.util.List;

public interface IReservationListView {
    void showReservationItems(List<ReservationEntity> mockData);

    void openReservationItemDetailsView(Long id);

    void showError(String string);

    interface IReservationListContract {
        void onResume();

        void onReservationItemSelected(Long id);

        void refreshReservationList();

        void onPause();
    }
}
