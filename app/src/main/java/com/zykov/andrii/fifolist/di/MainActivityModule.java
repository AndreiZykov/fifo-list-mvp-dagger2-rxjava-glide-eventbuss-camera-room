package com.zykov.andrii.fifolist.di;

import com.zykov.andrii.fifolist.di.scope.PerActivity;
import com.zykov.andrii.fifolist.mainactivity.IMainView;
import com.zykov.andrii.fifolist.mainactivity.MainActivity;
import com.zykov.andrii.fifolist.mainactivity.MainViewPresenter;

import dagger.Binds;
import dagger.Module;

@Module(includes = {})
public abstract class MainActivityModule {

    @PerActivity
    @Binds
    abstract IMainView provideReservationListView(MainActivity activity);

    @PerActivity
    @Binds
    abstract IMainView.IMainViewContract provideReservationListPresenter(MainViewPresenter presenter);
}
