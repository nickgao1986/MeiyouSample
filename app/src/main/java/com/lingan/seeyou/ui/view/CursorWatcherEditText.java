package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class CursorWatcherEditText extends EditText {

    private String mTargetUserName = "";
    private boolean mReplyToSomeOne = false;


    /**
     * 说说
     */
    private int oldDynamicId = 0;
    private int oldCommentId = 0;

    public int mDynamicId = 0;
    public int mCommentId = 0;

    public Object homeDynamicModel;
    public Object dynamicCommentModel;
    public View targetView; //触发控件y轴位置


    public CursorWatcherEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CursorWatcherEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CursorWatcherEditText(Context context) {
        super(context);
    }

    public boolean isReplyToSomeOne() {
        return mReplyToSomeOne;
    }

    private int mRefrenceId = -1;
    private int mDiegoFloorRefrenceId = -1;
    private int mSaveDiegoFloorRefrenceId = -1;

    public int getmRefrenceId() {
        return mRefrenceId;
    }

    public void setRefrenceId(int refrenceId) {
        mRefrenceId = refrenceId;
    }

    public int getmDiegoFloorRefrenceId() {
        return mDiegoFloorRefrenceId;
    }
    public void setDiegoFloorRefrenceId(int refrenceId) {
        mDiegoFloorRefrenceId = refrenceId;
    }

    public void setSaveDiegoFloorRefrenceId(int refrenceId) {
        mSaveDiegoFloorRefrenceId = refrenceId;
    }

    public String getmTargetUserName() {
        return mTargetUserName;
    }

    public void setmTargetUserName(String mTargetUserName) {
        this.mTargetUserName = mTargetUserName;
    }

    public boolean ismReplyToSomeOne() {
        return mReplyToSomeOne;
    }

    public void setmReplyToSomeOne(boolean mReplyToSomeOne) {
        this.mReplyToSomeOne = mReplyToSomeOne;
    }


    public void CleanSelf() {
        this.mTargetUserName = "";
        mReplyToSomeOne = false;
        mRefrenceId = -1;

        homeDynamicModel = new Object();
        dynamicCommentModel = new Object();

        oldCommentId = 0;
        oldDynamicId = 0;

        SpannableString emptySS = new SpannableString("");
        this.setText(emptySS);
    }

    /**
     * 楼层详情用法
     */
    public void CleanDiegoFloorSelf(int referenced_id) {
        this.mTargetUserName = "";
        mDiegoFloorRefrenceId = referenced_id;
        SpannableString emptySS = new SpannableString("");
        this.setText(emptySS);
    }

    public void resetEditText() {
        try {
            this.oldDynamicId = mDynamicId;
            this.oldCommentId = mCommentId;

            homeDynamicModel = new Object();
            dynamicCommentModel = new Object();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public float getTextSize() {
        return super.getTextSize();
    }

    public void setTargetUserName(String usrName, int refrenceId, Bitmap b) {
        try {
            this.mTargetUserName = usrName;
            this.mRefrenceId = refrenceId;
            this.mDiegoFloorRefrenceId = refrenceId;
            mTargetUserName = "@" + mTargetUserName + ":";
            mReplyToSomeOne = true;

            if(b == null || b.isRecycled()){
                return;
            }

            Drawable d = new BitmapDrawable(getContext().getResources(), b);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
            SpannableString ss = new SpannableString(" ");
            ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            this.setText(ss);
            this.append(" ");


            this.addTextChangedListener(new MyTextWatcher());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                if (mReplyToSomeOne) {
                    //移除监听并设置结果
                    if (start == 0 && before == 1) {
                        mReplyToSomeOne = false;
                        mRefrenceId = -1;
                    }
                }
                //移除监听并设置结果
                if (start == 0 && before == 1) {
                    mDiegoFloorRefrenceId = mSaveDiegoFloorRefrenceId;//清空的时候 默认把楼层主的id传进去回复
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            return super.dispatchTouchEvent(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        //不允许移动光标在 ImageSpan 之前
        if (mReplyToSomeOne) {
            try {
                if (selStart == 0 || selEnd == 0) {
                    this.setSelection(1);
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getHomeDynamicModel() {
        return homeDynamicModel;
    }

    public void setHomeDynamicModel(Object homeDynamicModel) {
        this.homeDynamicModel = homeDynamicModel;

    }

    public Object getDynamicCommentModel() {
        return dynamicCommentModel;
    }

    public void setDynamicCommentModel(Object dynamicCommentModel) {
        this.dynamicCommentModel = dynamicCommentModel;

    }

    public int getOldDynamicId() {
        return oldDynamicId;
    }

    public void setOldDynamicId(int oldDynamicId) {
        this.oldDynamicId = oldDynamicId;
    }

    public int getOldCommentId() {
        return oldCommentId;
    }

    public void setOldCommentId(int oldCommentId) {
        this.oldCommentId = oldCommentId;
    }

}