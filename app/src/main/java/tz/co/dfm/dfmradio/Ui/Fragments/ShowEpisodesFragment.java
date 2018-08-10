package tz.co.dfm.dfmradio.Ui.Fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tz.co.dfm.dfmradio.Adapters.LatestEpisodesAdapter;
import tz.co.dfm.dfmradio.Adapters.OnEpisodeClickListener;
import tz.co.dfm.dfmradio.Helpers.Constants;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;
import tz.co.dfm.dfmradio.Ui.Activities.EpisodeDetails;

import static tz.co.dfm.dfmradio.Adapters.LatestEpisodesAdapter.SHOWS_EPISODE_FRAGMENT;
import static tz.co.dfm.dfmradio.Helpers.Constants.BASE_URL;
import static tz.co.dfm.dfmradio.Helpers.Helper.buildMediaUrl;
import static tz.co.dfm.dfmradio.Helpers.Helper.buildThumbnailUrl;

/**
 * Use the {@link ShowEpisodesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings({"ConstantConditions", "StringBufferReplaceableByString"})
public class ShowEpisodesFragment extends Fragment implements OnEpisodeClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String SHOW_MODEL = "show_model";
    public static final String SHOWS_ADAPTER_STATE = "shows_adapter_state";
    public static final String EPISODE_ID = "episode_id";
    public static final String EPISODE_TITLE = "episode_title";
    public static final String EPISODE_DATE = "episode_date";
    public static final String SHOW_HOST = "show_host";
    public static final String SHOW_NAME = "show_name";
    public static final String EPISODE_MEDIA_TYPE = "episode_media_type";
    public static final String EPISODE_FILE_URL = "episode_file_url";
    public static final String EPISODE_DESCRIPTION = "episode_description";
    public static final String EPISODE_THUMBNAIL = "episode_thumbnail";
    private static final String SHOW_TITLE = "show_title";
    @BindView(R.id.rv_episodes)
    RecyclerView recyclerViewEpisodes;
    @BindView(R.id.sl_refresh_episodes)
    SwipeRefreshLayout swipeRefreshEpisodes;
    @BindView(R.id.btn_tap_to_refresh)
    Button btnTapToRefresh;
    Snackbar errorSnackbar;
    private List<Shows> showsList = new ArrayList<>();
    private LatestEpisodesAdapter latestEpisodesAdapter;
    private String showTitle;
    private String showId;
    private RequestQueue requestQueue;

    public ShowEpisodesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param showTitle represents Show title.
     * @return A new instance of fragment ShowEpisodesFragment.
     */
    public static ShowEpisodesFragment newInstance(String showTitle) {
        ShowEpisodesFragment fragment = new ShowEpisodesFragment();
        Bundle args = new Bundle();
        args.putString(SHOW_TITLE, showTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showTitle = getArguments().getString(SHOW_TITLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(true);
        if (isVisibleToUser && latestEpisodesAdapter != null) {
            loadServerData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadServerData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_show_episodes, container, false);
        ButterKnife.bind(this, mainView);
        requestQueue = Volley.newRequestQueue(getContext());
        swipeRefreshEpisodes.setOnRefreshListener(this);
        swipeRefreshEpisodes.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryLight));
        showId = String.valueOf(Constants.showsId.get(showTitle));
        boolean shouldLoadServerData = false;
        if (savedInstanceState != null) {
            latestEpisodesAdapter = savedInstanceState.getParcelable(SHOWS_ADAPTER_STATE);
        }

        if (latestEpisodesAdapter == null) {
            latestEpisodesAdapter = new LatestEpisodesAdapter(showsList, SHOWS_EPISODE_FRAGMENT);
            shouldLoadServerData = true;
        }

        latestEpisodesAdapter.setOnEpisodeClickListener(this);
        int gridLayoutManagerSpanCount = getContext().getResources().getInteger(R.integer.shows_grid_layout_span_count);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), gridLayoutManagerSpanCount);
        recyclerViewEpisodes.setLayoutManager(mLayoutManager);
        recyclerViewEpisodes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEpisodes.setAdapter(latestEpisodesAdapter);

        if (shouldLoadServerData)
            loadServerData();
        return mainView;
    }

    @OnClick(R.id.btn_tap_to_refresh)
    void refreshEpisodes() {
        btnTapToRefresh.setVisibility(View.GONE);
        loadServerData();
    }


    public void loadServerData() {
        swipeRefreshEpisodes.setRefreshing(true);
        btnTapToRefresh.setVisibility(View.GONE);
        String url = new StringBuilder()
                .append(BASE_URL)
                .append("getEpisodes")
                .append("/")
                .append(showId).toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            swipeRefreshEpisodes.setRefreshing(false);
            showsList.clear();
            if (response.length() == 0) {
                btnTapToRefresh.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            latestEpisodesAdapter.notifyDataSetChanged();
        }, error -> {
            swipeRefreshEpisodes.setRefreshing(false);
            if (getActivity() == null) return;
            errorSnackbar = Snackbar
                    .make(getActivity().findViewById(R.id.cl_main_view), R.string.network_error_message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry_load, view -> loadServerData());
            errorSnackbar.show();
        });
        requestQueue.add(jsonArrayRequest);
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
    public void onRefresh() {
        if (errorSnackbar != null) {
            if (errorSnackbar.isShown())
                errorSnackbar.dismiss();
        }
        swipeRefreshEpisodes.setRefreshing(true);
        loadServerData();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SHOWS_ADAPTER_STATE, latestEpisodesAdapter);
    }
}
