package nickgao.com.meiyousample.skin;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import nickgao.com.meiyousample.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class SkinUtil {

    public static final String TAG = "SkinUtil";
    public static final String SKIN_FOLDER = "skins";

    private SkinUtil() {
    }

    private static class SkinUtilHolder {
        public static final SkinUtil INSTANCE = new SkinUtil();
    }

    public static SkinUtil getInstance() {
        return SkinUtilHolder.INSTANCE;
    }

    /**
     * 获取皮肤包文件下载存储路径
     *
     * @param context
     * @return
     */
    public String getDownloadPath(Context context) {
        File skinDir = CacheDisc.getInstance(context).getCacheFileName(SKIN_FOLDER);

        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }
        return skinDir.getAbsolutePath();
    }

    public boolean isSkinFileExistsInSDCard(Context context, String fileName) {
        try {
            File file = new File(getDownloadPath(context) + File.separator + fileName);

            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean copyFile2Data(Context context, File srcFile, String fileName) {
        try {
            context = context.getApplicationContext();
            File dir = context.getDir(SKIN_FOLDER, Activity.MODE_PRIVATE);
            File distFile = new File(dir.getAbsolutePath() + File.separator + fileName);
            //测试时不判断
            if (distFile.exists()) {
                return true;
            }

            //源文件不存在
            if (!srcFile.exists()) {
                LogUtils.d("src file is not exists");
                return false;
            }

            return copyFile(srcFile, distFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean copyFile(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isSkinFileExistsInData(Context context, String fileName) {
        try {
            File dir = context.getDir(SKIN_FOLDER, Activity.MODE_PRIVATE);
            File file = new File(dir.getAbsolutePath() + File.separator + fileName);

            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void copyAssetFile2Data(Context context, String targetFileName, String fileSaveName) {
        try {
            context = context.getApplicationContext();
            InputStream in = context.getAssets().open(targetFileName);
            File dir = context.getDir(SKIN_FOLDER, Activity.MODE_PRIVATE);
            File distFile = new File(dir.getAbsolutePath() + File.separator + fileSaveName);
            //测试时不判断
            if (distFile.exists()) {
                return;
            }
            copyFile(in, distFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFile(InputStream in, File dst) {
        try {
            OutputStream out = new FileOutputStream(dst);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
