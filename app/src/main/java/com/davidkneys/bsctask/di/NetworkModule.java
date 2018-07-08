package com.davidkneys.bsctask.di;

import com.davidkneys.bsctask.api.BscApi;
import com.davidkneys.bsctask.api.Note;
import com.davidkneys.bsctask.api.PutNoteRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Module responsible for providing Network related dependencies.
 */
@Module
public class NetworkModule {

    private static final String BSC_NAMED_API_URL = "bscNamedApiUrl";

    @Singleton
    @Provides
    @Named(BSC_NAMED_API_URL)
    String providesBscRootEndpoint() {
        return BscApi.ROOT_ENDPOINT;
    }

    @Singleton
    @Provides
    Retrofit providesRetrofit(@Named(BSC_NAMED_API_URL) String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    BscApi providesBscApi(Retrofit retrofit) {
        return retrofit.create(BscApi.class);

//        return new BscApiTest(3);
    }

    public static class BscApiTest implements BscApi {

        private final Map<Integer, Note> data = new LinkedHashMap<>();
        private int simulatedTimeoutS;

        public BscApiTest(int simulatedTimeoutS) {
            this.simulatedTimeoutS = simulatedTimeoutS;

            data.put(1, new Note(1, "Buy shoes"));
            data.put(2, new Note(2, "Sell some stuff"));
            data.put(3, new Note(3, "Finish sample app for BSC"));
        }

        public Map<Integer, Note> getData() {
            return data;
        }

        @Override
        public Single<List<Note>> getNotes() {
            Single<List<Note>> notes = Single.just(new ArrayList<>(data.values()));
            return notes.delay(simulatedTimeoutS, TimeUnit.SECONDS);
        }

        @Override
        public Single<Note> getNote(int id) {
            return Single.just(data.get(id)).delay(simulatedTimeoutS, TimeUnit.SECONDS);
        }

        int counter = 0;

        @Override
        public Single<Note> createNote(PutNoteRequest note) {
            Note newNote = new Note(data.size() + 1 + counter++, note.getTitle());

            return Single.just(newNote).delay(simulatedTimeoutS, TimeUnit.SECONDS).map(note1 -> {
                data.put(newNote.getId(), newNote);
                return note1;
            });
        }

        @Override
        public Single<Note> updateNote(int id, PutNoteRequest note) {
            return Single.defer(() -> {
                        data.get(id).setTitle(note.getTitle());
                        return Single.just(data.get(id));
                    }
            ).delay(simulatedTimeoutS, TimeUnit.SECONDS);
        }

        @Override
        public Single<Response<Object>> removeNote(int id) {
            Single<Response<Object>> r = Single.just(Response.success(new Object()));
            return r.delay(simulatedTimeoutS, TimeUnit.SECONDS).map(o -> {
                data.remove(id);
                return o;
            });
        }
    }
}
