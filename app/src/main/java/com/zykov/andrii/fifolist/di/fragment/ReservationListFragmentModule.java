package com.zykov.andrii.fifolist.di.fragment;

import com.zykov.andrii.fifolist.di.scope.PerFragment;
import com.zykov.andrii.fifolist.fragment.reservationlist.IReservationListView;
import com.zykov.andrii.fifolist.fragment.reservationlist.ReservationListFragment;
import com.zykov.andrii.fifolist.fragment.reservationlist.ReservationListPresenter;

import dagger.Binds;
import dagger.Module;

@PerFragment
@Module()
public abstract class ReservationListFragmentModule {

    @Binds
    @PerFragment
    abstract IReservationListView provideReservationListView(ReservationListFragment fragment);

    @Binds
    @PerFragment
    abstract IReservationListView.IReservationListContract provideReservationListPresenter(ReservationListPresenter presenter);

}
