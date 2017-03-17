package nickgao.com.meiyousample.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.List;

import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/3/17.
 */

public class SearchCircleOverAllTagItemAdapter extends BaseAdapter {

    private Context mcContext;

    private List<String> list;
    private LayoutInflater inflater;

    public SearchCircleOverAllTagItemAdapter(Context context, List<String> list) {
        this.mcContext = context;
        this.list = list;
        inflater = ViewFactory.from(context).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        final String model = (String) getItem(position);

        view = inflater.inflate(R.layout.layout_search_category_gv_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tvItem);

        tv.setText(model);

        return view;
    }

}
