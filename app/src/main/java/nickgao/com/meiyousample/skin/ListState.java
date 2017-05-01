package nickgao.com.meiyousample.skin;

import java.util.HashMap;
import java.util.Map;

public class ListState {

    /**
     * 存放每个下载文件完成的下載狀態
     */
    public static Map<String,Integer> state = new HashMap<String, Integer>();
    /**
     * 存放每个下载文件完成的长度
     */
    public static Map<String,Integer> completeSizes=new HashMap<String, Integer>();
    /**
     * 存放每个下通知的时候文件完成的长度
     */
    public static Map<String,Integer> notifaSizes=new HashMap<String, Integer>();
    /**
     * 存放每个下载文件的总长度
     */
    public static Map<String,Integer> fileSizes=new HashMap<String, Integer>();

}
