package nickgao.com.meiyousample.personal;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class ExtendOperationController {

    List<ExtendOperationListener> mListeners;
    static ExtendOperationController mControll;

    public ExtendOperationController() {
        mListeners = new LinkedList<ExtendOperationListener>();
    }

    public static ExtendOperationController getInstance() {
        if (mControll == null) {
            mControll = new ExtendOperationController();
        }
        return mControll;
    }

    // FIXME: 16/11/9  这代码会导致内存泄漏, 需要引入 弱引用--zxb
    public void register(ExtendOperationListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }

    }

    public void unRegister(
            ExtendOperationListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }

    }

    /**
     * 通知执行操作
     *
     * @param
     */
    public void notify(int operationKey, Object object) {
        if (mListeners == null)
            return;

        for (int i = 0; i < mListeners.size(); i++) {
            ExtendOperationListener listener = mListeners.get(i);
            try {
                if (listener != null) {
                    listener.excuteExtendOperation(operationKey, object);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
