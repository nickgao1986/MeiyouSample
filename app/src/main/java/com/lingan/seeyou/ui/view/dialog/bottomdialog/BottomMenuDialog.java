package com.lingan.seeyou.ui.view.dialog.bottomdialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * 底部菜单对话框
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-31
 * Time: 下午4:00
 * To change this template use File | Settings | File Templates.
 * 重构 by zhuangyufeng on 2017
 */
@SuppressLint("ResourceAsColor")
public class BottomMenuDialog extends com.lingan.seeyou.ui.view.weel.BaseBottomDialog {


    protected TextView tvTitle;
    protected TextView tvContent;
    protected View lineView;
    protected LinearLayout linearContainer;
    protected List<BottomMenuModel> bottomMenuModels = new ArrayList<>();


    public BottomMenuDialog(Activity context, List<BottomMenuModel> list) {
        this(context, list, true);
    }

    public BottomMenuDialog(Activity context, List<BottomMenuModel> list, boolean isShowCancelButton) {
        super(context, list, isShowCancelButton);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        //  initDatas(list);

    }


    @Override
    public void initDatas(Object... params) {
        try {
            List<BottomMenuModel> list = (List<BottomMenuModel>) params[0];

            if (list != null) {
                if (bottomMenuModels == null)
                    bottomMenuModels = new ArrayList<>();
                bottomMenuModels.clear();
                bottomMenuModels.addAll(list);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public interface OnMenuSelectListener {
        public void OnSelect(int index, String title);
    }


    public interface OnSelectListener {
        public void OnSelect(int index, int flag);
    }

    public interface OnAnalyzeListener {
        public void onEvent(int index);
    }

    public OnMenuSelectListener menuSelectListener;


    public OnSelectListener onSelectListener;

    public OnAnalyzeListener onAnalyzeListener;

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnMenuSelectListener(OnMenuSelectListener listener) {
        menuSelectListener = listener;
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnSelectListener(OnSelectListener listener) {
        onSelectListener = listener;
    }

    public void setTitle(String str) {
        if (str == null)
            return;
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(str);
    }

    public void setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
            tvContent.setText(content);

        }
    }

    public void setOnAnalyzeListener(OnAnalyzeListener listener) {
        onAnalyzeListener = listener;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void initUI(Object... params) {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tv_content);
        lineView = findViewById(R.id.lineView);
        tvTitle.setVisibility(View.GONE);
        tvContent.setVisibility(View.GONE);
        lineView.setVisibility(View.GONE);
        //
        linearContainer = (LinearLayout) findViewById(R.id.linearContainer);
        linearContainer.removeAllViews();
        // LogUtils.d(TAG, "-->bottomMenuModels size:" + bottomMenuModels.size());
        if (bottomMenuModels != null) {
            //加入菜单
            addMenuButton();
            //加入取消
            if (params[1] instanceof Boolean) {
                if ((Boolean) params[1])
                    addCancelButton();
            } else {
                addCancelButton();
            }
        }
    }

    //加入菜单
    private void addMenuButton() {
        for (int i = 0; i < bottomMenuModels.size(); i++) {
            BottomMenuModel menuModel = bottomMenuModels.get(i);
            Button menuButton = (Button) ViewFactory.from(mContext).getLayoutInflater().inflate(R.layout.item_bottom_menu_dialog, linearContainer, false);
            menuButton.setText(menuModel.title);
            int pading = DeviceUtils.dip2px(mContext.getApplicationContext(), 8);
            menuButton.setPadding(pading, pading, pading, pading);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height = DeviceUtils.dip2px(getContext(), 48);
            final int index = i;
            final String title = menuModel.title;
            final int flag = menuModel.type;
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    if (menuSelectListener != null) {
                        menuSelectListener.OnSelect(index, title);
                    }
                    if (onSelectListener != null) {
                        onSelectListener.OnSelect(index, flag);
                    }
                    if (onAnalyzeListener != null) {
                        onAnalyzeListener.onEvent(index);
                    }
                    dismissDialogEx();
                }
            });
            if (i != 0) {
                addLineView(linearContainer, R.color.black_e, 0.5f);
            }
            linearContainer.addView(menuButton, layoutParams);
        }
    }

    //加入取消按钮
    private void addCancelButton() {
        Button button = (Button) ViewFactory.from(mContext).getLayoutInflater().inflate(R.layout.item_bottom_menu_dialog, linearContainer, false);;
        button.setText("取消");
        int pading = DeviceUtils.dip2px(mContext.getApplicationContext(), 8);
        button.setPadding(pading, pading, pading, pading);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.height = DeviceUtils.dip2px(getContext(), 48);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To change body of implemented methods use File | Settings | File Templates.
                if (menuSelectListener != null) {
                    menuSelectListener.OnSelect(-1, "");
                }

                if (onSelectListener != null) {
                    onSelectListener.OnSelect(-1, -1);
                }
                dismissDialogEx();
            }
        });
        addLineView(linearContainer, R.color.black_i, 10);
        linearContainer.addView(button, layoutParams);
    }

    //画线
    private void addLineView(LinearLayout parantView, int resId, float height) {
        LinearLayout.LayoutParams lineLayoutParmas = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DeviceUtils.dip2px(mContext.getApplicationContext(), height));
        View view = new View(mContext);
        SkinManager.getInstance().setDrawableBackground(view, resId);
        parantView.addView(view, lineLayoutParmas);
    }



    public void dismissDialogEx() {
        try {
            this.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.layout_bottom_menu_dialog_new;
    }

    @Override
    public void show() {
        if (TextUtils.isEmpty(tvTitle.getText())) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);
        }
        super.show();
    }

    @Override
    public View getRootView() {
        return findViewById(R.id.rootView);
    }

    public TextView getTitle() {
        return tvTitle;
    }


}
