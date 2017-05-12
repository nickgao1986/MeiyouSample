package nickgao.com.meiyousample;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.mypage.module.ViewHolder;
import nickgao.com.meiyousample.settings.MineControl;
import nickgao.com.meiyousample.settings.MineItemModel;
import nickgao.com.meiyousample.settings.MineSection;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created by gaoyoujian on 2017/3/25.
 */

public class MineListViewHolder extends ViewHolder {

    private LinearLayout llContent;
    private List<View> bannerList = null;
    private Activity mActivity;
    private int count;
    private LayoutInflater mLayoutInflater;

    public MineListViewHolder(Activity activity, MineSection mineSection, List<MineItemModel> mineItemModels) {
        bannerList = new ArrayList<>();
        this.mActivity = activity;
        mLayoutInflater = activity.getLayoutInflater();
        init(activity, mineSection, mineItemModels);
    }

    private void init(final Activity activity, final MineSection mineSection, List<MineItemModel> mineItemModels) {

        View convertView = mLayoutInflater.inflate(R.layout.layout_mine_list, null);
        initHolder(convertView);

        //动态生成banner
        for (int i = 0; i < mineItemModels.size(); i++) {
            MineItemModel model = mineItemModels.get(i);
            View banner = mLayoutInflater.inflate(R.layout.item_mine_list, null);
            banner.setTag(model);
            bannerList.add(banner);
            LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            llContent.addView(banner, llParam);
        }

        notifyDataSetChanged();
    }


    @Override
    public void initHolder(View convertView) {
        super.initHolder(convertView);
        llContent = (LinearLayout) convertView.findViewById(R.id.ll_mine_banner_content);
    }


    private void setCount(int count) {
        this.count = count;
    }

    private int getCount() {
        return count;
    }


    public void notifyDataSetChanged() {
        for (int i = 0; i < bannerList.size(); i++) {
            View banner = bannerList.get(i);
            MineListHolder viewHolder = refreshBanner(banner);
            //最后一个,不显示分割线
            if (i == bannerList.size() - 1) {
                viewHolder.vLine.setVisibility(View.GONE);
            }
        }
    }

