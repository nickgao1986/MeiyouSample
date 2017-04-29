package nickgao.com.meiyousample.firstPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lingan.seeyou.ui.view.skin.SkinManager;

import nickgao.com.meiyousample.R;

/**
 * Created by gaoyoujian on 2017/4/21.
 */

public class SeeyouActivity extends FragmentActivity{

    private NewsHomeFragment newsHomeFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seeyou_fragment);
        switchPage();

//        String str = getFromAssets("talk.txt");
//        try {
//            JSONObject object = new JSONObject(str);
//            RecommendTopicResponeModel model = new RecommendTopicResponeModel(this, object);
//            LogUtils.d("===size=" + model.list);
//        }catch (Exception ex) {
//
//        }
    }

    private void switchPage() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if (newsHomeFragment != null) {
//            newsHomeFragment.updateFragment(isSamePage, isFromNotify);
//            newsHomeFragment.handleShowFragment();
            fragmentTransaction.show(newsHomeFragment);
        } else {
            newsHomeFragment = new NewsHomeFragment();

            fragmentTransaction.add(R.id.flContainer, newsHomeFragment, MainTabType.TAB_NEWS_HOME);
        }
        fragmentTransaction.commitAllowingStateLoss();

    }

    private void initSkin() {
        try {
            //初始化皮肤
            SkinManager.getInstance().init(this, this.getResources(), this.getAssets());
            SkinManager.getInstance().setApply(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
