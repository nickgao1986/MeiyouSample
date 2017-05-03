package com.lingan.seeyou.ui.view.photo;


import com.lingan.seeyou.ui.view.photo.model.PreviewImageModel;

import java.util.List;

/**
 * 大图预览配置
 *
 * @author zhengxiaobin@xiaoyouzi.com
 * @since 17/3/13 下午2:35
 */
public class PreviewUiConfig {
    public static final int PAGE_INDICATOR_TEXT = 0;
    public static final int PAGE_INDECATOR_CIRCLE = 1;
    //是否显示标题
    public boolean bShowTextTitle;
    //是否隐藏状态栏，
    public boolean bHideStausBar;
    /**
     * 是否显示 标题栏；要有保存菜单， 这个值必须是TRUE
     */
    public boolean bHideTitleBar;
    //是否隐藏 右边图标
    public boolean bHideRightIcon;
    public int pageIndicatorMode = PAGE_INDECATOR_CIRCLE;
    /**
     * 进入模式，
     * {@link PreviewImageActivity#MODE_DELETE}
     */
    public int mode;
    //预览数据源
    public List<PreviewImageModel> list;
    //默认初始位置
    public int position;
    public PreviewImageActivity.OnOperationListener listener;

    /**
     * @param list
     * @param position
     */
    public PreviewUiConfig(int mode, List<PreviewImageModel> list, int position, PreviewImageActivity.OnOperationListener listener) {
        this.mode = mode;
        this.list = list;
        this.position = position;
        this.listener = listener;
    }

}
