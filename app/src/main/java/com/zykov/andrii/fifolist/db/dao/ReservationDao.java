package com.zykov.andrii.fifolist.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.zykov.andrii.fifolist.db.entity.ReservationEntity;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface ReservationDao {

    @Insert()
    Long insert(ReservationEntity reservations);

    @Query("SELECT * FROM reservation order by _id asc")
    Maybe<List<ReservationEntity>> getAll();

    @Query("SELECT * FROM reservation WHERE _id = :reservationId")
    Maybe<ReservationEntity> get(Long reservationId);

    @Delete
    int remove(ReservationEntity... reservations);

    @Update
    int update(ReservationEntity... reservationEntities);

}
