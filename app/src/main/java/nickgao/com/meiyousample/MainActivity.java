package nickgao.com.meiyousample;

import android.app.Activity;
import android.os.Bundle;

import com.lingan.seeyou.ui.view.HomeSlidingTabLayout;
import com.lingan.seeyou.ui.view.NewsHomeParallaxListview;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.adapter.HomeDynamicAdapter;
import nickgao.com.meiyousample.model.HomeDynamicModel;
import nickgao.com.meiyousample.utils.LogUtils;

public class MainActivity extends Activity {

    HomeSlidingTabLayout news_home_sliding_tab;
    NewsHomeParallaxListview listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news_home_sliding_tab = (HomeSlidingTabLayout) findViewById(R.id.news_home_sliding_tab);
        news_home_sliding_tab.setCustomTabView(R.layout.layout_home_classify_tab_item, R.id.homeTab);
        news_home_sliding_tab.setIsDrawDiver(true);
        listView = (NewsHomeParallaxListview)findViewById(R.id.news_home_listview);

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
        adapter.setHome(true);
        listView.setAdapter(adapter);

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


}
