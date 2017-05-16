package nickgao.com.framework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public class SeeyouBeanFactory {

    public static void init() {
        BeanFactory.init(new Proxy());
    }

    private static class Proxy extends BeanFactory.BeanCreateStrategy {


        Map<Class<?>, String> map = new HashMap<>();

        public Proxy() {
            map.put(UtilSaver.class, "nickgao.com.framework.utils.LocalUtilSaver");
            //map.put(LocalSocialService.class,"com.lingan.seeyou.share.SocialService");
        }

        @Override
        public Map<Class<?>, String> config() {
            return map;
        }
    }


}
