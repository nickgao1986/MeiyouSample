package nickgao.com.meiyousample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.alibaba.fastjson.JSON;
import com.lingan.seeyou.ui.view.LinearListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.gridview.LinearGrid;
import nickgao.com.meiyousample.gridview.SearchCircleOverAllFaqAdapter;
import nickgao.com.meiyousample.gridview.SearchCircleOverAllPhraseAdapter;
import nickgao.com.meiyousample.model.NewsDetailReviewListModel;
import nickgao.com.meiyousample.model.SearchCircleHomeModel.SearchCircleHomeItemModel;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/3/15.
 */

public class SeeyouActivity extends FragmentActivity {


    private LinearGrid mLvFaq;  //大家都在问
    private SearchCircleOverAllFaqAdapter mFaqAdapter;
    private List<String> mFaqKeyList;
    private List<Integer> mFaqIdList;
    private List<SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel> mPhraseList;
    private SearchCircleOverAllPhraseAdapter mPhraseAdapter;
    private LinearListView mllPhrase;  //问题短语

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search_circle);
        mFaqKeyList = new ArrayList<String>();
        mFaqKeyList.add("月经不调，能怀孕吗");
        mFaqKeyList.add("输卵管造影");
        mFaqKeyList.add("输卵管不通有什么症状");
        mFaqKeyList.add("输卵管堵塞怎么办？");
        mFaqKeyList.add("曾经人流，现在怀不上");
        mFaqKeyList.add("宫颈囊肿影响怀孕吗");
        mFaqKeyList.add("强阳同房还是转弱同房？");

        mFaqIdList = new ArrayList<Integer>();
        mFaqIdList.add(183);
        mFaqIdList.add(184);
        mFaqIdList.add(176);
        mFaqIdList.add(177);
        mFaqIdList.add(178);
        mFaqIdList.add(179);
        mFaqIdList.add(200);


        ArrayList mFaqKeyList = new ArrayList<String>();
        mFaqKeyList.add("月经不调，能怀孕吗");
        mFaqKeyList.add("输卵管造影");
        mFaqKeyList.add("输卵管不通有什么症状");
        mFaqKeyList.add("输卵管堵塞怎么办？");
        mFaqKeyList.add("曾经人流，现在怀不上");
        mFaqKeyList.add("宫颈囊肿影响怀孕吗");
        mFaqKeyList.add("强阳同房还是转弱同房？");

        mFaqIdList = new ArrayList<Integer>();
        mFaqIdList.add(183);
        mFaqIdList.add(184);
        mFaqIdList.add(176);
        mFaqIdList.add(177);
        mFaqIdList.add(178);
        mFaqIdList.add(179);
        mFaqIdList.add(200);


        int showSize = 6;
        mLvFaq = (LinearGrid) findViewById(R.id.lv_faq);
        mFaqAdapter = new SearchCircleOverAllFaqAdapter(mFaqKeyList.subList(1, showSize), mFaqIdList.subList(1, showSize), mLvFaq);
        mLvFaq.setAdapter(mFaqAdapter, 0);
        mPhraseList = new ArrayList<>();

        mllPhrase = (LinearListView) findViewById(R.id.llType);


       // SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel model = new SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel(mFaqKeyList,mFaqIdList,"备孕",0);
//        mPhraseList.clear();
//        mPhraseList.addAll(phraseList);
        contructPhraseList();
        mPhraseAdapter = new SearchCircleOverAllPhraseAdapter(this, mPhraseList);
        mllPhrase.setAdapter(mPhraseAdapter);

        new LoadFileTask().execute();

    }


    private void contructPhraseList() {
        ArrayList mFaqKeyList = new ArrayList<String>();
        mFaqKeyList.add("排卵试纸");
        mFaqKeyList.add("多囊");
        mFaqKeyList.add("子宫后位");
        mFaqKeyList.add("怀孕症状");
        mFaqKeyList.add("双胞胎");
        mFaqKeyList.add("宫寒");
        mFaqKeyList.add("生化");

        ArrayList mFaqIdList = new ArrayList<Integer>();
        mFaqIdList.add(183);
        mFaqIdList.add(184);
        mFaqIdList.add(176);
        mFaqIdList.add(177);
        mFaqIdList.add(178);
        mFaqIdList.add(179);
        mFaqIdList.add(200);

        SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel model = new SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel(mFaqKeyList,mFaqIdList,"备孕",0);



        mFaqKeyList = new ArrayList<String>();
        mFaqKeyList.add("护肤");
        mFaqKeyList.add("美白");
        mFaqKeyList.add("化妆");
        mFaqKeyList.add("祛痘");
        mFaqKeyList.add("面膜");
        mFaqKeyList.add("眼霜");


        mFaqIdList = new ArrayList<Integer>();
        mFaqIdList.add(183);
        mFaqIdList.add(184);
        mFaqIdList.add(176);
        mFaqIdList.add(177);
        mFaqIdList.add(178);
        mFaqIdList.add(179);


        SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel model1 = new SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel(mFaqKeyList,mFaqIdList,"备孕",0);



        mFaqKeyList = new ArrayList<String>();
        mFaqKeyList.add("丰胸");
        mFaqKeyList.add("减肥");
        mFaqKeyList.add("瘦腿");
        mFaqKeyList.add("瘦身");
        mFaqKeyList.add("郑多燕");
        mFaqKeyList.add("提臀");


        mFaqIdList = new ArrayList<Integer>();
        mFaqIdList.add(183);
        mFaqIdList.add(184);
        mFaqIdList.add(176);
        mFaqIdList.add(177);
        mFaqIdList.add(178);
        mFaqIdList.add(179);


        SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel model2 = new SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel(mFaqKeyList,mFaqIdList,"备孕",0);


        mPhraseList.add(model);
        mPhraseList.add(model1);

        mPhraseList.add(model2);

    }


    class LoadFileTask extends AsyncTask<Void, Void, NewsDetailReviewListModel> {

        @Override
        protected NewsDetailReviewListModel doInBackground(Void... params) {
            String str = getFromAssets("news.txt");
            NewsDetailReviewListModel data = null;
            String errorMsg = null;
            try {
                JSONObject jsonObject = new JSONObject(str);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    String dataString = jsonObject.optString("data");
                    if (!StringUtils.isNull(dataString)) {
                        data = JSON.parseObject(dataString, NewsDetailReviewListModel.class);
                    }
                } else {
                    errorMsg = jsonObject.getString("message");
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            LogUtils.d("=====str="+data.publisher.avatar);
            return data;
        }

        @Override
        protected void onPostExecute(NewsDetailReviewListModel homeDynamicModels) {

        }

    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
