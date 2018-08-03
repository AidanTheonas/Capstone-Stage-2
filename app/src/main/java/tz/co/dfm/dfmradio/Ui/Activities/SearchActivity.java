package tz.co.dfm.dfmradio.Ui.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.R;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnFocusChangeListener {
    @BindView(R.id.search_toolbar)
    Toolbar searchToolbar;
    @BindView(R.id.sv_search_episodes)
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(searchToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        searchView.setQueryHint(getString(R.string.search_episodes));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextFocusChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, "Clicked Submit:" + query, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(this, "Text changed:" + newText, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            Toast.makeText(this, "Focus changed", Toast.LENGTH_SHORT).show();
        }
    }
}
