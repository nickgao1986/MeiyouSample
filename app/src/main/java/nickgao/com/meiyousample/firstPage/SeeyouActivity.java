package nickgao.com.meiyousample.firstPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

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

}
