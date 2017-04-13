package www.weride.com.classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import www.weride.com.R;

/**
 * Created by Francis on 4/13/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<UserProfile> user;
    protected Context context;

    public RecyclerViewAdapter(List<UserProfile> user, Context context) {
        this.user = user;
        this.context = context;
    }


    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolders = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_data_list, parent, false);
        viewHolders = new RecyclerViewHolders(layoutView);
        return viewHolders;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.profileHeader.setText(user.get(position).getHeader());
        holder.profileContent.setText(user.get(position).getProfileContent());
    }

    @Override
    public int getItemCount() {
        return this.user.size();
    }
}
