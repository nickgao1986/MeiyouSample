package nickgao.com.meiyousample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.settings.MineControl;
import nickgao.com.meiyousample.settings.MineItemModel;
import nickgao.com.meiyousample.settings.MineModel;
import nickgao.com.meiyousample.settings.MineSection;
import nickgao.com.meiyousample.settings.ViewHolder;
import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * Created by gaoyoujian on 2017/3/25.
 */

public class MySettingsActivity extends Activity {

    private LinearLayout llContent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);

        llContent = (LinearLayout) findViewById(R.id.ll_my_datacontent);

        List<MineSection> sections = createSection();

        for (int i = 0; i < sections.size(); i++) {
            MineSection section = sections.get(i);
            MineListViewHolder viewHolder = null;
            if (i == 0) {
                viewHolder = new MineListViewHolder(this, section, createModel1());
            } else {
                viewHolder = new MineListViewHolder(this, section, createModel2());
            }

            llContent.addView(viewHolder.getRootView());
        }


    }

    private List<MineSection> createSection() {
        List<MineSection> list = new ArrayList<MineSection>();
        MineSection section = new MineSection();
        section.title = "数据管理";
        section.has_line = true;
        section.style = MineSection.STYLE_LIST;
        list.add(section);

        MineSection section1 = new MineSection();
        section1.title = "关注";
        section1.has_line = true;
        section1.style = MineSection.STYLE_LIST;
        list.add(section1);

        MineSection section2 = new MineSection();
        section2.title = "个人主页";
        section2.has_line = true;
        section2.style = MineSection.STYLE_LIST;
        list.add(section2);

        MineSection section3 = new MineSection();
        section3.title = "个性装扮";
        section3.has_line = true;
        section3.style = MineSection.STYLE_LIST;
        list.add(section3);

        MineSection section4 = new MineSection();
        section4.title = "订单";
        section4.has_line = true;
        section4.style = MineSection.STYLE_LIST;
        list.add(section4);

        return list;
    }

    private List<MineItemModel> createModel1() {

        List<MineItemModel> list = new ArrayList<MineItemModel>();
        MineItemModel model = new MineItemModel();
        model.title = "个性";
        model.newsCount = 3;
        list.add(model);

        MineItemModel model1 = new MineItemModel();
        model1.title = "个性1";
        model1.newsCount = 4;
        list.add(model1);


        MineItemModel model2 = new MineItemModel();
        model2.title = "个性2";
        model2.newsCount = 5;
        list.add(model2);
        return list;

    }

    private List<MineItemModel> createModel2() {

        List<MineItemModel> list = new ArrayList<MineItemModel>();

        MineItemModel model3 = new MineItemModel();
        model3.title = "个性3";
        model3.newsCount = 6;
        list.add(model3);


        MineItemModel model4 = new MineItemModel();
        model4.title = "个性5";
        model4.newsCount = 7;
        list.add(model4);

        return list;
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
                        // viewHolder = MineLayoutFactory.getInstance().createView(activity, mineSection, result.getItems().get(i), from);
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
