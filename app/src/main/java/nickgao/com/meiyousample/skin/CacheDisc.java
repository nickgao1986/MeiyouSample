package nickgao.com.meiyousample.skin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 缓存到sd卡上【没有则缓存到cache中】
 *
 * @author ziv
 */
public class CacheDisc {

    /**
     * 单张图片最大大小KB 用于压缩图片 400K
     */
    private static final int MAX_SIZE = 200;


    private Context mContext;
    private static String mFileName;

    private CacheDisc(Context context) {
        this.mContext = context;
        initFileName();
        initCacheFolder();
        clearCacheUncompleteFile();
    }

    private void initFileName() {
        mFileName = Environment.getExternalStorageDirectory().getPath() + "/" + mContext.getPackageName() + "_bitmapCache";
    }

    private static CacheDisc mInstance;

    public static CacheDisc getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CacheDisc(context);
        }
        return mInstance;
    }

    /**
     * 初始文件夹，若无则创建
     */
    public void initCacheFolder() {
        File f = new File(getCacheFile(mContext));
        if (!f.exists()) {
            f.mkdirs();
        }
        f = null;
    }

    /**
     * 清空未下载完成的图片文件
     */
    public void clearCacheUncompleteFile() {
        File f = new File(getCacheFile(mContext));
        if (f.isDirectory()) {
            File[] fileList = f.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File file : fileList) {
                    if (file.getName().endsWith("tmp")) {
                        file.delete();
                    }
                }
            }
        }
    }

    /**
     * 获取缓存到本地的所有图片文件大小
     */
    public long getCacheFileSize() {
        File f = new File(getCacheFile(mContext));
        long size = 0;
        if (f.isDirectory()) {
            File[] fileList = f.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File file : fileList) {
                    size += file.length();
                }
            }
        }
        return size;
    }

    /**
     * 首页分享截图图片
     */
    public void saveHomeCacheFiles(Bitmap bitmap) {
        File file = new File(getCacheFile(mContext),
                "screen_shot.jpg".hashCode() + "");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空缓存
     */
    public void clearCacheFiles() {
        File f = new File(getCacheFile(mContext));
        if (f.isDirectory()) {
            File[] fileList = f.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File file : fileList) {
                    file.delete();
                }
            }
        }
        f.delete();
    }

    /**
     * 通过URL获取bitmap，从文件中
     *
     * @param url
     * @param sampleSize
     * @return
     */
    public Bitmap getBitmapInFile(String url, boolean sampleSize) {
        return decodeFile(getCacheFileName(url.hashCode() + ""), sampleSize);
    }

    /**
     * 通过URL获取bitmap，从文件中
     *
     * @return
     */
    public Bitmap getBitmapInFileByCacheName(String cache) {
        return decodeFile(getCacheFileName(cache), false);
    }

    /**
     * 通过地址获取完整文件位置
     *
     * @param url
     * @return
     */
    public String getCacheFilePathByUrl(String url) {
        return getCacheFileByUrl(url).getAbsolutePath();
    }

    /**
     * 通过地址获取完整文件位置
     *
     * @param url
     * @return
     */
    public File getCacheFileByUrl(String url) {
        String fileName = url.hashCode() + "";
        return getCacheFileName(fileName);
    }

    /**
     * 图片下载的文件夹
     *
     * @return
     */
    public static String getCacheFile(Context context) {
        File cacheDir;
        mFileName = Environment.getExternalStorageDirectory().getPath() + "/" + context.getPackageName() + "_bitmapCache";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(mFileName);
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    public static String getCacheFile(Context context, String packagename) {
        File cacheDir;
        mFileName = Environment.getExternalStorageDirectory().getPath() + "/" + packagename + "_bitmapCache";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(mFileName);
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }


    /**
     * 压缩图片,存放的路径,需要对图库隐藏,变成点目录
     *
     * @return
     */
    public static String getCacheFileHide(Context context) {
        File cacheDir;
        mFileName = Environment.getExternalStorageDirectory().getPath() + "/" + "." + context.getPackageName() + "_bitmapCache";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(mFileName);
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }


    /**
     * 获取一个本地缓存的文件
     *
     * @param name
     * @return
     */
    public File getCacheFileName(String name) {
        return new File(getCacheFile(mContext), name);
    }

    /**
     * 解析文件[通过宽高]
     *
     * @param srcPath
     * @param width
     * @param height
     * @return
     */
    private Bitmap decodeFile(String srcPath, float width, float height,
                              boolean sampLength) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && w > width) {
            be = (int) (newOpts.outWidth / width);
        } else if (w < h && h > height) {
            be = (int) (newOpts.outHeight / height);
        }
        if (be <= 1) {
            if (sampLength) {
                be = getSampleSize(new File(srcPath).length());
            } else {
                be = 1;
            }
        }
        newOpts.inSampleSize = be;// 设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * 解析图片[以屏幕宽高为标准]
     *
     * @param url
     * @return
     */
    public Bitmap decodeFileWidthScreenWidthHeight(String url) {
        return decodeFile(getCacheFilePathByUrl(url), mContext.getResources()
                .getDisplayMetrics().widthPixels, mContext.getResources()
                .getDisplayMetrics().heightPixels, false);
    }

    /**
     * 解析文件[通过文件大小]
     *
     * @param file
     * @param sampleSize
     * @return
     */
    public Bitmap decodeFile(File file, boolean sampleSize) {
        if (true)
            return decodeFile(file.getAbsolutePath(), 320, 480, true);
        if (sampleSize) {
            int size = getSampleSize(file.length());
            if (size != 1) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = size;
                opts.inJustDecodeBounds = false;
                opts.inDither = false; // Disable Dithering mode
                opts.inPurgeable = true; // Tell to gc that whether it needs
                // free memory, the Bitmap can be
                // cleared
                opts.inInputShareable = true; // Which kind of reference will be
                // used to recover the Bitmap
                // data after being clear, when
                // it will be used in the future
                try {
                    return BitmapFactory.decodeFile(file.getAbsolutePath(),
                            opts);
                } catch (Exception e) {
                    System.err.println("Exception :" + e.toString());
                    e.printStackTrace();
                }
            }
        }
        try {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final long KB = 1024;
    public final long MB = KB * 1024;

    /**
     * 限制最大文件大小为200KB，超过则进行压缩
     *
     * @param size
     * @return
     */
    public int getSampleSize(long size) {
        if (size < KB) {
            return 1;
        } else if (size < MB) {
            int fileLenght = (int) (size / KB);
            int m = fileLenght / MAX_SIZE;
            m = m > 1 ? m : 1;
            return m;
        }
        return 10;
    }

    /**
     * 移除本地图片<br>
     * 没有开新线程
     *
     * @param url
     */
    public void removeImageFromSDSingle(String url) {
        String fileName = url.hashCode() + "";
        File f = getCacheFileName(fileName);
        if (f.exists()) {
            f.delete();
        }
        f = null;
    }

    public String getCacheFileName() {
        return mFileName;
    }

}
