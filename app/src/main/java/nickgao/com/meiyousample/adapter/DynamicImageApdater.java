package nickgao.com.meiyousample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lingan.seeyou.ui.view.BadgeImageView;
import com.meetyou.crsdk.util.ImageLoader;

import java.util.List;

import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created by gaoyoujian on 2017/4/28.
 */

public class DynamicImageApdater extends BaseAdapter {

    private static final String TAG = "DynamicImageApdater";
    private LayoutInflater mLayoutInflater;
    private List<String> imagesList;
    private int itemWH = 48;
    /*    private ImageUtil imageUtil;*/
    private Context context;
    private int catId;
    private View.OnLongClickListener onLongClickListener;

    public DynamicImageApdater(Context context, List<String> models, boolean isLocalPath, int catId) {
        super();
        this.context = context;
        this.catId = catId;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.imagesList = models;
        itemWH = (DeviceUtils.getScreenWidth(context.getApplicationContext()) - DeviceUtils.dip2px(context, 90)) / 3;  //扣掉其它控件宽度及间距90

    }

    public int getCount() {
        return imagesList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public void setEventTag() {

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.layout_dynamic_image_item, parent, false);
                viewHolder.initHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            String url = imagesList.get(position);

//            try {
//                if (url.contains(".gif")) {
//                    viewHolder.badgeImageView.setImageResource(R.drawable.apk_gif);
//                    viewHolder.badgeImageView.show();
//                } else {
//                    int[] widthHeight = UrlUtil.getWidthHeightByUrl(url);
//                    if (null != widthHeight && widthHeight.length > 1 && BitmapUtil.isLongPicture(widthHeight)) { // 显示长图标记,长图标准 高/宽 > 3.0则为长图
//                        viewHolder.badgeImageView.setImageResource(R.drawable.apk_longpic);
//                        viewHolder.badgeImageView.show();
//                    } else {
//                        viewHolder.badgeImageView.hide();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            viewHolder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (!StringUtils.isNull(url)) {
                ImageLoader.getInstance().displayImage(context.getApplicationContext(), viewHolder.ivPhoto, url, 0, 0, 0, R.color.black_f, false, DeviceUtils.dip2px(context, itemWH), DeviceUtils.dip2px(context, itemWH), null);
            }
            viewHolder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    YouMentEventUtils.getInstance().differentiate(context, "pj-dt", catId);
//                    int size = imagesList.size();
//                    List<PreviewImageModel> listPrev = new ArrayList<PreviewImageModel>();
//                    for (int i = 0; i < size; i++) {
//                        PreviewImageModel model = new PreviewImageModel();
//                       /* model.imageUtil = imageUtil;*/
//                        model.bLoaded = false;
//                        model.strUrl = imagesList.get(i);
//                        listPrev.add(model);
//                    }
//                    PreviewImageActivity.enterActivity((Activity) context, true, true, PreviewImageActivity.MODE_SAVE, listPrev, position, null);
                }
            });
            if (null != onLongClickListener) {
                viewHolder.ivPhoto.setOnLongClickListener(onLongClickListener);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertView;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }


    public class ViewHolder {
        public ViewHolder() {

        }

        public LoaderImageView ivPhoto;
        public ImageView ivBadge;
        public BadgeImageView badgeImageView;

        public void initHolder(View convertView) {
            this.ivPhoto = (LoaderImageView) convertView.findViewById(R.id.ivPhoto);
            resizeKuang();
//            this.ivBadge = (ImageView) convertView.findViewById(R.id.ivBadge);
//            badgeImageView = new BadgeImageView(context, ivPhoto);
//            badgeImageView.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
//            SkinEngine.getInstance().setViewBackground(mContext,this.ivPhoto,R.color.black_f);
        }


        private void resizeKuang() {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivPhoto.getLayoutParams();
            layoutParams.width = itemWH;
            layoutParams.height = itemWH;
            ivPhoto.requestLayout();
        }
    }

}
