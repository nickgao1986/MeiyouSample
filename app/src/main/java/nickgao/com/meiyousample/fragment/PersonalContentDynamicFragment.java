package nickgao.com.meiyousample.fragment;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.lingan.seeyou.ui.view.CursorWatcherEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.adapter.HomeDynamicAdapter;
import nickgao.com.meiyousample.model.HomeDynamicModel;
import nickgao.com.meiyousample.utils.LogUtils;


/**
 * 个人主页内容的adapter
 * @author kahn chaisen@xiaoyouzi.com
 * @version 1.0
 * @ClassName
 * @date 2017/2/24
 * @Description
 */
public class PersonalContentDynamicFragment extends PersonalContentFragment {

    private LinearLayout linearReply;
    private CursorWatcherEditText editReply;


    HomeDynamicAdapter adapter = null;

    private List<HomeDynamicModel> list;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
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

        HomeDynamicAdapter adapter = new HomeDynamicAdapter(mActivity, models);
        mListview.setAdapter(adapter);
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


    public List<HomeDynamicModel> getList() {
        return list;
    }



}
