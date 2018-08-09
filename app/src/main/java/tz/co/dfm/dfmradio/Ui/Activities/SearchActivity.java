package tz.co.dfm.dfmradio.Ui.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.Adapters.LatestEpisodesAdapter;
import tz.co.dfm.dfmradio.Adapters.OnEpisodeClickListener;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;

import static tz.co.dfm.dfmradio.Adapters.LatestEpisodesAdapter.SEARCH_ACTIVITY;
import static tz.co.dfm.dfmradio.Helpers.Constants.SEARCH_EPISODE_PATH;
import static tz.co.dfm.dfmradio.Helpers.Helper.buildMediaUrl;
import static tz.co.dfm.dfmradio.Helpers.Helper.buildThumbnailUrl;
import static tz.co.dfm.dfmradio.Ui.Activities.LoadSharedMedia.CONTENTS;
import static tz.co.dfm.dfmradio.Ui.Activities.LoadSharedMedia.STATUS;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_DATE;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_DESCRIPTION;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_FILE_URL;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_ID;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_MEDIA_TYPE;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_THUMBNAIL;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_TITLE;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_HOST;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_MODEL;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_NAME;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnEpisodeClickListener {
    @BindView(R.id.search_toolbar)
    Toolbar searchToolbar;
    @BindView(R.id.sv_search_episodes)
    SearchView searchView;
    @BindView(R.id.pb_searching)
    ProgressBar searchingProgress;
    @BindView(R.id.tv_no_episodes)
    TextView tvNoEpisodesFound;
    @BindView(R.id.rv_episodes)
    RecyclerView recyclerViewEpisodes;

    private List<Shows> showsList = new ArrayList<>();
    private LatestEpisodesAdapter latestEpisodesAdapter;
    SearchAsyncTask searchAsyncTask;

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

        latestEpisodesAdapter = new LatestEpisodesAdapter(showsList,SEARCH_ACTIVITY);
        latestEpisodesAdapter.setOnEpisodeClickListener(this);
        int gridLayoutManagerSpanCount = getResources().getInteger(R.integer.shows_search_grid_layout_span_count);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, gridLayoutManagerSpanCount);
        recyclerViewEpisodes.setLayoutManager(mLayoutManager);
        recyclerViewEpisodes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEpisodes.setAdapter(latestEpisodesAdapter);

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

    public void loadEpisode(String queryString){
        showsList.clear();
        String searchUrl = SEARCH_EPISODE_PATH + queryString;
        if(searchAsyncTask != null) {
            searchAsyncTask.cancel(true);
        }
        searchAsyncTask = new SearchAsyncTask();
        searchAsyncTask.execute(searchUrl);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        loadEpisode(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        loadEpisode(newText);
        return false;
    }

    private Activity getActivity(){
        return  this;
    }

    @Override
    public void loadEpisode(Shows shows, View view) {
        Intent intent = new Intent(getActivity(), EpisodeDetails.class);
        intent.putExtra(SHOW_MODEL, shows);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SearchAsyncTask extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvNoEpisodesFound.setVisibility(View.GONE);
            searchingProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            String url = strings[0];
            String inputLine;
            String serverResponse;
            try{
                URL sharedUrl = new URL(url);

                HttpURLConnection httpURLConnection = (HttpURLConnection)sharedUrl.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(30000);
                httpURLConnection.setReadTimeout(30000);
                httpURLConnection.connect();

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = bufferedReader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                bufferedReader.close();
                inputStreamReader.close();

                serverResponse = stringBuilder.toString();

            }catch (Exception ex){
                serverResponse = null;
            }
            try {
                if(serverResponse == null) return null;
                return new JSONObject(serverResponse);
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            searchingProgress.setVisibility(View.GONE);
            if(jsonObject == null){
                showErrorInformation();
            }else{
                if(jsonObject.optString(STATUS) == null || jsonObject.optInt(STATUS) == 0){
                    showErrorInformation();
                }else{
                    openDetailsActivity(jsonObject);
                }
            }
        }

        void openDetailsActivity(JSONObject jsonResults){
            try {
                JSONArray jsonArray = new JSONArray(jsonResults.optString(CONTENTS));
                for(int i = 0; i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int mediaType = jsonObject.optInt(EPISODE_MEDIA_TYPE);
                    String thumbnailUrl = buildThumbnailUrl(jsonObject.optString(EPISODE_THUMBNAIL));
                    String mediaFileUrl = buildMediaUrl(jsonObject.optString(EPISODE_FILE_URL), mediaType);

                    Shows shows = new Shows(
                            jsonObject.optInt(EPISODE_ID),
                            jsonObject.optString(SHOW_NAME),
                            jsonObject.optString(EPISODE_TITLE),
                            jsonObject.optString(EPISODE_DATE),
                            jsonObject.optString(EPISODE_DESCRIPTION),
                            thumbnailUrl,
                            mediaFileUrl,
                            jsonObject.optString(SHOW_HOST),
                            mediaType
                    );
                    showsList.add(shows);
                }
                latestEpisodesAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                showErrorInformation();
            }
        }
    }

    public void showErrorInformation(){
        searchingProgress.setVisibility(View.GONE);
        tvNoEpisodesFound.setVisibility(View.VISIBLE);
    }
}
