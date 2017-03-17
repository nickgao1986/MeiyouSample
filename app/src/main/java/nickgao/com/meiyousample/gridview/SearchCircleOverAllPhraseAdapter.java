package nickgao.com.meiyousample.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.model.SearchCircleHomeModel;
import nickgao.com.meiyousample.model.SearchCircleHomeModel.SearchCircleHomeItemModel;

/**
 * Created by gaoyoujian on 2017/3/17.
 */

public class SearchCircleOverAllPhraseAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater inflater;

    private List<SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel> bodyList;


    public SearchCircleOverAllPhraseAdapter(Context context, List<SearchCircleHomeModel.SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel> bodyList) {
        this.mContext = context;
        this.bodyList = bodyList;
        this.inflater = ViewFactory.from(context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return bodyList == null ? 0 : bodyList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return bodyList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup arg2) {
        ViewHolder vHolder;
        final SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel model = (SearchCircleHomeItemModel.SearchCircleHomeItemBodyModel) getItem(pos);

        if (view == null) {
            vHolder = new ViewHolder();
            view = inflater.inflate(R.layout.layout_search_category_item, null);
            vHolder.tvTitle = (TextView) view.findViewById(R.id.tvCategoryTitle);
            vHolder.gv = (GridViewExx) view.findViewById(R.id.gvCategory);

            view.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) view.getTag();
        }

        if (model.title != null && !model.title.equals("")) {
            vHolder.tvTitle.setText(model.title);
        }

        SearchCircleOverAllTagItemAdapter itemAdapter = new SearchCircleOverAllTagItemAdapter(mContext, model.keyList);


        //添加并且显示
        vHolder.gv.setAdapter(itemAdapter);

        //标签点击事件
        vHolder.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


            }
        });

        return view;
    }

    private class ViewHolder {
        private TextView tvTitle;
        private GridViewExx gv;

    }
}
