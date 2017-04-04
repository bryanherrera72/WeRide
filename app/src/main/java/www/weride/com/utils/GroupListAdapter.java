package www.weride.com.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import www.weride.com.R;
import www.weride.com.amazonaws.models.nosql.GroupsDO;

/**
 * Created by Francis on 3/31/2017.
 */

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {
    private LayoutInflater inflater;
    private GroupsDO group;
    Context context;
    List<GroupInfo> data =Collections.emptyList();
    public static String GROUP_NAME;
    public GroupListAdapter(Context context, List<GroupInfo>  data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        GROUP_NAME = group.getName();
    }
    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_group_row, parent, false);
        GroupViewHolder tempHolder = new GroupViewHolder(view);
        return tempHolder;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        final GroupInfo current = data.get(position);
        holder.title.setText(current.title);
        holder.editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Will come back to fill this later;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        Button editGroup;

        public GroupViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.listText);
            editGroup = (Button)itemView.findViewById(R.id.ebutton);
        }
    }
}
