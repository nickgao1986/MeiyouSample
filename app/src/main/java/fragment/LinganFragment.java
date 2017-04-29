package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public abstract class LinganFragment extends BaseFragment {

    protected TitleBarCommon titleBarCommon;
    protected RelativeLayout baseLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater = ViewFactory.from(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.base_layout, null);
        initBaseView(inflater, view);
        beforeInitView(view);
        initView(view);
        return view;
    }

    protected void beforeInitView(View view) {
    }

    protected int getHeadCommonLayoutId() {
        return R.id.head_common_layout;
    }

    private void initBaseView(LayoutInflater inflater, View view) {
        baseLayout = (RelativeLayout) view.findViewById(R.id.base_layout);
        titleBarCommon = (TitleBarCommon) view.findViewById(R.id.head_common_layout);
        titleBarCommon.setLayoutInflater(ViewFactory.from(getActivity()).getLayoutInflater());
        if (getLayout() > 0) {
            View childView = inflater.inflate(getLayout(), null);
            if (childView != null) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.FILL_PARENT);
                params.addRule(RelativeLayout.BELOW, R.id.head_common_layout);
                baseLayout.addView(childView, params);
            }
        }
    }

    protected ViewGroup getRootView() {
        return baseLayout;
    }

    protected abstract int getLayout();

    protected abstract void initView(View view);

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
