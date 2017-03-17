package com.lingan.seeyou.ui.view.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by lwh on 2015/11/3.
 */
public interface WebViewListener {
    /**
     * 处理分享
     * @param activity
     * @param webViewDO
     */
    public boolean handleShare(Activity activity, WebViewDO webViewDO);

    //统计点击我的订单）
    public void reportClickMyOrderEvent();

    //统计点击百川详情页
    public void reportClickBaichuanDetailPage(String productId, String source);

    //绑定淘宝ID
    public void handleBindTaobao(String tb_userid);

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);
    //点击了分享按钮
    public void handleClickShare(Activity activity, WebViewDO webViewDO);

    public void onDestroy();

    public void onCreate(Activity activity, Bundle bundle);
}
