package com.davidkneys.bsctask.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.davidkneys.bsctask.ui.CommonState;
import com.davidkneys.bsctask.ui.detail.NoteDetailVM;
import com.davidkneys.bsctask.ui.notelist.NotesVM;
import com.davidkneys.bsctask.utils.ViewModelDIFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Binding VMs to the map based on their class type.
 */
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NoteDetailVM.class)
    abstract ViewModel noteDetailVm(NoteDetailVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(NotesVM.class)
    abstract ViewModel notesVm(NotesVM vm);

    @Binds
    @IntoMap
    @ViewModelKey(CommonState.class)
    abstract ViewModel commonState(CommonState vm);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelDIFactory factory);
}
