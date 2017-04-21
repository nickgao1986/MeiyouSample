package nickgao.com.meiyousample.settings;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.LoaderImageView;

import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/3/25.
 */

abstract public class ViewHolder {

    /**
     * 显示images中图片的选择策略 -- 顺序，下一个
     */
    public static final int IMAGES_RANDOM_INDEX_NEXT = 1;

    /**
     * 显示images中图片的选择策略 -- 保持原样，不动
     */
    public static final int IMAGES_RANDOM_INDEX_NONE = 0;

    public LinearLayout ll_mine;
    public TextView tv_mine;
    public View mine_line;
    public TextView tv_mine_banner_rightarrow;
    public ImageView iv_mine_banner_rightarrow;
    public RelativeLayout rl_mine_banner_title;//该视图为titile,主要为了接收点击事件
    private LoaderImageView ivSectionIcon;
    private SparseIntArray imagesIndex;
    private SparseArray<String> tempIcon = null;


    public void initHolder(View convertView) {
        tv_mine = (TextView) convertView.findViewById(R.id.tv_mine);
        mine_line = convertView.findViewById(R.id.mine_line);
        ll_mine = (LinearLayout) convertView.findViewById(R.id.ll_mine);
        tv_mine_banner_rightarrow = (TextView) convertView.findViewById(R.id.tv_mine_banner_rightarrow);
        iv_mine_banner_rightarrow = (ImageView) convertView.findViewById(R.id.iv_mine_banner_rightarrow);
        rl_mine_banner_title = (RelativeLayout) convertView.findViewById(R.id.rl_mine_banner_title);
        ivSectionIcon = (LoaderImageView) convertView.findViewById(R.id.iv_layoutminelist_icon);
    }

    public abstract View getRootView();

}
