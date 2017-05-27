package com.com.meetyou.news.model;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.SeeyouApplication;
import nickgao.com.meiyousample.service.NewsReviewService;

/**
 * 资讯评论详情数据模型
 * Created by LinXin on 2017/2/23.
 */
public class NewsReviewDetailDataModel extends ListModel<NewsReviewDetailModel, NewsReviewModel> {

    private Disposable mDisposable;
    private int reviewId;//评论id
    private int lastId;//最后一条id
    private int pageSize = 20;
    private int gotoId;//跳楼模式，跳到某一楼的id
    private boolean isDownDirection = true;
    private boolean isShowHeader;//视频页面不显示header，资讯评论详情页面显示header。所以用是否显示header来判断要不要显示空界面
    private NewsReviewDetailModel mNewsReviewDetailModel;

    public NewsReviewDetailDataModel(List<NewsReviewModel> newsReviewInfos, int gotoId) {
        super(newsReviewInfos);
        this.gotoId = gotoId;
    }

    @Override
    public List<NewsReviewModel> map(NewsReviewDetailModel response) {
        return response.news_sub_review;
    }

    @Override
    public void setData(boolean isRefreshing, NewsReviewDetailModel response) {
        mNewsReviewDetailModel = response;
        super.setData(isRefreshing, response);
    }

    public void setShowHeader(boolean showHeader) {
        isShowHeader = showHeader;
    }

    @Override
    public boolean isEmpty() {
        if (mNewsReviewDetailModel == null)
            return true;
        if (isShowHeader) {
            return mNewsReviewDetailModel.news_review == null;
        }
        return super.isEmpty();
    }

    @Override
    protected boolean ensureHasNext(NewsReviewDetailModel response, List<NewsReviewModel> mapList) {
        return mapList != null && mapList.size() == pageSize;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
        lastId = 0;
    }

    public void setGotoId(int gotoId) {
        this.gotoId = gotoId;
    }

    private String getLoadDirection() {
        if (isDownDirection) {
            return "next";
        } else {
            return "prev";
        }
    }

    public void load(final OnLoadListener<NewsReviewDetailModel> listener) {
        listener.onStart();
        mDisposable = Flowable.create(new FlowableOnSubscribe<NewsReviewDetailModel>() {
            @Override
            public void subscribe(FlowableEmitter<NewsReviewDetailModel> emitter) throws Exception {
//                final HttpCall<HttpResult> call = mController.getNewsSubReviewList(reviewId, lastId, gotoId, pageSize, getLoadDirection());
//                emitter.setCancellable(new Cancellable() {
//                    @Override
//                    public void cancel() throws Exception {
//                        call.cancel();
//                    }
//                });
//                HttpResult result = call.execute();
//                if (emitter.isCancelled())
//                    return;
         //       NewsReviewDetailModel model = parse(result);
                String result =  NewsReviewService.sendRequest(SeeyouApplication.getContext());
                NewsReviewDetailModel model = parse(result);

               // NewsReviewDetailModel model = null;
                emitter.onNext(model);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NewsReviewDetailModel>() {
                    @Override
                    public void accept(NewsReviewDetailModel entity) throws Exception {
                        listener.onSuccess(entity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listener.onError(throwable);
                    }
                });
    }

    private NewsReviewDetailModel parse(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        int code = jsonObject.getInt("code");
        if (code == 0) {
            String dataString = jsonObject.optString("data");
            if (!StringUtils.isNull(dataString)) {
                return JSON.parseObject(dataString, NewsReviewDetailModel.class);
            }
        }
        throw new HttpStatusErrorException(code);
    }

//    private NewsReviewDetailModel parse(HttpResult httpResult) throws Exception {
//        if (httpResult == null)
//            throw new HttpUnKnowException();
//        if (httpResult.getVolleyError() != null)
//            throw httpResult.getVolleyError();
//        if (!httpResult.isSuccess())
//            throw new HttpFailedException();
//        if (httpResult.getResult() == null)
//            throw new HttpResultNullException();
//        //http请求成功
//        String jsonString = httpResult.getResult().toString();
//        JSONObject jsonObject = new JSONObject(jsonString);
//        int code = jsonObject.getInt("code");
//        if (code == 0) {
//            String dataString = jsonObject.optString("data");
//            if (!StringUtils.isNull(dataString)) {
//                return JSON.parseObject(dataString, NewsReviewDetailModel.class);
//            }
//        }
//        throw new HttpStatusErrorException(code);
//    }

    public void cancel() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Override
    public void preRefresh() {
        lastId = 0;
    }

    @Override
    public void preLoadNext() {
        if (mList.isEmpty()) {
            lastId = 0;
        } else {
            lastId = mList.get(mList.size() - 1).id;
        }
        isDownDirection = true;
    }

    public void setPrevData(NewsReviewDetailModel response) {
        List<NewsReviewModel> mapList = map(response);
        if (mapList == null) {
            return;
        }
        mList.addAll(0, mapList);
    }

    /**
     * 加载上一页
     */
    public void preLoadPrev() {
        if (mList.isEmpty()) {
            lastId = 0;
        } else {
            lastId = mList.get(0).id;
        }
        isDownDirection = false;
    }
}
