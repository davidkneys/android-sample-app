package com.davidkneys.bsctask.di;

import com.davidkneys.bsctask.ui.MainActivity;
import com.davidkneys.bsctask.ui.detail.NoteDetailFragment;
import com.davidkneys.bsctask.ui.notelist.NotesFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Root component for injection of application singleton Services
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class, ViewModelModule.class})
public interface ApplicationComponent {

    void inject(NotesFragment notesFragment);

    void inject(NoteDetailFragment noteDetailFragment);

    void inject(MainActivity mainActivity);
}
