package com.meiyou.framework.share;

import android.app.Activity;
import android.text.TextUtils;

import com.meiyou.framework.share.data.BaseShareInfo;
import com.meiyou.framework.share.data.ShareActionConf;
import com.meiyou.framework.share.data.ShareImage;
import com.meiyou.framework.share.data.ShareVideo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.SimpleShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.skin.ToastUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hxd on 15/7/28.
 */
public abstract class ShareItemController {
    protected static final String sTAG = "ShareItemController";
    protected BaseShareInfo shareInfoDO;
    protected WeakReference<Activity> activity;
    protected Activity context;
    protected List<SoftReference<ShareResultCallback>> mCallbackList;
    protected ShareResultCallback mDefaultCallback;

    public ShareItemController(Activity activity, BaseShareInfo shareInfoDO) {
        this.shareInfoDO = shareInfoDO;
        this.activity = new WeakReference<>(activity);
        this.context = this.activity.get();
        this.mCallbackList = new ArrayList<>();
        if (shareInfoDO.getActionConf().isUseDefaultCallback()) {
            mDefaultCallback = new ShareResultCallback() {
                @Override
                public void onStart(ShareType shareType) {

                }

                @Override
                public void onSuccess(ShareType shareType) {

                }

                @Override
                public void onFailed(ShareType shareType, int code, String error) {

                }

                @Override
                public void onEditViewDisappear(ShareType shareType) {

                }
            };
            mCallbackList.add(new SoftReference<>(mDefaultCallback));
        }

    }

    abstract public ShareType getShareType();

    public BizResult<Boolean> beforeShare() {
        BizResult<Boolean> result = new BizResult<>();
        result.setSuccess(true);
        result.setResult(true);
        return result;
    }

    public final void startShare() {
        try {
            BizResult<Boolean> before = beforeShare();
            if (before != null && before.isSuccess() &&
                    before.getResult() != null && before.getResult()) {
                BizResult<String> result = null;
                //即便是默认弹出编辑框的 分享类型比如新浪微博,也允许定制 是否要弹出编辑框
                boolean actionDriven = actionDriven();
                if (shareInfoDO.getActionConf().getEditViewMode() != ShareActionConf.EDIT_VIEW_MODE.DEFAULT) {
                    actionDriven = shareInfoDO.getActionConf().getEditViewMode() == ShareActionConf.EDIT_VIEW_MODE.USE;
                }
                /**
                 * 可惜不能传函数指针，无法将这里的if-else 多态化
                 */
                if (actionDriven) {
                    result = startShareView();
                    if (result != null && !result.isSuccess()) {
                        LogUtils.e("result failed " + result.getErrorMsg());
                        return;
                    }
                } else {
                    result = doShare();
                    if (result != null && !result.isSuccess()) {
                        LogUtils.e("result failed " + result.getErrorMsg());
                        return;
                    }
                }

            } else {
                final  int hashCode = getLastCallBackHashCode();
                for (SoftReference<ShareResultCallback> cbRef : mCallbackList) {
                    ShareResultCallback callback = cbRef.get();
                    if (callback != null) {
                        if (before != null) {
                            callback.onFailed(getShareType().setHashCode(hashCode), before.getErrorCode(), before.getErrorMsg());
                        } else {
                            callback.onFailed(getShareType().setHashCode(hashCode), 0, "");
                        }

                    }
                }
                if (before != null) {
                    ToastUtils.showToast(context, before.getErrorMsg());
                }

            }
        } catch (Exception e) {
            LogUtils.e(e.getLocalizedMessage());
        }

    }

    public BaseShareInfo getShareInfoDO() {
        return shareInfoDO;
    }


    protected abstract SimpleShareContent genShareContent();

    public abstract boolean actionDriven();

    protected abstract BizResult<String> startShareView();


    protected boolean needAuth() {
        return false;
    }

    public void startShareWithCheckAuth() {

        startShare();
    }



    protected UMShareAPI getUMSocialService() {
        return SocialService.getInstance().getUMSocialService();
    }


    public List<SoftReference<ShareResultCallback>> getCallbackList() {
        return this.mCallbackList;
    }

    /**
     * 注意:为了防止ShareItemController导致内存泄漏
     * 这里是以弱引用的方式 add 到mCallbackList
     * 如果你是 new ShareResultCallback 匿名类对象 ,
     * ShareResultCallback 是否被回调将取决于gc工作的时机,
     * 而这不是你想要的结果,建议自己持有 ShareResultCallback
     *
     * @param callback
     */
    public void addCallback(ShareResultCallback callback) {
        if (callback == null)
            return;
        mCallbackList.add(new SoftReference<ShareResultCallback>(callback));
    }

