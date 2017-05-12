package com.lingan.seeyou.ui.view.photo;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.photo.model.BucketModel;
import com.lingan.seeyou.ui.view.skin.ViewFactory;
import com.meetyou.crsdk.util.ImageLoader;

import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.okhttpexample.view.ImageLoadParams;
import nickgao.com.okhttpexample.view.LoaderImageView;


/**
 * Created with IntelliJ IDEA. R
 * Date: 14-7-4
 */
public class BucketActivity extends BasePhotoActivity {

    private List<BucketModel> mData;
    private ListView mListView;
    private int mViewSize;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PhotoController.getInstance(getApplicationContext()).finishPicking(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bucket_activity);
        titleBarCommon.setTitle("选择相册").setLeftButtonRes(-1).setRightTextViewString(R.string.cancel).setRightTextViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        mViewSize = display.getWidth()/3;
        mListView = (ListView) findViewById(R.id.listview);

        mData = PhotoController.getInstance(getApplicationContext()).getBuckets();
        mListView.setAdapter(new SimpleListAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BucketActivity.this, BucketOverviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Data", mData.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                //ToastUtils.showToast(getApplicationContext(), mData.get(position).Cover);
            }
        });

        getParentView().setBackgroundResource(R.drawable.bottom_bg);
    }

    @SuppressLint("InflateParams")
    private class SimpleListAdapter extends BaseAdapter {


        private static final String TAG = "SimpleListAdapter";

        @Override
        public int getCount() {
            return null != mData ?mData.size() :0;
        }

        @Override
        public Object getItem(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView){
                convertView = ViewFactory.from(getApplicationContext()).getLayoutInflater().inflate(R.layout.cp_bucket_lv_item_new,null);


                ((TextView) convertView.findViewById(R.id.tv)).setTextColor(getResources().getColor(R.color.black_a));
                convertView.findViewById(R.id.right_arrow).setBackgroundResource(R.drawable.apk_all_white_selector);
                convertView.findViewById(R.id.root_view_bucket_adapter_item).setBackgroundResource(R.drawable.apk_all_white_selector);
                convertView.findViewById(R.id.dividerLine).setBackgroundResource(R.drawable.apk_all_lineone);
            }

            BucketModel dataObj = mData.get(position);

            TextView tv = (TextView) convertView.findViewById(R.id.tv);
            LoaderImageView iv = (LoaderImageView) convertView.findViewById(R.id.image_buck);
            String bucketName = dataObj.Name;
            if (bucketName.length()>15){
                bucketName = bucketName.substring(0,14)+"...";
            }

            tv.setText(bucketName + " (" + dataObj.PhotoCount + ")");

            ImageLoadParams params = new ImageLoadParams();
            params.round= false;
            params.bgholder = R.color.black_f;
            params.height = params.width = 64;
            params.anim = false;
            ImageLoader.getInstance().displayImage(getApplicationContext(), iv, dataObj.Cover, params, null);
            return convertView;
        }
    }
}
