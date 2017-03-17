package nickgao.com.meiyousample.gridview;

import android.view.View;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * Created by gaoyoujian on 2017/3/17.
 */

public abstract class SearchDoubleColumnAdapter implements LinearGrid.GridAdapter {

    private int leftPadding;
    private LinearGrid linearGrid;

    public SearchDoubleColumnAdapter(LinearGrid linearGrid) {
        if (linearGrid == null)
            return;
        this.linearGrid = linearGrid;
        linearGrid.setLine(2);
        leftPadding = DeviceUtils.dip2px(linearGrid.getContext(), 10);
    }

    /**
     * 刷新页面
     */
    protected void notifyDataSetChanged() {
        linearGrid.notifyDataSetChanged(0);
    }



    @Override
    public View getView(int position, View view) {
        View itemView = ViewFactory.from(view.getContext()).getLayoutInflater().inflate(R.layout.item_search_circle_faq, null);
        View lineview = itemView.findViewById(R.id.line_view);
        View bottomLineView = itemView.findViewById(R.id.bottom_line);
        //
        if (position % 2 == 0)
            lineview.setVisibility(View.VISIBLE);
        else
            lineview.setVisibility(View.GONE);
        //
        int countSize = getCount();
        if (countSize - position <= (2 - countSize % 2)) {
            bottomLineView.setVisibility(View.INVISIBLE);
        } else {
            bottomLineView.setVisibility(View.INVISIBLE);
            if (position % 2 == 0)
                bottomLineView.setPadding(leftPadding, 0, 0, 0);
            else
                bottomLineView.setPadding(0, 0, 0, 0);
        }
        inflateView(position, itemView);
        return itemView;
    }

    /**
     * 返回布局的rootview
     *
     * @param rootView
     */
    public abstract void inflateView(int position, View rootView);
}
