package nickgao.com.framework.utils;

import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public class BeanFactory {
    static Map<Class<?>, Object> beanMap = new HashMap<>();

    static private BeanCreateStrategy instance;

    /**
     * 获取 接口类 class的单例
     *
     * @param clazz 无参构造的类
     * @return
     */
    static public <T> T get(Class<T> clazz) {
        return getBean(clazz, true);
    }

    /**
     * 获取 接口类 class的单例
     *
     * @param clazz  接口类
     * @param params 构造参数
     * @return
     */
    static public <T> T get(Class<T> clazz, Object... params) {
        return getBean(clazz, true, params);
    }

    static public <T> T getBean(Class<T> clazz, boolean singleton, Object... params) {
        if(instance==null){
            throw  new RuntimeException(" beanFactory has not been init! ");
        }
        if (!singleton) {
            return (T) instance.get(clazz,params);
        }
        T bean = (T) beanMap.get(clazz);
        if (bean == null) {
            bean = (T) instance.get(clazz,params);
            if (bean != null) {
                beanMap.put(clazz, bean);
            } else {
                throw new RuntimeException(" beanFactory has no Bean !");
            }
        }
        return bean;
    }

    static public void init(BeanCreateStrategy proxy) {
        instance = proxy;
        if (proxy.getBeanMap() != null) {
            beanMap.putAll(proxy.getBeanMap());
        }
    }

    /**
     *
     * strategy
     *
     */
    public static class BeanCreateStrategy {

        /**
         * 设置 接口 和具体子类的 map
         *
         * @return
         */
        public Map<Class<?>, String> config(){
            return null;
        }

        /**
         * 添加一些特色的类对象
         * 不是通过构造方法构造出来的
         */
        protected Map<Class<?>, Object> beanMap = new HashMap<>();

        public Map<Class<?>, Object> getBeanMap() {
            return beanMap;
        }

        public void setBeanMap(Map<Class<?>, Object> beanMap) {
            this.beanMap = beanMap;
        }

        /**
         * 通过 className 来实例化 接口类
         *
         * @param clazz 接口类
         * @return 实现接口的子类
         */
        public<T> T get(Class<T> clazz, Object... params) {
            if(config()==null){
                throw new RuntimeException("class config map null error!");
            }
            String clazzName = config().get(clazz);
            if (TextUtils.isEmpty(clazzName)) {
                throw new RuntimeException("no clazzName for class!");
            }
            try {
                Class<?> subClazz = Class.forName(clazzName);
                if (params == null) {
                    return (T) subClazz.newInstance();
                } else {
                    Class[] paramClazz =new Class[params.length];
                    for (int i = 0 ;i < paramClazz.length ;++i){
                        paramClazz[i] = params[i].getClass();
                    }
                    Constructor<?> constructor = subClazz.getDeclaredConstructor(paramClazz);
                    if (constructor == null) {
                        throw new RuntimeException("no match Constructor!!!");
                    }
                    constructor.setAccessible(true);
                    return (T) constructor.newInstance(params);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("class newInstance error!");
            }

        }

    }
}
