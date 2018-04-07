package com.zykov.andrii.fifolist.fragment.reservationdetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zykov.andrii.fifolist.R;
import com.zykov.andrii.fifolist.fragment.BackButtonHandler;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Observable;

public class ReservationDetailsFragment extends Fragment implements IReservationDetailsView , BackButtonHandler{

    public static String TAG = ReservationDetailsFragment.class.getSimpleName();

    private static final String RESERVATION_ID_STRING_IDENTIFIER = "RESERVATION_ID_STRING_IDENTIFIER";

    /**
     * if id equals null, we will create new reservation, otherwise we are editing existing one
     *
     * @param reservationId
     * @return Fragment
     */

    public static Fragment getInstance(Long reservationId) {
        Fragment f = new ReservationDetailsFragment();
        if (reservationId != null) {
            Bundle args = new Bundle();
            args.putLong(RESERVATION_ID_STRING_IDENTIFIER, reservationId);
            f.setArguments(args);
        }
        return f;
    }

    private RxPermissions rxPermissions;

    @BindView(R.id.fragment_reservation_details_reservation_image)
    ImageView reservationImageView;

    @BindView(R.id.fragment_reservation_details_full_name_edit_text)
    EditText fullNameEditText;

    @BindView(R.id.fragment_reservation_details_phone_number_edit_text)
    EditText phoneNumberEditText;

    @BindView(R.id.fragment_reservation_details_remove_button)
    Button removeReservationButton;

    @BindView(R.id.fragment_reservation_details_reservation_image_progress_bar)
    ProgressBar imageProgressBar;

    @BindView(R.id.fragment_reservation_details_save_button)
    Button saveButton;

    @Inject
    IReservationDetailsContract presenter;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_reservation_details_layout, container, false);
        ButterKnife.bind(this, view);
        Long reservationId = null;
        if (getArguments() != null && getArguments().containsKey(RESERVATION_ID_STRING_IDENTIFIER))
            reservationId = getArguments().getLong(RESERVATION_ID_STRING_IDENTIFIER);
        presenter.setReservationId(reservationId);
        rxPermissions = new RxPermissions(getActivity());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.getItem(0).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick(R.id.fragment_reservation_details_save_button)
    void onSaveReservationButtonClicked() {
        presenter.saveReservation();
    }

    @OnClick(R.id.fragment_reservation_details_remove_button)
    void onRemoveReservationButtonClicked() {
        presenter.removeReservation();
    }

    @OnClick(R.id.fragment_reservation_details_reservation_image)
    void onReservationImageClicked() {
        presenter.onReservationImageClicked();
    }

    @OnTextChanged(R.id.fragment_reservation_details_full_name_edit_text)
    void onFullNameTextChange(Editable text) {
        presenter.onFullNameTextChanged(text.toString());
    }

    @OnTextChanged(R.id.fragment_reservation_details_phone_number_edit_text)
    void onPhoneNumberTextChanged(Editable text) {
        presenter.onPhoneNumberTextChanged(text.toString());
    }

    @Override
    public void setFullName(String fullName) {
        fullNameEditText.setText(fullName);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        phoneNumberEditText.setText(phoneNumber);
    }


    /**
     * id imageFile is null, will show placeholder
     *
     * @param imageFile
     */

    @Override
    public void setReservationImage(File imageFile) {
        if (imageFile == null) {
            reservationImageView.setImageResource(R.drawable.ic_add_picture);
        } else {
            Uri imageUri = Uri.fromFile(imageFile);
            Glide.with(this).load(imageUri).into(reservationImageView);
        }
        reservationImageView.setVisibility(View.VISIBLE);
        imageProgressBar.setVisibility(View.GONE);
    }

    @Override
    public Observable<Boolean> getPermissionsObservable(String... permissions) {
        return rxPermissions.request(permissions);
    }

    @Override
    public Observable<Uri> getRequestImageObservable(Sources source) {
        return RxImagePicker.with(getActivity()).requestImage(source);
    }

    @Override
    public void showImageEditDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.fragment_reservation_details_edit_image_options_dialog_title)
                .setMessage(R.string.fragment_reservation_details_edit_image_options_dialog_message)
                .setPositiveButton(getString(R.string.fragment_reservation_details_edit_image_edit_button),
                        (dialogInterface, i) -> presenter.onEditImageClicked())
                .setNegativeButton(getString(R.string.fragment_reservation_details_edit_image_remove_button),
                        (dialogInterface, i) -> presenter.onRemoveImageClicked())
                .setNeutralButton(getString(android.R.string.cancel), null)
                .show();
    }

    @Override
    public void hideSaveButton(boolean b) {
        saveButton.setVisibility(b ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showError(String errorMessage) {
        Activity activity;
        if ((activity = getActivity()) != null && !activity.isFinishing()) {
            new AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.fragment_reservation_details_remove_error_dialog_title))
                    .setMessage(errorMessage)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    }

    @Override
    public void onRemoveReservationSuccess() {
        Activity activity;
        if ((activity = getActivity()) != null && !activity.isFinishing()) {
            Toast.makeText(activity, R.string.fragment_reservation_details_remove_success_dialog_message, Toast.LENGTH_SHORT).show();
            closeFragment(activity);
        }
    }

    @Override
    public void onSaveReservationSuccess() {
        Activity activity;
        if ((activity = getActivity()) != null && !activity.isFinishing()) {
            Toast.makeText(activity, R.string.fragment_reservation_details_save_success_dialog_message, Toast.LENGTH_SHORT).show();
            closeFragment(activity);
        }
    }

    private void closeFragment(Activity activity) {
        View view;
        if ((view = activity.getCurrentFocus()) != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        ((ReservationDetailsFragmentListener) activity).closeReservationDetailsFragment();
    }

    @Override
    public void showRemoveReservationConfirmationDialog() {
        Activity activity;
        if ((activity = getActivity()) != null && !activity.isFinishing()) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.fragment_reservation_details_confirm_remove_reservation_title)
                    .setMessage(R.string.fragment_reservation_details_confirm_remove_reservation_message)
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> presenter.removeReservationConfirmed())
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        }
    }

    @Override
    public boolean onBackPressed() {
        return presenter.onBackButtonPressed();
    }

    public interface ReservationDetailsFragmentListener {
        void closeReservationDetailsFragment();
    }

}
