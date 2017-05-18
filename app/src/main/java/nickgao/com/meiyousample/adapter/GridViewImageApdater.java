package nickgao.com.meiyousample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.lingan.seeyou.ui.view.skin.ViewFactory;
import com.meetyou.crsdk.util.ImageLoader;

import java.util.List;

import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.model.GridViewImageModel;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-11
 * Time: 下午4:57
 * To change this template use File | Settings | File Templates.
 */
public class GridViewImageApdater extends BaseAdapter {

    //private String TAG = "CalendarGridViewAdapter";

    private static final String TAG ="GridViewImageApdater";
	private LayoutInflater mLayoutInflater;
    private List<GridViewImageModel> mModels;
    private Context mContext;
    private int mItemWH = 48;


    public GridViewImageApdater(Context context, List<GridViewImageModel> models) {
        super();
        this.mLayoutInflater = ViewFactory.from(context).getLayoutInflater();
        this.mModels = models;
        this.mContext = context;
        mItemWH = (DeviceUtils.getScreenWidth(context)-DeviceUtils.dip2px(context,8*5))/4;

    }

    public int getCount() {
        return mModels.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }  

    private int lastPosition =-1;

    public void resetPosition(){
        lastPosition = -1;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

       
        try{
           ViewHolder tempHolder = null;
            if (convertView == null) {

                tempHolder = new ViewHolder();
                convertView =  mLayoutInflater.inflate(R.layout.layout_publish_shoushou_gridview_photo_item,parent, false);
                tempHolder.initHolder(convertView);
                convertView.setTag(tempHolder);
            } else {
                tempHolder = (ViewHolder) convertView.getTag();
            }

            if (parent.getChildCount() == position) {
            	 LogUtils.i(TAG, "---》getView position:" + position + "-->" + lastPosition);
                 showImage(position, tempHolder);
 	
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return convertView;
    }


    private void showImage(final int position,final ViewHolder tempHolder){
    	 final GridViewImageModel model = mModels.get(position);
         if(model.bitmap!=null)
             tempHolder.ivPhoto.setImageBitmap(model.bitmap);
         else if (!StringUtils.isNull(model.UrlThumbnail)){
         	//final ViewHolder holder = tempHolder;
         	LogUtils.i(TAG,"缩略图地址："+model.UrlThumbnail);
             ImageLoader.getInstance().displayImage(mContext.getApplicationContext(),tempHolder.ivPhoto,model.UrlThumbnail,0,0,0,R.color.black_f,false,mItemWH,mItemWH,null);

             //ImageLoader.getInstance().display(mContext.getApplicationContext(),tempHolder.ivPhoto,model.UrlThumbnail,0,0,mItemWH,mItemWH,null);
         	 //Picasso.with(mContext).load("file://" + model.UrlThumbnail).resize(mItemWH, mItemWH).centerCrop().into(tempHolder.ivPhoto);
         }else if (!StringUtils.isNull(model.Url)){
         	LogUtils.i(TAG,"原图地址："+model.Url);
             ImageLoader.getInstance().displayImage(mContext.getApplicationContext(),tempHolder.ivPhoto,model.Url,0,0,0,R.color.black_f,false,mItemWH,mItemWH,null);
             //ImageLoader.getInstance().display(mContext.getApplicationContext(),tempHolder.ivPhoto,model.Url,0,0,mItemWH,mItemWH,null);
         	//Picasso.with(mContext).load("file://" + model.Url).resize(mItemWH, mItemWH).centerCrop().into(tempHolder.ivPhoto);
         }else{
             tempHolder.ivPhoto.setImageResource(R.drawable.apk_sent_add);
         }
    }


    // 视图项
    public class ViewHolder {
    	
        public LoaderImageView ivPhoto;
        public void initHolder(View convertView) {
            this.ivPhoto = (LoaderImageView)convertView.findViewById(R.id.ivPhoto);

            resizeKuang();
        }


        public void resizeKuang(){
            RelativeLayout.LayoutParams layoutParams =(RelativeLayout.LayoutParams)ivPhoto.getLayoutParams();
            layoutParams.width = mItemWH;
            layoutParams.height=mItemWH;
            ivPhoto.requestLayout();
        }
    }

}
