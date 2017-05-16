package nickgao.com.meiyousample.mypage.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.RoundedImageView;
import com.meetyou.crsdk.util.ImageLoader;

import nickgao.com.framework.utils.StringUtil;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.settings.MineItemModel;
import nickgao.com.meiyousample.settings.MineSection;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.okhttpexample.view.AbstractImageLoader;
import nickgao.com.okhttpexample.view.ImageLoadParams;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

abstract public class ViewHolder {

    /**
     * 显示images中图片的选择策略 -- 顺序，下一个
     */
    public static final int IMAGES_RANDOM_INDEX_NEXT = 1;

    /**
     * 显示images中图片的选择策略 -- 保持原样，不动
     */
    public static final int IMAGES_RANDOM_INDEX_NONE = 0;

    public LinearLayout ll_mine;
    public TextView tv_mine;
    public View mine_line;
    public TextView tv_mine_banner_rightarrow;
    public ImageView iv_mine_banner_rightarrow;
    public RelativeLayout rl_mine_banner_title;//该视图为titile,主要为了接收点击事件
    private LoaderImageView ivSectionIcon;
    private SparseIntArray imagesIndex;

    /**
     * 该view用于哪个界面(暂时的用处为事件统计)
     */
    private int from;

    public ViewHolder() {

    }

    public void setFrom(int from) {
        this.from = from;
    }

    @SuppressLint("ResourceAsColor")
    public void fillResources(Context context) {
//        SkinEngine.getInstance().setViewTextColor(mContext, tv_mine, R.color.black_at);
//        SkinEngine.getInstance().setViewBackground(mContext, ll_mine, R.drawable.apk_all_white);
//        SkinEngine.getInstance().setViewBackground(mContext, mine_line, R.drawable.apk_all_lineone);
//        SkinEngine.getInstance().setViewTextColor(mContext, tv_mine_banner_rightarrow, R.color.black_b);
//        SkinEngine.getInstance().setViewBackground(mContext, iv_mine_banner_rightarrow, R.drawable.all_icon_arrow);
    }

    public void initHolder(View convertView) {
        tv_mine = (TextView) convertView.findViewById(R.id.tv_mine);
        mine_line = convertView.findViewById(R.id.mine_line);
        ll_mine = (LinearLayout) convertView.findViewById(R.id.ll_mine);
        tv_mine_banner_rightarrow = (TextView) convertView.findViewById(R.id.tv_mine_banner_rightarrow);
        iv_mine_banner_rightarrow = (ImageView) convertView.findViewById(R.id.iv_mine_banner_rightarrow);
        rl_mine_banner_title = (RelativeLayout) convertView.findViewById(R.id.rl_mine_banner_title);
        ivSectionIcon = (LoaderImageView) convertView.findViewById(R.id.iv_layoutminelist_icon);
    }

    /**
     * 更新视图
     */
    public abstract void notifyDataSetChanged();

    /**
     * 只更其中某一个item
     */
    public void notifyDataSetChanged(int assoId) {
        //默认实现,兼容一些老的视图
        notifyDataSetChanged();
    }

    /**
     * 得到根视图
     */
    public abstract View getRootView();