    private MineListHolder refreshBanner(View banner) {
        MineListHolder viewHolder = (MineListHolder) banner.getTag(R.id.tag_mine_grid_view_holder);
        if (viewHolder == null) {
            viewHolder = new MineListHolder();
            viewHolder.initHolder(banner);
            banner.setTag(R.id.tag_mine_grid_view_holder, viewHolder);
        }


        final MineItemModel mineItemModel = (MineItemModel) banner.getTag();


        viewHolder.tx_itemName.setText(mineItemModel.title);
        String url = mineItemModel.icon;

        //加载图片
        loadImg(viewHolder.icon_family, url, mineItemModel.asso_id, 30, mActivity.getApplicationContext());
//        setTips(viewHolder, mineItemModel);
//        setListener(mActivity, banner, mineItemModel);
//        showNew(viewHolder, mineItemModel);
        return viewHolder;
    }


//    public void notifyDataSetChanged(int assoId) {
//        for (int i = 0; i < bannerList.size(); i++) {
//            View banner = bannerList.get(i);
//            final MineItemModel mineItemModel = (MineItemModel) banner.getTag();
//            if (mineItemModel != null && mineItemModel.asso_id == assoId) {
//                refreshBanner(banner);
//            }
//        }
//    }


//    private ViewHolder refreshBanner(View banner) {
//        ViewHolder viewHolder = (ViewHolder) banner.getTag(R.id.tag_mine_grid_view_holder);
//        if (viewHolder == null) {
//            viewHolder = new ViewHolder();
//            viewHolder.initHolder(banner);
//            banner.setTag(R.id.tag_mine_grid_view_holder, viewHolder);
//        }
//        viewHolder.fillResources();
//        final MineItemModel mineItemModel = (MineItemModel) banner.getTag();
//
//        //根据用户身份,更改经期设置item的文案
//        if (mineItemModel.asso_id == MineControl.MY_PERIOD) {
//            String itemName = ModeSettingController.getModeTitle();
//            if (!StringUtils.isNull(itemName)) {
//                mineItemModel.title = itemName;
//            }
//        }
//
//        viewHolder.tx_itemName.setText(mineItemModel.title);
//        String url = mineItemModel.icon;
//        //加载图片
//        loadImg(viewHolder.icon_family, url, mineItemModel.asso_id, 30, mActivity.getApplicationContext());
//        setTips(viewHolder, mineItemModel);
//        setListener(mActivity, banner, mineItemModel);
//        showNew(viewHolder, mineItemModel);
//        return viewHolder;
//    }
//
//    /**
//     * 显示透出文字
//     */
//    private void setTips(MineListHolder viewHolder, MineItemModel mineItemModel) {
//        //设置透出文字
//        if (!StringUtils.isNull(mineItemModel.tips_title)) {
//            viewHolder.tvTips.setText(mineItemModel.tips_title);
//            viewHolder.tvTips.setVisibility(View.VISIBLE);
//        } else if (mineItemModel.asso_id == MineControl.MY_SKIN) {
//            //个性主题,显示主题名称
//            String tip = null;
//            if (DataSaveHelper.getInstance(mActivity.getApplicationContext()).getIsNightMode()) {
//                tip = DataSaveHelper.getInstance(mActivity.getApplicationContext()).getSkinNightName();
//                tip = StringUtils.isNull(tip) ? "夜间模式" : tip;
//            } else {
//                tip = DataSaveHelper.getInstance(mActivity.getApplicationContext()).getSkinName();
//                tip = StringUtils.isNull(tip) ? "默认" : tip;
//                //如果是默认主题，显示空字符串
//                if ("默认主题".equals(tip) || "默认".equals(tip)) {
//                    tip = "";
//                }
//            }
//            viewHolder.tvTips.setText(tip);
//            viewHolder.tvTips.setVisibility(View.VISIBLE);
//        } else if (mineItemModel.asso_id == MineControl.MY_COIN) {
//            //我的柚币,显示柚币数
//            String ub = MineControl.getInstance().getUbCount();
//            if (!StringUtils.isNull(ub) && !"0".equals(ub)) {
//                viewHolder.tvTips.setText(ub + "个");
//                viewHolder.tvTips.setVisibility(View.VISIBLE);
//            }
//        } else if (mineItemModel.asso_id == MineControl.MY_PERIOD) {
//            //经期设置根据身份显示周天之类的
//            String tip = ModeSettingController.getModeContent();
//            if (!StringUtils.isNull(tip)) {
//                viewHolder.tvTips.setText(tip);
//                viewHolder.tvTips.setVisibility(View.VISIBLE);
//            }
//        } else if (mineItemModel.asso_id == MineControl.MY_MODE) {
//            String tip = ModeSettingController.getMyStateTitle();
//            if (!StringUtils.isNull(tip)) {
//                viewHolder.tvTips.setText(tip);
//                viewHolder.tvTips.setVisibility(View.VISIBLE);
//            }
//        }
//
//        //设置透出图片
//        if (!StringUtils.isNull(mineItemModel.tips_icon)) {
//            viewHolder.li_itemminelist_tips.setVisibility(View.VISIBLE);
//            loadImg(viewHolder.li_itemminelist_tips, mineItemModel.tips_icon, 30, mActivity.getApplicationContext());
//        }
//
//    }


    class MineListHolder {
        private LoaderImageView icon_family;
        private LoaderImageView li_itemminelist_tips;
        private TextView tx_itemName;
        private RelativeLayout ll_item_mine;
        private RelativeLayout rl_item_mine;
        private TextView prompt_new;
        private TextView tvMyNewDot;
        private TextView tvTips;
        public View vLine;
        public LoaderImageView rivHeadImg;
        private FrameLayout flHeadImg;
        public ImageView iv_itemminelist_rightarrow;


        public void initHolder(View convertView) {
            tx_itemName = (TextView) convertView.findViewById(R.id.tx_itemName);
            icon_family = (LoaderImageView) convertView.findViewById(R.id.im_mineIcon);
            rl_item_mine = (RelativeLayout) convertView.findViewById(R.id.rl_item_mine);
            prompt_new = (TextView) convertView.findViewById(R.id.prompt_new);
            tvMyNewDot = (TextView) convertView.findViewById(R.id.tv_my_newdot);
            tvTips = (TextView) convertView.findViewById(R.id.tv_itemminelist_tips);
            li_itemminelist_tips = (LoaderImageView) convertView.findViewById(R.id.li_itemminelist_tips);
            vLine = convertView.findViewById(R.id.v_itemminelist_line);

            rivHeadImg = (LoaderImageView) convertView.findViewById(R.id.ri_itemminelist_headimg);
            flHeadImg = (FrameLayout) convertView.findViewById(R.id.fl_itemminelist_headimg);
            iv_itemminelist_rightarrow = (ImageView) convertView.findViewById(R.id.iv_itemminelist_rightarrow);
        }
    }

