package www.weride.com.classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import www.weride.com.R;



public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>{
    private LayoutInflater inflater;
    Context context;
    List<NoteInfo> data = Collections.emptyList();

    public NoteListAdapter(Context context, List<NoteInfo> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_note_row,parent,false);
        NoteViewHolder tempholder = new NoteViewHolder(view);
        return tempholder;
    }
    //fill the data items here
    @Override
    public void onBindViewHolder(final NoteViewHolder holder, int position) {
        final NoteInfo current = data.get(position);
        holder.title.setText(current.name);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public NoteViewHolder(View itemView) {
            super(itemView);
            title =(TextView) itemView.findViewById(R.id.listText);
        }
    }
}
