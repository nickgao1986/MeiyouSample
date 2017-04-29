package nickgao.com.meiyousample.firstPage;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lingan.seeyou.ui.view.HomeSlidingTabLayout;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.List;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.controller.NewsHomeController;

/**
 * Created by gaoyoujian on 2017/4/27.
 */

public class NewsHomeTabController {
    private Activity activity;
    public boolean bTopMenuShowed;
    private LinearLayout ll_special_category_content;
    private RelativeLayout linearMenu;
    private GridView categoryGridView;
    private HomeSlidingTabLayout pagerSlidingTabStrip;
    private int category_height;
    private ImageView ivMoreClassify;
    private HomeMoreAllClassfiyAdapter classfiyAdapter;
    private List<HomeClassifyModel> classifyModels;
    private OnCallBackListener listener;
    private Animation animation_out;
    private Animation animation_in;

    public NewsHomeTabController(Activity activity, List<HomeClassifyModel> classifyModels, boolean bTopMenuShowed) {
        this.bTopMenuShowed = bTopMenuShowed;
        this.activity = activity;
        this.classifyModels = classifyModels;
        initUI();
        initLogic();
        setLisenner();
    }

    private void initUI() {
        category_height = NewsHomeController.getInstance().getCurrentScrollY();
        ll_special_category_content = (LinearLayout) activity.findViewById(R.id.ll_special_category_content);
        if (bTopMenuShowed) {
            ll_special_category_content.setVisibility(View.VISIBLE);
        } else {
            ll_special_category_content.setVisibility(View.GONE);
        }
        categoryGridView = (GridView) activity.findViewById(R.id.special_gridview);
        pagerSlidingTabStrip = (HomeSlidingTabLayout) activity.findViewById(R.id.news_home_sliding_tab);
        ivMoreClassify = (ImageView) activity.findViewById(R.id.ivMoreClassify);
        linearMenu = (RelativeLayout) ll_special_category_content.findViewById(R.id.rl_special_category_list);

        animation_in = AnimationUtils.loadAnimation(activity, R.anim.menu_in);
        animation_out = AnimationUtils.loadAnimation(activity, R.anim.menu_out);
    }

    private void initLogic() {
        if (classfiyAdapter == null) {
            classfiyAdapter = new HomeMoreAllClassfiyAdapter(activity.getApplicationContext(), classifyModels);
            categoryGridView.setAdapter(classfiyAdapter);
        } else {
            classfiyAdapter.notifyDataSetChanged();
        }
    }

    private void setLisenner() {
        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                handlePopMenu();
            }
        });
    }

    /**
     * 分类的隐藏显示
     */
    public void handleCategoryMenu(OnCallBackListener listener) {
        this.listener = listener;
        handlePopMenu();
    }

    public void handlePopMenu() {
        if (!bTopMenuShowed) {
            showPopMenu();
        } else {
            hidePopMenu();
        }
    }

    public void hidePopMenu() {
        if (!bTopMenuShowed) {
            return;
        }
        bTopMenuShowed = false;
        if (listener != null) {
            listener.OnCallBack(bTopMenuShowed);
        }
        upAnimation();
        animation_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                pagerSlidingTabStrip.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_special_category_content.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        linearMenu.startAnimation(animation_out);
    }
    public void hide(){
        ll_special_category_content.setVisibility(View.GONE);
        hidePopMenu();

    }
    /**
     * 显示菜单 点击标题的时候
     */
    private void showPopMenu() {
        if (bTopMenuShowed) {
            return;
        }
        bTopMenuShowed = true;
        if (listener != null) {
            listener.OnCallBack(bTopMenuShowed);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_special_category_content.getLayoutParams();
        layoutParams.topMargin = category_height;
        ll_special_category_content.requestLayout();
        //显示
        pagerSlidingTabStrip.setVisibility(View.VISIBLE);
        ll_special_category_content.setVisibility(View.VISIBLE);
        downAnimation();
        //动画
        linearMenu.startAnimation(animation_in);
        //响应
        ll_special_category_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击收缩
                hidePopMenu();
            }
        });
    }

    /**
     * 按下箭头的动画
     */
    private void downAnimation() {
        ObjectAnimator.ofFloat(ivMoreClassify, "rotation", 0,45f).setDuration(500).start();
    }

    /**
     * 收回之后箭头的动画
     */
    private void upAnimation() {
        ObjectAnimator.ofFloat(ivMoreClassify, "rotation", 45f,90f).setDuration(500).start();
    }
}
