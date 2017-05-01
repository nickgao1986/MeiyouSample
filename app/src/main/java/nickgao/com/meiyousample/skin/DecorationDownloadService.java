package nickgao.com.meiyousample.skin;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nickgao.com.meiyousample.database.DecorationStatusDatabase;
import nickgao.com.meiyousample.utils.LogUtils;

/**
 * 下载服务，皮肤和表情
 * Created by Administrator on 2014/9/22.
 */
public abstract class DecorationDownloadService extends Service {

    //下载器集合
    public static Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
    //是否是皮肤，true 皮肤，false 表情
    public boolean isSkin;
    private Downloader downloader;
    private int fileSize;//文件大小
    private List<DownloadInfo> infos;// 存放下载信息类的集合
    private int threadCount = 1;//定义线程数
    protected DecorationThreadDatabase threadDataBase;
    protected DecorationStatusDatabase statusDataBase;
    protected static OnNotifationListener mListener;
    private int intCount = 0;//初始化文件几次

    /**
     * 发送广播，通知界面更新
     */
    protected abstract void sendBroadcast2Receiver(DecorationModel model, int completeSize, boolean flag);
    public abstract String getDownloadPath();
    protected abstract void onDownloadComplete(File file, DecorationModel model);
    /**
     * 初始化文件的异常
     * @param model
     */
    public abstract void setIntExcationInfo(DecorationModel model);

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        /**
         * 接收Download中每个线程传输过来的数据
         * **/
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 1) {
                    //正在下载，不断更新
                    final DecorationModel model = (DecorationModel) msg.obj;
                    int length = msg.arg1;
                    int completeSize = ListState.completeSizes.get(model.getFileName());
                    completeSize += length;
                    ListState.completeSizes.put(model.getFileName(), completeSize);
                    final int finalCompleteSize = completeSize;
                    int fileSize = ListState.fileSizes.get(model.getFileName());

                    LogUtils.d("aaaa: handle  fileSize： " + fileSize + " completeSize: " + completeSize);

                    if (completeSize == fileSize || completeSize == fileSize + 1 || completeSize + 1 == fileSize) {
                        //下载完成
                        threadDataBase.deleteThreadData(model);
                        //清空当前的数据
                        ListState.notifaSizes.put(model.getFileName(), 0);
                        ListState.completeSizes.put(model.getFileName(), 0);
                        //下载完成后重命名文件
                        File file = new File(getDownloadPath(), model.getTempFileName());
                        File targetFile = new File(getDownloadPath(), model.getFileName());
                        file.renameTo(targetFile);

                        //下载完后必须传targetFile文件，传file会找不到相对应文件
                        onDownloadComplete(targetFile, model);

                        //下載完成后向數據庫添加一條文件信息，用來查看下載完成的文件
                        ListState.state.put(model.getFileName(), SkinDownloadType.SKIN_COMPLETE);
                        statusDataBase.updateStatusModel(model, SkinDownloadType.SKIN_COMPLETE);
                        sendBroadcast2Receiver(model, completeSize, true);
                    } else {
                        int lastData = 0;
                        try {
                            lastData = ListState.notifaSizes.get(model.getFileName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LogUtils.d("aaaa: lastData: " + lastData + "  completeSize: " + completeSize + "   fileSize: " + fileSize);
                        float num = (float) (completeSize - lastData) / (float) fileSize;
                        int result = (int) (num * 100);
                        LogUtils.d("aaaa: result： " + result);
                        if (result >= 5) {
                            ListState.notifaSizes.put(model.getFileName(), completeSize);
                            sendBroadcast2Receiver(model, completeSize, false);
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                statusDataBase.updataComlepeteSizeModel(model, finalCompleteSize);
                            }
                        }).start();
                    }
                } else if (msg.what == 2) {
                    //前期工作准备好后，正式开始下载
                    DecorationModel model = (DecorationModel) msg.obj;
                    startDownload(model);
                }
            } catch (Exception e) {
                LogUtils.d("aaaa: 报错了");
                e.printStackTrace();
            }
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intCount = 0;
        if (intent != null) {
            DecorationModel model = (DecorationModel) intent.getSerializableExtra("model");
            String flag = intent.getStringExtra("flag");//标识
            if (model != null) {
                //根据不同标识进行不同操作
                //开始下载
                if (flag.equals("startDownload")) {
                    if (ListState.state.get(model.getFileName()) == null) {
                        ListState.state.put(model.getFileName(), SkinDownloadType.SKIN_INIT);
                        statusDataBase.addStatusModel(model, SkinDownloadType.SKIN_INIT, 0);
                    }
                    intStartFileLenth(model);//先初始化文件长度
                }
                //改变状态
                if (flag.equals("setState")) {
                    setState(model);
                }
//                if (flag.equals("delete")) {
//                    deleteData(model);
//                }
            }
        }
        return super.onStartCommand(intent,Service.START_FLAG_REDELIVERY,startId);
    }

    /**
     * 开始下载
     * @param model
     */
    public void startDownload(final DecorationModel model) {
        final String downPath = model.downLoadPath;//下载地址
        String savePath = getDownloadPath();//保存地址
        if (!threadDataBase.isNotExist(model.skinId)) {
            ToastUtils.showToast(getApplicationContext(), "文件已经存在下载列表！");
            return;
        }

        //LoadInfo是一个实体类,里面封装了一些下载所需要的信息,每个loadinfo对应1个下载器
        LoadInfo loadInfo = getDownloaderInfors(downPath, model);
        if (loadInfo.fileSize <= 0) {
            ToastUtils.showToast(getApplicationContext(), "无法获取下载文件");
            return;
        }
        //插入一条下载记录
        threadDataBase.saveInfos(infos, this);
        downloader = downloaders.get(model.getFileName());
        if (downloader == null) {
            ListState.fileSizes.put(model.getFileName(), 0);
            ListState.completeSizes.put(model.getFileName(), 0);
            downloader = new Downloader(model.getFileName(), savePath, model, isSkin, threadCount, this, mHandler);
            downloaders.put(model.getFileName(), downloader);//创建完一个新的下载器,必须把它加入到下载器集合里去
        }

        ListState.completeSizes.put(model.getFileName(), loadInfo.getComplete());
        ListState.fileSizes.put(model.getFileName(), loadInfo.getFileSize());
        // 调用方法开始下载
        downloader.download(infos);
    }

    public void intStartFileLenth(final DecorationModel model) {
        if (threadDataBase.isNotExist(model.skinId)) {
            //没有相应的线程未完成下载记录，要进行初始化
            new Thread(new Runnable() {
                @Override
                public void run() {
                    init(model.downLoadPath, model);
                }
            }).start();
        } else {
            //之前就有相应的未完成下载记录，不用初始化，直接开始下载
            startDownload(model);
        }
    }

    /**
     * 得到downloader里的信息
     * 首先进行判断是否是第一次下载，如果是第一次就要进行初始化，并将下载器的信息保存到数据库中
     * 如果不是第一次下载，那就要从数据库中读出之前下载的信息（起始位置，结束为止，文件大小等），并将下载信息返回给下载器
     *
     * @return
     */
    public LoadInfo getDownloaderInfors(String downPath, DecorationModel model) {
        if (threadDataBase.isNotExist(model.skinId)) {
            LogUtils.d("aaaa:  文件大小fileSize:  " + fileSize);
            int range = fileSize / threadCount;//设置每个线程应该下载的长度
            LogUtils.d("aaaa: range is:" + range);
            infos = new ArrayList<DownloadInfo>();//List<DownloadInfo>infos 里装的是每条线程的下载信息
            //初始化线程信息集合器,初始化每条线程的信息,为每条线程分配开始下载位置，结束位置
            for (int i = 0; i < threadCount - 1; i++) {
                //startPos是每条线程数乘以每条线程应该下载的长度,第0条,从0开始
                //endPos要减去1Byte是因为,不减1byte的地方是下一个线程开始的位置
                DownloadInfo info = new DownloadInfo(model.skinId, i, i * range, (i + 1) * range - 1, 0, downPath);
                LogUtils.d("aaaa: set threaid：" + info.getThreadId() + "startPos:" + info.getStartPos() + "endPos:" + info.getEndPos() + "CompeleteSize:" + info.getCompeleteSize() + "url:" + info.getUrl());
                infos.add(info);//把每条线程的信息加入到infos这个线程信息集合器里
            }
            //这里加入最后1个线程的信息,只所以单独拿出来是因为最后一条线程下载的结束位置应该为fileSize
            DownloadInfo info = new DownloadInfo(model.skinId, threadCount - 1, (threadCount - 1) * range, this.fileSize, 0, downPath);
            LogUtils.d("aaaa: set threaid：" + info.getThreadId() + "startPos:" + info.getStartPos() + "endPos:" + info.getEndPos() + "CompeleteSize:" + info.getCompeleteSize() + "url:" + info.getUrl());
            infos.add(info);
            //创建一个LoadInfo对象记载下载器的具体信息
            LoadInfo loadInfo = new LoadInfo(this.fileSize, 0, downPath);
            return loadInfo;
        } else {
            //如果不是第1次下载,得到数据库中已有的urlstr的下载器的具体信息
            infos = threadDataBase.getInfos(model.skinId);
            int size = 0;
            int completeSize = 0;
            for (DownloadInfo info : infos) {
                completeSize += info.getCompeleteSize();//把每条线程下载的长度累加起来,得到整个文件的下载长度
                size += info.getEndPos() - info.getStartPos() + 1;//计算出文件的大小,用每条线程的结束位置减去开始下载的位置,等于每条线程要下载的长度，然后累加
            }
            LogUtils.d("aaaa: infos: " + infos.size());
            LoadInfo loadInfo = new LoadInfo(size, completeSize, downPath);
            return loadInfo;
        }
    }

    /**
     * 初始化,要干的事:1.得到下载文件的长度
     * 2.在给定的保存路径创建文件,设置文件的大小
     */
    private void init(final String downPath, final DecorationModel model) {
        try {
            String savePath = getDownloadPath();//保存地址
            String fileName = model.getTempFileName();//文件名
            URL url = new URL(downPath);//通过给定的下载地址得到一个url
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//得到一个http连接
            conn.setConnectTimeout(5 * 1000);//设置连接超时为5秒钟
            conn.setRequestMethod("GET");//设置连接方式为GET
            //如果http返回的代码是200或者206则为连接成功
            LogUtils.d("aaaa: 初始化下载文件的大小请求码： " + conn.getResponseCode());
            int code = conn.getResponseCode();
            if (code >= 200 && code < 400) {
                //得到文件的大小
                fileSize = conn.getContentLength();
                LogUtils.d("aaaa: fileSize: " + fileSize);
                if (fileSize <= 0) {
                    LogUtils.d("aaaa: 文件大小没读到");
                    setIntExcationInfo(model);
                    return;
                }
                conn.disconnect();
                loadFile(savePath, fileName, model);
            } else {
                LogUtils.d("aaaa: 返回码 失败");
                setIntExcationInfo(model);
            }
        } catch (Exception e) {
            LogUtils.d("aaaa: 初始化文件的时候报异常了");
            setIntExcationInfo(model);
            e.printStackTrace();
        }
    }

    public void loadFile(String savePath, String fileName, DecorationModel model) {
        try {
            File dir = new File(savePath);
            //判斷文件夾是否存在，不存在則創建
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    LogUtils.d("aaaa:  mkdirs success.");
                }
            }
            File file = new File(savePath, fileName);
            RandomAccessFile randomFile = new RandomAccessFile(file, "rwd");
            randomFile.setLength(fileSize);//设置保存文件的大小
            randomFile.close();
            Message message = new Message();
            message.obj = model;
            message.what = 2;
            mHandler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            ++intCount;
            if (intCount < 4) {
                loadFile(savePath, fileName, model);
            } else {
                setIntExcationInfo(model);
            }
        }
    }

    /**
     * 改变文件状态的方法，如果文件正在下载，就  会暂停，如果暂停则开始下载
     * *
     */
    public void setState(DecorationModel model) {
        String fileName = model.getFileName();
        Downloader downloader = downloaders.get(model.getFileName());
        if (downloader != null) {
            int state = ListState.state.get(fileName);
            if (state == SkinDownloadType.SKIN_DOWNLOADING) {
                //正在下载则暂停
                state = SkinDownloadType.SKIN_PAUSE;
                ListState.state.put(fileName, state);
                statusDataBase.updateStatusModel(model, SkinDownloadType.SKIN_PAUSE);
                LogUtils.d("aaaa: 暂停: " + fileName);
            } else if (state == SkinDownloadType.SKIN_PAUSE) {
                //已经停止就开始下载
                reStartDownload(model);
                LogUtils.d("aaaa: 继续下载: " + fileName);
            }
        } else{
            //如果downloaders中没有url的数据,肯定是处于暂停状态，直接开始下载
            LogUtils.d("aaaa: 没有下载数据");
            ListState.state.put(fileName, SkinDownloadType.SKIN_PAUSE);
            reStartDownload(model);
        }
    }

    /**
     * 重新下载方法，如果下载暂停了，调用此方法重新下载
     * *
     */
    public void reStartDownload(DecorationModel model) {
        String downPath = model.downLoadPath;
        String savePath = getDownloadPath();//保存地址
        downloader = downloaders.get(model.getFileName());
        if (downloader == null) {
            downloader = new Downloader(model.getFileName(), savePath, model, isSkin, threadCount, this, mHandler);
            downloaders.put(model.getFileName(), downloader);//创建完一个新的下载器,必须把它加入到下载器集合里去
        }
        //LoadInfo是一个实体类,里面封装了一些下载所需要的信息,每个loadinfo对应1个下载器
        LoadInfo loadInfo = getDownloaderInfors(downPath, model);
        ListState.completeSizes.put(model.getFileName(), loadInfo.getComplete());
        ListState.fileSizes.put(model.getFileName(), loadInfo.getFileSize());
        // 调用方法开始下载
        downloader.download(infos);
    }

