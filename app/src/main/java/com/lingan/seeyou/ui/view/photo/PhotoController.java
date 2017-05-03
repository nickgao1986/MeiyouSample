//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lingan.seeyou.ui.view.photo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.CursorJoiner.Result;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.lingan.seeyou.ui.view.OnSaveBitmapListener;
import com.lingan.seeyou.ui.view.photo.model.BucketModel;
import com.lingan.seeyou.ui.view.photo.model.PhotoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import biz.threadutil.ThreadUtil;
import biz.util.BitmapUtil;
import de.greenrobot.event.EventBus;
import nickgao.com.meiyousample.controller.LinganController;
import nickgao.com.meiyousample.skin.CacheDisc;
import nickgao.com.meiyousample.utils.FileUtils;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;


public class PhotoController extends LinganController {
    private static final String TAG = "PhotoController";
    private Context mContext;
    private boolean mInited = false;
    private List<PhotoModel> mPhotos = null;
    private List<PhotoModel> mSelected = null;
    private List<BucketModel> mBuckets = null;
    private ContentResolver mResolver = null;
    private int mCount = 0;
    private boolean mHaveAddPhoto = false;
    private final long mRecentScope = 259200000L;
    private long mTimeStampToday = Calendar.getInstance().getTimeInMillis() / 1000L;
    private Uri mUriExternal;
    private Uri mUriInternal;
    public static long RECENT_BUCKET_ID = 101010101010L;
    private int mMaxSelect;
    private boolean mSingleCropable;
    private OnFinishPickingListener mListener;
    private OnDataChangeListener mDCListener;
    private OnDetectListner mDetectListener;
    private static String[] projection = new String[]{"_id", "_data", "title", "bucket_id", "bucket_display_name", "date_added"};
    private static String[] projectionThumb = new String[]{"_id", "image_id", "_data"};
    private boolean requestRefresh;
    private ContentObserver contentObserver;

    static class Holder {
        static PhotoController instance = new PhotoController();
    }

    public static PhotoController getInstance(Context context) {
        return Holder.instance;
    }
    public static PhotoController getInstance() {
        return Holder.instance;
    }
    public PhotoController() {

    }

