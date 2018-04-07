package com.zykov.andrii.fifolist.fragment.reservationdetails;

import android.Manifest;
import android.content.Context;
import android.os.Environment;

import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.Sources;
import com.zykov.andrii.fifolist.R;
import com.zykov.andrii.fifolist.db.FifoListDataBase;
import com.zykov.andrii.fifolist.db.dao.ReservationDao;
import com.zykov.andrii.fifolist.db.entity.ReservationEntity;
import com.zykov.andrii.fifolist.event.ImageUpdateEvent;
import com.zykov.andrii.fifolist.utils.ReservationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReservationDetailsPresenter implements IReservationDetailsView.IReservationDetailsContract {

    private IReservationDetailsView view;

    private Context context;

    private final ReservationDao reservationDao;

    private ReservationEntity reservationEntity = new ReservationEntity();

    @Inject
    public ReservationDetailsPresenter(Context context, IReservationDetailsView view, FifoListDataBase dataBase) {
        this.context = context;
        this.view = view;
        this.reservationDao = dataBase.getReservationDao();
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onBackButtonPressed() {
        if (ReservationUtils.isReservationNullOrEmpty(reservationEntity)) {
            removeReservation();
            return true;
        }
        return false;
    }

    @Override
    public void onEditImageClicked() {
        checkPermissionsAndOpenCamera();
    }

    @Override
    public void onRemoveImageClicked() {
        checkReservation();
        reservationEntity.setImageUri(null);
        processReservationEntity(reservationEntity);
    }

    @Override
    public void setReservationId(Long reservationId) {
        if (reservationId != null) {
            reservationDao.get(reservationId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::processReservationEntity, throwable -> {
                        // if for some reason this reservation was not found, which is impossible because we use current data only
                        processReservationEntity(reservationEntity);
                    });
        } else {
            Observable.fromCallable(() -> reservationDao.insert(reservationEntity))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reservationId1 -> {
                        if (reservationId1 >= 0) {
                            reservationEntity.setId(reservationId1);
                            processReservationEntity(reservationEntity);
                        } else {
                            throw new Exception("save reservation to database failed;");
                        }
                    }, throwable -> view.showError(context.getString(R.string.fragment_reservation_details_error_saving_reservation_dialog_message)));
        }
    }

    @Override
    public void saveReservation() {
        Observable.fromCallable(() -> reservationDao.update(reservationEntity) >= 0).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    if (success)
                        view.onSaveReservationSuccess();
                    else
                        throw new Exception("save reservation to database failed;");
                }, throwable -> view.showError(context.getString(R.string.fragment_reservation_details_error_saving_reservation_dialog_message)));

    }

    @Override
    public void removeReservation() {
        view.showRemoveReservationConfirmationDialog();
    }

    @Override
    public void onFullNameTextChanged(String fullName) {
        this.reservationEntity.setFullName(fullName);
        checkReservation();
    }

    @Override
    public void onPhoneNumberTextChanged(String phoneNumber) {
        this.reservationEntity.setPhoneNumber(phoneNumber);
        checkReservation();
    }

    @Override
    public void removeReservationConfirmed() {
        Observable.fromCallable(() -> reservationDao.remove(reservationEntity) >= 0)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    if (success) {
                        reservationEntity = null;
                        view.onRemoveReservationSuccess();
                    } else {
                        throw new Exception("remove reservation from database failed;");
                    }
                }, throwable -> view.showError(context.getString(R.string.
                        fragment_reservation_details_error_removing_reservation_dialog_message)));
    }

    @Override
    public void onReservationImageClicked() {
        if (ReservationUtils.isNullOrEmpty(reservationEntity.getImageUri())) {
            checkPermissionsAndOpenCamera();
        } else {
            view.showImageEditDialog();
        }
    }

    private void checkPermissionsAndOpenCamera() {
        view.getPermissionsObservable(Manifest.permission.CAMERA).subscribe(granted -> {
            if (granted) {
                view.getRequestImageObservable(Sources.CAMERA)
                        .flatMap(uri -> RxImageConverters.uriToFile(context, uri, buildImageFile()))
                        .subscribe(imageFile -> {
                            reservationEntity.setImageUri(imageFile.getAbsolutePath());
                            Observable.fromCallable(() -> reservationDao.update(reservationEntity) >= 0)
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(success -> {
                                        EventBus.getDefault().post(new ImageUpdateEvent());
                                    }, throwable -> {
                                        view.showError(context.getString(R.string.fragment_reservation_details_camera_error));
                                    });
                        });
            } else {
                view.showError(context.getString(R.string.fragment_reservation_details_permissions_deny));
            }
        });
    }

    private File buildImageFile() throws IOException {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(String.valueOf(System.currentTimeMillis()) + "_", ".jpg", storageDir);
    }

    private void processReservationEntity(ReservationEntity reservationEntity) {
        this.reservationEntity = reservationEntity;
        view.setFullName(reservationEntity.getFullName());
        view.setPhoneNumber(reservationEntity.getPhoneNumber());
        view.setReservationImage(reservationEntity.getImageUri() == null ?
                null : new File(reservationEntity.getImageUri()));
        checkReservation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageUpdatedEvent(ImageUpdateEvent event) {
        checkReservation();
        if (reservationEntity != null)
            view.setReservationImage(reservationEntity.getImageUri() == null ? null : new File(reservationEntity.getImageUri()));
    }

    private void checkReservation() {
        if(ReservationUtils.isReservationNullOrEmpty(reservationEntity)){
            view.hideSaveButton(true);
        } else {
            view.hideSaveButton(false);
        }
    }

}
