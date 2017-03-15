package nickgao.com.meiyousample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lingan.seeyou.ui.view.ParallaxScrollListView;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.adapter.HomeDynamicAdapter;
import nickgao.com.meiyousample.model.HomeDynamicModel;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.LogUtils;


/**
 * Created by gaoyoujian on 2017/3/14.
 */

public class HomeDynamicActivity extends Activity {

    private ParallaxScrollListView parallaxListview;

    ImageView ivBannerBg;
    RelativeLayout rl_custom_title_bar;
    private float headerHeight = 0f;
    private View mListViewHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_layout);
        parallaxListview = (ParallaxScrollListView) findViewById(R.id.home_list);
        rl_custom_title_bar = (RelativeLayout)findViewById(R.id.rl_custom_title_bar);

        rl_custom_title_bar.setAlpha(0);
        // 列表头部
       final View mListViewHeader = ViewFactory.from(this).getLayoutInflater().inflate(R.layout.layout_dynamic_home_list_header, null);

        ivBannerBg = (ImageView) mListViewHeader.findViewById(R.id.ivBannerBg);
        int bannerHeight = DeviceUtils.getScreenHeight(getApplicationContext()) / 4;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivBannerBg.getLayoutParams();
        if (bannerHeight > 0) {
            params.height = bannerHeight;
        }
        ivBannerBg.requestLayout();

        // 加入头部
        parallaxListview.setScaleView((RelativeLayout) mListViewHeader);
        parallaxListview.setParallaxImageView(ivBannerBg, 188);

        parallaxListview.setRefreshView(R.drawable.apk_rotate);
        parallaxListview.enableCanPullAlways();

        new LoadFileTask().execute();


        parallaxListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(headerHeight == 0) {
                    headerHeight = mListViewHeader.getMeasuredHeight();
                }
                setHeaderAlpha();

            }
        });
    }

    public int getScrollY() {
        View c = parallaxListview.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = parallaxListview.getFirstVisiblePosition();
        int top = c.getTop();
        Log.d("TAG","====firstVisiblePosition="+firstVisiblePosition+"top="+top+"c.getHeight()="+c.getHeight());
        int result = -top + firstVisiblePosition * c.getHeight();
        Log.d("TAG","====result="+result);

        return result ;
    }

    private void setHeaderAlpha() {
        if(headerHeight != 0  && headerHeight > getScrollY()) {
            float alpha = getScrollY() / headerHeight;
            rl_custom_title_bar.setAlpha(alpha);
        }else{
            if(rl_custom_title_bar.getAlpha() < 1) {
                rl_custom_title_bar.setAlpha(1);
            }
        }
    }




    private void initView() {
        String str = getFromAssets("dynamic.txt");
        LogUtils.d(str);
        List<HomeDynamicModel> models = new ArrayList<HomeDynamicModel>();
        try {
            JSONArray jsonArray = new JSONArray(str);
            if (null != jsonArray && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    HomeDynamicModel homeDynamicModel = new HomeDynamicModel(jsonArray.getJSONObject(i));
                    models.add(homeDynamicModel);
                }
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }

        HomeDynamicAdapter adapter = new HomeDynamicAdapter(this, models);
        parallaxListview.setAdapter(adapter);
    }


    public String getFromAssets(String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    class LoadFileTask extends AsyncTask<Void,Void,List<HomeDynamicModel>> {

        @Override
        protected List<HomeDynamicModel> doInBackground(Void... params) {
            String str = getFromAssets("dynamic.txt");
            LogUtils.d(str);
            List<HomeDynamicModel> models = new ArrayList<HomeDynamicModel>();
            try {
                JSONArray jsonArray = new JSONArray(str);
                if (null != jsonArray && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        HomeDynamicModel homeDynamicModel = new HomeDynamicModel(jsonArray.getJSONObject(i));
                        models.add(homeDynamicModel);
                    }
                }
            }catch (JSONException ex) {
                ex.printStackTrace();
            }
            return models;
        }

        @Override
        protected void onPostExecute(List<HomeDynamicModel> homeDynamicModels) {
            HomeDynamicAdapter adapter = new HomeDynamicAdapter(HomeDynamicActivity.this, homeDynamicModels);
            parallaxListview.setAdapter(adapter);
        }
    }
}