    /**
     * 初始化相册
     *
     * @param context
     */
    public void init(Context context) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("PhotoController must init at Main Thread");
        }
        if (mInited)
            return;
        mInited = true;
        mContext = context;
        this.mUriExternal = Media.EXTERNAL_CONTENT_URI;
        this.mUriInternal = Media.INTERNAL_CONTENT_URI;
        this.mMaxSelect = 9;
        this.mSingleCropable = false;
        this.requestRefresh = false;
        registerPhotoObserver();
        initPhotoDatas();
    }

    private void initPhotoDatas() {
        if (null == this.mPhotos) {
            LogUtils.d(TAG, "initPhotoDatas method-->rebuildData");
            rebuildData(null);
        }
    }

    //相冊监听
    private void registerPhotoObserver() {
        try {
            contentObserver = new ContentObserver(new Handler()) {
                public void onChange(boolean selfChange) {
                    if (!requestRefresh && !mHaveAddPhoto) {
                        requestRefresh = true;
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                LogUtils.d(TAG, "onChange1-->rebuildData");
                                rebuildData(null);
                                requestRefresh = false;
                                if (mDetectListener != null) {
                                    mDetectListener.onChange();
                                }
                            }
                        }, 3000L);
                    }
                }

                public void onChange(boolean selfChange, Uri uri) {
                    if (!requestRefresh && !mHaveAddPhoto) {
                        requestRefresh = true;
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                LogUtils.d(TAG, "onChange2-->rebuildData");
                                rebuildData(null);
                                requestRefresh = false;
                                if (mDetectListener != null) {
                                    mDetectListener.onChange();
                                }
                            }
                        }, 3000L);
                    }
                }
            };
            mResolver = mContext.getContentResolver();
            mResolver.registerContentObserver(Media.EXTERNAL_CONTENT_URI, true, contentObserver);
            mResolver.registerContentObserver(Media.INTERNAL_CONTENT_URI, true, contentObserver);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //不进行监听
    private void unRegisterObserver() {
        try {
            if (null != mResolver) {
                mResolver.unregisterContentObserver(contentObserver);
                mResolver.unregisterContentObserver(contentObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private interface OnResultListener {
        public void onResult();
    }

    private void rebuildData(final OnResultListener listener) {
        LogUtils.d(TAG, "rebuildData");
        mPhotos = new ArrayList();
        mBuckets = new ArrayList();
        /*this.mPhotos.addAll(this.getExternalDataWithThumbnail());
        this.mPhotos.addAll(this.getInternalDataWithThumbnail());*/
        ThreadUtil.addTaskForIO(mContext, "", new ThreadUtil.ITasker() {
            @Override
            public Object onExcute() {
                return queryPhotoDatasFromDB();
            }

            @Override
            public void onFinish(Object result) {
                if (result != null) {
                    List<PhotoModel> list = (List<PhotoModel>) result;
                    mPhotos.addAll(list);
                }
                if (mSelected == null)
                    mSelected = new ArrayList();
                if (mBuckets.size() > 0 && getRecentPhotos().size() > 0) {
                    BucketModel bucketRecent = new BucketModel(RECENT_BUCKET_ID, "最近", ((PhotoModel) getRecentPhotos()
                            .get(0)).Url, getRecentModelCount());
                    mBuckets.add(bucketRecent);
                }
                if (listener != null) {
                    listener.onResult();
                }
            }
        });
    }

    public void addNewPhoto(PhotoModel photoModel) {
        try {
            photoModel.IsRecent = true;
            photoModel.BucketId = RECENT_BUCKET_ID;
            if (this.mPhotos.indexOf(photoModel) == -1) {
                this.mPhotos.add(0, photoModel);
            }

            if (this.mSelected.indexOf(photoModel) == -1) {
                this.mSelected.add(0, photoModel);
            }

            if (this.mDCListener != null) {
                this.mDCListener.OnChange();
            }
            mHaveAddPhoto = true;
            LogUtils.d(TAG, "addNewPhoto rebuildData:" + photoModel.BucketId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<PhotoModel> queryPhotoDatasFromDB() {
        List<PhotoModel> list = new ArrayList<>();
        try {
            list.addAll(getExternalDataWithThumbnail());
            list.addAll(getInternalDataWithThumbnail());
        } catch (Exception var2) {
            var2.printStackTrace();
        }
        return list;
    }

    public void removeFromSelection(long id) {
        PhotoModel p = new PhotoModel();
        p.Id = id;
        if (null != this.mSelected && this.mSelected.size() > 0 && this.mSelected.indexOf(p) != -1) {
            this.mSelected.remove(this.mSelected.indexOf(p));
        }

    }

    public void setSelection(long[] selection) {
        if (null != selection && selection.length > 0) {
            long[] arr$ = selection;
            int len$ = selection.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                long i = arr$[i$];
                PhotoModel p = new PhotoModel();
                p.Id = i;
                if (null != this.mPhotos && this.mPhotos.indexOf(p) != -1) {
                    this.selectItem((PhotoModel) this.mPhotos.get(this.mPhotos.indexOf(p)));
                }
            }
        }

    }

    private List<PhotoModel> getExternalDataWithThumbnail() {
        ArrayList mData = new ArrayList();
        Cursor cursorPhoto = this.mResolver.query(this.mUriExternal, projection, (String) null, (String[]) null, "date_modified desc");
        Cursor cursorThumb = this.mResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projectionThumb, (String) null, (String[]) null, "_id desc");
        CursorJoiner cursorJoiner = new CursorJoiner(cursorPhoto, new String[]{"_id"}, cursorThumb, new String[]{"image_id"});
        Iterator i$ = cursorJoiner.iterator();

        while (i$.hasNext()) {
            Result joinerResult = (Result) i$.next();

            try {
                File exception = new File(cursorPhoto.getString(1));
                if (exception.exists()) {
                    boolean recent;
                    BucketModel bucketModel;
                    Cursor curCount;
                    if (joinerResult == Result.BOTH) {
                        recent = false;
                        String photoModel = cursorThumb.getString(2);
                        if (Math.abs(this.mTimeStampToday - cursorPhoto.getLong(5)) < 259200000L) {
                            recent = true;
                        }

                        bucketModel = new BucketModel(cursorPhoto.getLong(3), cursorPhoto.getString(4), cursorPhoto
                                .getString(1));
                        if (this.mBuckets.indexOf(bucketModel) == -1) {
                            curCount = this.mResolver.query(this.mUriExternal, projection, "bucket_id = ?", new String[]{String.valueOf(cursorPhoto
                                    .getLong(3))}, (String) null);
                            bucketModel.PhotoCount = curCount.getCount();
                            curCount.close();
                            this.mBuckets.add(bucketModel);
                        }

                        File curCount1 = new File("file://" + photoModel);
                        if (!curCount1.exists()) {
                            photoModel = null;
                        }

//                        long times = cursorPhoto.getLong(cursorPhoto
//                                .getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                        PhotoModel photoModel1 = new PhotoModel(cursorPhoto.getLong(0), cursorPhoto.getLong(3), cursorPhoto.getString(2), cursorPhoto.getString(1), recent, photoModel);

                        photoModel1.setTime(cursorPhoto.getLong(5));
                        if (mData.indexOf(photoModel1) == -1) {
                            mData.add(photoModel1);
                        }
                    } else {
                        recent = false;
                        if (Math.abs(this.mTimeStampToday - cursorPhoto.getLong(5)) < 259200000L) {
                            recent = true;
                        }

                        PhotoModel photoModel2 = new PhotoModel(cursorPhoto.getLong(0), cursorPhoto.getLong(3), cursorPhoto
                                .getString(2), cursorPhoto.getString(1), recent, cursorPhoto.getString(1));
                        photoModel2.setTime(cursorPhoto.getLong(5));
                        if (mData.indexOf(photoModel2) == -1) {
                            mData.add(photoModel2);
                        }

                        bucketModel = new BucketModel(cursorPhoto.getLong(3), cursorPhoto.getString(4), cursorPhoto
                                .getString(1));
                        if (this.mBuckets.indexOf(bucketModel) == -1) {
                            curCount = this.mResolver.query(this.mUriExternal, projection, "bucket_id = ?", new String[]{String.valueOf(cursorPhoto
                                    .getLong(3))}, (String) null);
                            bucketModel.PhotoCount = curCount.getCount();
                            curCount.close();
                            this.mBuckets.add(bucketModel);
                        }
                    }

                    if (cursorPhoto.isLast()) {
                        break;
                    }
                }
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        }

        cursorThumb.close();
        cursorPhoto.close();
        return mData;
    }

    private List<PhotoModel> getInternalDataWithThumbnail() {
        ArrayList mData = new ArrayList();
        Cursor cursorPhoto = this.mResolver.query(this.mUriInternal, projection, (String) null, (String[]) null, "date_modified desc");
        Cursor cursorThumb = this.mResolver.query(Thumbnails.INTERNAL_CONTENT_URI, projectionThumb, (String) null, (String[]) null, "_id desc");
        CursorJoiner cursorJoiner = new CursorJoiner(cursorPhoto, new String[]{"_id"}, cursorThumb, new String[]{"image_id"});
        Iterator i$ = cursorJoiner.iterator();

        while (i$.hasNext()) {
            Result joinerResult = (Result) i$.next();

            try {
                File exception = new File(cursorPhoto.getString(1));
                if (exception.exists()) {
                    boolean recent;
                    BucketModel bucketModel;
                    Cursor curCount;
                    if (joinerResult == Result.BOTH) {
                        recent = false;
                        String photoModel = cursorThumb.getString(2);
                        if (Math.abs(this.mTimeStampToday - cursorPhoto.getLong(5)) < 259200000L) {
                            recent = true;
                        }

                        bucketModel = new BucketModel(cursorPhoto.getLong(3), cursorPhoto.getString(4), cursorPhoto
                                .getString(1));
                        if (this.mBuckets.indexOf(bucketModel) == -1) {
                            curCount = this.mResolver.query(this.mUriInternal, projection, "bucket_id = ?", new String[]{String.valueOf(cursorPhoto
                                    .getLong(3))}, (String) null);
                            bucketModel.PhotoCount = curCount.getCount();
                            curCount.close();
                            this.mBuckets.add(bucketModel);
                        }

                        File curCount1 = new File("file://" + photoModel);
                        if (!curCount1.exists()) {
                            photoModel = null;
                        }

                        PhotoModel photoModel1 = new PhotoModel(cursorPhoto.getLong(0), cursorPhoto.getLong(3), cursorPhoto
                                .getString(2), cursorPhoto.getString(1), recent, photoModel);
                        photoModel1.setTime(cursorPhoto.getLong(5));
                        if (mData.indexOf(photoModel1) == -1) {
                            mData.add(photoModel1);
                        }
                    } else {
                        recent = false;
                        if (Math.abs(this.mTimeStampToday - cursorPhoto.getLong(5)) < 259200000L) {
                            recent = true;
                        }

                        PhotoModel photoModel2 = new PhotoModel(cursorPhoto.getLong(0), cursorPhoto.getLong(3), cursorPhoto
                                .getString(2), cursorPhoto.getString(1), recent, cursorPhoto.getString(1));
                        photoModel2.setTime(cursorPhoto.getLong(5));
                        if (mData.indexOf(photoModel2) == -1) {
                            mData.add(photoModel2);
                        }

                        bucketModel = new BucketModel(cursorPhoto.getLong(3), cursorPhoto.getString(4), cursorPhoto
                                .getString(1));
                        if (this.mBuckets.indexOf(bucketModel) == -1) {
                            curCount = this.mResolver.query(this.mUriInternal, projection, "bucket_id = ?", new String[]{String.valueOf(cursorPhoto
                                    .getLong(3))}, (String) null);
                            bucketModel.PhotoCount = curCount.getCount();
                            curCount.close();
                            this.mBuckets.add(bucketModel);
                        }
                    }

                    if (cursorPhoto.isLast()) {
                        break;
                    }
                }
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        }
        cursorThumb.close();
        cursorPhoto.close();
        return mData;
    }

    public boolean isSingleCropableMode() {
        return this.mSingleCropable;
    }

    public int getMaxSelect() {
        return this.mMaxSelect;
    }

    public void setmMaxSelect(int mMaxSelect) {
        this.mMaxSelect = mMaxSelect;
    }

    public boolean reachMaximum() {
        return this.mSelected.size() == this.mMaxSelect;
    }

    /**
     * 选取图片
     *
     * @param selected
     * @param maxSelect
     * @param cropable
     * @param listener
     * @param userid
     */
    public void startPicking(final List<PhotoModel> selected, final int maxSelect, final boolean cropable, final OnFinishPickingListener listener, final long userid) {
        this.mListener = listener;
        if (isListEmpty(mPhotos)) {
            if (mCount == 0) {
                mCount = 1;
                rebuildData(new OnResultListener() {
                    @Override
                    public void onResult() {
                        startPicking(selected, maxSelect, cropable, listener, userid);
                    }
                });
                return;
            }
        }

        if (!isListEmpty(selected)) {
            PhotoModel model = new PhotoModel();
            for (int bundle = 0; bundle < selected.size(); ++bundle) {
                model.Id = (selected.get(bundle)).Id;
                //防止因为没有权限导致的获取不到相册的问题
                if (isListEmpty(mPhotos)) {
                    break;
                }
                //// FIXME: 16/10/28  代码写的太乱,没看懂, 可以改为用 contain 判断
                if (null != this.mPhotos && this.mPhotos.indexOf(model) != -1) {
                    PhotoModel photoModel = this.mPhotos.get(this.mPhotos.indexOf(model));
                    //photoModel.Url = model.Url;
                    //photoModel.UrlThumbnail = model.UrlThumbnail;
                    if (this.mSelected.indexOf(photoModel) == -1) {
                        this.mSelected.add(photoModel);
                    }
                }
            }
        }
        this.mMaxSelect = maxSelect;
        if (this.mMaxSelect == 1 && cropable) {
            this.mSingleCropable = true;
        }
        gotoBucketActivity(userid);
    }

    private boolean isListEmpty(List list) {
        return list == null || list.size() == 0;
    }

    private void gotoBucketActivity(long userid) {
        try {
            Intent intent = new Intent(this.mContext, BucketOverviewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle var9 = new Bundle();
            BucketModel var10 = new BucketModel(RECENT_BUCKET_ID, "最近", (String) null);
            var9.putSerializable("Data", var10);

            var9.putLong("userid", userid);
            intent.putExtras(var9);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListener(OnFinishPickingListener listener) {
        mListener = listener;
    }

    public void finishPicking(boolean isCancel) {
        if (null != mListener) {
            mListener.OnFinish(isCancel, mSelected);
            mSingleCropable = false;
            clearSelection();
            recycleListener();
            EventBus.getDefault().post(new FinishPicking());
        }
    }

    public List<PhotoModel> getPhotosById(long id) {
        ArrayList specifyData = new ArrayList();

        for (int i = 0; i < mPhotos.size(); ++i) {
            PhotoModel dataObj = mPhotos.get(i);
            if (dataObj.BucketId == id) {
                specifyData.add(dataObj);
            }
        }
        return specifyData;
    }

    public List<PhotoModel> getAllPhotos() {
        return mPhotos;
    }

    public List<PhotoModel> getRecentPhotos() {
        ArrayList specifyData = new ArrayList();
        if (mPhotos == null)
            return specifyData;
        int max = mPhotos.size();
        if (max > 100) {
            max = 100;
        }
        for (int i = 0; i < max; ++i) {
            PhotoModel dataObj = mPhotos.get(i);
            if (dataObj.IsRecent) {
                specifyData.add(dataObj);
            }
        }
        return specifyData;
    }

    private int getRecentModelCount() {
        int count = 0;
        for (int i = 0; i < mPhotos.size(); ++i) {
            if ((mPhotos.get(i)).IsRecent) {
                ++count;
            }
        }
        if (count > 100) {
            count = 100;
        }
        return count;
    }

    public List<BucketModel> getBuckets() {
        return this.mBuckets;
    }

    public void selectItem(PhotoModel sbm) {
        if (this.mSelected.indexOf(sbm) != -1) {
            this.mSelected.remove(sbm);
        } else {
            this.mSelected.add(sbm);
        }

        if (this.mDCListener != null) {
            this.mDCListener.OnChange();
        }
    }

    public void selectItemForce(PhotoModel sbm) {
        if (this.mSelected.indexOf(sbm) == -1) {
            this.mSelected.add(sbm);
            if (this.mDCListener != null) {
                this.mDCListener.OnChange();
            }
        }
    }

    public boolean isItemSelected(PhotoModel photoModel) {
        return this.mSelected.indexOf(photoModel) != -1;
    }

    public void clearSelection() {
        if (mSelected != null) {
            this.mSelected.clear();
        }
    }

    public void clear() {
        clearSelection();
        mHaveAddPhoto = false;
    }

    public List<PhotoModel> getSelectedItems() {
        return this.mSelected;
    }

    private void recycleListener() {
        this.mDCListener = null;
        this.mListener = null;
    }

    public void setDetectListner(OnDetectListner listner) {
        mDetectListener = listner;
    }

    private interface OnDataChangeListener {
        void OnChange();
    }

    public interface OnDetectListner {
        public void onChange();
    }

    public interface OnFinishPickingListener {
        void OnFinish(boolean var1, List<PhotoModel> var2);
    }


    public void saveBitmap2FileInThread(final Activity context,
                                        final Bitmap bitmap, final String filename,
                                        final OnSaveBitmapListener listener) {
        submitLocalTask("save-bitmap-file", false, new Runnable() {
            @Override
            public void run() {
                final boolean result = BitmapUtil.saveBitmap2File(context, bitmap, filename,
                        10, 300 * 1024);
                bitmap.recycle();
                context.runOnUiThread(new Runnable() {//callback should run UiThread
                    @Override
                    public void run() {
                        if (result) {
                            if (listener != null) {
                                listener.OnSaveResult(true, filename);
                            }
                        } else {
                            if (listener != null) {
                                listener.OnSaveResult(false, filename);
                            }
                        }
                    }
                });
            }
        });
    }


    private PhotoModel isInList(List<PhotoModel> listPassFile, String url) {
        try {
            for (PhotoModel photoModel : listPassFile) {
                if (photoModel.Url.equals(url) || photoModel.UrlThumbnail.equals(url))
                    return photoModel;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 压缩文件列表
     */
    public void compressFileList(final Activity context, final String prefix, final List<String> listWantoCompressFile, final List<PhotoModel> listPassFile,
                                 final OnSaveBitmapListListener listener, final long userid) {
        submitLocalTask("save-bitmap-list-file", false, new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < listWantoCompressFile.size(); i++) {
                        final int index = i;
                        final File fileDest = new File(listWantoCompressFile.get(i));
                        final String strPathName = fileDest.getAbsolutePath();
                        final Bitmap bitmap;
                        final PhotoModel photoModel = isInList(listPassFile, strPathName);
                        if (photoModel != null) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (listener != null) {
                                        String url = photoModel.UrlThumbnail;
                                        if (StringUtils.isNull(url)) {
                                            url = photoModel.Url;
                                        }
                                        if (GifUtil.isGif(url)) {
                                            int[] widthHeightByUrl = BitmapUtil.getBitmapWidthHeight(url);
                                            int width = 0;
                                            int height = 0;
                                            if (widthHeightByUrl != null) {
                                                width = widthHeightByUrl[0];
                                                height = widthHeightByUrl[1];
                                            }

                                            String suffix = ".gif";
                                            String uploadName = ImageUploaderUtil.getUploadName(prefix, userid, width, height, suffix);
                                            File uploadFile = new File(CacheDisc.getCacheFileHide(context), uploadName);
                                            try {
                                                FileUtils.copyFile(new File(url), uploadFile);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (!uploadFile.exists()) {
                                                LogUtils.d(TAG, "出现Copy Gif 异常了1!:" + url);
                                            }
                                            listener.OnSaveResult(true, index, uploadName);
                                        } else {
                                            File file = new File(url);
                                            if (!(file.exists())) {
                                                LogUtils.d(TAG, "出现异常了1!:" + url);
                                            } else {
                                                LogUtils.d(TAG, "无需 压缩:" + url);
                                            }
                                            listener.OnSaveResult(true, index, file.getName());
                                        }


                                    }
                                }
                            });
                        } else {
                            // get bitmap
                            int angle = BitmapUtil.getBitmapRotatoAngle(fileDest);
                            Context application = context.getApplicationContext();
                            Bitmap bitmapSrc = BitmapUtil.getBitmapByLocalPathName(application, strPathName);
                            if (angle != 0) {
                                bitmap = BitmapUtil.rotateBitmap(bitmapSrc, angle);
                            } else {
                                bitmap = bitmapSrc;
                            }
                            String filename;
                            boolean result;
                            if (bitmap != null) {
                                filename = ImageUploaderUtil.createSaveFileName(prefix, bitmap, userid);//.getInstance(context).getTataquanSaveFileName(bitmap, false, strPathName, userid);
                                result = BitmapUtil.saveBitmap2File(context, bitmap, filename, 10, 300 * 1024);
                                bitmap.recycle();
                                final boolean finalResult = result;
                                final String finalFilename = filename;
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (finalResult) {
                                            if (listener != null) {
                                                listener.OnSaveResult(true, index, finalFilename);
                                            }
                                        } else {
                                            if (listener != null) {
                                                listener.OnSaveResult(false, index, finalFilename);
                                            }
                                        }
                                    }
                                });
                            } else {
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (listener != null) {
                                            listener.OnSaveResult(false, 0, strPathName);
                                        }
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void insertPic2SystemDb(final Context context, final Bitmap bitmap) {
        submitLocalTask("instert-pic-system-db", new Runnable() {
            @Override
            public void run() {
                ContentResolver cr = context.getContentResolver();
                String url = Media.insertImage(cr, bitmap, System.currentTimeMillis() + "", "");
                Uri uri = Uri.parse(url);
                String strPathName = getFilePathByContentResolver(context, uri);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uriImage = Uri.fromFile(new File(strPathName));
                intent.setData(uriImage);
                context.sendBroadcast(intent);
            }
        });
    }

    /**
     * 插入已经存在的图片进入系统图库，如果是GIF图，会再生成一张JPEG
     *
     * @param context
     * @param picFile
     */
    public static  void insertPic2Gallery(Context context, File picFile) {
        // 把文件插入到系统图库
        if (picFile == null || !picFile.exists()) {
            LogUtils.d(TAG, "insertPic2Gallery 失败：图片不存在 ");
            return;
        }
        String path = picFile.getAbsolutePath();
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    path, picFile.getName(), null);
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(TAG, "insertPic2Gallery 失败：file: " + path);
        }

    }

    private String getFilePathByContentResolver(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (null == c) {
            throw new IllegalArgumentException(
                    "Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() != 1) || !c.moveToFirst()) {
            } else {
                filePath = c.getString(
                        c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            c.close();
        }
        return filePath;
    }

    public class FinishPicking {
        public FinishPicking() {

        }
    }

    public List<PhotoModel> reQueryAllPhotoDatasFromDB() {
        return queryPhotoDatasFromDB();
    }
}
