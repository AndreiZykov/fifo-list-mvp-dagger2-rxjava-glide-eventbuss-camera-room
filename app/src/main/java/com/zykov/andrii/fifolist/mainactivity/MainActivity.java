package com.zykov.andrii.fifolist.mainactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.zykov.andrii.fifolist.R;
import com.zykov.andrii.fifolist.event.ReservationDataUpdateEvent;
import com.zykov.andrii.fifolist.fragment.BackButtonHandler;
import com.zykov.andrii.fifolist.fragment.reservationdetails.ReservationDetailsFragment;
import com.zykov.andrii.fifolist.fragment.reservationlist.ReservationListFragment;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements IMainView, ReservationListFragment.ReservationListFragmentListener, ReservationDetailsFragment.ReservationDetailsFragmentListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private final int REQUEST_TAKE_PHOTO_RESULT = 1;

    private final int CAMERA_PERMISSION_RESULT = 2;

    @Inject
    public IMainViewContract presenter;

    private Menu menu;

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.fifo_toolbar));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // in our case always back button
                presenter.onHomeItemSelected();
                return true;
            case R.id.add_reservation_action:
                presenter.onAddReservationItemSelected();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void closeReservationDetailsFragment() {
        menu.getItem(0).setVisible(true);
        getSupportActionBar().setTitle(R.string.fragment_reservation_list_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportFragmentManager().popBackStackImmediate();
        EventBus.getDefault().post(new ReservationDataUpdateEvent());
    }

    @Override
    public void startReservationListView() {
        if (getSupportFragmentManager().findFragmentByTag(ReservationListFragment.TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_fragment_container, ReservationListFragment.getInstance(), ReservationListFragment.TAG)
                    .commit();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_fragment_container);
        if(fragment != null) {
            if (fragment instanceof BackButtonHandler) {
                if (((BackButtonHandler) fragment).onBackPressed()) {
                    return;
                }
            }
            if (fragment instanceof ReservationDetailsFragment) {
                closeReservationDetailsFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public void onReservationItemSelected(Long reservationId) {
        presenter.onReservationItemSelected(reservationId);
    }


    /**
     * if Long reservationId is null, we will open reservation editor for new item
     *
     * @param reservationId
     */

    @Override
    public void openReservationDetailedView(Long reservationId) {
        // to prevent double click quick work around
        if (getSupportFragmentManager().findFragmentByTag(ReservationDetailsFragment.TAG) == null) {
            getSupportActionBar().setTitle(
                    reservationId == null ?
                            getString(R.string.fragment_reservation_details_new_reservation_title) :
                            getString(R.string.fragment_reservation_details_update_reservation_title)
            );
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity_fragment_container, ReservationDetailsFragment.getInstance(reservationId), ReservationDetailsFragment.TAG)
                    .addToBackStack(ReservationListFragment.TAG)
                    .commit();
        }
    }

    // state
    @Override
    public void processBackAction() {
        onBackPressed();
    }

}
