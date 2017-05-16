package nickgao.com.framework.utils;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public class BeanManager {

    public BeanManager() {
    }

    public static UtilSaver getUtilSaver() {
        return (UtilSaver)BeanFactory.get(UtilSaver.class);
    }



}
