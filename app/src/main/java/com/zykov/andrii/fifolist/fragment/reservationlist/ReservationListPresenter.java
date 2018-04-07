package com.zykov.andrii.fifolist.fragment.reservationlist;

import android.content.Context;

import com.zykov.andrii.fifolist.R;
import com.zykov.andrii.fifolist.db.FifoListDataBase;
import com.zykov.andrii.fifolist.db.dao.ReservationDao;
import com.zykov.andrii.fifolist.db.entity.ReservationEntity;
import com.zykov.andrii.fifolist.event.ReservationDataUpdateEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import java.util.List;
import java.util.ArrayList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ReservationListPresenter implements IReservationListView.IReservationListContract {

    private final IReservationListView view;

    private final Context context;

    private final ReservationDao reservationDao;

    @Inject
    public ReservationListPresenter(Context context, IReservationListView view, FifoListDataBase dataBase) {
        this.context = context;
        this.view = view;
        this.reservationDao = dataBase.getReservationDao();
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        refreshReservationList();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void refreshReservationList() {
        reservationDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                            view.showReservationItems(data);
                        },
                        throwable -> {
                            view.showError(context.getString(R.string.fragment_reservation_list_can_not_refresh_list));
                        });
    }

    @Override
    public void onReservationItemSelected(Long id) {
        view.openReservationItemDetailsView(id);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onReservationDataUpdate(ReservationDataUpdateEvent event) {
        refreshReservationList();
    }
}
