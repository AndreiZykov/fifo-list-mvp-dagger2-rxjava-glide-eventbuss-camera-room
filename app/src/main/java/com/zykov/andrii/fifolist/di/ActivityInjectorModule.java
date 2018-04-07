package com.zykov.andrii.fifolist.di;

import com.zykov.andrii.fifolist.di.scope.PerActivity;
import com.zykov.andrii.fifolist.mainactivity.MainActivity;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;

@Module(includes = {AndroidInjectionModule.class})
public abstract class ActivityInjectorModule {
    @PerActivity
    @ContributesAndroidInjector(modules = {MainActivityModule.class})
    abstract MainActivity mainActivityInjector();
}