    public void removeCallback(ShareResultCallback callback) {
        for (SoftReference<ShareResultCallback> tmp : mCallbackList) {
            ShareResultCallback cb = tmp.get();
            if (cb != null && cb == callback) {
                mCallbackList.remove(tmp);
            }
        }
    }

    /**
     * 执行分享
     *
     * @return
     */

    /**
     * 执行分享
     *
     * @return
     */
    protected BizResult<String> doShare() {

        if (getShareType() == ShareType.SINA) {
            SocialService.getInstance().getPlatformInfo(context, ShareType.SINA, new GetDataListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onComplete(int i, Map<String, String> map) {
                    if ((map == null) || map.isEmpty() || (shareInfoDO == null)) {
                        runShare();
                    } else {
                        String longUrl = shareInfoDO.getUrl();
                        String accessToken = map.get("access_token");
                        if (TextUtils.isEmpty(longUrl) || TextUtils.isEmpty(accessToken)) {
                            runShare();
                        } else {
                            HttpUrl url = new HttpUrl.Builder().scheme("https")
                                    .host("api.weibo.com")
                                    .addPathSegments("/2/short_url/shorten.json")
                                    .addQueryParameter("access_token", accessToken)
                                    .addQueryParameter("url_long", longUrl)
                                    .build();
                            Request request = new Request.Builder().url(url).get().build();
                            Call call = new OkHttpClient().newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runShare();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        String responseContent = response.body().string();
                                        if (!TextUtils.isEmpty(responseContent)) {
                                            try {
                                                JSONObject json = new JSONObject(responseContent);
                                                JSONArray jsonArray = json.getJSONArray("urls");
                                                if (jsonArray != null) {
                                                    int length = jsonArray.length();
                                                    if (length > 0) {
                                                        JSONObject jsonItem = jsonArray.getJSONObject(0);
                                                        if (jsonItem.getBoolean("result")) {
                                                            String shortUrl = jsonItem.getString("url_short");
                                                            if (!TextUtils.isEmpty(shortUrl)) {
                                                                shareInfoDO.setUrl(shortUrl);
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (JSONException e) {

                                            }
                                        }
                                    }
                                    runShare();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onError(int code, String message) {

                }

                @Override
                public void onCancle() {

                }
            });
        } else {
            runShare();
        }
        BizResult<String> result = new BizResult<>();
        result.setSuccess(true);
        return result;
    }

    private int getLastCallBackHashCode(){
        int hashCode = 0;
        if(mCallbackList!=null && mCallbackList.size()>0){
            ShareResultCallback callback = mCallbackList.get(mCallbackList.size()-1).get();
            if(callback!=null)
                hashCode = callback.hashCode();
        }
        return hashCode;
    }
    //开始分享
    private void runShare() {

        SimpleShareContent shareContent = genShareContent();
        ShareAction shareAction = new ShareAction(context);
        //图片
        if (shareContent.getImage() != null) {
            shareAction.withMedia(shareContent.getImage());
        }
        //音乐
        if (shareContent.getMusic() != null) {
            shareAction.withMedia(shareContent.getMusic());
        }
        //视频
        if (shareContent.getVideo() != null) {
            shareAction.withMedia(shareContent.getVideo());
        }
        //标题
        if (!StringUtils.isNull(shareContent.getTitle())) {
            shareAction.withTitle(shareContent.getTitle());
        }
        //内容
        if (!StringUtils.isNull(shareContent.getText())) {
            shareAction.withText(shareContent.getText());
        }
        //目标链接
        if (!StringUtils.isNull(shareContent.getTargeturl())) {
            shareAction.withTargetUrl(shareContent.getTargeturl());
        }
        //兼容新浪直接分享,不会携带targeturl的问题
        if(getShareType() == ShareType.SINA && shareInfoDO.isDirectShare()){
            shareAction.withText(shareContent.getText()+shareContent.getTargeturl());
            shareContent.setText(shareContent.getText()+shareContent.getTargeturl());
        }
        final  int hashCode = getLastCallBackHashCode();
        shareAction.setPlatform(getShareType().getShareMedia()).setCallback(new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA share_media) {
                for (SoftReference<ShareResultCallback> cbRef : mCallbackList) {
                    ShareResultCallback callback = cbRef.get();
                    if (callback != null) {
                        callback.onSuccess(getShareType().setHashCode(hashCode));
                    }
                }
                ToastUtils.showToast(context, "分享成功");
                ShareListController.invokeInterceptorAfter(getShareType().setHashCode(hashCode),shareInfoDO,ShareResult.SUCCESS);
                ShareListController.invokeSyncInterceptorAfter(getShareType().setHashCode(hashCode),shareInfoDO,ShareResult.SUCCESS);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                try {
                    ToastUtils.showToast(context, "分享失败 " + throwable == null ? "" : throwable.getMessage());
                    ShareListController.invokeInterceptorAfter(getShareType().setHashCode(hashCode),shareInfoDO,ShareResult.FAIL);
                    ShareListController.invokeSyncInterceptorAfter(getShareType().setHashCode(hashCode),shareInfoDO,ShareResult.FAIL);
                    for (SoftReference<ShareResultCallback> cbRef : mCallbackList) {
                        ShareResultCallback callback = cbRef.get();
                        if (callback != null) {
                            callback.onFailed(getShareType().setHashCode(hashCode), -1, throwable == null ? "" : throwable.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtils.showToast(context, "分享被取消");
                ShareListController.invokeInterceptorAfter(getShareType().setHashCode(hashCode),shareInfoDO,ShareResult.CANCLE);
                ShareListController.invokeSyncInterceptorAfter(getShareType().setHashCode(hashCode),shareInfoDO,ShareResult.CANCLE);
                for (SoftReference<ShareResultCallback> cbRef : mCallbackList) {
                    ShareResultCallback callback = cbRef.get();
                    if (callback != null) {
                        callback.onFailed(getShareType().setHashCode(hashCode), -1, "分享被取消");
                    }
                }
            }

        }).share();
    }

    protected SimpleShareContent setShareMediaInfo(SimpleShareContent baseShareContent) {
        if (shareInfoDO.getShareMediaInfo() == null) {
            return baseShareContent;
        }
        switch (shareInfoDO.getShareMediaInfo().getType()) {
            case IMAGE:
                setImage(baseShareContent);
                break;
            case VIDEO:
                setVideo(baseShareContent);
                break;
        }

        return baseShareContent;
    }

    private SimpleShareContent setVideo(SimpleShareContent baseShareContent) {
        if ((shareInfoDO.getShareMediaInfo() == null) ||
                !(shareInfoDO.getShareMediaInfo() instanceof ShareVideo)) {
            return baseShareContent;
        }
        ShareVideo shareVideoInfoDO = (ShareVideo) shareInfoDO.getShareMediaInfo();
        if (shareVideoInfoDO == null) {
            return baseShareContent;
        }
        if (StringUtils.isNotEmpty(shareVideoInfoDO.getVideoUrl())) {
            UMVideo umVideo = new UMVideo(shareVideoInfoDO.getVideoUrl());
            if (StringUtils.isNotEmpty(shareVideoInfoDO.getThumbUrl())) {
                umVideo.setThumb(shareVideoInfoDO.getThumbUrl());
            }
            umVideo.setTargetUrl(shareVideoInfoDO.getVideoUrl());
            umVideo.setTitle(shareInfoDO.getTitle());

            baseShareContent.setVideo(umVideo);
        }
        return baseShareContent;
    }

    protected SimpleShareContent setImage(SimpleShareContent baseShareContent) {
        if ((shareInfoDO.getShareMediaInfo() == null) ||
                !(shareInfoDO.getShareMediaInfo() instanceof ShareImage)) {
            return baseShareContent;
        }
        UMImage image = null;
        ShareImage shareImageInfoDO = (ShareImage) shareInfoDO.getShareMediaInfo();
        if (shareImageInfoDO == null) {
            return baseShareContent;
        }
        if (StringUtils.isNotEmpty(shareImageInfoDO.getImageUrl())) {
            image = new UMImage(context, shareImageInfoDO.getImageUrl());
            if (StringUtils.startsWithIgnoreCase(shareImageInfoDO.getImageUrl(), "http"))
                image.setTargetUrl(shareImageInfoDO.getImageUrl());
        }
        if (shareImageInfoDO.hasLocalImage()) {
            image = processLocalImage(shareImageInfoDO.getLocalImage());
        }
        if (image != null) {
            baseShareContent.setImage(image);
        }
        return baseShareContent;
    }

    //wx 要自己搞
    protected UMImage processLocalImage(int id) {
        return new UMImage(context, id);
    }

}
