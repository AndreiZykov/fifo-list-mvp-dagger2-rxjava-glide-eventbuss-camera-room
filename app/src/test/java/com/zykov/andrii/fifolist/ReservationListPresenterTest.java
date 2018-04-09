package com.zykov.andrii.fifolist;

import android.content.Context;

import com.zykov.andrii.fifolist.db.FifoListDataBase;
import com.zykov.andrii.fifolist.db.dao.ReservationDao;
import com.zykov.andrii.fifolist.db.entity.ReservationEntity;
import com.zykov.andrii.fifolist.fragment.reservationlist.IReservationListView;
import com.zykov.andrii.fifolist.fragment.reservationlist.ReservationListPresenter;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Maybe;

import java.util.List;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by andrii on 4/8/18.
 */

public class ReservationListPresenterTest {

    @ClassRule
    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

    @Mock
    private IReservationListView mockView;

    @Mock
    private FifoListDataBase mockDataBase;

    @Mock
    private Context mockContext;

    @Mock
    private ReservationDao mockReservationDao;

    @Captor
    ArgumentCaptor<List<ReservationEntity>> reservationEntityArgumentCaptor;

    private IReservationListView.IReservationListContract presenter;

    private List<ReservationEntity> mockReservationList = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockDataBase.getReservationDao()).thenReturn(mockReservationDao);
        presenter = new ReservationListPresenter(
                mockContext, mockView, mockDataBase
        );

        for(int i = 0; i < 10; i++)
            mockReservationList.add(new ReservationEntity());

    }

    @Test
    public void testRefreshReservationListSuccess(){
        when(mockReservationDao.getAll()).thenReturn(Maybe.just(mockReservationList));
        presenter.refreshReservationList();

        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView, times(1)).showReservationItems(reservationEntityArgumentCaptor.capture());
        assertEquals(mockReservationList.size(), reservationEntityArgumentCaptor.getValue().size());

    }

    @Test
    public void testRefreshReservationListError(){
        when(mockReservationDao.getAll()).thenReturn(Maybe.error(new Exception()));
        presenter.refreshReservationList();

        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView, times(1)).showError(null);

    }

    @Test
    public void test(){
        presenter.onReservationItemSelected(1L);
        InOrder inOrder = Mockito.inOrder(mockView);
        inOrder.verify(mockView, times(1)).openReservationItemDetailsView(1L);
    }

}
