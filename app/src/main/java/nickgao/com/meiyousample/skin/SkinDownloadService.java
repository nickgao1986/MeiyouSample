package nickgao.com.meiyousample.skin;

import android.content.Context;
import android.content.Intent;

import java.io.File;

import nickgao.com.meiyousample.database.SkinStastus_DataBase;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class SkinDownloadService extends DecorationDownloadService {

    public static void doIntent(Context context, SkinModel model, String flag, OnNotifationListener listener) {
        DecorationDownloadService.mListener = listener;
        Intent intent = new Intent();
        intent.setClass(context, SkinDownloadService.class);
        intent.putExtra("model", model);
        intent.putExtra("flag", flag);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        isSkin = true;
        if (threadDataBase == null) {
            threadDataBase = new SkinThread_DataBase(this);
        }
        if (statusDataBase == null) {
            statusDataBase = new SkinStastus_DataBase(this);
        }
        super.onCreate();
    }

    @Override
    public String getDownloadPath() {
        return SkinUtil.getInstance().getDownloadPath(this);
    }

    @Override
    protected void onDownloadComplete(File file, DecorationModel model) {

    }

    /**
     * 对三个界面发送广播 用于更新界面下载进度数据
     *
     * @param model
     * @param completeSize
     */
    @Override
    protected void sendBroadcast2Receiver(DecorationModel model, int completeSize, boolean flag) {

        //精品界面
        Intent intentBoutique = new Intent();
        intentBoutique.setAction(SkinDownloadType.UPDATE_SKIN_ACTION);
        intentBoutique.putExtra("completeSize", completeSize);
        intentBoutique.putExtra("flag", flag);
        intentBoutique.putExtra("skinId", model.skinId);
        sendBroadcast(intentBoutique);
    }

    /**
     * 初始化文件的异常
     * @param model
     */
    @Override
    public void setIntExcationInfo(DecorationModel model) {
        model.updateStastus = SkinDownloadType.SKIN_INT_NO_NETWORK;
        ListState.state.put(model.getFileName(), model.updateStastus);//把状态设置为暂停
        statusDataBase.updateStatusModel(model, model.updateStastus);
        Intent intentMySkin = new Intent();
        intentMySkin.setAction(SkinDownloadType.UPDATE_SKIN_ACTION);
        intentMySkin.putExtra("int_pause", true);
        intentMySkin.putExtra("skinId", model.skinId);
        sendBroadcast(intentMySkin);
    }
}
