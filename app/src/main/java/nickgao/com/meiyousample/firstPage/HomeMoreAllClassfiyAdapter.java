package nickgao.com.meiyousample.firstPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.List;

import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/4/27.
 */

public class HomeMoreAllClassfiyAdapter extends BaseAdapter {

    private List<HomeClassifyModel> data;

    private LayoutInflater inflater;

    private Context context;

    private int size = 0;

    public HomeMoreAllClassfiyAdapter(Context context, List<HomeClassifyModel> list) {
        this.inflater = ViewFactory.from(context).getLayoutInflater();
        this.data = list;
        this.size = list.size();
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.size;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeClassifyModel classifyModel = data.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_home_all_tab, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(classifyModel.name);

        if (classifyModel.isSelect) {
            SkinManager.getInstance().setDrawableBackground(viewHolder.tvName,R.drawable.bg_transparent_reb_selector);
            viewHolder.tvName.setTextColor(SkinManager.getInstance().getAdapterColorStateList(R.color.btn_home_select_color_selector));
            viewHolder.tvName.setSelected(true);
        } else {
            SkinManager.getInstance().setDrawableBackground(viewHolder.tvName,R.drawable.selector_mark);
            viewHolder.tvName.setTextColor(SkinManager.getInstance().getAdapterColorStateList(R.color.item_search_tags_text_color_selector));
            viewHolder.tvName.setSelected(false);
        }
        return convertView;
    }

    class ViewHolder {
        public TextView tvName;
    }
}