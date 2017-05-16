package nickgao.com.framework.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by gaoyoujian on 2017/3/5.
 */

public class FileUtils {

    /**
     * 复制文件
     *
     * @param src
     * @param dest
     * @return
     */
    public static boolean copyFile(File src, File dest) {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = null;
        BufferedInputStream inBuff = null;

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = null;
        BufferedOutputStream outBuff = null;
        try {
            input = new FileInputStream(src);
            inBuff = new BufferedInputStream(input);

            // 新建文件输出流并对它进行缓冲
            output = new FileOutputStream(dest);
            outBuff = new BufferedOutputStream(output);
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                inBuff.close();
                outBuff.close();
                output.close();
                input.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }


}
