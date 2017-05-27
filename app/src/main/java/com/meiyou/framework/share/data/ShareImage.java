package com.meiyou.framework.share.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxd on 16/7/18.
 */
public class ShareImage extends ShareMediaInfo {
    /**
     * 分享的图片链接
     * 支持 scheme :  http://   or file://
     */
    private List<String> imageList = new ArrayList<>();
    /**
     * 本地照片id ,R.drawble.icon
     */
    private int localImage = -1;



    public void setImageUrl(String image) {
        this.imageList.add(0, image);
    }

    public void setImage(List<String> imageList) {
        if (imageList != null && !imageList.isEmpty()) {
            this.imageList.addAll(imageList);
        }
    }

    public int getLocalImage() {
        return localImage;
    }

    public void setLocalImage(int localImage) {
        this.localImage = localImage;
    }

    public boolean hasLocalImage() {
        return localImage > 0;
    }

    @Override
    public TYPE getType() {
        return TYPE.IMAGE;
    }

    public String getImageUrl() {
        return imageList.size() > 0 ? imageList.get(0) : "";
    }
}
