package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;


public class LinearListView extends LinearLayout {
    private boolean isRemoveDivider;
    private boolean isHorizontal;
    private Context context;
    private ListAdapter mAdapter;
    private int mDividerLeftMargin;
    public static final String TAG="divider";

    public LinearListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LinearListView(Context context) {
        super(context);
        this.context = context;
    }

    private DataSetObserver mDataObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            setupChildren();
        }

        @Override
        public void onInvalidated() {
            setupChildren();
        }

    };

    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataObserver);
        }
        setupChildren();
    }

    private void setupChildren(){
        if (mAdapter == null) {
            return;
        }
        removeAllViews();
        if (isHorizontal) {
            setOrientation(LinearLayout.HORIZONTAL);
        } else {
            setOrientation(LinearLayout.VERTICAL);

        }
        int density = (int) getResources().getDisplayMetrics().density;
        density = density <= 0 ? 1 : 1;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View view = mAdapter.getView(i, null, null);
            addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            if (i != mAdapter.getCount() - 1 && !isRemoveDivider) {
                View divider = new View(getContext());
                divider.setTag(TAG);
                SkinManager.getInstance().setDrawableBackground(divider, R.drawable.apk_all_lineone);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, density);
                layoutParams.leftMargin = DeviceUtils.dip2px(getContext(), mDividerLeftMargin);
                addView(divider, layoutParams);
            }
        }
    }


    public void setAdapter(ListAdapter adapter, int leftWidth) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataObserver);
        }
        mDividerLeftMargin = leftWidth;
        setupChildren();
    }

    public void setRemoveDivider(boolean isRemoveDivider) {
        this.isRemoveDivider = isRemoveDivider;
    }

    public void setHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }


}



