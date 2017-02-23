package www.weride.com.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mapzen.android.graphics.MapzenMap;

import www.weride.com.R;

public class SearchActivity extends AppCompatActivity {
    private Toolbar searchbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchbar = (Toolbar) findViewById(R.id.search_bar);
        setSupportActionBar(searchbar);
    }
}