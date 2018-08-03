package tz.co.dfm.dfmradio.Ui.CustomViews;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.exoplayer2.ui.PlayerView;

public class EpisodesPlayerView extends PlayerView {
    public EpisodesPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int desiredHeight = width * 2 / 3;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY));
    }
}
