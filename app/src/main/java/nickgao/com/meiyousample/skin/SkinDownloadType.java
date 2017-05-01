package nickgao.com.meiyousample.skin;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public interface SkinDownloadType {
    public static final int SKIN_CONVERT_COIN = -1;//未兑换柚币状态

    public static final int SKIN_INIT = 0;//初始化状态

    public static final int SKIN_DOWNLOADING = 1;//正在下载状态

    public static final int SKIN_PAUSE = 2;//暂停状态

    public static final int SKIN_COMPLETE = 3;//完成状态

    public static final int SKIN_APPLY = 4;//已启用状态

    public static final int SKIN_UPDATE_VERSON = 5;//有新版本状态

    public static final int SKIN_NO_NETWORK = 6;//网络断掉状态
    public static final int SKIN_INT_NO_NETWORK = 7;//初始化文件大小的时候网络断掉状态

    //广播 所有皮肤界面更新
    public static final String UPDATE_SKIN_ACTION = "updateSkin";
    //广播 所有表情界面更新
    public static final String UPDATE_EXPRESSION_ACTION = "updateExpression";


    public static final String update_adapter_action = "updateAdapterUI";//广播行为 所有皮肤主题列表adapter界面刷新
    public static final String update_skin_detail_action = "updateDetailUI";//预览界面 广播行为
    public static final String update_my_skin_action = "updateMySkinUI";//兑换过的皮肤列表 广播行为
    public static final String update_my_night_action = "updateMyNightUI";//我的界面夜间模式 广播行为
    public static final String update_boutique_action = "updateBoutiqueUI";//精品界面广播行为
}