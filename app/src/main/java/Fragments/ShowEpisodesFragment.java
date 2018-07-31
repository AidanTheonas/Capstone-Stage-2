package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.Adapters.LatestEpisodesAdapter;
import tz.co.dfm.dfmradio.Helpers.Constants;
import tz.co.dfm.dfmradio.Models.Test;
import tz.co.dfm.dfmradio.R;

/**
 * Use the {@link ShowEpisodesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowEpisodesFragment extends Fragment {
    @BindView(R.id.rv_episodes)
    RecyclerView recyclerViewEpisodes;

    private List<Test> testList = new ArrayList<>();
    private LatestEpisodesAdapter latestEpisodesAdapter;

    private static final String SHOW_TITLE = "show_title";
    private String mShowTitle;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_episodes, container, false);
        ButterKnife.bind(this,view);

        latestEpisodesAdapter = new LatestEpisodesAdapter(testList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerViewEpisodes.setLayoutManager(mLayoutManager);
        recyclerViewEpisodes.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEpisodes.setAdapter(latestEpisodesAdapter);

        Log.e("Fragment:",mShowTitle+":"+String.valueOf(Constants.showsId.get(mShowTitle)));

        prepareTestData();
        return view;
    }

    public void prepareTestData(){
        for(int i = 0;i<50;i++){
            Test test = new Test("Some contents here");
            testList.add(test);
        }
        latestEpisodesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
