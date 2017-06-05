package nickgao.com.meiyousample.adapter;

import android.app.Activity;

import com.meetyou.crsdk.util.ImageLoader;

import java.util.List;

import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.model.HomeDynamicModel;
import nickgao.com.meiyousample.model.IHomeDynamicType;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.okhttpexample.view.IFrescoImageView;


/**
 * 说说首页adapter
 * Created by Administrator on 2014/7/11.
 */
public class HomeDynamicMultiItemAdapter extends BaseMultiItemQuickAdapter<HomeDynamicModel, BaseViewHolder> {


    private static final int LAYOUT_TYPE_DEFAULT = 0;  //默认  说说布局
    private static final int LAYOUT_TYPE_SHARE = 1;    //话题分享布局
    private static final int LAYOUT_TYPE_CIRCLE_RECOMMEND = 2;  //话题推荐布局

    private static final int TYPE_COUNT = 2;//2种布局
    private Activity context;
    private List<HomeDynamicModel> models;
    private int bigImageWidth;
    private int fullImageWidht;//推荐话题，宽度铺满
    private int tvContentWidth = 200;
    private int fromID; //来源id

    //是否是个人主页
    private boolean isPersonal = false;


    public HomeDynamicMultiItemAdapter(Activity context, List<HomeDynamicModel> dataList) {
        super(dataList);
        this.context = context;
        models = dataList;
        bigImageWidth = DeviceUtils.getScreenWidth(context.getApplicationContext()) / 2; //大图限宽高  屏幕1/2
        fullImageWidht = DeviceUtils.getScreenWidth(context.getApplicationContext()) - DeviceUtils.dip2px(context.getApplicationContext(), 80);
        tvContentWidth = ((DeviceUtils.getScreenWidth(context.getApplicationContext()) - DeviceUtils.dip2px(context.getApplicationContext(), 80)));

        addItemType(LAYOUT_TYPE_DEFAULT, R.layout.layout_home_dynamic_item);
        addItemType(LAYOUT_TYPE_SHARE, R.layout.layout_home_dynamic_item_share);
    }




    @Override
    protected void convert(BaseViewHolder helper, HomeDynamicModel item) {
        switch (item.getItemType()) {
            case LAYOUT_TYPE_DEFAULT:
                helper.setText(R.id.tvNickname,item.screenName);
                if (!StringUtils.isNull(item.content)) {
                    helper.setText(R.id.tvContent,item.content);
                    helper.setVisible(R.id.tvContent,true);
                } else {
                    helper.setVisible(R.id.tvContent,false);
                }
                 break;
            case LAYOUT_TYPE_SHARE:
                helper.setText(R.id.tvNickname,item.screenName);
                if (!StringUtils.isNull(item.shareWords)) {
                    String words = item.shareWords;
                    helper.setVisible(R.id.tvContent,true);
                    helper.setText(R.id.tvContent,words);
                } else {
                    helper.setVisible(R.id.tvContent,false);
                }

                if (null != item.imagesList && item.imagesList.size() > 0 && !StringUtils.isNull(item
                        .imagesList.get(0))) {
                    String url = item.imagesList.get(0);

                    ImageLoader.getInstance().displayImage(context.getApplicationContext(), (IFrescoImageView)helper.getView(R.id.ivShareIcon), url, R.drawable
                                    .tata_img_goodtopic, 0, 0, 0, false,
                            this.context.getResources().getDimensionPixelOffset(R.dimen.list_icon_height_56), this.context.getResources().getDimensionPixelOffset(R.dimen.list_icon_height_56), null);

                } else {
                    helper.setImageResource(R.id.ivShareIcon,R.drawable.tata_img_goodtopic);
                }
                helper.setText(R.id.tvShareContent,item.content);

                if (item.type == IHomeDynamicType.TIPS_SHARE) {  //贴士显示分类
                    if (!StringUtils.isNull(item.tipCategory)) {
                        helper.setVisible(R.id.tvSharePublisher,true);
                        helper.setText(R.id.tvSharePublisher,item.tipCategory);
                    } else {
                        helper.setVisible(R.id.tvSharePublisher,false);
                    }
                } else {
                    if (!StringUtils.isNull(item.publisher)) {
                        helper.setVisible(R.id.tvSharePublisher,true);
                        helper.setText(R.id.tvSharePublisher,item.publisher);
                    } else {
                        helper.setVisible(R.id.tvSharePublisher,false);
                    }
                }
                break;
        }
    }
}
