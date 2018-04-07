package com.zykov.andrii.fifolist.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.zykov.andrii.fifolist.db.dao.ReservationDao;
import com.zykov.andrii.fifolist.db.entity.ReservationEntity;

@Database(entities = {ReservationEntity.class}, version = 1)
public abstract class FifoListDataBase extends RoomDatabase {

    private static FifoListDataBase instance;

    public abstract ReservationDao getReservationDao();

    public static FifoListDataBase getInstance(Context context) {
        if (instance == null)
            synchronized (FifoListDataBase.class) {
                if (instance == null)
                    instance = Room.databaseBuilder(context, FifoListDataBase.class, "com.zykov.andrii.fifolist").build();
            }
        return instance;
    }

}
