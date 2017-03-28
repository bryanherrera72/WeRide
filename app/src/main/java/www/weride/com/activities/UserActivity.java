package www.weride.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import www.weride.com.R;
import www.weride.com.classes.GroupsListAdapter;
import www.weride.com.classes.UserInfo;
import www.weride.com.classes.UserListAdapter;

public class UserActivity extends AppCompatActivity {
    private Button addButton;
    private Button removeButton;

    private List<UserInfo> li;
    private RecyclerView userslist;
    private UserListAdapter useradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        //create datalist
        li = getData();
        //toolbar stuff
        Intent intent = getIntent();
        String title = intent.getStringExtra(GroupsListAdapter.GROUP_NAME);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        //create the recyclerview and set the adapter (adapter takes the list with the data)
        userslist = (RecyclerView) findViewById(R.id.users_list);
        useradapter = new UserListAdapter(this.getBaseContext(), li);
        userslist.setAdapter(useradapter);
        userslist.setLayoutManager(new LinearLayoutManager(this.getBaseContext()));

    }
    public static List<UserInfo> getData(){
        List<UserInfo> data = new ArrayList<>();
        String[] titles = {"the good", "the bad", "the ugly"};
        for(int i = 0; i < titles.length; i++){
            UserInfo current = new UserInfo();
            current.userName = titles[i];
            current.admin = false;
            data.add(current);
        }
        return data;
    }

}
