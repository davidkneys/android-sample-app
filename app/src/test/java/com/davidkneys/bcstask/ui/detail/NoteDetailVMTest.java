package com.davidkneys.bcstask.ui.detail;

import com.davidkneys.bsctask.api.Note;
import com.davidkneys.bsctask.di.NetworkModule;
import com.davidkneys.bsctask.service.DataRepository;
import com.davidkneys.bsctask.service.OnlineChecker;
import com.davidkneys.bsctask.ui.detail.NoteDetailVM;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NoteDetailVMTest {

    private NoteDetailVM vm;
    private DataRepository dataRepositoryMock = mock(DataRepository.class);

    private OnlineChecker testOnlineChecker = new OnlineChecker() {
        @Override
        public Observable<Boolean> isOnline() {
            return Observable.just(true);
        }

        @Override
        public void setOnline(boolean online) {

        }
    };

    @Test
    public void updateNoteTest() {
        String TEST_NEW_TITLE = "Test new Title";

        NetworkModule.BscApiTest testApi = new NetworkModule.BscApiTest(0);
        Note TEST_NOTE = testApi.getData().values().iterator().next();
        dataRepositoryMock = spy(new DataRepository(testApi, testOnlineChecker));

        vm = new NoteDetailVM(dataRepositoryMock, testOnlineChecker, Schedulers::single);
        vm.selectNote(TEST_NOTE);
        // wait until note is selected
        vm.observeViewState().test().awaitCount(1);

        //--------------------------------
        vm.update(TEST_NEW_TITLE);
        verify(dataRepositoryMock).updateNote(TEST_NOTE, TEST_NEW_TITLE);
    }
}
