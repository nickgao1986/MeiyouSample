package nickgao.com.meiyousample.settings;

import java.lang.ref.WeakReference;

/**
 * Created by gaoyoujian on 2017/3/25.
 */

public class MineControl {

    /**
     * 刷新banner的视图,主要用于轮播banner中的图片
     */
    public static final int MY_REFRESH_BANNER = -2;


    public static final int MY_REFRESH_ALL_MINE_UI = -1;
    //这些静态常亮为AssoId,以此判断"我的"中的数据的跳转逻辑
    /**
     * 我的订单
     */
    public static final int MY_ORDER = 1;
    /**
     * 我的话题
     */
    public static final int MY_TOPIC = 2;
    /**
     * 我的日记
     */
    public static final int MY_MYDIARY = 3;
    /**
     * 我的提醒
     */
    public static final int MY_REMIND = 4;
    /**
     * 密友圈
     */
    public static final int MY_SHUOSHUO = 5;
    /**
     * 柚币中心
     */
    public static final int MY_COIN = 6;
    /**
     * 个性装扮
     */
    public static final int MY_SKIN = 7;
    /**
     * 更多设置
     */
    public static final int MY_SET = 8;

    /**
     * 帮助与反馈
     */
    public static final int MY_HELP_FEEDBACK = 25;

    /**
     * 柚子街帮助与反馈
     */
    public static final int MY_HELP_FEEDBACK_YOUZIJIE = 26;

    /**
     * 任务中心
     */
    public static final int MY_TASK_CENTER = 97;

    /**
     * 经期设置
     */
    public static final int MY_PERIOD = 136;

    /**
     * 人生阶段
     */
    public static final int MY_MODE = 28;


    /**
     * 试用中心
     */
    public static final int TRIALCENTER = 129;


    public static final int MY_COLLECT = 156;
    /**
     * 我的主页
     */
    public static final int MY_HOME_PAGE = 167;

    /**
     * 我的界面数据生成的view将用于哪个界面-我的界面
     */
    public static final int MINE_VIEWHOLDER_FROM_MINE = 0;

    /**
     * 我的界面数据生成的view将用于哪个界面-柚币任务
     */
    public static final int MINE_VIEWHOLDER_FROM_UCOIN_TASK = 1;

    public static final String MINE_MODEL = "mine_model";
    public static final String MINE_DATA_MODEL = "mine_data_model";
    private static MineControl controll;

    //预加载数据,在app启动时加载数据,真正使用完后,置空待回收
    private WeakReference<MineModel> initMineModeCache;
    //我的柚币数
    private String ubCount;

    public static MineControl getInstance() {
        if (controll == null) {
            controll = new MineControl();
        }
        return controll;
    }



}
