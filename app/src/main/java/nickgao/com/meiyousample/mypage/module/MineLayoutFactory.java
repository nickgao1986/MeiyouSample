package nickgao.com.meiyousample.mypage.module;

import android.app.Activity;

import java.util.Iterator;
import java.util.List;

import nickgao.com.meiyousample.MineListViewHolder;
import nickgao.com.meiyousample.settings.MineItemModel;
import nickgao.com.meiyousample.settings.MineSection;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class MineLayoutFactory {

    private static MineLayoutFactory mineLayoutFactory;

    private MineLayoutFactory() {

    }

    public static MineLayoutFactory getInstance() {
        if (mineLayoutFactory == null) {
            mineLayoutFactory = new MineLayoutFactory();
        }
        return mineLayoutFactory;
    }

    /**
     * 创建我的界面布局
     *
     * @param activity
     * @param mineSection
     * @param mineItemModels
     * @return
     */
    public ViewHolder createView(Activity activity, MineSection mineSection, List<MineItemModel> mineItemModels) {

//        filterData(mineSection, mineItemModels);

        ViewHolder viewHolder = null;
        switch (mineSection.style) {
            case MineSection.STYLE_NINE:
                viewHolder = new MineGridViewViewHolder(activity, mineSection, mineItemModels);
                break;
            case MineSection.STYLE_LIST:
                viewHolder = new MineListViewHolder(activity, mineSection, mineItemModels);
                break;
            default:
                viewHolder = new MineGridViewViewHolder(activity, mineSection, mineItemModels);
                break;
        }

        return viewHolder;
    }

    private void filterData(MineSection mineSection, List<MineItemModel> mineItemModels) {
        try {
            if (mineItemModels != null && mineItemModels.size() > 0) {

                for (Iterator<MineItemModel> iter = mineItemModels.iterator(); iter.hasNext(); ) {

                    MineItemModel itemModelTemp = iter.next();
                    if (itemModelTemp != null
                            && (itemModelTemp.uri_type == 67 || itemModelTemp.uri_type == 92 ||
                            (null != itemModelTemp.attr_text && itemModelTemp.attr_text.contains("try.seeyouyima.com")))) {
                        iter.remove();
                    }

                }

            }

            if (mineSection != null && (mineItemModels == null || mineItemModels.size() == 0)) {
                mineSection.is_hide = true;
                mineSection.has_line = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建我的界面布局
     *
     * @param activity
     * @param mineSection
     * @param mineItemModels
     * @param from
     * @return
     */
    public ViewHolder createView(Activity activity, MineSection mineSection, List<MineItemModel> mineItemModels, int from) {
        ViewHolder viewHolder = createView(activity, mineSection, mineItemModels);
        if (viewHolder != null) {
            viewHolder.setFrom(from);
        }
        return viewHolder;
    }

}
