package nickgao.com.meiyousample.gridview;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/3/17.
 */

public class SearchCircleOverAllFaqAdapter extends SearchDoubleColumnAdapter  {

    private List<String> mKeyList;
    private List<Integer> mIdList;


    public SearchCircleOverAllFaqAdapter(List<String> mKeyList, List<Integer> mIdList, LinearGrid linearGrid) {
        super(linearGrid);
        this.mKeyList = mKeyList;
        this.mIdList = mIdList;
    }

    @Override
    public int getCount() {
        return mKeyList.size();
    }

    public String getItem(int position) {
        return mKeyList.get(position);
    }

    public int getIdList(int position) {
        return mIdList.get(position);
    }



    @Override
    public void inflateView(int position, View rootView) {
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitle.setText(mKeyList.get(position));
    }
}
