package nickgao.com.meiyousample.fragment;

import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.meiyousample.adapter.HomeDynamicAdapter;
import nickgao.com.meiyousample.model.HomeDynamicModel;
import nickgao.com.meiyousample.model.dynamicModel.DynamicContent;
import nickgao.com.meiyousample.model.dynamicModel.DynamicData;
import nickgao.com.meiyousample.personal.PersonalListener;
import nickgao.com.meiyousample.service.DynamicService;
import nickgao.com.meiyousample.service.ServiceFactory;


/**
 * 个人主页内容的adapter
 * @author kahn chaisen@xiaoyouzi.com
 * @version 1.0
 * @ClassName
 * @date 2017/2/24
 * @Description
 */
public class PersonalContentDynamicFragment extends PersonalContentFragment implements PersonalListener {


    private Handler mHandler = new Handler();
    HomeDynamicAdapter mAdapter = null;
    private List<HomeDynamicModel> models = new ArrayList<HomeDynamicModel>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendRequest();
        initView();
    }


    private void initView() {
        mAdapter = new HomeDynamicAdapter(mActivity, models);
        mListview.setAdapter(mAdapter);
    }

    @Override
    public void subClassLoadMore() {
        if(models != null && models.size() > 0) {
            setLoadingState(LoadingState.LOADING_MORE);
            LogUtils.d("======dynamic subclassLoadmore");
            DynamicService service = (DynamicService) ServiceFactory.getInstance().getService(DynamicService.class.getName());
            service.sendRequest(this,models.get(models.size() - 1).sort);
        }
    }

    @Override
    public void onSuccess(DynamicData response,final boolean isLoadMore) {
        DynamicContent[] contents = response.content;
        final ArrayList<HomeDynamicModel> myList = new ArrayList<HomeDynamicModel>();
        for (int i=0; i < contents.length; i++) {
            DynamicContent dynamicContent = contents[i];
            HomeDynamicModel model = new HomeDynamicModel();
            model.screenName = dynamicContent.screen_name;
            model.isAllowOperate = dynamicContent.allow_operate;
            model.iconUrl = dynamicContent.avatar;
            model.content = dynamicContent.content;
            model.type = dynamicContent.type;
            model.imagesList  = new ArrayList<String>();
            for (int j=0; j < dynamicContent.images.length; j++) {
                model.imagesList.add(dynamicContent.images[j]);
            }
            myList.add(model);
        }

        if(!isLoadMore) {
            models.clear();
            models.addAll(myList);

        }else{
            models.addAll(myList);
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(!isLoadMore) {
                    if(myList.size() == 0) {
                        setLoadingState(LoadingState.NO_DATA);
                    }else{
                        setLoadingState(LoadingState.LOADING_COMPLETE);
                    }
                }else{
                    setLoadingState(LoadingState.FOOTER_COMPLETE);
                }
                mAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    void sendRequest() {
        setLoadingState(LoadingState.LOADING_NEW_DATA);

        DynamicService service = (DynamicService) ServiceFactory.getInstance().getService(DynamicService.class.getName());
        service.sendRequest(this,0);
    }

    @Override
    public void onFail() {

    }
}
