package www.weride.com.classes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import www.weride.com.R;
import www.weride.com.fragments.GroupFragment;

/**
 * Created by bryanherrera on 4/11/17.
 */

public class GroupsListAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<Group> groups;
    LayoutInflater mInflater;
    ActiveToggler at;
    public GroupsListAdapter(Context context, ArrayList<Group> items, ActiveToggler at){
        mContext = context;
        groups = items;
        this.at = at;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int i) {
        return groups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.list_item_group,viewGroup,false);
        TextView grouptitle = (TextView) rowView.findViewById(R.id.group_title);
        Button setActive = (Button) rowView.findViewById(R.id.set_active_button);

        final Group currentgroup = (Group) getItem(i);
        setActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                at.setAsActiveGroup(currentgroup.getId());
            }
        });
        grouptitle.setText(currentgroup.getTitle());
        return rowView;
    }

}
