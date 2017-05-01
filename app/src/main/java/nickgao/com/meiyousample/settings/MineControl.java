package nickgao.com.meiyousample.settings;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;

import nickgao.com.meiyousample.mypage.module.MineLayoutFactory;
import nickgao.com.meiyousample.mypage.module.ViewHolder;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.StringUtils;

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

    public MineModel getMine(Activity context) {
        MineModel mineModel = null;
        String str = getFromAssets(context, "mefragment.txt");
        mineModel = parseJsonToMineModel(str);

        return mineModel;
    }


    public MineModel parseJsonToMineModel(String result) {

        MineModel mineModel = null;
        if (!StringUtils.isNull(result)) {
            Gson gson = new Gson();
            Type type = new TypeToken<MineModel>() {
            }.getType();
            mineModel = gson.fromJson(result, type);
        }

        return mineModel;
    }

    public String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成我的界面样式的view,并添加到content中
     *
     * @param result
     * @param content
     * @param viewHolderList
     */
    public void updateViewToMyContent(Activity activity, MineModel result, ViewGroup content, List<ViewHolder> viewHolderList) {
        updateViewToMyContent(activity, result, content, viewHolderList, MineControl.MINE_VIEWHOLDER_FROM_MINE);
    }

    /**
     * 生成我的界面样式的view,并添加到content中
     *
     * @param result
     * @param content
     * @param viewHolderList
     */
    public void updateViewToMyContent(Activity activity, MineModel result, ViewGroup content, List<ViewHolder> viewHolderList, int from) {
        if (result != null && content != null && viewHolderList != null) {
            content.removeAllViews();
            viewHolderList.clear();
            //生成视图
            List<MineSection> sectionList = result.getSections();
            if (sectionList != null) {
                for (int i = 0; i < sectionList.size(); i++) {
                    MineSection mineSection = sectionList.get(i);
                    ViewHolder viewHolder = null;
                    if (result.getItems() != null && i < result.getItems().size()) {
                        viewHolder = MineLayoutFactory.getInstance().createView(activity, mineSection, result.getItems().get(i), from);
                    }
                    if (viewHolder == null) {
                        continue;
                    }
                    viewHolderList.add(viewHolder);
                    View convertView = viewHolder.getRootView();
                    //把视图添加到界面上去
                    if (mineSection.has_line) {
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout
                                .LayoutParams.WRAP_CONTENT);
                        llp.setMargins(0, DeviceUtils.dip2px(activity, 10), 0, 0);
                        convertView.setLayoutParams(llp);
                    }
                    content.addView(convertView);
                }
            }
        }

    }

}
