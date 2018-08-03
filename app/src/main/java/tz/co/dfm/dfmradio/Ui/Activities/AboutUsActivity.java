package tz.co.dfm.dfmradio.Ui.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.dfm.dfmradio.R;

public class AboutUsActivity extends AppCompatActivity {

    @BindView(R.id.wv_about_us)
    WebView wvAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        wvAboutUs.loadUrl("file:///android_asset/about_dfm.html");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
