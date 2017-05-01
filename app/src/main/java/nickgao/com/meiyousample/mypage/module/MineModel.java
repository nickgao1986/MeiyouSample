package nickgao.com.meiyousample.mypage.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nickgao.com.meiyousample.settings.MineItemModel;
import nickgao.com.meiyousample.settings.MineSection;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class MineModel implements Serializable {
    public List<MineSection> sections = new ArrayList<>();
    public List<List<MineItemModel>> items = new ArrayList<>();


    public List<List<MineItemModel>> getItems() {
        return items;
    }

    public void setItems(List<List<MineItemModel>> items) {
        this.items = items;
    }

    public List<MineSection> getSections() {
        return sections;
    }

    public void setSections(List<MineSection> sections) {
        this.sections = sections;
    }


}