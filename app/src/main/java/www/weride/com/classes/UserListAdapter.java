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



public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder>{
    private LayoutInflater inflater;
    Context context;
    List<UserInfo> data = Collections.emptyList();

    public UserListAdapter(Context context, List<UserInfo> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_user_row,parent,false);
        UserViewHolder tempholder = new UserViewHolder(view);
        return tempholder;
    }
    //fill the data items here
    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        final UserInfo current = data.get(position);
        holder.title.setText(current.userName);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public UserViewHolder(View itemView) {
            super(itemView);
            title =(TextView) itemView.findViewById(R.id.listText);
        }
    }
}