package nickgao.com.meiyousample.personal;


import nickgao.com.meiyousample.model.reply.ReplyData;

/**
 * Created by gaoyoujian on 2017/4/27.
 */

public interface ReplyListener {
    public void onSuccess(ReplyData response, boolean isLoaderMore);
    public void onFail();
}
