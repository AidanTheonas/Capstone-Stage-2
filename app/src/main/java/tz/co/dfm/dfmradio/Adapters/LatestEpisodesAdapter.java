package tz.co.dfm.dfmradio.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.Models.Test;
import tz.co.dfm.dfmradio.R;

public class LatestEpisodesAdapter extends RecyclerView.Adapter<LatestEpisodesAdapter.LatestEpisodeViewHolder> {
    private List<Test> testList;

    public LatestEpisodesAdapter(List<Test> testList){
        this.testList = testList;
    }

    @NonNull
    @Override
    public LatestEpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_latest_episode, parent, false);
        return new LatestEpisodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LatestEpisodeViewHolder holder, int position) {
        holder.testContents.setText("Position:".concat(String.valueOf(position)).concat("\t\t").concat(testList.get(position).getSomeContents()));
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    class LatestEpisodeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_test_contents)
        TextView testContents;

        LatestEpisodeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
