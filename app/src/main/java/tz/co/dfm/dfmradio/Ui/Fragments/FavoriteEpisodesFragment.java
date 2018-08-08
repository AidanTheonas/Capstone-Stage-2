package tz.co.dfm.dfmradio.Ui.Fragments;

import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.Adapters.LatestEpisodesAdapter;
import tz.co.dfm.dfmradio.Adapters.OnEpisodeClickListener;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;
import tz.co.dfm.dfmradio.Ui.Activities.EpisodeDetails;

public class FavoriteEpisodesFragment extends Fragment implements OnEpisodeClickListener,LoaderManager.LoaderCallbacks<Cursor> {
    public static final String SHOW_MODEL = "show_model";
    @BindView(R.id.rv_episodes)
    RecyclerView recyclerViewEpisodes;
    private List<Shows> showsList = new ArrayList<>();
    private LatestEpisodesAdapter latestEpisodesAdapter;

    public FavoriteEpisodesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_episodes, container, false);
        ButterKnife.bind(this, view);

        latestEpisodesAdapter = new LatestEpisodesAdapter(showsList);
        latestEpisodesAdapter.setOnEpisodeClickListener(this);
        int gridLayoutManagerSpanCount = getContext().getResources().getInteger(R.integer.shows_grid_layout_span_count);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), gridLayoutManagerSpanCount);
        recyclerViewEpisodes.setLayoutManager(mLayoutManager);
        recyclerViewEpisodes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEpisodes.setAdapter(latestEpisodesAdapter);

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
