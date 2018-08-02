package tz.co.dfm.dfmradio.Ui.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;

import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_MODEL;

public class EpisodeDetails extends AppCompatActivity {
    public static final String PLAYER_USER_AGENT = "episodeVideo";
    private static final String PLAYER_POSITION = "playerPosition";
    private static final String PLAYER_STATE = "playerState";

    boolean isPlayWhenReady = true;
    private static long currentPlayerPosition = -1;

    @BindView(R.id.pv_episode)
    PlayerView pvEpisode;
    @BindView(R.id.exo_buffering)
    ProgressBar pbExoBuffering;

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
        if(intent.hasExtra(SHOW_MODEL)){
            shows = intent.getParcelableExtra(SHOW_MODEL);
            Uri mediaFileUri = Uri.parse(shows.getEpisodeMediaFile());
            initializePlayer(mediaFileUri);
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
                    } else{
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
    void returnToMainScreen(){
        onBackPressed();
    }

    @OnClick(R.id.exo_full_screen_button)
    void toggleFullScreen(){
        Toast.makeText(this,"Toggle full screen",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.exo_share_button)
    void shareMedia(){
        Toast.makeText(this,"Share media",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.exo_add_favorite_button)
    void addToFavorites(){
        Toast.makeText(this,"Add to Favorites",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
