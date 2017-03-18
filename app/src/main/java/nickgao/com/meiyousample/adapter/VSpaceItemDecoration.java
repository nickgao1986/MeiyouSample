package nickgao.com.meiyousample.adapter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 垂直间隔
 * Created by LinXin on 2016/3/28 19:19.
 */
public class VSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;
    private int paddingLeft;
    private int paddingRight;
    private Paint mLinePaint;

    public VSpaceItemDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    public VSpaceItemDecoration(int mVerticalSpaceHeight, int paddingLeft, int paddingRight, @ColorInt int color) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.mLinePaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        this.mLinePaint.setColor(color);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int count = parent.getAdapter().getItemCount();
        if (position < count - 1) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mLinePaint != null) {
            int left = parent.getPaddingLeft() + paddingLeft;
            int right = parent.getWidth() - parent.getPaddingRight() - paddingRight;

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 2; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mVerticalSpaceHeight;
                c.drawRect(left, top, right, bottom, mLinePaint);
            }
        } else {
            super.onDraw(c, parent, state);
        }
    }
}
