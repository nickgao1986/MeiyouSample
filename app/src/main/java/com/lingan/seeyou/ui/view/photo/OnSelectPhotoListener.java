package com.lingan.seeyou.ui.view.photo;


import com.lingan.seeyou.ui.view.photo.model.PhotoModel;

import java.util.List;

/**
 * 图片选择器回调
 *
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 16/9/22 上午9:55
 */
public interface OnSelectPhotoListener {
    /**
     * 图片选择取消
     */
    void onCancel();

    /**
     * 选中后马上回调
     *
     * @param listSelected
     */
    public void onResultSelect(List<PhotoModel> listSelected);

    /**
     * 选中后，再回调onResultSelect后进行压缩，压缩后返回图片路径
     *
     * @param compressPath
     */
    public void onResultSelectCompressPath(List<String> compressPath);

}
