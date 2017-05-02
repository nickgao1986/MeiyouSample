package nickgao.com.meiyousample.statusbar;

import java.util.List;
import java.util.Map;

/**
 * Created by gaoyoujian on 2017/5/2.
 */

public class StatusbarConfig {

    //是否启用
    private boolean mIsEnableStatusBar;
    //默认颜色
    private int mDefaultColor;
    //不需要启用的列表
    private List<String> mIgnorePageList;
    //需要特殊颜色的列表
    private Map<String,Integer> mSpecailPageMap;
    //状态栏深色
    private Map<String,Boolean> mSpecailStatusMap;

    public Map<String, Boolean> getSpecailStatusMap() {
        return mSpecailStatusMap;
    }

    public void setSpecailStatusMap(Map<String, Boolean> specailStatusMap) {
        mSpecailStatusMap = specailStatusMap;
    }

    public boolean isEnableStatusBar() {
        return mIsEnableStatusBar;
    }

    public int getDefaultColor() {
        return mDefaultColor;
    }

    public List<String> getIgnorePageList() {
        return mIgnorePageList;
    }

    public Map<String, Integer> getSpecailPageMap() {
        return mSpecailPageMap;
    }

    private StatusbarConfig(Builder builder) {
        mIsEnableStatusBar = builder.mIsEnableStatusBar;
        mDefaultColor = builder.mDefaultColor;
        mIgnorePageList = builder.mIgnorePageList;
        mSpecailPageMap = builder.mSpecailPageMap;
        mSpecailStatusMap = builder.mSpecailStatusMap;
        if(mIsEnableStatusBar && mDefaultColor==0){
            throw new RuntimeException("mDefaultColor must be set if enable statusbar");
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    /**
     * {@code StatusbarConfig} builder static inner class.
     */
    public static final class Builder {
        private boolean mIsEnableStatusBar;
        private int mDefaultColor;
        private List<String> mIgnorePageList;
        private Map<String, Integer> mSpecailPageMap;
        private Map<String,Boolean> mSpecailStatusMap;

        private Builder() {
        }

        /**
         * Sets the {@code mIsEnableStatusBar} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code mIsEnableStatusBar} to set
         * @return a reference to this Builder
         */
        public Builder withIsEnableStatusBar(boolean val) {
            mIsEnableStatusBar = val;
            return this;
        }

        /**
         * Sets the {@code mDefaultColor} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code mDefaultColor} to set
         * @return a reference to this Builder
         */
        public Builder withDefaultColor(int val) {
            mDefaultColor = val;
            return this;
        }

        /**
         * Sets the {@code mIgnorePageList} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code mIgnorePageList} to set
         * @return a reference to this Builder
         */
        public Builder withIgnorePageList(List<String> val) {
            mIgnorePageList = val;
            return this;
        }

        /**
         * Sets the {@code mSpecailPageMap} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code mSpecailPageMap} to set
         * @return a reference to this Builder
         */
        public Builder withSpecailPageMap(Map<String, Integer> val) {
            mSpecailPageMap = val;
            return this;
        }
        public Builder withSpecailStatusMap(Map<String, Boolean> val) {
            mSpecailStatusMap = val;
            return this;
        }
        /**
         * Returns a {@code StatusbarConfig} built from the parameters previously set.
         *
         * @return a {@code StatusbarConfig} built with parameters of this {@code StatusbarConfig.Builder}
         */
        public StatusbarConfig build() {
            return new StatusbarConfig(this);
        }
    }


}
