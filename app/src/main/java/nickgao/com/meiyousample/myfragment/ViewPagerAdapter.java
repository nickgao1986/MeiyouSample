package nickgao.com.meiyousample.myfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by gaoyoujian on 2017/6/5.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> listFragment;
    private List<String> titleList;

    public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> listFragment, List<String> titleList) {
        super(fragmentManager);
        this.listFragment = listFragment;
        this.titleList = titleList;
    }


    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
