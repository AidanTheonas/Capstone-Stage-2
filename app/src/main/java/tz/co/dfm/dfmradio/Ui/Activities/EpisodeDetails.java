package tz.co.dfm.dfmradio.Ui.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tz.co.dfm.dfmradio.Helpers.Helper;
import tz.co.dfm.dfmradio.Models.FavoriteEpisodesContract;
import tz.co.dfm.dfmradio.Models.FavoriteEpisodesProvider;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;
import tz.co.dfm.dfmradio.Widget.DFMRadioWidgetService;

import static tz.co.dfm.dfmradio.Helpers.Constants.MEDIA_TYPE_AUDIO;
import static tz.co.dfm.dfmradio.Helpers.Constants.WATCH_EPISODE_PATH;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_MODEL;

@SuppressWarnings("StringBufferReplaceableByString")
public class EpisodeDetails extends AppCompatActivity {
  public static final String PLAYER_USER_AGENT = "episodeVideo";
  private static final String PLAYER_POSITION = "playerPosition";
  private static final String PLAYER_STATE = "playerState";
  private static final String ANALYTICS_ADD_TO_FAVORITES = "ADD_TO_FAVORITES";
  private static final int STORAGE_ACCESS_REQUEST_CODE = 10;
  private static long currentPlayerPosition = -1;
  boolean isPlayWhenReady = true;

  private FirebaseAnalytics firebaseAnalytics;

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
    pvEpisode.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

    firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    firebaseAnalytics.setAnalyticsCollectionEnabled(true);
    firebaseAnalytics.setMinimumSessionDuration(20000);
    firebaseAnalytics.setSessionTimeoutDuration(600000);