//    private void deleteData(DecorationModel model) {
//        String url = model.getFileName();
//        LogUtils.d("aaaa: 刪除" + url);
//        threadDataBase.deleteThreadData(model);//刪除數據庫裏面所有關於這個url的下載信息
//        statusDataBase.deleteStatusmModel(model);
//        if (downloaders.get(url) != null) {
//            LogUtils.d("aaaa; 刪除" + url + "删除数据库信息");
//            downloaders.remove(url);
//        }
//
//        if (ListState.completeSizes.get(url) != null) {
//            ListState.completeSizes.put(url, 0);
//            //這裡之所以不用completeSizes.remove(url)，是因為這裡執行之後，上面的Handler還在執行，
//            //那樣的話上面的Handler 就會找不到completeSizes.get(url)，會報空指針異常
//            LogUtils.d("aaaa: 清空" + url + "已下载长度");
//        }
//        if (ListState.fileSizes.get(url) != null) {
//            ListState.fileSizes.put(url, 0);
//            LogUtils.d("aaaa: 清空" + url);
//        }
//        if (mListener != null) {
//            mListener.onNitifation(0);
//        }
//    }

    //    public void deleteSDCardFolder(File dir) {
//        File to = new File(dir.getAbsolutePath() + System.currentTimeMillis());
//        dir.renameTo(to);
//        if (to.isDirectory()) {
//            String[] children = to.list();
//            for (int i = 0; i < children.length; i++) {
//                File temp = new File(to, children[i]);
//                if (temp.isDirectory()) {
//                    deleteSDCardFolder(temp);
//                } else {
//                    boolean b = temp.delete();
//                    if (b == false) {
//                    }
//                }
//            }
//            to.delete();
//        }
//    }
}
