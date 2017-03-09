package www.weride.com.classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import www.weride.com.R;

/**
 * Created by bryanherrera on 2/28/17.
 */

//note from miguel: removed references to image view
public class GroupsListAdapter extends RecyclerView.Adapter<GroupsListAdapter.GroupViewHolder>{
    private LayoutInflater inflater;
    List<GroupInfo> data = Collections.emptyList();

    public GroupsListAdapter(Context context, List<GroupInfo> data){
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_group_row,parent,false);
        GroupViewHolder tempholder = new GroupViewHolder(view);
        return tempholder;
    }
    //fill the data items here
    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        GroupInfo current = data.get(position);
        holder.title.setText(current.title);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public GroupViewHolder(View itemView) {
            super(itemView);
            title =(TextView) itemView.findViewById(R.id.listText);
        }
    }
}
