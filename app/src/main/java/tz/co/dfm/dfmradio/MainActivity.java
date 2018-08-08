package tz.co.dfm.dfmradio;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.Adapters.LatestShowsViewPagerAdapter;
import tz.co.dfm.dfmradio.Helpers.Constants;
import tz.co.dfm.dfmradio.Ui.Activities.SearchActivity;
import tz.co.dfm.dfmradio.Ui.Activities.SettingsActivity;
import tz.co.dfm.dfmradio.Ui.Fragments.FavoriteEpisodesFragment;
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        showsTab.setupWithViewPager(showsViewPager);
        setupViewPager();
    }

    private void setupViewPager() {
        LatestShowsViewPagerAdapter adapter = new LatestShowsViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FavoriteEpisodesFragment(), Constants.FAVORITE_SHOWS);
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
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                overridePendingTransition(0, 0);
                return true;
            case R.id.item_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                overridePendingTransition(0, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