    protected void initTitle(final Activity activity, final MineSection mineSection) {

        if (mineSection.is_hide) {
//            tv_mine.setVisibility(View.GONE);
//            mine_line.setVisibility(View.GONE);
            rl_mine_banner_title.setVisibility(View.GONE);
        } else {
//            tv_mine.setVisibility(View.VISIBLE);
//            mine_line.setVisibility(View.VISIBLE);
            rl_mine_banner_title.setVisibility(View.VISIBLE);

            //是否显示图标
            if (!StringUtils.isNull(mineSection.icon)) {
                loadImg(ivSectionIcon, mineSection.icon, 30, activity.getApplicationContext());
            } else {
                ivSectionIcon.setVisibility(View.GONE);
            }


        }
        //是否显示[更多]按钮
        if (mineSection.has_more) {
            tv_mine_banner_rightarrow.setVisibility(View.VISIBLE);
            iv_mine_banner_rightarrow.setVisibility(View.VISIBLE);
        } else {
            tv_mine_banner_rightarrow.setVisibility(View.GONE);
            iv_mine_banner_rightarrow.setVisibility(View.GONE);
        }
        //是否能够点击title
        rl_mine_banner_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineItemModel temp0 = new MineItemModel();
                temp0.asso_id = mineSection.asso_id;
                temp0.trace_type = mineSection.trace_type;
                temp0.title = mineSection.title;

                MineItemModel temp = new MineItemModel();
                temp.attr_text = mineSection.attr_text;
                temp.attr_id = mineSection.attr_id;
                temp.uri_type = mineSection.uri_type;
            }
        });

        tv_mine.setText(mineSection.title);
    }

    /**
     * 加载LoaderImageView类型的视图（主要用作，九宫格视图，列表视图）
     *
     * @param loadImg
     * @param url
     * @param width
     * @param context
     */
    protected void loadImg(LoaderImageView loadImg, String url, int assoid, int width, Context context) {

        //优化图片显示，本地有的icon，先显示icon，再显示网络加载的icon，避免白色闪一下。使用bgholder和isFade属性
        if (tempIcon == null) {
            tempIcon = new SparseArray<>();
            tempIcon.put(1, "me_icon_dingdan");
            tempIcon.put(2, "apk_mine_topic");
            tempIcon.put(3, "apk_mine_darily");
            tempIcon.put(5, "me_icon_care");
            tempIcon.put(7, "apk_mine_skin");
            tempIcon.put(8, "me_icon_set");
            tempIcon.put(156, "me_icon_collect");
        }

        if (!StringUtil.isNull(url) && !url.contains("http://")) {
            int imageResource = getResource(context, url);
            loadImg.setImageResource(imageResource);
            loadImg.setBackgroundDrawable(null);
        } else {
            int itemWH = DeviceUtils.dip2px(context, width);
            int defualtHolder = 0;
            if (tempIcon.indexOfKey(assoid) >= 0) {
                defualtHolder = getResource(context, tempIcon.get(assoid));
            }

//            ImageLoader.getInstance().displayImage(context, loadImg, url, defualtHolder, 0, 0, defualtHolder, false, itemWH, itemWH, null);
            ImageLoadParams loadParams = new ImageLoadParams();
            loadParams.defaultholder = defualtHolder;
            loadParams.failholder = 0;
            loadParams.retryholder = 0;
            loadParams.bgholder = 0;
            loadParams.round = false;
            loadParams.width = itemWH;
            loadParams.height = itemWH;
            loadParams.isFade = false;
            ImageLoader.getInstance().displayImage(context, loadImg, url, loadParams, null);
            loadImg.setBackgroundDrawable(null);
        }
    }

    /**
     * 加载LoaderImageView类型的视图（主要用作，九宫格视图，列表视图）
     *
     * @param loadImg
     * @param url
     * @param width
     * @param context
     */
    protected void loadImg(LoaderImageView loadImg, String url, int width, Context context) {
        //-200是随便写的，为了找一个没有的key
        loadImg(loadImg, url, -200, width, context);
    }

    private SparseArray<String> tempIcon = null;

    /**
     * 加载banner图的宽高，该方法主要用于大图小图banner，可以根据实际实现  getImageViewWidth， getImageViewHeight指定视图的宽高
     *
     * @param context
     * @param iv
     * @param url
     * @see ViewHolder#getImageViewWidth ，ViewHolder#getImageViewHeight
     */
    protected void loadBannerImg(final Context context, final RoundedImageView iv, String url) {

        iv.setBackgroundDrawable(null);
        if (!StringUtil.isNull(url) && !url.contains("http://")) {
            int imageResource = getResource(context, url);
            iv.setImageResource(imageResource);
        } else {
            ImageLoadParams params = new ImageLoadParams();
            ImageLoader.getInstance().loadImage(context, url, params, new AbstractImageLoader.onCallBack() {
                @Override
                public void onSuccess(ImageView imageView1, Bitmap bitmap, String url, Object... obj) {
                    if (bitmap != null) {
                        float width = bitmap.getWidth();
                        float height = bitmap.getHeight();
                        int imgWidth = getImageViewWidth(context);
                        int bannerHeight = getImageViewHeight(context, width, height);
                        ViewGroup.LayoutParams lp = iv.getLayoutParams();
                        if (lp == null) {
                            lp = new ViewGroup.LayoutParams(imgWidth, bannerHeight);
                        }
                        lp.width = imgWidth;
                        lp.height = bannerHeight;
                        iv.setLayoutParams(lp);
                        iv.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onFail(String url, Object... obj) {
                    iv.setImageResource(R.drawable.apk_remind_noimage);
                }

                @Override
                public void onProgress(int total, int progess) {
                }

                @Override
                public void onExtend(Object... object) {
                }
            });
        }
    }

    /**
     * 获取要显示的图片地址
     *
     * @param model
     * @return
     */
    protected String getImagesUrl(MineItemModel model) {
        return getImagesUrl(model, IMAGES_RANDOM_INDEX_NONE);
    }

    protected String getImagesUrl(MineItemModel model, int strategy) {
        String url;
        if (isAdUseImages(model)) {
            url = model.images[getImagesRandomIndex(model.asso_id, model.images.length, strategy)];
        } else {
            url = model.icon;
        }
        return url;
    }

    /**
     * 获取images数组中显示到的位置，图片轮播
     *
     * @param assoId
     * @param max
     * @return
     */
    private int getImagesRandomIndex(int assoId, int max, int strategy) {

        if (imagesIndex == null) {
            imagesIndex = new SparseIntArray();
        }

        int index = imagesIndex.get(assoId);

        switch (strategy) {
            case IMAGES_RANDOM_INDEX_NONE:

                break;
            case IMAGES_RANDOM_INDEX_NEXT:

                if (++index >= max) {
                    index = 0;
                }

                imagesIndex.put(assoId, index);


                break;
        }

        return index;

    }

    /**
     * 是否使用isad字段，images中的图片进行轮播
     *
     * @param model
     * @return
     */
    protected boolean isAdUseImages(MineItemModel model) {
        return model != null && model.is_ad == MineItemModel.IS_AD_HAS && model.images != null && model.images.length > 0;
    }

    /**
     * 调用loadBannerImg方法时，需要知道图片的宽高
     *
     * @param mActivity
     * @param width
     * @param height
     * @return
     */
    protected int getImageViewHeight(Context mActivity, float width, float height) {
        return 0;
    }

    /**
     * 调用loadBannerImg方法时，需要知道图片的宽高
     *
     * @param mActivity
     * @return
     */
    protected int getImageViewWidth(Context mActivity) {
        return 0;
    }

    /**
     * 保存点击状态
     *
     * @param mineItemModel
     */
    protected void setListener(final Activity mContext, View banner, final MineItemModel mineItemModel) {
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClickStatus(mContext.getApplicationContext(), mineItemModel);
                notifyDataSetChanged();
            }
        });
    }

    protected void saveClickStatus(Context mActivity, MineItemModel model) {

    }


    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param imageName
     * @return
     */
    public int getResource(Context context, String imageName) {
        Context ctx = context.getApplicationContext();
        int resId = context.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        return resId;
    }
}
