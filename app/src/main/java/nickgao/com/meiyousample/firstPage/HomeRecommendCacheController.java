package nickgao.com.meiyousample.firstPage;

import com.lingan.seeyou.ui.view.MultiImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyoujian on 2017/4/24.
 */

public class HomeRecommendCacheController {

    private static HomeRecommendCacheController instance;


    public static HomeRecommendCacheController getInstance() {
        if (null == instance) {
            instance = new HomeRecommendCacheController();
        }
        return instance;
    }

    public List<MultiImageView.DisplayImageModel> getDisplayImageModel(List<String> shrinkList, TalkModel model) {
        return getDisplayImageModel(shrinkList, model, model.recomm_type == HomeType.TYPE_NEWS_TOPIC);
    }

    public List<MultiImageView.DisplayImageModel> getDisplayImageModel(List<String> shrinkList, TalkModel model, boolean isShowBadge) {
        List<MultiImageView.DisplayImageModel> displayImageModels = new ArrayList<>();
        for (String image : shrinkList) {
            MultiImageView.DisplayImageModel displayImageModel = new MultiImageView.DisplayImageModel();
            displayImageModel.imageUrl = image;
            displayImageModel.imgs_count = model.imgs_count;
            displayImageModel.video_time = model.video_time;
            displayImageModel.isShowNewsTopic = isShowBadge;
            displayImageModel.news_type = model.news_type;
            displayImageModels.add(displayImageModel);
        }
        return displayImageModels;
    }

}
