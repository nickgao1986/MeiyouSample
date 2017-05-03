package com.lingan.seeyou.ui.view.photo.model;

import android.view.View;

/**
 * 图片选择器 参数配置
 *
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 16/9/21 下午6:02
 */
public class PhotoConfig {
    /**
     * 扩展的图片选择器文字
     */
    public String mMenuItem;
    public View.OnClickListener mMenuItemListener;
    /**
     * 可以选择的最大数量
     */
    public int mMaxCount;
    /**
     * 是否进行裁剪
     */
    public boolean mCrop;
    public long mUserId;
    /**
     * 是否显示标题
     */
    public boolean isShowTitle;
    public String title = "";

    //图片名称前缀
    public String mPrefix;
    /**
     * 配置是否生效,遗留参数
     */
    public boolean mEnablePhotoOption = true;
    /**
     * 是否直接进入图片选择页面，不出现 "拍照"的菜单选项；
     */
    public boolean isOnlyEnableGallery = false;

    public String getMenuItem() {
        return mMenuItem;
    }

    public View.OnClickListener getMenuItemListener() {
        return mMenuItemListener;
    }

    public int getMaxCount() {
        return mMaxCount;
    }

    public boolean isCrop() {
        return mCrop;
    }

    public long getUserId() {
        return mUserId;
    }

    public boolean isEnablePhotoOption() {
        return mEnablePhotoOption;
    }

    public PhotoConfig() {
    }

    /**
     * @param mMaxCount 最大可选择图片
     * @param mCrop     是否裁剪
     * @param mUserId
     */
    public PhotoConfig(int mMaxCount, boolean mCrop, long mUserId) {
        this.mMaxCount = mMaxCount;
        this.mCrop = mCrop;
        this.mUserId = mUserId;
    }

    public PhotoConfig(int mMaxCount, boolean mCrop, long mUserId, String prefix) {
        this.mMaxCount = mMaxCount;
        this.mCrop = mCrop;
        this.mUserId = mUserId;
        this.mPrefix = prefix;
    }

}
