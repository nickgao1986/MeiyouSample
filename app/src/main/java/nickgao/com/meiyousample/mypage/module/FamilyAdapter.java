package nickgao.com.meiyousample.mypage.module;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.LoaderImageView;
import com.lingan.seeyou.ui.view.skin.ViewFactory;

import java.util.List;

import fresco.view.ImageLoader;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.DeviceUtils;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class FamilyAdapter extends BaseAdapter {

    private Context mContext;
    private List<FamilyModel> mFamilyModelList;
    private int itemWH = 0;

    public FamilyAdapter(Context mContext, List<FamilyModel> mFamilyModelList) {
        this.mFamilyModelList = mFamilyModelList;
        this.mContext = mContext;
        this.itemWH = DeviceUtils.dip2px(mContext, 44);
    }

    @Override
    public int getCount() {
        return mFamilyModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFamilyModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = ViewFactory.from(mContext).getLayoutInflater().inflate(R.layout.item_family, null);
            viewHolder.initHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int itemHeight = DeviceUtils.getScreenWidth(mContext.getApplicationContext()) / 4;
        GridView.LayoutParams lp = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
        lp.height = itemHeight;
        lp.width = itemHeight;
        convertView.setLayoutParams(lp);

        drawSplitLine(position, (RelativeLayout) convertView);
        //viewHolder.fillResources();
        viewHolder.tx_familyName.setText(mFamilyModelList.get(position).app_name);
        String url = mFamilyModelList.get(position).app_logo;
        viewHolder.icon_family.setBackgroundDrawable(null);
        ImageLoader.getInstance().displayImage(mContext.getApplicationContext(), viewHolder.icon_family, url, 0, 0, 0, 0, false, itemWH, itemWH, null);
        return convertView;
    }

    /**
     * 画9宫格的分割线
     * @param position
     * @param banner
     */
    private void drawSplitLine(int position, RelativeLayout banner) {
        RelativeLayout root = banner;
        //九宫格分割线使用padding把北京露出来,来实现
        int hang = position / 4;
        int lie = position % 4;
        int maxHang = (getCount() - 1) / 4;
        if (hang < maxHang) {
            if (lie == 3) {
                root.setPadding(0, 0, 0, 1);
            } else {
                root.setPadding(0, 0, 1, 1);
            }
        } else if (lie != 3 && position != getCount() - 1) {
            root.setPadding(0, 0, 1, 0);
        }
        if (lie != 3 && position == getCount() - 1) {
            root.setPadding(0, 0, 1, 0);
        }
    }

    class ViewHolder {

        private LoaderImageView icon_family;
        private TextView tx_familyName;
        private LinearLayout ll_item_mine;
        private RelativeLayout rl_item_mine;

        public void fillResources() {
//            SkinEngine.getInstance().setViewTextColor(mContext, tx_familyName, R.color.black_a);
//            SkinEngine.getInstance().setViewBackground(mContext, ll_item_mine, R.drawable.apk_all_white_selector);
//            SkinEngine.getInstance().setViewBackgroundColor(mContext, rl_item_mine, R.color.black_e);
            //UI需求,分割线透明度减少40%
            if(rl_item_mine != null && rl_item_mine.getBackground() != null){
                rl_item_mine.getBackground().setAlpha(153);
            }
        }

        public void initHolder(View convertView) {
            tx_familyName = (TextView) convertView.findViewById(R.id.tx_familyName);
            icon_family = (LoaderImageView) convertView.findViewById(R.id.icon_family);
            ll_item_mine = (LinearLayout) convertView.findViewById(R.id.ll_item_mine);
            rl_item_mine = (RelativeLayout) convertView.findViewById(R.id.rl_item_mine);
        }
    }
}
