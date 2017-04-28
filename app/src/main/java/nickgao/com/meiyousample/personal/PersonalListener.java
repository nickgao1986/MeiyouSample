package nickgao.com.meiyousample.personal;

import nickgao.com.meiyousample.model.dynamicModel.DynamicData;

/**
 * Created by gaoyoujian on 2017/4/27.
 */

public interface PersonalListener {
    public void onSuccess(DynamicData response,boolean isLoaderMore);
    public void onFail();
}
