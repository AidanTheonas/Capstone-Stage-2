package tz.co.dfm.dfmradio.Adapters;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

public class LatestEpisodesAdapter extends RecyclerView.Adapter<LatestEpisodesAdapter.LatestEpisodeViewHolder> implements Parcelable {
    private List<Shows> showsList;
    private OnEpisodeClickListener onEpisodeClickListener;
    public static final int SHOWS_EPISODE_FRAGMENT = 1;
    public static final int SEARCH_ACTIVITY = 2;

    private int source;

    public LatestEpisodesAdapter(List<Shows> shows,int source) {
        this.showsList = shows;
        this.source = source;
        setHasStableIds(true);
    }

    private LatestEpisodesAdapter(Parcel in) {
        showsList = in.createTypedArrayList(Shows.CREATOR);
        source = in.readInt();
    }

    public static final Creator<LatestEpisodesAdapter> CREATOR = new Creator<LatestEpisodesAdapter>() {
        @Override
        public LatestEpisodesAdapter createFromParcel(Parcel in) {
            return new LatestEpisodesAdapter(in);
        }

        @Override
        public LatestEpisodesAdapter[] newArray(int size) {
            return new LatestEpisodesAdapter[size];
        }
    };

    public void setOnEpisodeClickListener(OnEpisodeClickListener onEpisodeClickListener) {
        this.onEpisodeClickListener = onEpisodeClickListener;
    }

    @NonNull
    @Override
    public LatestEpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if(source == SHOWS_EPISODE_FRAGMENT){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_latest_episode, parent, false);
        }else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_search_results, parent, false);
        }
        return new LatestEpisodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LatestEpisodeViewHolder holder, int position) {
        Shows show = showsList.get(position);
        holder.tvEpisodeTitle.setText(show.getEpisodeTitle());
        holder.tvEpisodeSubTitle.setText(show.getEpisodeDate());
        if (!show.getEpisodeThumbnail().trim().equals("")) {
            Picasso.get().load(show.getEpisodeThumbnail())
                    .into(holder.ivShowEpisodeImage);
        }
        holder.cvEpisodeCard.setOnClickListener(v -> onEpisodeClickListener.loadEpisode(show, v));
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(showsList);
        dest.writeInt(source);
    }

    class LatestEpisodeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_episode_title)
        TextView tvEpisodeTitle;
        @BindView(R.id.tv_episode_subtitle)
        TextView tvEpisodeSubTitle;
        @BindView(R.id.iv_show_episode)
        ImageView ivShowEpisodeImage;
        @BindView(R.id.cv_episode)
        CardView cvEpisodeCard;

        LatestEpisodeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
