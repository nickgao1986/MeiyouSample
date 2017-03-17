package com.lingan.seeyou.ui.view.webview;

/**
 * Created by lwh on 2015/11/3.
 */
public class WebViewConfig {

    //默认分享图片
    private String defaultShareImageUrl="";
    //默认分享图标
    private String defaultShareIconUrl="";

    private int mThemeId;

    //是否追加UserAgent
    private boolean isAppendUserAgent = false;

    private WebViewConfig(Builder builder) {
        setDefaultShareImageUrl(builder.defaultShareImageUrl);
        setDefaultShareIconUrl(builder.defaultShareIconUrl);
        setThemeId(builder.mThemeId);
        setAppendUserAgent(builder.isAppendUserAgent);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getDefaultShareImageUrl() {
        return defaultShareImageUrl;
    }

    public void setDefaultShareImageUrl(String defaultShareImageUrl) {
        this.defaultShareImageUrl = defaultShareImageUrl;
    }

    public String getDefaultShareIconUrl() {
        return defaultShareIconUrl;
    }

    public void setDefaultShareIconUrl(String defaultShareIconUrl) {
        this.defaultShareIconUrl = defaultShareIconUrl;
    }

    public int getThemeId() {
        return mThemeId;
    }

    public void setThemeId(int themeId) {
        mThemeId = themeId;
    }

    public boolean isAppendUserAgent() {
        return isAppendUserAgent;
    }

    public void setAppendUserAgent(boolean appendUserAgent) {
        isAppendUserAgent = appendUserAgent;
    }


    /**
     * {@code WebViewConfig} builder static inner class.
     */
    public static final class Builder {
        private String defaultShareImageUrl;
        private String defaultShareIconUrl;
        private int mThemeId;
        private boolean isAppendUserAgent;

        private Builder() {
        }

        /**
         * Sets the {@code defaultShareImageUrl} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code defaultShareImageUrl} to set
         * @return a reference to this Builder
         */
        public Builder withDefaultShareImageUrl(String val) {
            defaultShareImageUrl = val;
            return this;
        }

        /**
         * Sets the {@code defaultShareIconUrl} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code defaultShareIconUrl} to set
         * @return a reference to this Builder
         */
        public Builder withDefaultShareIconUrl(String val) {
            defaultShareIconUrl = val;
            return this;
        }

        /**
         * Sets the {@code mThemeId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code mThemeId} to set
         * @return a reference to this Builder
         */
        public Builder withThemeId(int val) {
            mThemeId = val;
            return this;
        }

        /**
         * Sets the {@code isAppendUserAgent} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code isAppendUserAgent} to set
         * @return a reference to this Builder
         */
        public Builder withIsAppendUserAgent(boolean val) {
            isAppendUserAgent = val;
            return this;
        }

        /**
         * Returns a {@code WebViewConfig} built from the parameters previously set.
         *
         * @return a {@code WebViewConfig} built with parameters of this {@code WebViewConfig.Builder}
         */
        public WebViewConfig build() {
            return new WebViewConfig(this);
        }
    }
}
