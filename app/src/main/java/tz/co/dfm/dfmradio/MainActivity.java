package tz.co.dfm.dfmradio;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import Fragments.ShowEpisodesFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.Adapters.LatestShowsViewPagerAdapter;
import tz.co.dfm.dfmradio.Helpers.Constants;

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
}
