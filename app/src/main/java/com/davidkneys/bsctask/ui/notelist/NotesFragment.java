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
import com.davidkneys.bsctask.ui.NoteUI;
import com.davidkneys.bsctask.utils.BaseFragment;
import com.davidkneys.bsctask.utils.LocaleManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * UI representing list of all Notes.
 */
public class NotesFragment extends BaseFragment implements NotesAdapter.Listener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btnAddNote)
    View btnAddNote;
    @BindView(R.id.editNote)
    EditText editNote;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.notesList)
    RecyclerView notesList;

    private NotesAdapter notesAdapter;
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
        ButterKnife.bind(this, getView());

        vm = ViewModelProviders.of(this, factory()).get(NotesVM.class);

        notesList.setLayoutManager(new LinearLayoutManager(getContext()));
        notesAdapter = new NotesAdapter(getContext(), this);
        notesList.setAdapter(notesAdapter);
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
                .observeViewState()
                .subscribe(notesData -> {
                    swipeRefreshLayout.setRefreshing(notesData.isRefreshing());
                    notesAdapter.loadWithData(notesData.getData());
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
    public void onNoteClick(NoteUI note) {
        if (!note.isDirty()) {
            commonState().select(note.getNote());
        }
    }

    @Override
    public void onNoteDelete(NoteUI delete) {
        // todo we do not support delete from list itself yet
    }
}