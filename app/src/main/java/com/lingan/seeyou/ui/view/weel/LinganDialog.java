package com.lingan.seeyou.ui.view.weel;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

/**
 * Created by hxd on 15/11/18.
 */
public class LinganDialog extends Dialog {
    private static final String TAG ="LinganDialog" ;
    protected ViewFactory viewFactory;
    public View layoutView;
    private Context mContext;

    public LinganDialog(Context context) {
        super(context);
        mContext = context;
        initViewFactory();
    }

    public LinganDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        initViewFactory();
    }


    @Override
    public void setContentView(int layoutResID) {
        layoutView = viewFactory.getLayoutInflater().inflate(layoutResID, null);
        super.setContentView(layoutView);
    }

    @Override
    public void show() {
        super.show();
      /*  if (SkinManager.getInstance().isApply()) {
            SkinManager.getInstance().applyView(viewFactory, (ViewGroup) layoutView, null);
        }*/
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if(mContext instanceof Activity){
//            LogUtils.d(TAG,"BehaviorActivityWatcher LinganDialog onWindowFocusChanged hasFocus:"+hasFocus+"==>name:"+((Activity)mContext).getClass().getSimpleName());
//            BehaviorActivityWatcher watcher = (BehaviorActivityWatcher) WatcherManager.getInstance().getWatcher(WatcherKey.KEY_BEHAVIOR_STACK);
//            if(watcher!=null){
//                watcher.addDialogWindowFocusChanged((Activity)mContext,hasFocus);
//            }
//        }
    }

    @Override
    public void setContentView(View view) {
        throw new RuntimeException("do not use this method");
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        throw new RuntimeException("do not use this method");
    }

    protected LinganDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initViewFactory() {
        if (viewFactory == null) {
            viewFactory = ViewFactory.from(getContext());
        }
    }


}
