package www.weride.com.classes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import www.weride.com.R;
import www.weride.com.activities.UserActivity;

/**
 * Created by bryanherrera on 2/28/17.
 */

//note from miguel: removed references to image view
public class GroupsListAdapter extends RecyclerView.Adapter<GroupsListAdapter.GroupViewHolder>{
    private LayoutInflater inflater;

    public static final String GROUP_NAME = "www.weride.com.activities.UserActivity";
    Context context;
    List<GroupInfo> data = Collections.emptyList();

    public GroupsListAdapter(Context context, List<GroupInfo> data){
        this.context = context;
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
    public void onBindViewHolder(final GroupViewHolder holder, int position) {
        final GroupInfo current = data.get(position);
        //set the title for each card
        holder.title.setText(current.title);
        holder.editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when selecting the button we go to the use activity for the group.
                //pass the name of the group
                Intent myIntent = new Intent(context, UserActivity.class);
                myIntent.putExtra(GROUP_NAME,current.title);
                context.startActivity(myIntent);
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
            title =(TextView) itemView.findViewById(R.id.listText);
            editGroup = (Button) itemView.findViewById(R.id.ebutton);
        }
    }
}
