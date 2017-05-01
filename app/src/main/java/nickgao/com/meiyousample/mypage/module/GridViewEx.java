package nickgao.com.meiyousample.mypage.module;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class GridViewEx extends GridView {
    private boolean mIsOnMeasure;

    public GridViewEx(Context context){
        super(context);
    }

    public GridViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mIsOnMeasure = true;
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mIsOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }

    public boolean isOnMeasure() {
        return mIsOnMeasure;
    }
}
