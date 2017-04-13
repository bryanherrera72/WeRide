package www.weride.com.classes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Francis on 4/13/17.
 */
import www.weride.com.R;

public class RecyclerViewHolders extends RecyclerView.ViewHolder{
    public TextView profileHeader;
    public TextView profileContent;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        profileHeader=(TextView)itemView.findViewById(R.id.heading);
        profileContent = (TextView)itemView.findViewById(R.id.profile_content);
    }
}