    @Override
    public View getRootView() {
        return ll_mine;
    }


//    private void showSmallRedDot(final MineListHolder viewHolder, MineItemModel mineItemModel, IsNewStatus isNewStatus) {
//
//        //蜜友圈显示更新消息用户头像
//        if (mineItemModel.asso_id == MineControl.MY_SHUOSHUO) {
//            showShuoshuoHeadRedDot(viewHolder, isNewStatus);
//        } else {
//            viewHolder.prompt_new.setText("");
//            ViewGroup.LayoutParams layoutParams = viewHolder.prompt_new
//                    .getLayoutParams();
//            layoutParams.width = DeviceUtils.dip2px(mActivity.getApplicationContext(), 9);
//            layoutParams.height = DeviceUtils.dip2px(mActivity.getApplicationContext(), 9);
//            viewHolder.tvMyNewDot.setVisibility(View.GONE);
//            viewHolder.prompt_new.setVisibility(View.VISIBLE);
//        }
//
//    }


//    private void showShuoshuoHeadRedDot(final MineListHolder viewHolder, IsNewStatus isNewStatus) {
//
//        viewHolder.tvMyNewDot.setVisibility(View.GONE);
////        viewHolder.prompt_new.setVisibility(View.GONE);
//
//        //已经显示了,就不在更新新的头像了
//        if (viewHolder.flHeadImg.getVisibility() != View.VISIBLE) {
//            viewHolder.flHeadImg.setVisibility(View.VISIBLE);
//            String url = isNewStatus.getAvatar();
//            if (StringUtils.isNull(url)) {
//                url = "xxxxx";
//            }
//            ImageLoadParams params = new ImageLoadParams();
//            params.round = true;
//            params.failholder = R.drawable.apk_mine_photo;
//            params.width = DeviceUtils.dip2px(mActivity, 30);
//            params.height = DeviceUtils.dip2px(mActivity, 30);
//            viewHolder.rivHeadImg.setBackgroundDrawable(null);
//            ImageLoader.getInstance().displayImage(mActivity.getApplicationContext(), viewHolder.rivHeadImg, url, params, null);
//        }
//
//    }


//    private void showNew(MineListHolder viewHolder, MineItemModel mineItemModel) {
//        IsNewStatus isNewStatus = IsNewStatus.getIsNewStatus(mActivity.getApplicationContext(), mineItemModel.asso_id);
//        if (isNewStatus != null) {
//            //是否点击过
//            if (!isNewStatus.isClick()) {
//                //优先级    数字>红点>NEW
//                //如果有数字就显示数字
//                if (isNewStatus.getCount() > 0) {
//                    showRedNO(viewHolder, mineItemModel, isNewStatus);
//                } else {
//                    //更新就显示小红点
//                    if (isNewStatus.isShowRedDot()) {
//                        showSmallRedDot(viewHolder, mineItemModel, isNewStatus);
//                    } else {
//                        if (isNewStatus.isUpNew()) {
//                            showRedNew(viewHolder);
//                        } else {
//                            hideRed(viewHolder, mineItemModel);
//                        }
//                    }
//                }
//            } else {
//                hideRed(viewHolder, mineItemModel);
//            }
//
//        } else {
//            hideRed(viewHolder, mineItemModel);
//        }
//
//    }


    /**
     * 显示new
     *
     * @param viewHolder
     */
    private void showRedNew(MineListHolder viewHolder) {
        viewHolder.tvMyNewDot.setVisibility(View.VISIBLE);
        viewHolder.prompt_new.setVisibility(View.GONE);
    }


    /**
     * 隐藏所有红色标识
     *
     * @param viewHolder
     */
    private void hideRed(MineListHolder viewHolder, MineItemModel mineItemModel) {
        viewHolder.prompt_new.setVisibility(View.GONE);
        viewHolder.tvMyNewDot.setVisibility(View.GONE);
        viewHolder.flHeadImg.setVisibility(View.GONE);

        if (mineItemModel.asso_id == MineControl.MY_HELP_FEEDBACK || mineItemModel.asso_id == MineControl.MY_HELP_FEEDBACK_YOUZIJIE) {
            viewHolder.tvTips.setVisibility(View.GONE);
            viewHolder.tvTips.setText("");
        }

    }


}