    if (savedInstanceState != null) {
      currentPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION);
      isPlayWhenReady = savedInstanceState.getBoolean(PLAYER_STATE);
    }
    Intent intent = getIntent();
    if (intent.hasExtra(SHOW_MODEL)) {
      shows = intent.getParcelableExtra(SHOW_MODEL);
      Uri mediaFileUri = Uri.parse(shows.getEpisodeMediaFile());
      initializePlayer(mediaFileUri);

      String episodeSubtitle =
          Helper.buildEpisodeSubTitle(
              shows.getEpisodeDate(), shows.getEpisodeHostName(), shows.getShowName());
      setDetails(
          shows.getEpisodeTitle(),
          episodeSubtitle,
          shows.getEpisodeDescription(),
          shows.getMediaType(),
          shows.getEpisodeThumbnail());
      updateFavoriteButtons();
    }
  }

  public void updateFavoriteButtons() {
    if (isAddedToFavorites() > 0) {
      actionFavoriteButton.setCompoundDrawablesWithIntrinsicBounds(
          0, R.drawable.ic_favorite_primary_24dp, 0, 0);
      actionFavoriteButton.setText(R.string.remove_from_favorites);
      exoFavoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
      actionFavoriteButton.setTag(1);
      exoFavoriteButton.setTag(1);
    } else {
      actionFavoriteButton.setCompoundDrawablesWithIntrinsicBounds(
          0, R.drawable.ic_favorite_border_primary_24dp, 0, 0);
      actionFavoriteButton.setText(R.string.add_to_favorites);
      exoFavoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
      actionFavoriteButton.setTag(0);
      exoFavoriteButton.setTag(0);
    }
    DFMRadioWidgetService.updateDFMWidget(this);
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    if (episodeExoPlayer != null) {
      outState.putLong(PLAYER_POSITION, episodeExoPlayer.getCurrentPosition());
      outState.putBoolean(PLAYER_STATE, episodeExoPlayer.getPlayWhenReady());
    }
  }

  public void setDetails(
      String episodeTitle,
      String episodeSubTitle,
      String episodeDescription,
      int mediaType,
      String mediaUrl) {
    tvEpisodeTitle.setText(episodeTitle);
    tvEpisodeSubTitle.setText(episodeSubTitle);
    tvEpisodeDescription.setText(episodeDescription);

    Picasso.get()
        .load(Uri.parse(mediaUrl))
        .placeholder(R.drawable.remote_thumbnail_placeholder)
        .error(R.drawable.remote_thumbnail_placeholder)
        .into(ivExoPlayerThumbnail);

    if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      TextView tvLandscapeEpTitle = findViewById(R.id.tv_episode_title_landscape);
      tvLandscapeEpTitle.setText(episodeTitle);
      TextView tvLandscapeEpSubTitle = findViewById(R.id.tv_episode_sub_title_landscape);
      tvLandscapeEpSubTitle.setText(episodeSubTitle);
    }

    if (mediaType == MEDIA_TYPE_AUDIO) {
      ivExoPlayerThumbnail.setVisibility(View.VISIBLE);
      exoView.setVisibility(View.GONE);
      exoFullScreenButton.setVisibility(View.INVISIBLE);

      RelativeLayout.LayoutParams layoutParams =
          (RelativeLayout.LayoutParams) exoShareButton.getLayoutParams();
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      exoShareButton.setLayoutParams(layoutParams);

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
      episodeExoPlayer.addListener(
          new Player.DefaultEventListener() {
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
      MediaSource mediaSource =
          new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(this, userAgent))
              .createMediaSource(sourceUri);
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

  @OnClick(R.id.exo_add_favorite_button)
  void exoAddFavorites() {
    ToggleFavorites(exoFavoriteButton);
  }

  @OnClick(R.id.add_to_favorites)
  void addFavorite() {
    ToggleFavorites(actionFavoriteButton);
  }

  public void ToggleFavorites(View view) {
    int tag = Integer.parseInt(view.getTag().toString());
    if (tag == 0) {
      addToFavorites();
    } else {
      removeFromFavorites();
    }
  }

  @OnClick(R.id.share_episode)
  void shareEpisode() {
    checkPermissionShare();
  }

  @OnClick(R.id.exo_share_button)
  void shareMedia() {
    checkPermissionShare();
  }

  public void checkPermissionShare() {
    if (isStoragePermissionGranted()) {
      performShare();
    } else {
      ActivityCompat.requestPermissions(
          this,
          new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
          STORAGE_ACCESS_REQUEST_CODE);
    }
  }

  @SuppressLint("DefaultLocale")
  public void performShare() {

      Bundle bundle = new Bundle();
      bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, shows.getEpisodeId());
      bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, shows.getEpisodeTitle());
      firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);

    ivExoPlayerThumbnail.setDrawingCacheEnabled(true);
    BitmapDrawable bitmapDrawable = (BitmapDrawable) ivExoPlayerThumbnail.getDrawable();
    Bitmap episodeThumbnailBitmap = bitmapDrawable.getBitmap();
    FileOutputStream fileOutputStream;
    File writeFile = null;
    try {
      File filePath = Environment.getExternalStorageDirectory();
      File storageDirectory =
          new File(filePath.getAbsolutePath() + File.separator + "DFMRadio Podcast");
      if (!storageDirectory.isDirectory()) {
        boolean directoryMakeStatus = storageDirectory.mkdirs();
        if (!directoryMakeStatus) {
          Toast.makeText(this, R.string.error_sharing_episode, Toast.LENGTH_SHORT).show();
          return;
        }
      }
      String episodeThumbnailFileName =
          String.format("%s.jpg", shows.getEpisodeId() + "_" + shows.getEpisodeTitle());
      writeFile = new File(storageDirectory, episodeThumbnailFileName);
      fileOutputStream = new FileOutputStream(writeFile);
      episodeThumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
      fileOutputStream.flush();
      fileOutputStream.close();
    } catch (Exception e) {
      Toast.makeText(this, R.string.error_sharing_episode, Toast.LENGTH_SHORT).show();
    }

    if (writeFile != null) {
      String mediaUrl =
          new StringBuilder().append(WATCH_EPISODE_PATH).append(shows.getEpisodeId()).toString();

      String shareText =
          new StringBuilder()
              .append(
                  shows.getMediaType() == MEDIA_TYPE_AUDIO
                      ? getString(R.string.listen_now)
                      : getString(R.string.watch_now))
              .append("\n")
              .append(shows.getEpisodeTitle())
              .append("\n")
              .append(mediaUrl)
              .toString();

      Intent shareEpisode = new Intent(Intent.ACTION_SEND);
      shareEpisode.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(writeFile));
      shareEpisode.putExtra(Intent.EXTRA_TEXT, shareText);
      shareEpisode.setType("*/*");
      startActivity(Intent.createChooser(shareEpisode, getString(R.string.share_episode)));
    }
  }

  public boolean isStoragePermissionGranted() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      int result = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
      return result == PackageManager.PERMISSION_GRANTED;
    } else {
      return true;
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == STORAGE_ACCESS_REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        performShare();
      } else {
        Snackbar allowStorageAccessPermission =
            Snackbar.make(
                    findViewById(R.id.cl_episode_details_main_view),
                    R.string.allow_storage_permission,
                    Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.app_settings, view -> openAppSettings());
        TextView tvSnackbar =
            allowStorageAccessPermission
                .getView()
                .findViewById(android.support.design.R.id.snackbar_text);
        tvSnackbar.setMaxLines(5);
        allowStorageAccessPermission.show();
      }
    }
  }

  void openAppSettings() {
    final Intent intent = new Intent();
    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.setData(Uri.parse("package:" + getPackageName()));
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
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

  public void addToFavorites() {
    final Uri[] returnedURI = new Uri[1];
    ContentValues values = new ContentValues();
    values.put(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_ID, shows.getEpisodeId());
    values.put(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_SHOW_NAME, shows.getShowName());
    values.put(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_TITLE, shows.getEpisodeTitle());
    values.put(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_DATE, shows.getEpisodeDate());
    values.put(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_DESCRIPTION, shows.getEpisodeDescription());
    values.put(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_THUMBNAIL_FILE, shows.getEpisodeThumbnail());
    values.put(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_MEDIA_FILE, shows.getEpisodeMediaFile());
    values.put(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_SHOW_HOST, shows.getEpisodeHostName());
    values.put(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_MEDIA_TYPE, shows.getMediaType());
    values.put(FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_TIMESTAMP, Helper.getCurrentTimeAsInteger());
    returnedURI[0] =
        getContentResolver().insert(FavoriteEpisodesProvider.CONTENT_URI, values);
    if (returnedURI[0] == null) {
      Toast.makeText(this, R.string.error_adding_episode_to_favorites, Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(this, R.string.episode_added_to_favorites, Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, shows.getEpisodeId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getResources().getString(R.string.add_to_favorites)+shows.getEpisodeTitle());
        firebaseAnalytics.logEvent(ANALYTICS_ADD_TO_FAVORITES, bundle);
      updateFavoriteButtons();
    }
  }

  public void removeFromFavorites() {
    int data =
        getContentResolver()
            .delete(
                FavoriteEpisodesProvider.CONTENT_URI,
                FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_ID + " =?",
                new String[] {shows.getEpisodeId() + ""});
    if (data > 0) {
      Toast.makeText(this, R.string.episode_removed, Toast.LENGTH_LONG).show();
      updateFavoriteButtons();
    } else {
      Toast.makeText(this, R.string.error_removing_episode, Toast.LENGTH_LONG).show();
    }
  }

  public int isAddedToFavorites() {
    int count = 0;
    Cursor cursor =
        getContentResolver()
            .query(
                FavoriteEpisodesProvider.CONTENT_URI,
                null,
                FavoriteEpisodesContract.FavoriteEpisodesEntry.COLUMN_EPISODE_ID + " =?",
                new String[] {shows.getEpisodeId() + ""},
                null);
    if (cursor != null) {
      count = cursor.getCount();
      cursor.close();
    }

    return count;
  }
}
