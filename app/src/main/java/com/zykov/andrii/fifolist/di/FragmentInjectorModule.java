package com.zykov.andrii.fifolist.di;

import com.zykov.andrii.fifolist.di.fragment.ReservationDetailsFragmentModule;
import com.zykov.andrii.fifolist.di.fragment.ReservationListFragmentModule;
import com.zykov.andrii.fifolist.di.scope.PerFragment;
import com.zykov.andrii.fifolist.fragment.reservationdetails.ReservationDetailsFragment;
import com.zykov.andrii.fifolist.fragment.reservationlist.ReservationListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Module(includes = {AndroidSupportInjectionModule.class})
abstract class FragmentInjectorModule {
    @PerFragment
    @ContributesAndroidInjector(modules = {ReservationListFragmentModule.class})
    abstract ReservationListFragment reservationListFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = {ReservationDetailsFragmentModule.class})
    abstract ReservationDetailsFragment reservationDetailsFragmentInjector();
}
