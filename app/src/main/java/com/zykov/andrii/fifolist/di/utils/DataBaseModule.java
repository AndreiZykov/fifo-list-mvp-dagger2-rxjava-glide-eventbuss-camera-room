package com.zykov.andrii.fifolist.di.utils;

import android.content.Context;

import com.zykov.andrii.fifolist.db.FifoListDataBase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataBaseModule {
    @Singleton
    @Provides
    FifoListDataBase provideDataBase(Context context) {
        return FifoListDataBase.getInstance(context);
    }
}
