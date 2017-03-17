package nickgao.com.meiyousample.gridview;

/**
 * Created by gaoyoujian on 2017/3/17.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.HashMap;

/**
 * @author 庄育锋
 */
public class LinearGrid extends LinearLayout implements OnClickListener {
    private Context context;
    private float scale;
    private int line = 1;// 列数
    private int id = 0;// 背景
    private int dipWidth;// 中间相距宽度
    private int dipHeight;// 上下相距高度
    private LinearLayout itemLinear;
    private LayoutParams itemLp;
    private GridAdapter adapter;
    private OnLinearGridItemClickListener_L listener;
    private HashMap<Integer, Integer> viewPositions;

    public LinearGrid(Context context) {
        this(context, null);
    }

    public LinearGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("NewApi")
    public LinearGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        scale = context.getResources().getDisplayMetrics().density;
        setOrientation(LinearLayout.VERTICAL);
        viewPositions = new HashMap<>();
    }

    /**
     * 设置显示的列数
     *
     * @param l
     */
    public void setLine(int l) {
        if (l < 1) {
            return;
        }
        this.line = l;
    }

    /**
     * 设置item背景
     *
     * @param id
     */
    public void setItemBackgroundResource(int id) {
        this.id = id;
    }

    /**
     * 刷新列表
     */
    public void notifyDataSetChanged(int flag) {
        this.removeAllViews();
        setView(flag);
    }

    /**
     * 设置左右item之间的距离
     */
    public void setDividerWidth(float dpValue) {
        dipWidth = (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置上下item之间的距离
     */
    public void setDividerHeight(float dpValue) {
        dipHeight = (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param ad
     * @param flag 0的时候表示按权重分配宽
     */
    public void setAdapter(GridAdapter ad, int flag) {
        this.adapter = ad;
        notifyDataSetChanged(flag);
    }

    private void setView(int flag) {
        if (adapter == null) {
            Log.e("LinearGrid", "适配器为空");
            return;
        }
        int size = adapter.getCount();
        int lastNum = size % line != 0 ? (line - size % line) : 0;// 最后一行空余的个数
        for (int i = 0; i < size; i++) {
            View itemView = adapter.getView(i, this);// 得到item的View
            if (id != 0) {
                itemView.setBackgroundResource(id);
            }
            viewPositions.put(itemView.hashCode(), i);// 为每个item设置编号
            if (itemView.isClickable()) {
                itemView.setOnClickListener(this);
            }
            if (i % line == 0) {// 创建行容器
                itemLinear = new LinearLayout(context);// 行容器
                itemLinear.setOrientation(LinearLayout.HORIZONTAL);
                addView(itemLinear, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                if (i != 0) {
                    if (itemLp == null) {
                        itemLp = (LayoutParams) itemLinear.getLayoutParams();
                        itemLp.topMargin = dipHeight;
                    } else {
                        itemLinear.setLayoutParams(itemLp);
                    }
                }
            }
            FrameLayout frame = new FrameLayout(context);
            itemLinear.addView(frame);
            setWidth(itemLinear, frame, flag);
            frame.addView(itemView);
            if (i == (size - 1)) {// 最后一行
                for (int j = 0; j < lastNum; j++) {// 填充最后的位置
                    FrameLayout frames = new FrameLayout(context);
                    itemLinear.addView(frames);
                    setWidth(itemLinear, frames, flag);
                }
            }
        }
    }

    // 设置item之前的距离
    private void setWidth(LinearLayout itemLinear, FrameLayout frame, int flag) {
        LayoutParams fLp = (LayoutParams) frame.getLayoutParams();
        if (flag == 0) {
            fLp.weight = 1;
            fLp.width = 0;
            fLp.height = LayoutParams.WRAP_CONTENT;
        } else {
            fLp.width = LayoutParams.WRAP_CONTENT;
            fLp.height = LayoutParams.WRAP_CONTENT;
        }

        if (itemLinear.getChildCount() == 1) {
            fLp.leftMargin = 0;
        } else {
            fLp.leftMargin = dipWidth;
        }
        frame.setLayoutParams(fLp);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onLinearGridClickListener(v, viewPositions.get(v.hashCode()));
        }
    }

    // 适配器
    public interface GridAdapter {
        public int getCount();

        public View getView(int position, View view);
    }

    // 列表点击事件
    public interface OnLinearGridItemClickListener_L {
        public void onLinearGridClickListener(View view, int position);
    }

    public void setOnLinearGridItemClickListener(OnLinearGridItemClickListener_L l) {
        this.listener = l;
    }

}
