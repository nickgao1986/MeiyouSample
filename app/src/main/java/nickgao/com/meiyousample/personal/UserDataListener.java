package nickgao.com.meiyousample.personal;

import nickgao.com.meiyousample.model.UserHomePage.UserHomePage;

/**
 * Created by gaoyoujian on 2017/4/28.
 */

public interface UserDataListener {
    public void onSuccess(UserHomePage response);
    public void onFail();
}
