package nickgao.com.meiyousample.myfragment;

import android.content.Context;

import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.adapter.BaseMultiItemQuickAdapter;
import nickgao.com.meiyousample.adapter.BaseViewHolder;
import nickgao.com.meiyousample.adapter.MultipleItem;

/**
 * Created by gaoyoujian on 2017/5/25.
 */

public class DynamicHomeAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {



    public DynamicHomeAdapter(Context context, List data) {
        super(data);
        addItemType(MultipleItem.DEFAULT, R.layout.layout_home_dynamic_item);
        addItemType(MultipleItem.SHARE, R.layout.layout_home_dynamic_item_share);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
//            case MultipleItem.DEFAULT:
//                helper.setText(R.id.tv, item.getContent());
//                break;
//            case MultipleItem.SHARE:
//                switch (helper.getLayoutPosition() %
//                        2) {
//                    case 0:
//                        helper.setImageResource(R.id.iv, R.mipmap.animation_img1);
//                        break;
//                    case 1:
//                        helper.setImageResource(R.id.iv, R.mipmap.animation_img2);
//                        break;
//
//                }
//                break;
        }
    }

}