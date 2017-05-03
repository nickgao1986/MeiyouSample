package biz.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;

import com.lingan.seeyou.ui.view.RoundedImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import biz.threadutil.ThreadUtil;
import nickgao.com.meiyousample.skin.CacheDisc;
import nickgao.com.meiyousample.utils.DeviceUtils;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;
import nickgao.com.meiyousample.utils.UrlUtil;

/**
 * Bitmap工具类 Created by Administrator on 13-12-5.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BitmapUtil {

    private static final String TAG = "BitmapUtil";
    public static final int IMAGE_WIDHT_MIN = 200;
    public static final int IMAGE_HEIGHT_MIN = 200;

    /**
     * 获取图片宽高,通过解析bitmap Option
     *
     * @param strPathName
     * @return
     */
    public static int[] getBitmapWidthHeight(String strPathName) {
        try {
            File file = new File(strPathName);
            if (!file.exists())
                return null;
            FileInputStream inputStream = new FileInputStream(file);
            final FileDescriptor fd = inputStream.getFD();

            //compute width height
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            int orgWidth = options.outWidth;
            int orgHeight = options.outHeight;
            int[] wh = new int[2];
            wh[0] = orgWidth;
            wh[1] = orgHeight;
            return wh;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static void setRoundImageView(RoundedImageView roundedImageView, Bitmap bitmap) {
        try {
            roundedImageView.setCornerRadius(10.0F);
            roundedImageView.setOval(true);
            roundedImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            roundedImageView.setImageBitmap(bitmap);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    /**
     * 读取本地路径的图片
     *
     * @param context
     * @param strPathName
     * @return
     */
    public static Bitmap getBitmapByLocalPathName(Context context, String strPathName) {
        try {
            // FIXME: 17/2/7 特殊处理
            if (strPathName.contains("www.mp.meiyou.com/")) {
                strPathName = UrlUtil.url2FileName(strPathName);
            }

            File file = new File(strPathName);
            if (!file.exists()) return null;
            FileInputStream inputStream = new FileInputStream(file);
            final FileDescriptor fd = inputStream.getFD();

            //compute width height
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);

            //compute compress para
            int orgWidth = options.outWidth;
            int orgHeight = options.outHeight;

            int destWidth = orgWidth;
            int destHeight = orgHeight;
            //宽度最大480；
            if (orgWidth > DeviceUtils.getScreenWidth(context)) {
                destWidth = DeviceUtils.getScreenWidth(context);
                destHeight = orgHeight * destWidth / orgWidth;
                options.inSampleSize = calculateInSampleSize(options, destWidth, destHeight);
            } else {
                options.inSampleSize = 1;
            }

            LogUtils.d(TAG, "--getBitmapByLocalPathName: "
                    + "\n--->strPathName:" + strPathName
                    + "\n--->orgWidth:" + orgWidth
                    + "\n--->orgHeight:" + orgHeight
                    + "\n--->destWidth:" + destWidth
                    + "\n--->destHeight:" + destHeight
                    + "\n--->inSampleSize:" + options.inSampleSize
            );

            options.inJustDecodeBounds = false;
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeFile(strPathName, options);
                }
            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
                options.inSampleSize++;
                bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeFile(strPathName, options);
                }
            }
            //roate if need
            int angle = getBitmapRotatoAngle(file);
            if (angle > 0) {
                rotateBitmap(bitmap, angle);
            }
            inputStream.close();
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public interface OnSaveBitmapListener {
        public void OnSaveResult(boolean success, String strSaveFileName);
    }

    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param bitmap
     * @param filename
     * @param listener
     */
    public static void saveBitmap2FileInThread(final Context context,
                                               final Bitmap bitmap, final String filename,
                                               final OnSaveBitmapListener listener) {

        saveBitmap2FileInThread(context, bitmap, filename, 300 * 1024, listener);
    }

    public static void saveBitmap2FileInThread(final Context context,
                                               final Bitmap bitmap, final String filename, final long maxSize,
                                               final OnSaveBitmapListener listener) {
        if (bitmap != null) {
            ThreadUtil.addTaskForIO(context.getApplicationContext(), "", new ThreadUtil.ITasker() {
                        @Override
                        public Object onExcute() {
                            try {
                                return saveBitmap2File(
                                        context,
                                        bitmap,
                                        filename,
                                        10,
                                        maxSize);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return false;
                        }

                        //new ThreadUtil().addTask(context.getApplicationContext(), new ITasker() {
                        @Override
                        public void onFinish(
                                Object result) {
                            try {
                                //bitmap.recycle();

                                Boolean flag = (Boolean) result;
                                if (flag) {
                                    if (listener != null) {
                                        listener.OnSaveResult(
                                                true,
                                                filename);
                                    }
                                } else {
                                    if (listener != null) {
                                        listener.OnSaveResult(
                                                false,
                                                filename);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
            );
        } else {
            if (listener != null) {
                listener.OnSaveResult(
                        false,
                        filename);
            }
        }
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 主要是用于获取头像
     *
     * @param context
     * @param strPathName
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapByLocalPathName(Context context, String strPathName, int width, int height) {
        try {
            File file = new File(strPathName);
            if (!file.exists())
                return null;
            FileInputStream inputStream = new FileInputStream(file);
            final FileDescriptor fd = inputStream.getFD();
            //compute width height
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);

            options.inSampleSize = calculateInSampleSize(options, width, height);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeFile(strPathName, options);
            }
            //roate if need
            int angle = getBitmapRotatoAngle(file);
            if (angle > 0) {
                rotateBitmap(bitmap, angle);
            }
            inputStream.close();
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                    bitmap.getWidth() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
            //return _bmp;
            return output;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;

    }

    // 计算图片的缩放值
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int maxWidth, int maxHeight) {
        try {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (width > maxWidth || height > maxHeight) {
                if (width > height) {
                    inSampleSize = Math.round((float) height / (float) maxHeight);
                } else {
                    inSampleSize = Math.round((float) width / (float) maxWidth);
                }
                final float totalPixels = width * height;
                final float maxTotalPixels = maxWidth * maxHeight * 2;
                while (totalPixels / (inSampleSize * inSampleSize) > maxTotalPixels) {
                    inSampleSize++;
                }
            }
            return inSampleSize;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
        try {

            LogUtils.d(TAG, "-->rotateBitmapIfNeeded angle:" + angle);
            if (angle != 0) {
                Matrix m = new Matrix();
                m.postRotate(angle);
                Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                return result;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;

    }

    public static int getBitmapRotatoAngle(File bitmapFile) {
        try {
            if (bitmapFile != null && bitmapFile.exists()) {
                ExifInterface exif;
                try {
                    exif = new ExifInterface(bitmapFile.getPath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return 0;
                }
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                int angle;
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        angle = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        angle = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        angle = 270;
                        break;
                    default:
                        angle = 0;
                        break;
                }
                LogUtils.d(TAG, "-->getBitmapRotatoAngle angle:" + angle);
                return angle;

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;

    }


    private static long compressFile(Context context, Bitmap bitmap, String filename, int quanlity) {
        FileOutputStream fileOutputStream = null;
        try {
            //运营后台分配头像，文件名包含特殊字符无法保存，需要处理；
            if (filename.contains("www.mp.meiyou.com/")) {
                filename = UrlUtil.url2FileName(filename);
            }
            //小于100k,不进行压缩
            File file = new File(CacheDisc.getCacheFileHide(context), filename);
            if (file.isFile()) {
                file.delete();
            }
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quanlity, fileOutputStream);
            //// FIXME: 16/10/28  压缩完,图片高宽 需要重算

            long len = new File(CacheDisc.getCacheFileHide(context), filename).length();
            return len;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return 0;
    }

    public static boolean saveBitmap2File(Context context, Bitmap bitmap, String filename, int step, long maxSize) {
        try {
            if (bitmap == null) {
                return false;
            }
            //小于maxSize,不进行压缩
            long length = compressFile(context, bitmap, filename, 100);
            LogUtils.d(TAG, "保存前大小为：" + length);
            if (length < maxSize) {
                return true;
            }

            //大于100k,压缩质量百分之50
            length = compressFile(context, bitmap, filename, 50);
            LogUtils.d(TAG, "保存后大小为：" + length);

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;

    }


    /**
     * 通过地址获取图片宽高
     *
     * @param url
     * @return
     */
    public static int[] getWidthHeightByUrl(String url) {
        try {
            if (url != null) {
                String[] splits = url.split("_");
                if (splits.length >= 3) {
                    String widthStr = splits[splits.length - 2];
                    String heightStr = splits[splits.length - 1];
                    int index = heightStr.indexOf(".");
                    if (index > 0) {
                        heightStr = heightStr.substring(0, index);
                    }
                    int width = Integer.parseInt(widthStr);
                    int height = Integer.parseInt(heightStr);

                    return new int[]{width, height};
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            //e.printStackTrace();
        }
        return null;
    }


    public interface OnNotifyListener {
        public void OnNotify(Bitmap bitmap);
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static void compressImage(final Bitmap image, final OnNotifyListener lisenter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (image == null || image.isRecycled()) {
                        if (lisenter != null) {
                            lisenter.OnNotify(null);
                        }
                        return;
                    }
                    //return ;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    int options = 100;
                    while (baos.toByteArray().length > (1024 * 100)
                            && options > 0) {
                        LogUtils.d(TAG, "压缩参数：" + options);
                        options -= 20;
                        baos.reset();
                        image.compress(Bitmap.CompressFormat.JPEG, options,
                                baos);
                    }
                    LogUtils.d(TAG, "截屏后,图片大小为：" + baos.toByteArray().length);
                    //image.recycle();
                    ByteArrayInputStream isBm = new ByteArrayInputStream(baos
                            .toByteArray());
                    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
                    isBm.close();
                    if (lisenter != null) {
                        lisenter.OnNotify(bitmap);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // To change body of implemented methods use File | Settings |
                // File Templates.

            }
        }).start();
    }

    /**
     * 按指定宽高拼接图片地址
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
    public static String getThumbUrl(String url, int width, int height, int quality) {
        if (TextUtils.isEmpty(url) || url.contains("?imageView2")
                || !url.contains("http")) { // 如果已经是转换过的地址不再转换
            return url;
        }

        if (quality <= 0) {
            quality = 40;
        } else if (quality > 100) {
            quality = 100;
        }
        return StringUtils.join(new Object[]{url, "?imageView2/1/q/", quality, "/w/", width, "/h/", height});
    }

    /**
     * 获取转换高斯模糊效果的七牛地址
     *
     * @param originurl
     * @return
     */
    public static String getGaussianBlurImage(String originurl) {
        if (StringUtils.isNull(originurl)) {
            return "http://sc.seeyouyima.com/forum/data/556ecd775a34c_540_540.jpg?imageMogr2/blur/50x100/thumbnail/!375x245r";
        }

//        return StringToolUtils.buildString(originurl, "?imageMogr2/blur/50x100/thumbnail/!375x245r");
        return StringUtils.join(new Object[]{originurl, "?imageMogr2/blur/50x100/thumbnail/!375x245r"});
    }

    /**
     * 是否是长图
     *
     * @return 高/宽 > 3才算是长图,
     * 改为3吧
     */
    public static boolean isLongPicture(int[] widthHeight) {
        if (null != widthHeight && widthHeight.length > 1 && widthHeight[0] > 0 && widthHeight[1] > 0 && 1.0f * widthHeight[1] / widthHeight[0] > 3) {
            return true;
        }
        return false;
    }




}
