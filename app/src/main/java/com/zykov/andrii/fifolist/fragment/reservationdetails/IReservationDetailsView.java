package com.zykov.andrii.fifolist.fragment.reservationdetails;

import android.net.Uri;

import com.mlsdev.rximagepicker.Sources;

import java.io.File;

import io.reactivex.Observable;

/**
 * Created by andrii on 4/4/18.
 */

public interface IReservationDetailsView {
    void setFullName(String fullName);

    void setPhoneNumber(String phoneNumber);

    void onRemoveReservationSuccess();

    void showError(String s);

    void onSaveReservationSuccess();

    void showRemoveReservationConfirmationDialog();

    void setReservationImage(File imageFIle);

    Observable<Boolean> getPermissionsObservable(String... permissions);

    Observable<Uri> getRequestImageObservable(Sources soucre);
    
    void showImageEditDialog();

    void hideSaveButton(boolean b);

    interface IReservationDetailsContract {
        void onResume();

        void setReservationId(Long reservationId);

        void saveReservation();

        void removeReservation();

        void onFullNameTextChanged(String string);

        void onPhoneNumberTextChanged(String string);

        void removeReservationConfirmed();

        void onReservationImageClicked();

        void onPause();

        boolean onBackButtonPressed();

        void onEditImageClicked();

        void onRemoveImageClicked();
    }
}
