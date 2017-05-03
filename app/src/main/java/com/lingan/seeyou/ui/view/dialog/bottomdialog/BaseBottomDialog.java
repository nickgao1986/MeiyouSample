package com.lingan.seeyou.ui.view.dialog.bottomdialog;

import android.content.Context;

/**
 * 底部对话框 基类
 * Created by Administrator on 13-10-10.
 *  user com.meiyou.framework.ui.widgets.wheel.BaseBottomDialog instead
 */
@Deprecated
public abstract class BaseBottomDialog extends com.lingan.seeyou.ui.view.weel.BaseBottomDialog{

    public BaseBottomDialog(Context context, Object... params) {
        super(context, params);
    }
    public BaseBottomDialog(int style, Context context, Object... params) {
        super(style,context,params);
    }
}/*extends LinganDialog {

    protected Activity mContext;
    protected View mRootView;

    *//**
     * 获取对话框布局
     *
     * @return
     *//*
    public abstract int getLayoutId();

    *//**
     * 获取 布局的父亲View
     *
     * @return
     *//*
    public abstract View getRootView();


    *//**
     * 初始化数据
     *//*

    public abstract void initDatas(Object... params);

    *//**
     * 初始化UI
     *//*

    public abstract void initUI(Object... params);

    public BaseBottomDialog(Activity context, Object... params) {
        super(context);
        mContext = context;
        init(mContext, params);
    }

    public BaseBottomDialog(int style, Activity context, Object... params) {
        super(context, style);
        mContext = context;
        init(mContext, params);
    }

    private void init(Activity context, Object... params) {
        try {
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //布局
            setContentView(getLayoutId());
            mRootView = getRootView();
            mRootView.setBackgroundDrawable(new ColorDrawable(0x0000ff00));

            //初始化数据
            initDatas(params);

            //初始化UI
            initUI(params);

            //宽度和动画
            Window dialogWindow = getWindow();
            ColorDrawable dw = new ColorDrawable(0x0000ff00);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = DeviceUtils.getScreenWidth(context);

            dialogWindow.setBackgroundDrawable(dw);
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.DialogBottomPopupAnimation);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public void show() {
        super.show();
        //setCanceledOnTouchOutside(true);
        WindowManager windowManager = mContext.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (display.getWidth()); //设置宽度
        getWindow().setWindowAnimations(R.style.DialogBottomPopupAnimation);
        getWindow().setAttributes(lp);
    }

    public void setCancelable(boolean flag) {
        setCanceledOnTouchOutside(flag);
    }
}*/

