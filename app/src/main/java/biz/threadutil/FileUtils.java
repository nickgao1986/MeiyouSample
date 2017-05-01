package biz.threadutil;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import nickgao.com.meiyousample.utils.LogUtils;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class FileUtils {

    static String SDPATH = Environment.getExternalStorageDirectory() == null?"":Environment.getExternalStorageDirectory() + "/";
    private static String TAG = "FileUtil";

    public FileUtils() {
    }

    public static void saveObjectToLocal(Context context, Object obj, String strSaveFileName) {
        try {
            if(obj == null) {
                return;
            }

            context.deleteFile(strSaveFileName);
            FileOutputStream e = context.openFileOutput(strSaveFileName, 0);
            ObjectOutputStream out = new ObjectOutputStream(e);
            out.writeObject(obj);
            e.close();
            out.close();
        } catch (Exception var5) {
            LogUtils.e(var5.getLocalizedMessage());
        }

    }

    public static Object getObjectFromLocal(Context context,
                                            String strSaveFileName) {
        FileInputStream is = null;
        ObjectInputStream in = null;
        try {
            Object object = new Object();
            File file = context.getFileStreamPath(strSaveFileName);

            if (file != null && file.exists()) {
                LogUtils.d(TAG, "文件路径为：" + file.getAbsolutePath() + " 文件大小为："
                        + file.length());

                is = context.openFileInput(strSaveFileName);
                in = new ObjectInputStream(is);
                Object obj = in.readObject();
                if (obj == null) {
                    // is.close();
                    // in.close();
                    return object;
                } else {
                    object = obj;
                }

                return object;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }
}
