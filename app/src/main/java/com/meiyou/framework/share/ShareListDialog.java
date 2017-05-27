package com.meiyou.framework.share;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.ViewFactory;
import com.meiyou.framework.share.data.BaseShareInfo;

import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.skin.ToastUtils;

/**
 * Created by hxd on 15/7/27.
 */
public class ShareListDialog extends Dialog {
    protected LinearLayout layoutTop, layoutBottom;
    protected View view;
    //protected TextView tvTitle;
    protected HorizontalScrollView hsViewTop, hsViewBottom;
    protected ShareListController shareListController;
    protected ShareType[] shareTypes;
    protected BaseShareInfo shareInfoDO;
    protected Activity activity;
    protected ShareTypeChoseListener choseListener;
    protected ShareResultCallback mShareResultCallback;
    private OnActivityFinishListener onActivityFinishListener;
    protected ViewFactory viewFactory;

    int showItemNum = 4;

    public ShareListDialog(Activity context, BaseShareInfo shareInfoDO,
                           ShareTypeChoseListener choseListener) {
        this(context, shareInfoDO, choseListener, null);
    }

    public ShareListDialog(Activity context, BaseShareInfo shareInfoDO,
                           ShareTypeChoseListener choseListener,
                           ShareResultCallback callback) {
        super(context);
        init(context, shareInfoDO, choseListener);
        this.activity = context;
        mShareResultCallback = callback;
    }


    public ShareListDialog(Builder builder) {
        super(builder.activity);
        shareListController = builder.shareListController;
        choseListener = builder.choseListener;
        mShareResultCallback = builder.mShareResultCallback;
        setOnActivityFinishListener(builder.onActivityFinishListener);
        setActivity(builder.activity);
        shareInfoDO = builder.shareInfoDO;
        init(builder.activity, shareInfoDO, choseListener);
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public void setDialogTitle(String title) {
        if (!StringUtils.isNull(title)) {
            //tvTitle.setText(title);
        }
    }

    public void setOnActivityFinishListener(OnActivityFinishListener listener) {
        this.onActivityFinishListener = listener;
    }

    public void setShowItemNum(int num) {
        showItemNum = num;
    }

    @Override
    public void show() {
        super.show();
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = DeviceUtils.getScreenWidth(activity);// 设置宽度
        getWindow().setAttributes(lp);

    }

    protected void initUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View layoutView = viewFactory.getLayoutInflater().inflate(R.layout.dialog_share_list, null);
        setContentView(layoutView);

        Window dialogWindow = getWindow();
        ColorDrawable dw = new ColorDrawable(0x0000ff00);

        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawable(dw);
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.DialogBottomAnimation);
        }

        view = findViewById(R.id.view);
        hsViewTop = (HorizontalScrollView) findViewById(R.id.hsViewTop);
        hsViewBottom = (HorizontalScrollView) findViewById(R.id.hsViewBottom);
        //tvTitle = (TextView) findViewById(R.id.tvTitle);
        layoutTop = (LinearLayout) findViewById(R.id.ll_top_share);
        layoutBottom = (LinearLayout) findViewById(R.id.ll_bottom_share);
        findViewById(R.id.dialog_share_cancel).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareListDialog.this.dismiss();
                        if (onActivityFinishListener != null) {
                            onActivityFinishListener.onActivityFinish();
                        }
                    }
                });

        // tvTitle.setVisibility(View.VISIBLE);
        //tvTitle.setText(R.string.share_to);
        initTopView();
        initBottomView();

    }

    protected void initData(Activity activity, BaseShareInfo shareInfoDO, ShareTypeChoseListener choseListener) {
        shareListController = ShareListController.getInstance();
        shareTypes = getShareTypeList();
        this.shareInfoDO = shareInfoDO;
        this.activity = activity;
        this.choseListener = choseListener;
    }


    protected void init(Activity context, BaseShareInfo shareInfoDO, ShareTypeChoseListener choseListener) {
        viewFactory = ViewFactory.from(context);
        initData(context, shareInfoDO, choseListener);
        initUI();
    }

    protected ShareType[] getShareTypeList() {
        return ShareType.getDefaultShareTypeValues();
    }

    protected void initTopView() {
        if (shareTypes.length > 0) {
            hsViewTop.setVisibility(View.VISIBLE);
            for (int i = 0; i < shareTypes.length; ++i) {
                final ShareType shareType = shareTypes[i];
                View view_top = LayoutInflater.from(getContext()).inflate(R.layout.layout_share_text_view, null);
                ImageView ivShare = (ImageView) view_top.findViewById(R.id.ivShare);
                ivShare.setBackgroundResource(shareType.getIconId());
                final TextView tvShare_top = (TextView) view_top.findViewById(R.id.tvShare);
                tvShare_top.setText(shareType.getTitleId());
                view_top.setTag(shareType);
                //
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //layoutParams.width = (DeviceUtils.getScreenWidth(getContext()) - DeviceUtils.dip2px(getContext(), 10)) / showItemNum;
//                layoutParams.width = (DeviceUtils.getScreenWidth(getContext()) - DeviceUtils.dip2px(getContext(), 20)) / 4;
                layoutParams.rightMargin = DeviceUtils.dip2px(getContext(), 10);
                view_top.setLayoutParams(layoutParams);
                layoutTop.addView(view_top);
                view_top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareType type = (ShareType) v.getTag();
                        if (choseListener != null) {
                            shareInfoDO = choseListener.onChoose(shareType, shareInfoDO);
                        }
                        if (shareInfoDO == null) {
                            ToastUtils.showToast(activity, R.string.share_content_empty);
                            return;
                        }
                        SocialService.getInstance().prepare(activity);
                        shareListController.onShare(activity, type, shareInfoDO, mShareResultCallback);
                        ShareListDialog.this.dismiss();
                    }
                });
            }
        } else {
            hsViewTop.setVisibility(View.GONE);
        }
    }

    protected void initBottomView() {
        hsViewBottom.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public interface OnActivityFinishListener {
        void onActivityFinish();
    }


    public static final class Builder {
        private ShareListController shareListController;
        private ShareTypeChoseListener choseListener;
        private ShareResultCallback mShareResultCallback;
        private OnActivityFinishListener onActivityFinishListener;
        private Activity activity;
        private BaseShareInfo shareInfoDO;

        private Builder() {
        }

        public Builder shareListController(ShareListController val) {
            shareListController = val;
            return this;
        }

        public Builder choseListener(ShareTypeChoseListener val) {
            choseListener = val;
            return this;
        }

        public Builder shareResultCallback(ShareResultCallback val) {
            mShareResultCallback = val;
            return this;
        }

        public Builder onActivityFinishListener(OnActivityFinishListener val) {
            onActivityFinishListener = val;
            return this;
        }

        public Builder activity(Activity val) {
            activity = val;
            return this;
        }

        public Builder shareInfoDO(BaseShareInfo val) {
            shareInfoDO = val;
            return this;
        }

        public ShareListDialog build() {
            return new ShareListDialog(this);
        }

//        public ShareWithCopyUrlListDialog PeriodBuild() {
//            return new ShareWithCopyUrlListDialog(this);
//        }
    }
}
