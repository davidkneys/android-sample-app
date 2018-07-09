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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * UI representing detail of one Note.
 */
public class NoteDetailFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.editTitle)
    EditText editTitle;
    @BindView(R.id.loadingData)
    View loadingData;
    @BindView(R.id.loadedContent)
    View loadedContent;

    @BindView(R.id.btnRemove)
    Button btnRemove;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;

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
        ButterKnife.bind(this, getView());

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

        subscribe(vm.observeViewState().subscribe(viewState -> {
            editTitle.setText(viewState.getTitle());

            loadingData.setVisibility(viewState.isLoading() ? View.VISIBLE : View.GONE);
            loadedContent.setVisibility(viewState.isLoading() ? View.GONE : View.VISIBLE);
        }));
    }
}
