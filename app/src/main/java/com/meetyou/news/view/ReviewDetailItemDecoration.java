package com.meetyou.news.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.meiyousample.R;


/**
 * 垂直间隔
 * Created by LinXin on 2016/3/28 19:19.
 */
public class ReviewDetailItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;
    private int paddingLeft;
    private Paint mLinePaint;

    public ReviewDetailItemDecoration(Context context) {
        this.mVerticalSpaceHeight = 1;
        this.paddingLeft = DeviceUtils.dip2px(context, 0);
        int color = SkinManager.getInstance().getAdapterColor(R.color.black_e);
        this.mLinePaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        this.mLinePaint.setColor(color);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int count = parent.getAdapter().getItemCount();
        if (count <= 2)
            return;
        int position = parent.getChildAdapterPosition(view);
        if (position > 0) {//第一项是头部，所以不画分割线
            outRect.bottom = mVerticalSpaceHeight;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int adapterCount = parent.getAdapter().getItemCount();
        if (adapterCount <= 2)
            return;
        if (mLinePaint != null) {
            int left = parent.getPaddingLeft() + paddingLeft;
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);
                int adapterPosition = parent.getChildAdapterPosition(child);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mVerticalSpaceHeight;
                if (adapterPosition == adapterCount - 2) {//倒数第二项画满宽的直线
                    c.drawRect(parent.getPaddingLeft(), top, right, bottom, mLinePaint);
                } else if (adapterPosition > 0) {//第一项是头部，所以不画分割线
                    c.drawRect(left, top, right, bottom, mLinePaint);
                }
            }
        } else {
            super.onDraw(c, parent, state);
        }
    }
}
