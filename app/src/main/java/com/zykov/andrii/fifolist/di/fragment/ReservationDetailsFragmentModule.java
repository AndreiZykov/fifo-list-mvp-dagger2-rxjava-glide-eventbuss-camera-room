package com.zykov.andrii.fifolist.di.fragment;

import com.zykov.andrii.fifolist.di.scope.PerFragment;
import com.zykov.andrii.fifolist.fragment.reservationdetails.IReservationDetailsView;
import com.zykov.andrii.fifolist.fragment.reservationdetails.ReservationDetailsFragment;
import com.zykov.andrii.fifolist.fragment.reservationdetails.ReservationDetailsPresenter;

import dagger.Binds;
import dagger.Module;

@PerFragment
@Module()
public abstract class ReservationDetailsFragmentModule {

    @Binds
    @PerFragment
    abstract IReservationDetailsView provideReservationDetailsView(ReservationDetailsFragment fragment);

    @Binds
    @PerFragment
    abstract IReservationDetailsView.IReservationDetailsContract provideReservationListPresenter(ReservationDetailsPresenter presenter);
}
