package nickgao.com.meiyousample.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyoujian on 2017/3/25.
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