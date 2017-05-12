package nickgao.com.meiyousample.mypage.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.settings.MineItemModel;
import nickgao.com.meiyousample.settings.MineSection;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class MineGridViewViewHolder extends ViewHolder {

    private LinearLayout llContent;
    private List<View> bannerList = null;
    private Activity mActivity;
    private int count;

    public MineGridViewViewHolder(Activity activity, MineSection mineSection, List<MineItemModel> mineItemModels) {
        bannerList = new ArrayList<>();
        this.mActivity = activity;
        init(activity, mineSection, mineItemModels);
    }

    private void init(final Activity activity, final MineSection mineSection, List<MineItemModel> mineItemModels) {

        View convertView = ViewFactory.from(activity).getLayoutInflater().inflate(R.layout.layout_mine_list, null);
        initHolder(convertView);
        initTitle(activity, mineSection);
//        fillResources(activity);

        int pageNo = 4;
        int itemHeight = DeviceUtils.getScreenWidth(activity.getApplicationContext()) / 4;
        setCount(mineItemModels.size());

        for (int i = 0; i < mineItemModels.size(); i += pageNo) {
            LinearLayout ll = new LinearLayout(activity.getApplicationContext());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setLayoutParams(param);

            //生成九宫格并缓存
            for (int j = i; j < i + pageNo && j < mineItemModels.size(); j++) {
                View banner = ViewFactory.from(activity).getLayoutInflater().inflate(R.layout.item_mine_grid, null);
                banner.setTag(mineItemModels.get(j));
                bannerList.add(banner);
                ll.addView(banner, new LinearLayout.LayoutParams(itemHeight, itemHeight));
            }

            //给九宫格不满一行，进行补位
            int count = ll.getChildCount();
            if(count > 0 && count < pageNo) {
                for(int k = 0; k < pageNo - count; k++) {
                    View cover = ViewFactory.from(activity).getLayoutInflater().inflate(R.layout.item_mine_grid_cover, null);
                    ll.addView(cover, new LinearLayout.LayoutParams(itemHeight, itemHeight));
                }
            }

            llContent.addView(ll);
        }

        notifyDataSetChanged();
    }


    @Override
    public void fillResources(Context context) {
//        super.fillResources(mContext);
//        for (View banner : bannerList) {
//            ViewHolder v = (ViewHolder) banner.getTag(R.id.tag_mine_grid_view_holder);
//            if (v != null) {
//                v.fillResources();
//            }
//        }
        /*super.fillResources(mContext);
        SkinEngine.getInstance().setViewBackground(mContext.getApplicationContext(), llContent, R.color.white_an);
        for (View banner : bannerList) {
            ViewHolder v = (ViewHolder) banner.getTag(R.id.tag_mine_grid_view_holder);
            if (v != null) {
                v.fillResources();
            }
        }*/
    }

    @Override
    public void initHolder(View convertView) {
        super.initHolder(convertView);
        llContent = (LinearLayout) convertView.findViewById(R.id.ll_mine_banner_content);
//        gv_mine = (GridViewEx) convertView.findViewById(R.id.gv_mine);
    }

    private void childFillResources(Context context, View child) {
//        SkinEngine.getInstance().setViewTextColor(mContext, (TextView) child.findViewById(R.id.tx_itemName), R.color.black_at);
//            SkinEngine.getInstance().setViewBackgroundColor(mContext, rl_item_mine, R.color.black_test);
//        SkinEngine.getInstance().setViewBackground(mContext, child.findViewById(R.id.ll_item_mine), R.drawable.apk_all_white_selector);
    }

    private void setCount(int count) {
        this.count = count;
    }

    private int getCount() {
        return count;
    }

    @Override
    public void notifyDataSetChanged() {
        int position = 0;
        for (View banner : bannerList) {
            refreshBanner(banner);
            drawSplitLine(position, (RelativeLayout) banner);
            position++;
        }
    }

    @Override
    public void notifyDataSetChanged(int assoId) {
        for (View banner : bannerList) {
            final MineItemModel mineItemModel = (MineItemModel) banner.getTag();
            if(mineItemModel != null && mineItemModel.asso_id == assoId) {
                refreshBanner(banner);
            }
        }
    }

    /**
     * 刷新banner
     * @param banner
     * @return banner中的viewholder
     */
    private ViewHolder refreshBanner(View banner) {
        ViewHolder viewHolder = (ViewHolder) banner.getTag(R.id.tag_mine_grid_view_holder);
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.initHolder(banner);
            banner.setTag(R.id.tag_mine_grid_view_holder, viewHolder);
        }
        viewHolder.fillResources();
        final MineItemModel mineItemModel = (MineItemModel) banner.getTag();
        viewHolder.tx_itemName.setText(mineItemModel.title);
        String url = mineItemModel.icon;
        loadImg(viewHolder.icon_family, url, 44, mActivity.getApplicationContext());
        setListener(mActivity, banner, mineItemModel);
        showNew(viewHolder, mineItemModel);
        return viewHolder;
    }

    /**
     * 画9宫格的分割线
     * @param position
     * @param banner
     */
    private void drawSplitLine(int position, RelativeLayout banner) {
        RelativeLayout root = banner;
        //九宫格分割线使用padding把北京露出来,来实现
        int hang = position / 4;
        int lie = position % 4;
        int maxHang = (getCount() - 1) / 4;
        if (hang < maxHang) {
            if (lie == 3) {
                root.setPadding(0, 0, 0, 1);
            } else {
                root.setPadding(0, 0, 1, 1);
            }
        } else if (lie != 3 && position != getCount() - 1) {
            root.setPadding(0, 0, 1, 0);
        }
        if (lie != 3 && position == getCount() - 1) {
            root.setPadding(0, 0, 1, 0);
        }
    }

    @Override
    public View getRootView() {
        return ll_mine;
    }

    class ViewHolder {
        private LoaderImageView icon_family;
        private TextView tx_itemName;
        private LinearLayout ll_item_mine;
        private RelativeLayout rl_item_mine;
        private TextView prompt_new;
        private TextView tvMyNewDot;

        @SuppressLint("ResourceAsColor")
//        public void fillResources() {
//            SkinEngine.getInstance().setViewTextColor(mActivity, tx_itemName, R.color.black_at);
//            SkinEngine.getInstance().setViewBackgroundColor(mActivity, rl_item_mine, R.color.black_e);
//            SkinEngine.getInstance().setViewBackground(mActivity, ll_item_mine, R.drawable.apk_all_white_selector);
//            SkinEngine.getInstance().setViewBackground(mActivity, rl_item_mine, R.color.black_e);
//        }
        public void fillResources() {
            /*SkinEngine.getInstance().setViewTextColor(mActivity, tx_itemName, R.color.black_a);
            SkinEngine.getInstance().setViewBackgroundColor(mActivity, rl_item_mine, R.color.black_e);*/
            //UI需求,分割线透明度减少40%
            if(rl_item_mine != null && rl_item_mine.getBackground() != null){
                rl_item_mine.getBackground().setAlpha(153);
            }
//            SkinEngine.getInstance().setViewBackground(mActivity, ll_item_mine, R.drawable.apk_all_white_selector);
        }

        public void initHolder(View convertView) {
            tx_itemName = (TextView) convertView.findViewById(R.id.tx_itemName);
            icon_family = (LoaderImageView) convertView.findViewById(R.id.im_mineIcon);
            ll_item_mine = (LinearLayout) convertView.findViewById(R.id.ll_item_mine);
            rl_item_mine = (RelativeLayout) convertView.findViewById(R.id.rl_item_mine);
            prompt_new = (TextView) convertView.findViewById(R.id.prompt_new);
            tvMyNewDot = (TextView) convertView.findViewById(R.id.tv_my_newdot);
        }
    }

    /**
     * 显示带数字的红点
     *
     * @param viewHolder
     */
    private void showRedNO(ViewHolder viewHolder, int count) {

        if (count < 100) {
            viewHolder.prompt_new.setBackgroundResource(R.drawable.apk_all_newsbg);
            ViewGroup.LayoutParams layoutParams = viewHolder.prompt_new
                    .getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            viewHolder.prompt_new.setText(String.valueOf(count));
        } else {
            viewHolder.prompt_new.setBackgroundResource(R.drawable.apk_all_newsbigbg);
            ViewGroup.LayoutParams layoutParams = viewHolder.prompt_new
                    .getLayoutParams();
            layoutParams.width = DeviceUtils.dip2px(mActivity.getApplicationContext(), 20);
            layoutParams.height = DeviceUtils.dip2px(mActivity.getApplicationContext(), 14);
            viewHolder.prompt_new.setText("99+");
        }

        if (count <= 0) {
            viewHolder.prompt_new.setVisibility(View.GONE);
        } else {
            viewHolder.prompt_new.setVisibility(View.VISIBLE);
        }
//        ViewUtilController.getInstance().setPromotionBigWithCount(mContext, viewHolder.prompt_new, 88, 0, 0);
        viewHolder.tvMyNewDot.setVisibility(View.GONE);
    }

    /**
     * 隐藏所有红色标识
     *
     * @param viewHolder
     */
    private void hideRed(ViewHolder viewHolder) {
        viewHolder.prompt_new.setVisibility(View.GONE);
        viewHolder.tvMyNewDot.setVisibility(View.GONE);
    }

    /**
     * 显示new
     *
     * @param viewHolder
     */
    private void showRedNew(ViewHolder viewHolder) {
        viewHolder.tvMyNewDot.setVisibility(View.VISIBLE);
        viewHolder.prompt_new.setVisibility(View.GONE);
    }

    /**
     * 显示小红点
     *
     * @param viewHolder
     */
    private void showSmallRedDot(ViewHolder viewHolder) {
        viewHolder.prompt_new.setText("");
        ViewGroup.LayoutParams layoutParams = viewHolder.prompt_new
                .getLayoutParams();
        layoutParams.width = DeviceUtils.dip2px(mActivity.getApplicationContext(), 9);
        layoutParams.height = DeviceUtils.dip2px(mActivity.getApplicationContext(), 9);
        viewHolder.tvMyNewDot.setVisibility(View.GONE);
        viewHolder.prompt_new.setVisibility(View.VISIBLE);
    }



    /**
     * 显示上新逻辑
     *
     * @param viewHolder
     * @param mineItemModel
     */
    private void showNew(ViewHolder viewHolder, MineItemModel mineItemModel) {

    }
}
