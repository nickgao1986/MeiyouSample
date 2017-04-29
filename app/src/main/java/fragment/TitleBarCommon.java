package fragment;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class TitleBarCommon extends RelativeLayout {
    private TextView mTvTitle, mTvLeft, mTvRight;
    private ImageView mIvLeft, mIvRight;
    private View mTitleContainer;
    private Context context;

    public TitleBarCommon(Context context) {
        this(context, null);

    }

    public TitleBarCommon(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.mTitleContainer = LayoutInflater.from(context).inflate( R.layout.layout_base_header_common, this,true);
        init();
    }

    public int getTitleBarHeight(){
        return DeviceUtils.dip2px(context,44);
    }

    public void init() {
        mTvTitle = (TextView) mTitleContainer.findViewById(R.id.baselayout_tv_title);
        mIvLeft = (ImageView) mTitleContainer.findViewById(R.id.baselayout_iv_left);
        mTvLeft = (TextView) mTitleContainer.findViewById(R.id.baselayout_tv_left);
        mIvRight = (ImageView) mTitleContainer.findViewById(R.id.baselayout_iv_right);
        mTvRight = (TextView) mTitleContainer.findViewById(R.id.baselayout_tv_right_yunqi);
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        mTvTitle = tvTitle;
    }

    public TextView getTvLeft() {
        return mTvLeft;
    }

    public void setTvLeft(TextView tvLeft) {
        mTvLeft = tvLeft;
    }

    public TextView getTvRight() {
        return mTvRight;
    }

    public void setTvRight(TextView tvRight) {
        mTvRight = tvRight;
    }

    public ImageView getIvLeft() {
        return mIvLeft;
    }

    public void setIvLeft(ImageView ivLeft) {
        mIvLeft = ivLeft;
    }

    public ImageView getIvRight() {
        return mIvRight;
    }

    public void setIvRight(ImageView ivRight) {
        mIvRight = ivRight;
    }

    public View getTitleContainer() {
        return mTitleContainer;
    }

    public void setTitleContainer(View titleContainer) {
        mTitleContainer = titleContainer;
    }

    private LayoutInflater mLayoutInflater;
    public void setLayoutInflater(LayoutInflater inflater){
        mLayoutInflater = inflater;
    }
    /**自定义头部**/
    public void setCustomTitleBar(int customTitleLayoutId){
        if (customTitleLayoutId <=0) {
            this.setVisibility(View.GONE);
            return ;
        }
        removeAllViews();
        View baseHead;
        if(mLayoutInflater!=null){
            baseHead = mLayoutInflater.inflate(customTitleLayoutId, null);
        }else{
            baseHead = View.inflate(context,customTitleLayoutId, null);
        }
        RelativeLayout.LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addView(baseHead,params);
    }

    /**自定义头部**/
    public void setCustomTitleBar(View baseHead){
        if (baseHead == null) {
            this.setVisibility(View.GONE);
            return ;
        }
        removeAllViews();
//        View baseHead = View.inflate(context,customTitleLayoutId, null);
        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addView(baseHead,params);
    }
    /**
     * 设置标题文字 (若要隐藏设置参数为-1)
     *
     * @param resId
     */
    public TitleBarCommon setTitle(int resId) {
        if (resId != -1) {
            mTvTitle.setText(resId);
            mTvTitle.setVisibility(View.VISIBLE);
        } else {
            mTvTitle.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置标题名称
     *
     * @param str
     * @return
     */
    public TitleBarCommon setTitle(String str) {
        try {
            mTvTitle.setText(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    /**
     * 设置字体颜色
     *
     * @param color
     */
    public TitleBarCommon setTitleColor(int color) {
        if (color != -1) {
            mTvTitle.setTextColor(color);
        }
        return this;
    }

    /**
     * 获取标题控件
     *
     * @return
     */
    public TextView getTitle() {
        return mTvTitle;
    }

    /**
     * 左TextView 文字
     *
     * @param resId
     * @return
     */
    public TitleBarCommon setLeftTextViewString(int resId) {
        if (resId != -1) {
            mTvLeft.setText(resId);
            mTvLeft.setVisibility(View.VISIBLE);
        } else {
            mTvLeft.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置左TextView点击事件
     *
     * @param lsnr
     * @return
     */
    public TitleBarCommon setLeftTextViewListener(OnClickListener lsnr) {
        mTvLeft.setOnClickListener(lsnr);
        return this;
    }

    /**
     * 获取左边按钮控件
     *
     * @return
     */
    public TextView getLeftTextView() {
        return mTvLeft;
    }

    /**
     * 右TextView 文字
     *
     * @param resId
     * @return
     */
    public TitleBarCommon setRightTextViewString(int resId) {
        if (resId != -1) {
            mTvRight.setText(resId);
            mTvRight.setVisibility(View.VISIBLE);
        } else {
            mTvRight.setVisibility(View.GONE);
        }
        return this;
    }
    /**
     * 右TextView 文字
     *
     * @param
     * @return
     */
    public TitleBarCommon setRightTextViewString(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTvRight.setText(text);
            mTvRight.setVisibility(View.VISIBLE);
        } else {
            mTvRight.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置右TextView点击事件
     *
     * @param lsnr
     * @return
     */
    public TitleBarCommon setRightTextViewListener(OnClickListener lsnr) {
        mTvRight.setOnClickListener(lsnr);
        return this;
    }

    /**
     * 获取右边按钮控件，
     *
     * @return
     */
    public TextView getRightTextView() {
        return mTvRight;
    }

    /**
     * 设置左按钮资源文件
     *
     * @param resId
     * @return
     */
    public TitleBarCommon setLeftButtonRes(int resId) {
        if (resId != -1) {
            mIvLeft.setVisibility(View.VISIBLE);
            mIvLeft.setImageDrawable(context.getResources().getDrawable(resId));
        } else {
            mIvLeft.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置左按钮点击事件
     *
     * @param lsnr
     * @return
     */
    public TitleBarCommon setLeftButtonListener(OnClickListener lsnr) {
        mIvLeft.setOnClickListener(lsnr);
        return this;
    }

    /**
     * 获取左边按钮控件
     *
     * @return
     */
    public View getLeftButtonView() {
        return mIvLeft;
    }

    /**
     * 设置右按钮资源文件
     *
     * @param resId
     * @return
     */
    public TitleBarCommon setRightButtonRes(int resId) {
        if (resId != -1) {
            mIvRight.setVisibility(View.VISIBLE);
            mIvRight.setImageDrawable(context.getResources().getDrawable(resId));
            mTvRight.setVisibility(View.GONE);
        } else {
            mIvRight.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置右按钮点击事件
     *
     * @param lsnr
     * @return
     */
    public TitleBarCommon setRightButtonListener(OnClickListener lsnr) {
        mIvRight.setOnClickListener(lsnr);
        return this;
    }

    /**
     * 获取右边按钮控件
     *
     * @return
     */
    public View getRightButtonView() {
        return mIvRight;
    }

    /**
     * 设置各按钮点击事件 (只显示常用的2个按钮)
     *
     * @param lsnrLeft  左
     * @param lsnrRight 右
     */
    public TitleBarCommon setButtonListener(OnClickListener lsnrLeft,
                                            OnClickListener lsnrRight) {
        mIvLeft.setOnClickListener(lsnrLeft);
        mIvRight.setOnClickListener(lsnrRight);
        return this;
    }

    /**
     * 设置各按钮的图片资源 若要隐藏设置参数为-1 (只s显示常用的2个按钮)
     *
     * @param resIdLeft  左1
     * @param resIdRight 右1
     */
    public TitleBarCommon setButtonResources(int resIdLeft, int resIdRight) {
        if (resIdLeft != -1) {
            mIvLeft.setImageDrawable(context.getResources().getDrawable(resIdLeft));
            mIvLeft.setVisibility(View.VISIBLE);
        } else {
            mIvLeft.setVisibility(View.GONE);
        }
        if (resIdRight != -1) {
            mIvRight.setImageDrawable(context.getResources().getDrawable(resIdRight));
            mIvRight.setVisibility(View.VISIBLE);
        } else {
            mIvRight.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置titleBar背景颜色
     *
     * @param color
     */
    @SuppressWarnings("deprecation")
    public TitleBarCommon setTitleBarBackgroundColor(int color) {
        if (color != -1) {
            mTitleContainer.setBackgroundColor(color);
        } else {
            mTitleContainer.setBackgroundDrawable(null);
        }
        return this;
    }

    /**
     * 获取titleBar布局
     *
     * @return
     */
    public View getTitleBar() {
        return mTitleContainer;
    }

    /**
     * titleBar皮肤更新
     */
    public void updateSkin() {
       // ResourceUtils.setBackgroundDrawableCompatible(context, mTitleContainer, R.drawable.apk_default_titlebar_bg);
        mTitleContainer.setBackgroundResource(R.drawable.apk_default_titlebar_bg);
        mIvLeft.setImageDrawable(context.getResources().getDrawable(R.drawable.back_layout));
        mTvTitle.setTextColor(context.getResources().getColor(R.color.white_a));
        mTvRight.setTextColor(context.getResources().getColor(R.color.white_a));
        mTvLeft.setTextColor(context.getResources().getColor(R.color.white_a));
    }

    /****
     * 隐藏返回键
     */
    public void hideBack(){
        mIvLeft.setVisibility(View.GONE);
    }
}
