package com.davidkneys.bsctask.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.davidkneys.bsctask.R;
import com.davidkneys.bsctask.utils.BaseFragment;

/**
 * UI representing detail of one Note.
 */
public class NoteDetailFragment extends BaseFragment {

    private Toolbar toolbar;

    private EditText editTitle;
    private View loadingData;
    private View loadedContent;

    private Button btnRemove;
    private Button btnUpdate;

    private NoteDetailVM vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injector().inject(this);
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = getView().findViewById(R.id.toolbar);
        editTitle = getView().findViewById(R.id.editTitle);
        loadingData = getView().findViewById(R.id.loadingData);
        loadedContent = getView().findViewById(R.id.loadedContent);

        btnRemove = getView().findViewById(R.id.btnRemove);
        btnUpdate = getView().findViewById(R.id.btnUpdate);

        vm = ViewModelProviders.of(this, factory()).get(NoteDetailVM.class);

        toolbar.setNavigationOnClickListener(v -> commonState().clearSelection());

        btnRemove.setOnClickListener(v -> {
            vm.delete();
            commonState().clearSelection();
        });

        btnUpdate.setOnClickListener(v -> {
            vm.update(editTitle.getText().toString());
            commonState().clearSelection();
        });

        subscribe(commonState()
                .observeSelectedNote()
                .subscribe(note ->
                        vm.selectNote(note)
                ));

        subscribe(vm.observeSelectedNote().subscribe(note -> {
            editTitle.setText(note.getNote().getTitle());

            loadingData.setVisibility(note.isDirty() ? View.VISIBLE : View.GONE);
            loadedContent.setVisibility(note.isDirty() ? View.GONE : View.VISIBLE);
        }));
    }
}
