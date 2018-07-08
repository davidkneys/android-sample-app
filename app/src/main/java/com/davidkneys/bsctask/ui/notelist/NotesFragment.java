package com.davidkneys.bsctask.ui.notelist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.davidkneys.bsctask.R;
import com.davidkneys.bsctask.ui.detail.NoteDetailVM;
import com.davidkneys.bsctask.utils.BaseFragment;
import com.davidkneys.bsctask.utils.LocaleManager;

/**
 * UI representing list of all Notes.
 */
public class NotesFragment extends BaseFragment implements NotesAdapter.Listener {

    private Toolbar toolbar;
    private View btnAddNote;
    private EditText editNote;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView notesList;

    private NotesVM vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injector().inject(this);
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = getView().findViewById(R.id.toolbar);
        btnAddNote = getView().findViewById(R.id.btnAddNote);
        editNote = getView().findViewById(R.id.editNote);
        swipeRefreshLayout = getView().findViewById(R.id.swipeToRefresh);
        notesList = getView().findViewById(R.id.notesList);

        vm = ViewModelProviders.of(this, factory()).get(NotesVM.class);

        notesList.setLayoutManager(new LinearLayoutManager(getContext()));
        setupToolbarMenu();

        btnAddNote.setOnClickListener(v -> {
            if (vm.createNote(editNote.getText().toString())) {
                editNote.setText("");
            } else {
                Toast.makeText(getContext(), R.string.not_empty_title_warning, Toast.LENGTH_SHORT).show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            vm.refresh();
        });

        subscribe(vm
                .observeNotesListViewData()
                .subscribe(notesData -> {
                    swipeRefreshLayout.setRefreshing(notesData.isRefreshing());
                    notesList.setAdapter(new NotesAdapter(getContext(), notesData.getData(), this));
                }));
    }

    private void setupToolbarMenu() {
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_cz: {
                    LocaleManager.changeLanguage(getActivity(), LocaleManager.LANG_CS);
                    return true;
                }
                case R.id.action_en: {
                    LocaleManager.changeLanguage(getActivity(), LocaleManager.LANG_EN);
                    return true;
                }
                default:
                    return false;
            }
        });
    }

    @Override
    public void onNoteClick(NoteDetailVM.NoteUI note) {
        if (!note.isDirty()) {
            commonState().select(note.getNote());
        }
    }

    @Override
    public void onNoteDelete(NoteDetailVM.NoteUI delete) {
        // todo we do not support delete from list itself yet
    }
}
