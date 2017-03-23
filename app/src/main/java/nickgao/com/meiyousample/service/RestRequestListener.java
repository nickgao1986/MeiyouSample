package nickgao.com.meiyousample.service;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public interface RestRequestListener {

    void onRequestSuccess();

    void onRequestFailure(int errorCode);
}
