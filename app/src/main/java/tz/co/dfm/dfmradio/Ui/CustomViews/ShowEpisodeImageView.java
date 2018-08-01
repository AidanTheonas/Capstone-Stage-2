package tz.co.dfm.dfmradio.Ui.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ShowEpisodeImageView extends ImageView{

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, (int) (measuredWidth / 1.5f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public ShowEpisodeImageView(Context context) {
        super(context);
    }

    public ShowEpisodeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowEpisodeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
