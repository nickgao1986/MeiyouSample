package nickgao.com.meiyousample.adapter;

import android.content.Context;
import android.util.Log;

/**
 * 全屏视频事件
 * Created by wuzhongyou on 2016/11/15.
 */
public class TopicFullScreenController {

    Context mContext;

    public TopicFullScreenController(int mNewsId, String mClassName, String time, String uniqueVideoListId, boolean isPlaying, int progress) {
       //this.mContext = context;
        Log.d("TAG","=====TopicFullScreenController");
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void getContext() {
        Log.d("TAG","=====getContext="+mContext);
    }

}
