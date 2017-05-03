package com.lingan.seeyou.ui.view.photo;

/**
 * 压缩回调
 * 会把图片压缩 到 PKGNAME_BitmapCache里面后回调
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 16/10/14 上午9:54
 */
public interface OnSaveBitmapListListener {
    /**
     * 
     * @param success
     * @param position
     * @param imagePath 压缩后的图片名字,
     */
    public void OnSaveResult(boolean success, int position, String imagePath);
}
