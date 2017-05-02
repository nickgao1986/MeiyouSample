package biz;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class XiuAlertDialog extends LinganDialog implements View.OnClickListener {

    protected Activity mContext;

    protected View mRootView, centerLineView;
    protected String strContent;
    protected String strTitle;

    protected TextView tvTitle;
    protected TextView tvContent;
    protected Button mButtonOK;
    protected Button mButtonCancel;
    protected ImageView mIvLogo;

    protected LinearLayout linearTop;
    protected onDialogClickListener mListener;

    public XiuAlertDialog(Activity context, String strTitle, String strContent) {
        super(context);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        mContext = context;
        this.strTitle = strTitle;
        this.strContent = strContent;
        initView();

    }

    public XiuAlertDialog(Activity context, int strResTitle, int strResContent) {
        super(context);
        mContext = context;
        if (strResTitle != -1) {
            this.strTitle = context.getString(strResTitle);
        }
        if (strResContent != -1) {
            this.strContent = context.getString(strResContent);
        }
        initView();
    }

    /**
     * 一行提示语
     *
     * @param context
     * @param strTitle
     */
    public XiuAlertDialog(Activity context, String strTitle) {
        super(context);
        mContext = context;
        this.strTitle = strTitle;
        strContent = null;
        initView();
    }

    @SuppressWarnings("deprecation")
    protected void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_alert_pink);
        mRootView = findViewById(R.id.rootView);
        mRootView.setBackgroundDrawable(new ColorDrawable(0x0000ff00));

        linearTop = (LinearLayout) findViewById(R.id.dialog_top);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(strTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        centerLineView = findViewById(R.id.center_line);
        if (strContent == null) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(strContent);
        }

        mButtonOK = (Button) findViewById(R.id.btnOK);
        mButtonOK.setOnClickListener(this);
        mButtonCancel = (Button) findViewById(R.id.btnCancle);
        mButtonCancel.setOnClickListener(this);

        mIvLogo = (ImageView) findViewById(R.id.ivLogo);

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mListener != null) {
                    mListener.onCancle();
                }
            }
        });

        //fillResource();

        Window dialogWindow = getWindow();
        dialogWindow.setWindowAnimations(R.style.alertDialogWindowAnimation); //设置窗口弹出动画
        ColorDrawable dw = new ColorDrawable(0x0000ff00);
        dialogWindow.setBackgroundDrawable(dw);
    }

    /**
     * set dialog background
     */
//    public void setCustomBackground(int resTop, int resBottom) {
//        ResourceUtils.setBackgroundDrawableCompatible(mContext, linearTop, resTop);
//        ResourceUtils.setBackgroundDrawableCompatible(mContext, findViewById(R.id.dialog_bottom), resBottom);
//    }

    public XiuAlertDialog setOnClickListener(
            onDialogClickListener onClickListener) {
        mListener = onClickListener;
        return this;
    }

    public void dismissDialogEx() {
        try {
            this.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onClick(View v) {
        dismissDialogEx();

        int id = v.getId();
        if (id == R.id.btnOK) {
            if (mListener != null) {
                mListener.onOk();
            }
        } else if (id == R.id.btnCancle) {
            if (mListener != null) {
                mListener.onCancle();
            }
        }
    }

    public void setImageRes(int resId) {
        if (resId != -1) {
            mIvLogo.setImageResource(resId);
            mIvLogo.setVisibility(View.VISIBLE);
        } else {
            mIvLogo.setVisibility(View.GONE);
        }
    }

    public XiuAlertDialog setButtonOkText(String strText) {
        mButtonOK.setText(strText);
        return this;
    }

    public XiuAlertDialog setButtonOkText(int strText) {
        mButtonOK.setText(mContext.getString(strText));
        return this;
    }

    public XiuAlertDialog setButtonCancleBack(int resId) {
        mButtonCancel.setBackgroundResource(resId);
        return this;
    }

    public XiuAlertDialog setButtonOKBack(int resId) {
        mButtonOK.setBackgroundResource(resId);
        return this;
    }

    public XiuAlertDialog setButtonCancleText(String strText) {
        mButtonCancel.setText(strText);
        return this;
    }

    public XiuAlertDialog setButtonCancleText(int strText) {
        mButtonCancel.setText(mContext.getString(strText));
        return this;
    }

    public XiuAlertDialog setButtonCancleTextColor(int textColor) {
        mButtonCancel.setTextColor(textColor);
        return this;
    }

    public XiuAlertDialog setTextSpace(float add, float mult) {
        tvContent.setLineSpacing(add, mult);
        return this;
    }

    public void setTitleVisible(boolean visible) {
        if (visible) {
            linearTop.setVisibility(View.VISIBLE);
        } else {
            linearTop.setVisibility(View.GONE);
        }
    }

    public void setContentGravity(int gravity) {
        tvContent.setGravity(gravity);
    }

    public TextView getContentTextView() {
        return tvContent;
    }

    public void setContentPadding(int left, int top, int right, int bottom) {
        tvContent.setPadding(left, top, right, bottom);
    }

    // 设置内容高一点
    public void setContentHigher() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvContent
                .getLayoutParams();
        params.topMargin = 5;
        params.bottomMargin = 5;
        tvContent.requestLayout();

    }

    // 显示一个按钮
    public XiuAlertDialog showOneButton() {
        try {
            mButtonCancel.setVisibility(View.GONE);
            centerLineView.setVisibility(View.GONE);
            mButtonOK.setBackgroundResource(R.drawable.rectangle_bottom_corners_selector);
            /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mButtonOK.getLayoutParams();
            mButtonOK.requestLayout();*/
            show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public void show() {
        try {
            super.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public interface onDialogClickListener {
        public void onOk();

        public void onCancle();
    }


    /**
     * 单独一个按钮的对话框
     */
    public static void setDialog(Activity context) {
        final XiuAlertDialog dialog = new XiuAlertDialog(
                context, "提示", context.getResources().getString(
                R.string.community_most_circle));
        dialog.showOneButton();


    }


}