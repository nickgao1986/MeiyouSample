package biz.download;

/**
 * 下载状态
 * Created by lwh on 2015/9/17.
 */
public enum  DownloadStatus {
  /*  *//**
     * 正在下载
     *//*
    public static final int DOWNLOAD_ING=1;
    *//**
     * 下载完成
     *//*
    public static final int DOWNLOAD_COMPLETE=2;
    *//**
     * 下载失败
     *//*
    public static final int DOWNLOAD_FAIL=3;
*/
    DOWNLOAD_START(999),DOWNLOAD_ING(1000),  DOWNLOAD_COMPLETE(1100), DOWNLOAD_FAIL(1101);

    public static DownloadStatus valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {
            case 999:
                return DOWNLOAD_START;
            case 1000:
                return DOWNLOAD_ING;
            case 1100:
                return DOWNLOAD_COMPLETE;
            case 1101:
                return DOWNLOAD_FAIL;
        }
        return null;
    }

    public int value() {
        return this.nCode;
    }

    private int nCode;
    private DownloadStatus(int _nCode) {
        this.nCode = _nCode;
    }



}
