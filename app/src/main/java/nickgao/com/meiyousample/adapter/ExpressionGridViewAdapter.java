package nickgao.com.meiyousample.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.List;

import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.EmojiConversionUtil;
import nickgao.com.meiyousample.utils.model.ExpressionModel;
import nickgao.com.meiyousample.utils.model.ExpressionTabType;
import nickgao.com.meiyousample.utils.model.ExpressionType;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * gridview 表情适配器
 * Created by lwh on 2015/7/29.
 */

public class ExpressionGridViewAdapter extends BaseAdapter {
    private static final String TAG = "ExpressionGridViewAdapter";
    private Context mContext;
    private List<ExpressionModel> listDatas;
    private LayoutInflater inflater;
    private int size = 0;
    private int itemWidth;
    private int itemHeight;
    private Resources resources;
    private boolean bShowGifName;
    /**
     *
     * @param context
     * @param expressionTabType 类型，用于动态计算宽高
     * @param list 数据源
     */
    public ExpressionGridViewAdapter(Context context, int expressionTabType, List<ExpressionModel> list, int itemwidth, int itemheight, boolean bShowGifName) {
        this.inflater = ViewFactory.from(context).getLayoutInflater();
        this.listDatas = list;
        this.size = list.size();
        this.mContext = context;
        this.itemHeight = itemheight;
        this.itemWidth = itemwidth;
        this.bShowGifName = bShowGifName;
        //this.bShowDeleteBtn= bShowDeleteBtn;
        if(itemWidth==0 && itemHeight==0) {
            //计算格子宽高
            switch (expressionTabType){
                case ExpressionTabType.DEFAULT:{
                    // - DeviceUtils.dip2px(context, 20)
                    itemWidth = DeviceUtils.getScreenWidth(context) / EmojiConversionUtil.PAGE_COLUMN_COUNT;
                    itemHeight = DeviceUtils.dip2px(context, 180) / EmojiConversionUtil.PAGE_LINE_COUNT;
                    break;
                }
                case ExpressionTabType.GIF:
                case ExpressionTabType.COMMOM:
                default:{
                 /*   // - DeviceUtils.dip2px(context, 20)
                    itemWidth = DeviceUtils.getScreenWidth(context) / ExpressionEngine.PAGE_COLUMN_COUNT;
                    if(bShowGifName)
                         itemHeight = DeviceUtils.dip2px(context, 180 - 40) / ExpressionEngine.PAGE_LINE_COUNT;
                    else
                         itemHeight = DeviceUtils.dip2px(context, 180) / ExpressionEngine.PAGE_LINE_COUNT;*/
                    break;
                }
            }
        }
    }

    @Override
    public int getCount() {
        return this.size;
    }

    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExpressionModel emoji = listDatas.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.expression_griditem_emoji, null);
            viewHolder.ivEmoji = (LoaderImageView) convertView.findViewById(R.id.item_iv_emoji);
            viewHolder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.tv_title.setTextColor(mContext.getResources().getColor(R.color.black_a));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.ivEmoji.getLayoutParams();
            layoutParams.height = itemHeight;
            layoutParams.width = itemWidth;
            viewHolder.ivEmoji.requestFocus();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ExpressionModel model = listDatas.get(position);
        viewHolder.tv_title.setVisibility(View.GONE);
        switch (model.type){
            //删除
            case ExpressionType.DELETE:{
                convertView.setBackgroundDrawable(null);
                viewHolder.ivEmoji.setImageResource(R.drawable.btn_emoji_del_selector);
                //SkinEngine.getInstance().setViewImageDrawable(mContext, viewHolder.ivEmoji, R.drawable.btn_emoji_del_selector);
                break;
            }
            //动态图
            /*case ExpressionType.GIF:{
                if(!StringUtils.isNull(emoji.packagename)){
                    if(resources==null){
                        resources = SkinEngine.getInstance().getApkFileResources(mContext,model.packagename);
                    }
                    if(resources==null){
                        LogUtils.d(TAG,"-->resources is null"+model.packagename+":"+model.expressionSubModel.gif_name);
                    }
                    LogUtils.d(TAG, "position:" + position + "--size:" + size + "--model.type:" + model.type + "--gifname:" + emoji.expressionSubModel.gif_name);
                    Bitmap bitmap = ExpressionEngine.getInstance().getEmojiFromCache(mContext, resources, model.packagename, emoji.expressionSubModel.gif_name);
                    if(bitmap!=null)
                        viewHolder.ivEmoji.setImageBitmap(bitmap);
                    else{
                        ImageLoader.getInstance().displayImage(mContext, viewHolder.ivEmoji, emoji.iconurl, 0, 0, 0, 0, false, 0, 0, null);
                        //viewHolder.ivEmoji.setImageResource(R.drawable.apk_meetyou_two);
                        LogUtils.d(TAG,"-->model.expressionSubModel.gif_name:"+model.expressionSubModel.gif_name+"-->is NULL");
                    }
                }else{
                    ImageLoader.getInstance().displayImage(mContext, viewHolder.ivEmoji, emoji.iconurl, 0, 0, 0, 0, false, 0, 0, null);
                }

                if(bShowGifName){
                    viewHolder.tv_title.setVisibility(View.VISIBLE);
                    viewHolder.tv_title.setText(model.gif_ui_name);
                }else{
                    viewHolder.tv_title.setVisibility(View.GONE);
                }
                break;
            }*/
            //emoji
            case ExpressionType.EMOJI:{
                viewHolder.ivEmoji.setImageBitmap(EmojiConversionUtil.getInstace().getEmojiFromCache(mContext, model.emojiModel.getId(), 0, 0));
                break;
            }
            //空白
            case ExpressionType.NONE:{
                viewHolder.ivEmoji.setVisibility(View.INVISIBLE);
                viewHolder.ivEmoji.setImageDrawable(null);
                convertView.setBackgroundDrawable(null);
                break;
            }
        }
        return convertView;
    }

    class ViewHolder {
        public LoaderImageView ivEmoji;
        public TextView tv_title;
        //public GifImageView gifView;
    }
}
