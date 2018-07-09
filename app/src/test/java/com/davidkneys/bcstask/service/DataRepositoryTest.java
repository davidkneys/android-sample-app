package com.davidkneys.bcstask.service;

import com.davidkneys.bsctask.api.Note;
import com.davidkneys.bsctask.api.PutNoteRequest;
import com.davidkneys.bsctask.di.NetworkModule;
import com.davidkneys.bsctask.service.DataRepository;
import com.davidkneys.bsctask.service.OnlineChecker;
import com.davidkneys.bsctask.ui.NoteUI;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.BehaviorSubject;

public class DataRepositoryTest {

    private static final String TEST_NOTE_TITLE = "Test Note";

    private class BscApiTest extends NetworkModule.BscApiTest {

        private boolean apiCreateNoteCalled = false;

        public BscApiTest() {
            super(0);
        }

        @Override
        public Single<Note> createNote(PutNoteRequest note) {
            apiCreateNoteCalled = true;
            return super.createNote(note);
        }
    }

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
    public void dataInitTest() {
        BscApiTest bscApiTest = new BscApiTest();

        OnlineChecker onlineChecker = new OnlineChecker() {
            private BehaviorSubject<Boolean> onlineStream = BehaviorSubject.createDefault(false);

            @Override
            public Observable<Boolean> isOnline() {
                return onlineStream;
            }

            @Override
            public void setOnline(boolean online) {
                if (onlineStream.getValue() != online) {
                    onlineStream.onNext(online);
                }
            }
        };

        DataRepository dataRepository = new DataRepository(bscApiTest, onlineChecker);

        TestObserver<List<NoteUI>> testObserver = dataRepository.notes().test();
        testObserver.awaitCount(1);
        testObserver.assertNoValues();

        //fetch should be initiated every time when App comes back to online state
        onlineChecker.setOnline(true);
        testObserver.awaitCount(1);
        List<List<NoteUI>> values = testObserver.values();
        Assert.assertTrue(values.size() == 1);

        //fetch should be initiated every time when App comes back to online state
        onlineChecker.setOnline(false);
        onlineChecker.setOnline(true);
        testObserver.awaitCount(2);
        values = testObserver.values();

        Assert.assertTrue(values.size() == 2);
        testObserver.assertNotComplete();
    }


    @Test
    public void createNoteSuccessTest() {
        BscApiTest bscApiTest = new BscApiTest();
        int startingDataSize = bscApiTest.getData().size();

        DataRepository dataRepository = new DataRepository(bscApiTest, testOnlineChecker);

        TestObserver<List<NoteUI>> testObserver = dataRepository.notes().test();

        // wait for first initiation by online checker
        testObserver.awaitCount(1);
        dataRepository.createNote(TEST_NOTE_TITLE);

        // now we want to have at least 3 events,
        // - first from start
        // - second right after creation of note is fired with fake Note
        // - third coming from BscAPI
        testObserver.awaitCount(3);

        testObserver.assertValueAt(0, new Predicate<List<NoteUI>>() {
            @Override
            public boolean test(List<NoteUI> noteUIS) throws Exception {
                return noteUIS.size() == startingDataSize;
            }
        });

        testObserver.assertValueAt(1, new Predicate<List<NoteUI>>() {
            @Override
            public boolean test(List<NoteUI> noteUIS) throws Exception {
                NoteUI note = noteUIS.get(0);
                return note.isDirty() && note.getNote().getTitle().equals(TEST_NOTE_TITLE);
            }
        });

        testObserver.assertValueAt(2, new Predicate<List<NoteUI>>() {
            @Override
            public boolean test(List<NoteUI> noteUIS) throws Exception {
                NoteUI note = noteUIS.get(0);
                return !note.isDirty() && note.getNote().getTitle().equals(TEST_NOTE_TITLE);
            }
        });

        Assert.assertTrue(bscApiTest.apiCreateNoteCalled);
    }
}
