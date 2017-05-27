package com.com.meetyou.news.model;

import java.io.Serializable;
import java.util.List;

import nickgao.com.meiyousample.model.NewsReviewReferenceModel;

/**
 * 资讯评论Model
 * Created by huangyuxiang on 2016/11/14.
 */

public class NewsReviewModel implements Serializable {
    public int id;
    public String content;
    public String created_at;
    public int review_count;
    public int praise_count;
    public boolean is_praise;
    public boolean is_author;//是否是作者
    public NewsReviewPublisherModel publisher;
    public NewsReviewReferenceModel reference;
    public NewsDetailBean news_detail;
    public List<NewsReviewModel> sub_review;

    public static class NewsDetailBean {
        public int id;
        public String title;
        public String images;
        public String redirect_url;
    }
}
