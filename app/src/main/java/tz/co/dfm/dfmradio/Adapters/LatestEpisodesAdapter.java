package tz.co.dfm.dfmradio.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;

public class LatestEpisodesAdapter extends RecyclerView.Adapter<LatestEpisodesAdapter.LatestEpisodeViewHolder> {
    private List<Shows> showsList;
    private Context context;

    public LatestEpisodesAdapter(List<Shows> shows, Context context) {
        this.showsList = shows;
        this.context = context;
        setHasStableIds(true);
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
        Shows show = showsList.get(position);
        StringBuilder episodeSubtitle = new StringBuilder()
                .append(show.getEpisodeDate())
                .append(" ")
                .append(context.getString(R.string.by_string))
                .append(" ")
                .append(show.getEpisodeHostName());

        holder.tvEpisodeTitle.setText(show.getEpisodeTitle());
        holder.tvEpisodeSubTitle.setText(episodeSubtitle);
        if(!show.getEpisodeThumbnail().trim().equals("")) {
            Picasso.get().load(show.getEpisodeThumbnail())
                    .into(holder.ivShowEpisodeImage);
        }
    }

    @Override
    public int getItemCount() {
        return showsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class LatestEpisodeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_episode_title)
        TextView tvEpisodeTitle;
        @BindView(R.id.tv_episode_subtitle)
        TextView tvEpisodeSubTitle;
        @BindView(R.id.iv_show_episode)
        ImageView ivShowEpisodeImage;

        LatestEpisodeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
