package nickgao.com.meiyousample.friend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.BadgeImageView;
import com.lingan.seeyou.ui.view.LoaderImageView;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.List;

import fresco.view.ImageLoader;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class FamousPersonFriendAdapter extends BaseAdapter {
    public List<AddFriendModel> friendModels;
    public Activity mContext;
    private LayoutInflater mInflater;


    public FamousPersonFriendAdapter(Activity mContext, List<AddFriendModel> models) {
        this.friendModels = models;
        this.mContext = mContext;
        mInflater = ViewFactory.from(mContext).getLayoutInflater();


    }

    @Override
    public int getCount() {
        return friendModels.size();
    }

    @Override
    public Object getItem(int position) {
        return friendModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_follow_recommend_friend_item, null);
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.rlBase.getLayoutParams();
            params.topMargin = DeviceUtils.dip2px(mContext, 10);
            holder.rlBase.requestLayout();
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.rlBase
                    .getLayoutParams();
            params.topMargin = 0;
            holder.rlBase.requestLayout();
        }
//        SkinEngine.getInstance().setViewBackground(mContext, holder.rlBase, R.drawable.apk_all_white_selector);
        try {
            final AddFriendModel model = friendModels.get(position);
            //头像
            ImageLoader.getInstance().displayImage(mContext.getApplicationContext(), holder.ivHeadPic, model.img_url_small, R.drawable.apk_mine_photo,
                    0, 0, 0, true,
                    ImageLoader.getBuckImageWH(mContext.getApplicationContext()), ImageLoader.getBuckImageWH(mContext.getApplicationContext()), null
            );
            //名字
            holder.tvName.setText(model.name);
            //内容
            holder.tvContent.setText(model.reason);

            holder.tvDynamicnum.setText("动态:" + StringUtils.getDynamicnum(model.dynamicnum));
            //粉丝
            holder.tvFans.setText(" 粉丝:" + StringUtils.getDynamicnum(model.fans));
            //是否关注
           // handleFollowStatus(model, holder);
            //设置V认证图标
            if (model.isVip > 0) {
                if (null == holder.bvVerify) {
                    holder.bvVerify = new BadgeImageView(mContext.getApplicationContext(), holder.ivHeadPic);
                    holder.bvVerify.setBadgePosition(BadgeImageView.POSITION_BOTTOM_RIGHT);
                    holder.bvVerify.setImageResource(R.drawable.apk_personal_v);
                }
                holder.bvVerify.show();
            } else {
                if (holder.bvVerify != null && holder.bvVerify.isShown()) {
                    holder.bvVerify.hide();
                }
            }
            holder.tvFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }



    class ViewHolder {
        private RelativeLayout rlBase;
        private LoaderImageView ivHeadPic;
        private TextView tvName, tvContent;
        private TextView tvDynamicnum, tvFans;
        private TextView tvFollow;
        public BadgeImageView bvVerify;
        private ImageView friend_arrow;

        @SuppressLint("ResourceAsColor")
        public void init(View view) {
            rlBase = (RelativeLayout) view.findViewById(R.id.rlBase);
            ivHeadPic = (LoaderImageView) view.findViewById(R.id.ivHeadPic);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            tvDynamicnum = (TextView) view.findViewById(R.id.tvDynamicnum);
            tvFans = (TextView) view.findViewById(R.id.tvFans);
            tvFollow = (TextView) view.findViewById(R.id.tvFollow);
            friend_arrow = (ImageView) view.findViewById(R.id.friend_arrow);

//            SkinEngine.getInstance().setViewBackground(mContext, rlBase, R.drawable.apk_all_white_selector);
//            SkinEngine.getInstance().setViewBackground(mContext, view.findViewById(R.id.view), R.drawable.apk_all_linetwo);
//            SkinEngine.getInstance().setViewBackground(mContext, view.findViewById(R.id.divider), R.drawable.apk_all_lineone);
//
//            SkinEngine.getInstance().setViewTextColor(mContext, tvName, R.color.black_a);
//            SkinEngine.getInstance().setViewTextColor(mContext, tvContent, R.color.black_a);
//            SkinEngine.getInstance().setViewTextColor(mContext, tvDynamicnum, R.color.black_b);
//            SkinEngine.getInstance().setViewTextColor(mContext, tvFans, R.color.black_b);
//            SkinEngine.getInstance().setViewTextColorStateList(mContext, tvFollow, R.color.item_search_tags_text_color_selector);
//            SkinEngine.getInstance().setViewImageDrawable(mContext, friend_arrow, R.drawable.all_icon_arrow);
        }
    }
}
