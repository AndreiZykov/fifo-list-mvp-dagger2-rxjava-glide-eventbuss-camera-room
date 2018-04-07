package com.zykov.andrii.fifolist.di;

import android.content.Context;

import com.zykov.andrii.fifolist.FifoListApplication;
import com.zykov.andrii.fifolist.di.utils.DataBaseModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {ActivityInjectorModule.class, DataBaseModule.class, FragmentInjectorModule.class})
public interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder setContext(Context context);
        ApplicationComponent build();
    }
    void inject(FifoListApplication fifoListApplication);
}
