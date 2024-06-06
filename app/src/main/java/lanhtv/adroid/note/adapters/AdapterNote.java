package lanhtv.adroid.note.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import lanhtv.adroid.note.R;
import lanhtv.adroid.note.interfaces.Deletenote;
import lanhtv.adroid.note.interfaces.OpenNoteDetail;
import lanhtv.adroid.note.model.Note;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.noteViewHolder>  {


    private final Context context;
    private List<Note> list;
    private final Deletenote deletenote;
    private final OpenNoteDetail openNoteDetail;

    public AdapterNote(Context context,Deletenote deletenote,OpenNoteDetail openNoteDetail) {
        this.context = context;
        this.deletenote = deletenote;
        this.openNoteDetail = openNoteDetail;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Note> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note_layout, parent, false);
        return new noteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder holder, int position) {
        Note note = list.get(position);
        if(note == null)return;

        holder.title.setText(note.title);
        holder.delete.setOnClickListener(view -> deletenote.deleteNote(note,view.getContext()));
        holder.itemView.setOnClickListener(view -> openNoteDetail.OpenNoteDetail(note));

    }

    @Override
    public int getItemCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }

    public static class noteViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView delete;
        public noteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            delete = itemView.findViewById(R.id.delete_note);
        }
    }
}
