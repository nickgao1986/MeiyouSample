package nickgao.com.messages;

import nickgao.com.framework.CommomCallBack;

/**
 * Created by gaoyoujian on 2017/5/15.
 */

public class MessageFunction {

    public void addXiaomiRegCallback(CommomCallBack xiaomiRegCallback) {
        MessageController.getInstance().addXiaomiRegCallback(xiaomiRegCallback);
    }


}
