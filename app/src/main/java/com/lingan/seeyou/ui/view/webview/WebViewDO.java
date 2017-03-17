package com.lingan.seeyou.ui.view.webview;

import java.io.Serializable;

/**
 * Created by lwh on 2015/11/3.
 */
public class WebViewDO implements Serializable{
    private int code;
    private String data;
    private String message;
    private String title;
    private String content;
    private String url;
    private String image_url;
    private int b_count;
    private int currency_task_id;
    private int product_id;

    private int id;
    private int category;
    private String type;
    private String item_id;
    private int shop_type;
    private int quantity;
    private String sku_id;
    private int coin_amount;
    private boolean isListenShare;
    public String mCurrentWebViewUrl;

    private Object mObject;

    public Object getObject() {
        return mObject;
    }

    public void setObject(Object object) {
        mObject = object;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getB_count() {
        return b_count;
    }

    public void setB_count(int b_count) {
        this.b_count = b_count;
    }

    public int getCurrency_task_id() {
        return currency_task_id;
    }

    public void setCurrency_task_id(int currency_task_id) {
        this.currency_task_id = currency_task_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getShop_type() {
        return shop_type;
    }

    public void setShop_type(int shop_type) {
        this.shop_type = shop_type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public int getCoin_amount() {
        return coin_amount;
    }

    public void setCoin_amount(int coin_amount) {
        this.coin_amount = coin_amount;
    }

    public String getCurrentWebViewUrl() {
        return mCurrentWebViewUrl;
    }

    public void setCurrentWebViewUrl(String currentWebViewUrl) {
        mCurrentWebViewUrl = currentWebViewUrl;
    }

    public boolean isListenShare() {
        return isListenShare;
    }

    public void setListenShare(boolean listenShare) {
        isListenShare = listenShare;
    }
}
