package tz.co.dfm.dfmradio.Ui.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tz.co.dfm.dfmradio.MainActivity;
import tz.co.dfm.dfmradio.Models.Shows;
import tz.co.dfm.dfmradio.R;

import static tz.co.dfm.dfmradio.Helpers.Helper.buildMediaUrl;
import static tz.co.dfm.dfmradio.Helpers.Helper.buildThumbnailUrl;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_DATE;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_DESCRIPTION;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_FILE_URL;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_ID;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_MEDIA_TYPE;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_THUMBNAIL;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.EPISODE_TITLE;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_HOST;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_MODEL;
import static tz.co.dfm.dfmradio.Ui.Fragments.ShowEpisodesFragment.SHOW_NAME;

public class LoadSharedMedia extends AppCompatActivity {

    public static final String STATUS = "status";
    public static final String CONTENTS = "contents";
    @BindView(R.id.pb_loading_link)
    ProgressBar loadingLinkProgressBar;
    @BindView(R.id.btn_view_other_episodes)
    Button btnViewOtherEpisodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_shared_media);
        ButterKnife.bind(this);
        Uri uri = this.getIntent().getData();
        if (uri != null) {
            new LoadSharedUrlAsyncTask().execute(uri.toString());
        } else {
            loadingLinkProgressBar.setVisibility(View.GONE);
            btnViewOtherEpisodes.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_view_other_episodes)
    void openHomeActivity() {
        Intent intent = new Intent(LoadSharedMedia.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private Activity getActivity() {
        return this;
    }

    public void showErrorInformation() {
        loadingLinkProgressBar.setVisibility(View.GONE);
        btnViewOtherEpisodes.setVisibility(View.VISIBLE);
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadSharedUrlAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            String url = strings[0];
            String inputLine;
            String serverResponse;
            try {
                URL sharedUrl = new URL(url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) sharedUrl.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.connect();

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                bufferedReader.close();
                inputStreamReader.close();

                serverResponse = stringBuilder.toString();

            } catch (Exception ex) {
                serverResponse = null;
            }
            try {
                if (serverResponse != null) {
                    return new JSONObject(serverResponse);
                } else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject == null) {
                showErrorInformation();
            } else {
                if (jsonObject.optString(STATUS) == null || jsonObject.optInt(STATUS) == 0) {
                    showErrorInformation();
                } else {
                    openDetailsActivity(jsonObject);
                }
            }
        }

        void openDetailsActivity(JSONObject jsonResults) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(jsonResults.optString(CONTENTS));
                loadingLinkProgressBar.setVisibility(View.GONE);

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

                Intent intent = new Intent(getActivity(), EpisodeDetails.class);
                intent.putExtra(SHOW_MODEL, shows);
                getActivity().startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            } catch (JSONException e) {
                showErrorInformation();
            }
        }
    }
}
