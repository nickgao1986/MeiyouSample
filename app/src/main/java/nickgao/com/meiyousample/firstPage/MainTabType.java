package nickgao.com.meiyousample.firstPage;

/**
 * Created by gaoyoujian on 2017/4/21.
 */

public class MainTabType {

    public static final String TAB_HOME = "/home";
    public static final String TAB_NEWS_HOME = "/newshome";  //NewsHomeFragment
    public static final String TAB_PREGNANCY_HOME = "/pregnancyhome";
    public static final String TAB_CALENDAR = "/record";  //DynamicHomeFragment
    public static final String TAB_COMMUNITY = "/circles";  //PersonalFragment
    public static final String TAB_TODAYSALE = "/sale";  //AddFriendFragment
    public static final String TAB_MINE = "/mine";  //MyFragment

    private static MainTabType mInstance;

    public static MainTabType getInstance() {
        if (mInstance == null) {
            mInstance = new MainTabType();
        }
        return mInstance;
    }

    public MainTabType() {

    }

    /**
     * 根据tab key取出位置
     *
     * @param tag
     * @return
     */
    public static int getPositionByTag(String tag) {
        if (tag.trim().equals(TAB_HOME) || tag.trim().equals(TAB_PREGNANCY_HOME)) {
            return 0;
        } else if (tag.trim().equals(TAB_CALENDAR)) {
            return 1;
        } else if (tag.trim().equals(TAB_COMMUNITY)) {
            return 2;
        } else if (tag.trim().equals(TAB_TODAYSALE)) {
            return 3;
        } else if (tag.trim().equals(TAB_MINE)) {
            return 4;
        }
        return -1;
    }


}
