package nickgao.com.meiyousample.firstPage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaoyoujian on 2017/4/24.
 */

public class NewsHomePagerAdapter extends FragmentStatePagerAdapter {
    private Map<Integer, NewsHomeClassifyFragment> classifyFragments = new HashMap<>();
    private Map<Integer, Boolean> classifyFragmentsIsLoadingNetDatas;
    private Map<Integer, Integer> mRoundLists = new HashMap<>();
    private List<HomeClassifyModel> classifyModels;
    private int currentSelectedPage;
    private NewsHomePagerAdapter homePagerAdapter;
    private int mCurrentPosition;//当前分类页面

    public NewsHomePagerAdapter(FragmentManager fm, List<HomeClassifyModel> classifyModels, Map<Integer, Boolean> classifyFragmentsIsLoadingNetDatas, Map<Integer, Integer> roundLists) {
        super(fm);
        this.classifyModels = classifyModels;
        this.classifyFragmentsIsLoadingNetDatas = classifyFragmentsIsLoadingNetDatas;
        this.mRoundLists = roundLists;
    }



    @Override
    public Fragment getItem(int position) {
        HomeClassifyModel model = classifyModels.get(position);
        if (mRoundLists.get(model.getCatid()) == null) {
            mRoundLists.put(model.getCatid(), 0);
        }
        return NewsHomeClassifyFragment.newInstance(model.getCatid(), model.getName(), position, currentSelectedPage, classifyFragmentsIsLoadingNetDatas.get(model.catid), mRoundLists.get(model.getCatid()));
    }

    public void changeCurrentSelectPage(int currentSelectedPage) {
        this.currentSelectedPage = currentSelectedPage;
    }

    @Override
    public int getCount() {
        return classifyModels.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        NewsHomeClassifyFragment classifyFragment = (NewsHomeClassifyFragment) super.instantiateItem(container, position);
        classifyFragments.put(position, classifyFragment);
        return classifyFragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        classifyFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return classifyModels.get(position).getName();
    }

    public NewsHomeClassifyFragment getPositionFragment() {
        Fragment mPositionFragment = getCurrentFragment();
        if (mPositionFragment != null) {
            return (NewsHomeClassifyFragment) mPositionFragment;
        }
        return null;
    }

    public void setLoadingNetData(Map<Integer, Boolean> classifyFragmentsIsLoadingNetDatas) {
        this.classifyFragmentsIsLoadingNetDatas = classifyFragmentsIsLoadingNetDatas;
    }

    public void setRoundData(Map<Integer, Integer> roundData) {
        this.mRoundLists = roundData;
    }

    /**
     * 获取当前页面的fragment
     *
     * @return
     */
    public Fragment getCurrentFragment() {
        Fragment mCurrentPrimaryItem = null;
        try {
            Field e = FragmentStatePagerAdapter.class.getDeclaredField("mCurrentPrimaryItem");
            e.setAccessible(true);
            Object val = e.get(this);
            mCurrentPrimaryItem = (Fragment) val;
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return mCurrentPrimaryItem;
    }



}
