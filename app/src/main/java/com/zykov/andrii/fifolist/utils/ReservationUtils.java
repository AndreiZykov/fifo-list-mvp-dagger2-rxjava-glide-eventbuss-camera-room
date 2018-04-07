package com.zykov.andrii.fifolist.utils;

import com.zykov.andrii.fifolist.db.entity.ReservationEntity;

/**
 * Created by andrii on 4/7/18.
 */

public class ReservationUtils {
    public static boolean isReservationNullOrEmpty(ReservationEntity reservationEntity) {
        return reservationEntity == null ||
                (isNullOrEmpty(reservationEntity.getImageUri()) &&
                        isNullOrEmpty(reservationEntity.getFullName()) &&
                        isNullOrEmpty(reservationEntity.getPhoneNumber()));
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
