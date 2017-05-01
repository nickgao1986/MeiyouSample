package activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.lingan.seeyou.ui.view.skin.ViewFactory;

import fragment.TitleBarCommon;
import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class LinganActivity extends FragmentActivity {
    protected static final String TAG = "LinganActivity";
    protected final String uniqueId = this.getClass().getSimpleName() + Math.random();
    protected TitleBarCommon titleBarCommon;
    protected RelativeLayout baseLayout;
    protected boolean bUseCustomAnimation = false;
    protected Object[] args = null;
    private boolean applyedChange;
    protected ViewFactory viewFactory;
    protected LayoutInflater myLayoutInflater;
    public Context context;
    private View mRootView;
    //是否自定义布局
    private boolean mIsCustomLayout;

    public boolean isCustomLayout() {
        return mIsCustomLayout;
    }

    public void setCustomLayout(boolean customLayout) {
        mIsCustomLayout = customLayout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        specialAnimation();
        initLayoutInflater();
        initBaseView();
        initTitleBar();
    }
    //重写方法实现自定义需求
    protected void initTitleBar() {
        setTitlebarAction();
    }

    protected void setTitlebarAction() {
        if (titleBarCommon != null) {
            if (titleBarCommon.getLeftButtonView() != null) {
                titleBarCommon.getLeftButtonView().setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LinganActivity.this.finish();
                            }
                        });
            }
        }
    }


    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        return myLayoutInflater == null ? super.getLayoutInflater() : myLayoutInflater;
    }

    private void specialAnimation() {
        if (!bUseCustomAnimation) {
            overridePendingTransition(R.anim.activity_new_in,
                    R.anim.activity_old_out);
        }
    }

    private void initBaseView() {
        if(!isCustomLayout()){
            mRootView = getLayoutInflater().inflate(R.layout.base_layout, null);
            super.setContentView(mRootView);
            baseLayout = (RelativeLayout) findViewById(R.id.base_layout);
            titleBarCommon = (TitleBarCommon) findViewById(R.id.head_common_layout);
            titleBarCommon.setLayoutInflater(ViewFactory.from(getApplicationContext()).getLayoutInflater());
        }
    }

    public RelativeLayout getParentView() {
        return baseLayout;

    }
    /**
     * must before setContentView
     */
    protected void switchViewFactory() {
        if (viewFactory == null) {
            viewFactory = ViewFactory.from(this);
        }
    }

    protected void initLayoutInflater() {
        switchViewFactory();
        if (viewFactory != null) {
            myLayoutInflater = viewFactory.getLayoutInflater(this);
        } else {
            myLayoutInflater = super.getLayoutInflater();
        }
    }

    /**
     * @param layoutResID int
     */
    @Override
    public void setContentView(int layoutResID) {
        if(!isCustomLayout()){
            View view = getLayoutInflater().inflate(layoutResID, null);
            addChildView(view);
        }else{
            View view = getLayoutInflater().inflate(layoutResID, null);
            super.setContentView(view);
        }

    }

    private void addChildView(View view) {
        if(baseLayout!=null){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.FILL_PARENT);
            params.addRule(RelativeLayout.BELOW, R.id.head_common_layout);
            if (view != null) {
                baseLayout.addView(view, params);
            }
        }
    }

}
