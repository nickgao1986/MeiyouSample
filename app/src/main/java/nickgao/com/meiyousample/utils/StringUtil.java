package nickgao.com.meiyousample.utils;

/**
 * Created by gaoyoujian on 2017/3/5.
 */

public class StringUtil {

    public static boolean isWhitespace(int c){
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
    }

    public static String getFiveNum(int total_review) {
        String num;
        if(total_review > 9999) {
            num = total_review / 10000 + "万";
        } else {
            num = total_review + "";
        }

        return num;
    }
}
