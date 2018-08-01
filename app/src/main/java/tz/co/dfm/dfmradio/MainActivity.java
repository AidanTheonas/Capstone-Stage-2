package tz.co.dfm.dfmradio;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.Adapters.LatestShowsViewPagerAdapter;
import tz.co.dfm.dfmradio.Ui.Activities.SearchActivity;
import tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment;

import static tz.co.dfm.dfmradio.Helpers.Constants.shows;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.vp_episodes)
    ViewPager showsViewPager;
    @BindView(R.id.tab_shows)
    TabLayout showsTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mainToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        showsTab.setupWithViewPager(showsViewPager);
        setupViewPager();
    }

    private void setupViewPager() {
        LatestShowsViewPagerAdapter adapter = new LatestShowsViewPagerAdapter(getSupportFragmentManager());
        for (String show : shows) {
            adapter.addFragment(ShowEpisodesFragment.newInstance(show), show);
        }
        showsViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.item_search:
                    Intent myIntent = new Intent(this, SearchActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(0,0);
                return true;
            case R.id.item_settings:
                Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_about_us:
                Toast.makeText(this,"About us",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
