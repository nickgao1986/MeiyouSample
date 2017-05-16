package nickgao.com.meiyousample.service;

import java.lang.reflect.InvocationTargetException;

import nickgao.com.framework.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class ServiceFactory {

    private static final String TAG = "ServiceFactory";

    private static ServiceFactory instance = new ServiceFactory();

    private IRequestFactory requestFactory;

    private ServiceFactory() {
        requestFactory = new RestRequestFactory();
    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public IRequestFactory getRequestFactory() {
        return requestFactory;
    }

    public void setRequestFactory(IRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    public AbstractService getService(String name) {
        AbstractService service = null;
        try {
            Class c = Class.forName(name);
            Class parameterType = IRequestFactory.class;
            java.lang.reflect.Constructor constructor = null;
            constructor = c.getConstructor(parameterType);
            Object parameter = this.requestFactory;
            service = (AbstractService) constructor.newInstance(parameter);
        } catch (ClassNotFoundException e) {
            LogUtils.e(TAG, "getService", e);
        } catch (NoSuchMethodException e) {
            LogUtils.e(TAG, "getService", e);
        } catch (InstantiationException e) {
            LogUtils.e(TAG, "getService", e);
        } catch (IllegalAccessException e) {
            LogUtils.e(TAG, "getService", e);
        } catch (InvocationTargetException e) {
            LogUtils.e(TAG, "getService", e);
        }

        return service;
    }

}
