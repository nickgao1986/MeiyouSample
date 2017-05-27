package com.com.meetyou.news.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 资讯话题详情Model
 * Created by huangyuxiang on 2016/11/15.
 */

public class NewsReviewDetailModel implements Serializable {
    public NewsReviewModel news_review; //主评论
    public List<NewsReviewModel> news_sub_review = new ArrayList<>(); //子评论
}
