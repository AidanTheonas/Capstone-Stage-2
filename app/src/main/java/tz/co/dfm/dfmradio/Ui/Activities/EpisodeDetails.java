package tz.co.dfm.dfmradio.Ui.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tz.co.dfm.dfmradio.Helpers.Helper;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;

import static tz.co.dfm.dfmradio.Helpers.Constants.MEDIA_TYPE_AUDIO;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_MODEL;

public class EpisodeDetails extends AppCompatActivity {
    public static final String PLAYER_USER_AGENT = "episodeVideo";
    private static final String PLAYER_POSITION = "playerPosition";
    private static final String PLAYER_STATE = "playerState";
    private static long currentPlayerPosition = -1;
    boolean isPlayWhenReady = true;
    @BindView(R.id.pv_episode)
    PlayerView pvEpisode;
    @BindView(R.id.exo_buffering)
    ProgressBar pbExoBuffering;
    @BindView(R.id.tv_episode_title)
    TextView tvEpisodeTitle;
    @BindView(R.id.tv_episode_sub_title)
    TextView tvEpisodeSubTitle;
    @BindView(R.id.tv_episode_description)
    TextView tvEpisodeDescription;
    @BindView(R.id.exo_thumbnail)
    ImageView ivExoPlayerThumbnail;

    @BindView(R.id.exo_full_screen_button)
    ImageView exoFullScreenButton;
    @BindView(R.id.exo_add_favorite_button)
    ImageView exoFavoriteButton;
    @BindView(R.id.exo_share_button)
    ImageView exoShareButton;
    @BindView(R.id.add_to_favorites)
    Button actionFavoriteButton;

    @BindView(R.id.exo_shutter)
    View exoView;

    ExoPlayer episodeExoPlayer = null;
    Shows shows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            currentPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION);
            isPlayWhenReady = savedInstanceState.getBoolean(PLAYER_STATE);
        }
        Intent intent = getIntent();
        if (intent.hasExtra(SHOW_MODEL)) {
            shows = intent.getParcelableExtra(SHOW_MODEL);
            Uri mediaFileUri = Uri.parse(shows.getEpisodeMediaFile());
            initializePlayer(mediaFileUri);

            String episodeSubtitle = Helper.buildEpisodeSubTitle(shows.getEpisodeDate(), shows.getEpisodeHostName(), this);
            setDetails(
                    shows.getEpisodeTitle(),
                    episodeSubtitle,
                    shows.getEpisodeDescription(),
                    shows.getMediaType(),
                    shows.getEpisodeThumbnail()
            );
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (episodeExoPlayer != null) {
            outState.putLong(PLAYER_POSITION, episodeExoPlayer.getCurrentPosition());
            outState.putBoolean(PLAYER_STATE, episodeExoPlayer.getPlayWhenReady());
        }
    }

    public void setDetails(String episodeTitle, String episodeSubTitle, String episodeDescription, String mediaType, String mediaUrl) {
        tvEpisodeTitle.setText(episodeTitle);
        tvEpisodeSubTitle.setText(episodeSubTitle);
        tvEpisodeDescription.setText(episodeDescription);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TextView tvLandscapeEpTitle = findViewById(R.id.tv_episode_title_landscape);
            tvLandscapeEpTitle.setText(episodeTitle);
            TextView tvLandscapeEpSubTitle = findViewById(R.id.tv_episode_sub_title_landscape);
            tvLandscapeEpSubTitle.setText(episodeSubTitle);
        }

        if (mediaType.trim().equals(MEDIA_TYPE_AUDIO.trim())) {
            ivExoPlayerThumbnail.setVisibility(View.VISIBLE);
            exoView.setVisibility(View.GONE);
            exoFullScreenButton.setVisibility(View.INVISIBLE);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) exoShareButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            exoShareButton.setLayoutParams(layoutParams);

            Picasso.get()
                    .load(Uri.parse(mediaUrl))
                    .into(ivExoPlayerThumbnail);
            pvEpisode.setControllerShowTimeoutMs(0);
            pvEpisode.setControllerHideOnTouch(false);
        } else {
            pvEpisode.setControllerShowTimeoutMs(2000);
        }
    }

    public void initializePlayer(Uri sourceUri) {
        if (episodeExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            episodeExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            pvEpisode.setPlayer(episodeExoPlayer);
            episodeExoPlayer.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    super.onPlayerStateChanged(playWhenReady, playbackState);
                    if (playWhenReady && playbackState == Player.STATE_READY) {
                        pbExoBuffering.setVisibility(View.GONE);
                    } else {
                        if (playbackState == Player.STATE_BUFFERING) {
                            pbExoBuffering.setVisibility(View.VISIBLE);
                            if (currentPlayerPosition != -1) {
                                episodeExoPlayer.seekTo(currentPlayerPosition);
                                currentPlayerPosition = -1;
                            }
                        }
                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    super.onPlayerError(error);
                }
            });
            String userAgent = Util.getUserAgent(this, PLAYER_USER_AGENT);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(this, userAgent)).createMediaSource(sourceUri);
            episodeExoPlayer.prepare(mediaSource);
            episodeExoPlayer.setPlayWhenReady(isPlayWhenReady);
        }
    }


    private void releasePlayer() {
        if (episodeExoPlayer != null) {
            episodeExoPlayer.stop();
            episodeExoPlayer.release();
            episodeExoPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @OnClick(R.id.exo_back_button)
    void returnToMainScreen() {
        onBackPressed();
    }

    @OnClick(R.id.exo_full_screen_button)
    void toggleFullScreen() {
        setFullScreen();
    }

    @OnClick(R.id.exo_share_button)
    void shareMedia() {
        Toast.makeText(this, "Share media", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.exo_add_favorite_button)
    void addToFavorites() {
        Toast.makeText(this, "Add to Favorites", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.add_to_favorites)
    void addFavorite() {
        Toast.makeText(this, "Add to Favorites", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.share_episode)
    void shareEpisode() {
        Toast.makeText(this, "Add to Favorites", Toast.LENGTH_SHORT).show();
    }

    public void setFullScreen() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
