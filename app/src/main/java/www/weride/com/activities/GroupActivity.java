package www.weride.com.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import www.weride.com.R;
import www.weride.com.classes.GroupInfo;
import www.weride.com.classes.GroupsListAdapter;

/**
 * Created by Kaz Cruz on 3/2/2017.
 */

public class GroupActivity extends Activity {
    private EditText group_name;
    private Button group_create;
    private String groupId;
    private List<GroupInfo> li;
    private RecyclerView groupslist;
    private GroupsListAdapter groupadapter;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);
        li = getData();
        // Inflate the layout for this fragment
        groupslist = (RecyclerView) findViewById(R.id.groups_list);
        groupadapter = new GroupsListAdapter(this, li);
        groupslist.setAdapter(groupadapter);
        groupslist.setLayoutManager(new LinearLayoutManager(this));
        final Button createGroup = (Button) findViewById(R.id.create_group_button);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create the dialog.
                final Dialog dialog = new Dialog(getApplicationContext());
                dialog.setContentView(R.layout.create_group_dialog);
                group_name = (EditText)dialog.findViewById(R.id.group_name);
                group_create = (Button)dialog.findViewById(R.id.group_create);

                //non functioning firebase stuff
                /*
                db = FirebaseDatabase.getInstance();
                reference = db.getReference("Groups");
                */
                ////////////

                group_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = group_name.getText().toString();
                        if(TextUtils.isEmpty(groupId)){
                            createGroup(name);
                            groupadapter.notifyDataSetChanged();
                            dialog.hide();
                        }

                        //startActivity(new Intent(CreateGroup.this, GroupActivity.class));
                        //finish();
                    }

                });
                dialog.show();
            }
        });
    }
    public static List<GroupInfo> getData(){
        List<GroupInfo> data = new ArrayList<>();
        int[] icons = {R.drawable.ic_one, R.drawable.ic_two, R.drawable.ic_three};
        String[] titles = {"Bryan's Group", "Friends", "Work"};
        for(int i = 0; i < titles.length && i < icons.length; i++){
            GroupInfo current = new GroupInfo();
            current.iconId = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }

    public void createGroup(String s){
        GroupInfo group = new GroupInfo();
        group.iconId = R.drawable.ic_group;
        group.title = s;
        li.add(group);

    }
}
