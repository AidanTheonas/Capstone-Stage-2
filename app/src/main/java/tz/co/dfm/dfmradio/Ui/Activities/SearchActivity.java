package tz.co.dfm.dfmradio.Ui.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import butterknife.OnClick;
import tz.co.dfm.dfmradio.Adapters.LatestEpisodesAdapter;
import tz.co.dfm.dfmradio.Adapters.OnEpisodeClickListener;
import tz.co.dfm.dfmradio.Models.SearchSuggestionsProvider;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;
import tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment;

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

public class SearchActivity extends AppCompatActivity
    implements SearchView.OnQueryTextListener,
        OnEpisodeClickListener,
        AdapterView.OnItemClickListener {
  public static final String SHOWS_LIST_STATE = "SHOWS_LIST_STATE";

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

  @BindView(R.id.ll_search_layout)
  LinearLayout searchMainLayout;

  SearchAsyncTask searchAsyncTask;
  SearchView.SearchAutoComplete searchAutoComplete;
  private List<Shows> showsList;
  private LatestEpisodesAdapter latestEpisodesAdapter;

  @SuppressLint("RestrictedApi")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    ButterKnife.bind(this);
    setSupportActionBar(searchToolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchView.setQueryHint(getString(R.string.search_episodes));
    searchView.setIconifiedByDefault(false);
    searchView.setOnQueryTextListener(this);

    if (searchManager != null)
      searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

    searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
    searchAutoComplete.setOnItemClickListener(this);
    searchAutoComplete.setTextColor(getResources().getColor(R.color.colorTextIcons));
    float marginLeft =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
    searchAutoComplete.setPadding((int) marginLeft, 0, 0, 0);
    searchAutoComplete.setDropDownAnchor(R.id.search_toolbar);

    ImageView clearSearchView =
        searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
    clearSearchView.setOnClickListener(
        v -> {
          searchAutoComplete.setText("");
          showsList.clear();
          latestEpisodesAdapter.notifyDataSetChanged();
          tvNoEpisodesFound.setVisibility(View.GONE);
          searchView.requestFocus();
        });

    if (savedInstanceState != null) {
      latestEpisodesAdapter =
          savedInstanceState.getParcelable(ShowEpisodesFragment.SHOWS_ADAPTER_STATE);
      showsList = savedInstanceState.getParcelableArrayList(SHOWS_LIST_STATE);
    } else {
      showsList = new ArrayList<>();
      latestEpisodesAdapter = new LatestEpisodesAdapter(showsList, SEARCH_ACTIVITY);
    }

    latestEpisodesAdapter.setOnEpisodeClickListener(this);
    int gridLayoutManagerSpanCount =
        getResources().getInteger(R.integer.shows_search_grid_layout_span_count);
    RecyclerView.LayoutManager mLayoutManager =
        new GridLayoutManager(this, gridLayoutManagerSpanCount);
    recyclerViewEpisodes.setLayoutManager(mLayoutManager);
    recyclerViewEpisodes.setItemAnimator(new DefaultItemAnimator());
    recyclerViewEpisodes.setAdapter(latestEpisodesAdapter);
  }

  @OnClick(R.id.iv_back_btn)
  void goToPrevious() {
    onBackPressed();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(0, 0);
  }

  public void loadEpisode(String queryString) {
    String searchUrl = SEARCH_EPISODE_PATH + queryString;
    if (searchAsyncTask != null) {
      searchAsyncTask.cancel(true);
    }
    searchAsyncTask = new SearchAsyncTask();
    searchAsyncTask.execute(searchUrl);
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    searchView.clearFocus();
    loadEpisode(query);
    saveSearchQuery(query);
    return false;
  }

  public void saveSearchQuery(String query) {
    SearchRecentSuggestions searchRecentSuggestions =
        new SearchRecentSuggestions(
            this, SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
    searchRecentSuggestions.saveRecentQuery(query, null);
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    return false;
  }

  private Activity getActivity() {
    return this;
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

  public void showErrorInformation() {
    showsList.clear();
    searchingProgress.setVisibility(View.GONE);
    tvNoEpisodesFound.setVisibility(View.VISIBLE);
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(ShowEpisodesFragment.SHOWS_ADAPTER_STATE, latestEpisodesAdapter);
    outState.putParcelableArrayList(SHOWS_LIST_STATE, (ArrayList<? extends Parcelable>) showsList);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Cursor cursor = (Cursor) parent.getItemAtPosition(position);
    String queryString =
        cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
    loadOnItemClickAndVoiceSearch(queryString);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      loadOnItemClickAndVoiceSearch(query);
      saveSearchQuery(query);
    }
  }

  void loadOnItemClickAndVoiceSearch(String queryString) {
    searchAutoComplete.setText(queryString);
    searchAutoComplete.setSelection(queryString.length());
    loadEpisode(queryString);
  }

  @SuppressLint("StaticFieldLeak")
  private class SearchAsyncTask extends AsyncTask<String, Void, JSONObject> {

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
      try {
        URL sharedUrl = new URL(url);

        HttpURLConnection httpURLConnection = (HttpURLConnection) sharedUrl.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(30000);
        httpURLConnection.connect();

        InputStreamReader inputStreamReader =
            new InputStreamReader(httpURLConnection.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null) {
          stringBuilder.append(inputLine);
        }
        bufferedReader.close();
        inputStreamReader.close();

        serverResponse = stringBuilder.toString();

      } catch (Exception ex) {
        serverResponse = null;
      }
      try {
        if (serverResponse == null) return null;
        return new JSONObject(serverResponse);
      } catch (JSONException e) {
        return null;
      }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
      super.onPostExecute(jsonObject);

      searchingProgress.setVisibility(View.GONE);
      if (jsonObject == null) {
        showErrorInformation();
      } else {
        if (jsonObject.optString(STATUS) == null || jsonObject.optInt(STATUS) == 0) {
          showErrorInformation();
        } else {
          openDetailsActivity(jsonObject);
        }
      }
    }

    void openDetailsActivity(JSONObject jsonResults) {
      try {
        JSONArray jsonArray = new JSONArray(jsonResults.optString(CONTENTS));
        showsList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject jsonObject = jsonArray.getJSONObject(i);

          int mediaType = jsonObject.optInt(EPISODE_MEDIA_TYPE);
          String thumbnailUrl = buildThumbnailUrl(jsonObject.optString(EPISODE_THUMBNAIL));
          String mediaFileUrl = buildMediaUrl(jsonObject.optString(EPISODE_FILE_URL), mediaType);

          Shows shows =
              new Shows(
                  jsonObject.optInt(EPISODE_ID),
                  jsonObject.optString(SHOW_NAME),
                  jsonObject.optString(EPISODE_TITLE),
                  jsonObject.optString(EPISODE_DATE),
                  jsonObject.optString(EPISODE_DESCRIPTION),
                  thumbnailUrl,
                  mediaFileUrl,
                  jsonObject.optString(SHOW_HOST),
                  mediaType);
          showsList.add(shows);
        }
        latestEpisodesAdapter.notifyDataSetChanged();
      } catch (JSONException e) {
        showErrorInformation();
      }
    }
  }
}
