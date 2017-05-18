package nickgao.com.framework.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA. User: Administrator Date: 14-12-9 Time: 下午5:32 To
 * change this template use File | Settings | File Templates.
 */
public class KeyUtil {
    /**
     * 获取说说的verify
     * <p/>
     * 取中间16位
     *
     * @param context
     * @param content
     * @return
     */
    public static String getShuoshuoVerify(Context context, String content) {
        try {
            String str = content
                    + BeanManager.getUtilSaver().getUserId(context)
                    + System.currentTimeMillis();
            String md5 = md5(str);
            if (!StringUtils.isNull(md5) && md5.length() == 16) {
                return md5.substring(0, 16);
            } else if (!StringUtils.isNull(md5) && md5.length() == 32) {
                return md5.substring(8, 24);
            }
            return md5;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取签名key
     * 签名方式:fuid+key md5加密 截取前6位 都转大写
     * <p/>
     * 注册 登录
     */
    private static String KEY = "382DF4FA64CAB43CD31B75B5CD7CC9EC660E6F42";//签名KEY

    @SuppressLint("DefaultLocale")
    public static String getVerifyKey(String content) {
        try {
            String str = KEY + content;
            String md5 =md5(str);
            if (!StringUtils.isNull(md5) && md5.length() >= 6) {
                return md5.substring(0, 6).toUpperCase();
            } else {
                return md5.toUpperCase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    /**
     * 获取说说回复的加密key
     * <p/>
     * key值： ios： 9FD2EBD24F959BA71B4CBFC87081C367AF9752DB
     * android：382DF4FA64CAB43CD31B75B5CD7CC9EC660E6F42
     * sign取值规则：md5(键值+发布内容) -->获取前6位转换成大写 然后发布的时候，
     * 把sign一起post提交
     * <p/>
     * 发动态，回复动态
     * 发帖，回帖
     *
     * @return
     */

    @SuppressLint("DefaultLocale")
    public static String getVerifyKeyTwo(String content, String firstImage) {
        try {
            String key = "382DF4FA64CAB43CD31B75B5CD7CC9EC660E6F42";
            String md5 = "";
            if (!StringUtils.isNull(content)) {
                md5 =md5(key + content);
            } else if (!StringUtils.isNull(firstImage)) {
                md5 =md5(key + firstImage);
            } else {
                md5 =md5(key);
            }
            if (md5 != null && md5.length() > 5) {
                String result = md5.substring(0, 6);
                return result.toUpperCase();
            }
            return md5;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

    }


    /**
     * 转换成md5值
     *
     * @param src
     * @return
     */
    private static  String md5(String src) {

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = src.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

}

