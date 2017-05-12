package nickgao.com.meiyousample.personal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import nickgao.com.meiyousample.R;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class ImageViewWithMask extends FrameLayout {

    private Context context;
    private LoaderImageView loaderImageView;
    private ImageView imageView;

    public ImageViewWithMask(Context context) {
        super(context);
        init(context);
    }

    public ImageViewWithMask(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageViewWithMask(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //一个图片控件
        loaderImageView = new LoaderImageView(context);
        FrameLayout.LayoutParams lpLoaderImageView = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        loaderImageView.setLayoutParams(lpLoaderImageView);
        loaderImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        loaderImageView.setImageResource(R.drawable.personal_fragment_header_background);

        imageView = new ImageView(context);
        imageView.setLayoutParams(lpLoaderImageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.personal_fragment_header_pic_mask);

        addView(loaderImageView);
        // addView(imageView);
    }

    public void removeMask() {
        if(this.getChildCount() == 2) {
            removeView(imageView);
            invalidate();
        }
    }


    public void addMask() {
        if(this.getChildCount() == 1) {
            addView(imageView);
            invalidate();
        }
    }

    public Drawable getDrawable() {
        return loaderImageView.getDrawable();
    }

    public LoaderImageView getLoaderImageView() {
        return loaderImageView;
    }

    public void setImageBitmap(Bitmap bitmap) {
        loaderImageView.setImageBitmap(bitmap);
    }

}

