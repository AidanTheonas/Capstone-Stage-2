package tz.co.dfm.dfmradio.Ui.Fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tz.co.dfm.dfmradio.Adapters.LatestEpisodesAdapter;
import tz.co.dfm.dfmradio.Adapters.OnEpisodeClickListener;
import tz.co.dfm.dfmradio.Models.FavoriteEpisodesColumns;
import tz.co.dfm.dfmradio.Models.FavoriteEpisodesProvider;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;
import tz.co.dfm.dfmradio.Ui.Activities.EpisodeDetails;

import static tz.co.dfm.dfmradio.Adapters.LatestEpisodesAdapter.SHOWS_EPISODE_FRAGMENT;

@SuppressWarnings("ConstantConditions")
public class FavoriteEpisodesFragment extends Fragment implements OnEpisodeClickListener, LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {
    public static final String SHOW_MODEL = "show_model";
    private static final int FAVORITE_EPISODES_LOADER = 105;
    @BindView(R.id.rv_episodes)
    RecyclerView recyclerViewEpisodes;
    @BindView(R.id.sl_refresh_episodes)
    SwipeRefreshLayout swipeRefreshEpisodes;
    @BindView(R.id.btn_tap_to_refresh)
    Button btnTapToRefresh;
    private List<Shows> showsList = new ArrayList<>();
    private LatestEpisodesAdapter latestEpisodesAdapter;

    public FavoriteEpisodesFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(true);
        if (isVisibleToUser & getActivity() != null) {
            getActivity().getSupportLoaderManager().restartLoader(FAVORITE_EPISODES_LOADER, null, this);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_episodes, container, false);
        ButterKnife.bind(this, view);

        btnTapToRefresh.setText(R.string.no_favorite_episodes_added);

        swipeRefreshEpisodes.setOnRefreshListener(this);
        swipeRefreshEpisodes.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));

        latestEpisodesAdapter = new LatestEpisodesAdapter(showsList, SHOWS_EPISODE_FRAGMENT);
        latestEpisodesAdapter.setOnEpisodeClickListener(this);
        int gridLayoutManagerSpanCount = getContext().getResources().getInteger(R.integer.shows_grid_layout_span_count);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), gridLayoutManagerSpanCount);
        recyclerViewEpisodes.setLayoutManager(mLayoutManager);
        recyclerViewEpisodes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEpisodes.setAdapter(latestEpisodesAdapter);

        getActivity().getSupportLoaderManager().initLoader(FAVORITE_EPISODES_LOADER, null, this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void loadEpisode(Shows shows, View view) {
        Intent intent = new Intent(getActivity(), EpisodeDetails.class);
        intent.putExtra(SHOW_MODEL, shows);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
            getActivity().startActivity(intent, bundle);
        } else {
            getActivity().startActivity(intent);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        swipeRefreshEpisodes.setRefreshing(true);
        Uri FAVORITE_EPISODES_URI = FavoriteEpisodesProvider.FavoriteEpisodes.CONTENT_URI;
        return new CursorLoader(
                getActivity(),
                FAVORITE_EPISODES_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        swipeRefreshEpisodes.setRefreshing(false);
        showsList.clear();
        if (data == null || data.getCount() == 0) {
            btnTapToRefresh.setVisibility(View.VISIBLE);
        } else {
            btnTapToRefresh.setVisibility(View.GONE);
            updateList(data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(FAVORITE_EPISODES_LOADER, null, this);
    }

    public void updateList(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                Shows shows = new Shows(
                        cursor.getInt(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_EPISODE_ID)),
                        cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_SHOW_NAME)),
                        cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_EPISODE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_EPISODE_DATE)),
                        cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_EPISODE_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_THUMBNAIL_FILE)),
                        cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_MEDIA_FILE)),
                        cursor.getString(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_SHOW_HOST)),
                        cursor.getInt(cursor.getColumnIndex(FavoriteEpisodesColumns.COLUMN_MEDIA_TYPE))
                );
                showsList.add(shows);
            } while (cursor.moveToNext());
            latestEpisodesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }


    @Override
    public void onRefresh() {
        btnTapToRefresh.setVisibility(View.GONE);
        getActivity().getSupportLoaderManager().restartLoader(FAVORITE_EPISODES_LOADER, null, this);
    }

    @OnClick(R.id.btn_tap_to_refresh)
    void refreshFavoriteEpisodes() {
        btnTapToRefresh.setVisibility(View.GONE);
        getActivity().getSupportLoaderManager().restartLoader(FAVORITE_EPISODES_LOADER, null, this);
    }
}
