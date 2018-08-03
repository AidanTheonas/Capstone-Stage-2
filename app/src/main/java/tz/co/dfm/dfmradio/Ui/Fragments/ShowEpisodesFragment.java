package tz.co.dfm.dfmradio.Ui.Fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
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
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.Adapters.LatestEpisodesAdapter;
import tz.co.dfm.dfmradio.Adapters.OnEpisodeClickListener;
import tz.co.dfm.dfmradio.Helpers.Constants;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;
import tz.co.dfm.dfmradio.Ui.Activities.EpisodeDetails;

/**
 * Use the {@link ShowEpisodesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ConstantConditions")
public class ShowEpisodesFragment extends Fragment implements OnEpisodeClickListener {
    public static final String SHOW_MODEL = "show_model";
    private static final String SHOW_TITLE = "show_title";
    @BindView(R.id.rv_episodes)
    RecyclerView recyclerViewEpisodes;
    private List<Shows> showsList = new ArrayList<>();
    private LatestEpisodesAdapter latestEpisodesAdapter;
    private String mShowTitle;
    private String mShowId;

    public ShowEpisodesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param showTitle Parameter 1.
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
            mShowTitle = getArguments().getString(SHOW_TITLE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_episodes, container, false);
        ButterKnife.bind(this, view);

        mShowId = String.valueOf(Constants.showsId.get(mShowTitle));

        latestEpisodesAdapter = new LatestEpisodesAdapter(showsList, getContext());
        latestEpisodesAdapter.setOnEpisodeClickListener(this);
        int gridLayoutManagerSpanCount = getContext().getResources().getInteger(R.integer.shows_grid_layout_span_count);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), gridLayoutManagerSpanCount);
        recyclerViewEpisodes.setLayoutManager(mLayoutManager);
        recyclerViewEpisodes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEpisodes.setAdapter(latestEpisodesAdapter);

        prepareTestData();
        return view;
    }

    public void prepareTestData() {
        String[] photoArray = {
                "http://142.93.29.1/images/2018-07-30_03_hillsong_united_-_scandal_of_grace.jpg",
                "https://images.pexels.com/photos/372326/pexels-photo-372326.jpeg?auto=compress&cs=tinysrgb&h=350",
                "http://i.imgur.com/DvpvklR.png",
                "https://images.pexels.com/photos/236339/pexels-photo-236339.jpeg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/213207/pexels-photo-213207.jpeg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/2871/building-architecture-church-monastery.jpg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/335887/pexels-photo-335887.jpeg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/161081/eucharist-body-of-christ-church-mass-161081.jpeg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/831056/pexels-photo-831056.jpeg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/52062/pexels-photo-52062.jpeg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/227390/pexels-photo-227390.jpeg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/145847/pexels-photo-145847.jpeg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/730588/pexels-photo-730588.jpeg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/415708/pexels-photo-415708.jpeg?auto=compress&cs=tinysrgb&h=350",
                "https://images.pexels.com/photos/794559/pexels-photo-794559.jpeg?auto=compress&cs=tinysrgb&h=350"
        };

        String randomStr = "https://images.pexels.com/photos/794559/pexels-photo-794559.jpeg?auto=compress&cs=tinysrgb&h=350";
        for (int i = 0; i < 2; i++) {
            randomStr = photoArray[new Random().nextInt(photoArray.length)];
            Shows shows = new Shows(
                    "D-Love",
                    "D-Love na Annastazia Rugaba",
                    "July 30, 2018",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam suscipit tristique dignissim. Pellentesque sed eros elementum, porttitor risus sed, pellentesque mauris. Vivamus dapibus a tortor nec auctor. Quisque mauris purus, vestibulum eget ligula ut, tristique rutrum sem. Duis velit quam, eleifend a libero et, ullamcorper vulputate massa. In quis rutrum nisi, sed aliquet ipsum. Nunc eros nibh, lobortis sit amet dictum a, lacinia sit amet elit. Sed porttitor mollis lectus, at varius sem euismod eget.",
                    "10 COMMENTS",
                    randomStr,
                    "http://142.93.29.1/media/2018-07-30_02_hillsong_united_-_up_in_arms.mp3",
                    "Annastazia Rugaba",
                    "audio"
            );
            showsList.add(shows);
        }
        Shows shows = new Shows(
                "D-Love",
                "D-Love na Annastazia Rugaba",
                "July 30, 2018",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam suscipit tristique dignissim. Pellentesque sed eros elementum, porttitor risus sed, pellentesque mauris. Vivamus dapibus a tortor nec auctor. Quisque mauris purus, vestibulum eget ligula ut, tristique rutrum sem. Duis velit quam, eleifend a libero et, ullamcorper vulputate massa. In quis rutrum nisi, sed aliquet ipsum. Nunc eros nibh, lobortis sit amet dictum a, lacinia sit amet elit. Sed porttitor mollis lectus, at varius sem euismod eget.",
                "10 COMMENTS",
                randomStr,
                "https://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_30mb.mp4",
                "Annastazia Rugaba",
                "video"
        );
        showsList.add(shows);
        latestEpisodesAdapter.notifyDataSetChanged();
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

}
