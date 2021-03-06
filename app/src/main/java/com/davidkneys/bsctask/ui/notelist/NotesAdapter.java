package com.davidkneys.bsctask.ui.notelist;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidkneys.bsctask.R;
import com.davidkneys.bsctask.ui.NoteUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for List of Notes.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteVH> {

    private final Context context;
    private final Listener listener;

    private final List<NoteUI> data = new ArrayList<>();

    public NotesAdapter(Context context,
                        Listener listener) {
        this.context = context.getApplicationContext();
        this.listener = listener;
        setHasStableIds(true);
    }

    public void loadWithData(List<NoteUI> newData) {
        this.data.clear();
        this.data.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getNote().getId();
    }

    @Override
    public NoteVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteVH(LayoutInflater.from(context).inflate(R.layout.note_list_item, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(NoteVH holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class NoteVH extends RecyclerView.ViewHolder {

        private TextView textId;
        private TextView textTitle;

        private Listener listener;
        private Resources resources;

        public NoteVH(View itemView, Listener listener) {
            super(itemView);
            this.textId = itemView.findViewById(R.id.textNoteId);
            this.textTitle = itemView.findViewById(R.id.textNoteTitle);

            this.listener = listener;
            this.resources = itemView.getResources();
        }

        public void bind(NoteUI item) {
            textTitle.setText(item.getNote().getTitle());
            if (item.isDirty()) {
                itemView.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.pendingNote, null));
                textId.setText(resources.getString(R.string.syncing));
            } else {
                itemView.setBackgroundColor(0);
                textId.setText(String.format("Id: %d", item.getNote().getId()));
            }

            itemView.setOnClickListener(v -> {
                listener.onNoteClick(item);
            });
        }
    }

    interface Listener {
        void onNoteClick(NoteUI note);

        void onNoteDelete(NoteUI delete);
    }
}
