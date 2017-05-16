package com.lingan.seeyou.ui.view.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebResourceResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import nickgao.com.framework.utils.LogUtils;

/**
 * Created by lwh on 2015/11/4.
 */
public class WebViewCacheLoader {
    private static final String TAG = "webview-WebViewCacheLoader";
    private static WebViewCacheLoader instance;
    private Context mContext;
    public WebViewCacheLoader(Context context){
        mContext = context;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WebResourceResponse imgLoaderLoad(String urlString) {
        WebResourceResponse response = null;

//        if (urlString.contains(".png") || urlString.contains(".jpg")) {
//            LogUtils.d(TAG, "shouldInterceptRequest url=" + urlString + "  threadInfo" + Thread.currentThread());
//            try {
//                final PipedOutputStream out = new PipedOutputStream();
//                PipedInputStream in = new PipedInputStream(out);
//                ImageLoadParams paramsV = new ImageLoadParams();
//                ImageLoader.getInstance().loadImage(mContext,urlString,paramsV,new AbstractImageLoader.onCallBack(){
//
//                    @Override
//                    public void onSuccess(ImageView imageView, Bitmap bitmap, String url, Object... obj) {
//                        if (bitmap != null) {
//                            try {
//                                out.write(Bitmap2Bytes(bitmap));
//                                out.close();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFail(String url, Object... obj) {
//
//                    }
//
//                    @Override
//                    public void onProgress(int total, int progess) {
//
//                    }
//
//                    @Override
//                    public void onExtend(Object... object) {
//
//                    }
//                });
//
//                /*ImageLoader.getInstance().loadImage(urlString, paramsV,new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String s, View view) {
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String s, View view, FailReason failReason) {
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String s, View view) {
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                        if (bitmap != null) {
//                            try {
//                                out.write(Bitmap2Bytes(bitmap));
//                                out.close();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });*/
//                response = new WebResourceResponse("image/png", "UTF-8", in);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        return response;
    }
/*
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WebResourceResponse NewHttpLoad(final Context ctx, final String urlString, final WebLoadResultInterface listener) {
        WebResourceResponse response = null;
        {
            try {
                TaskManager.getInstance().submit()
                TaskManager.getInstance().sumbit(new SeeyouAsyncTask(TAG, urlString, SeeyouAsyncTask.TYPE_NETWORK_NORMAL) {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        return httpLoad(urlString);
                    }

                    @Override
                    protected void onPostExecute(Object result) {
                        listener.handleWebloadResult(result);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return response;
    }*/

    /**
     * 下载指定URL的源信息
     *
     * @param urlString
     * @return
     */
    public byte[] downloadWebResoucr(String urlString) {
        return httpLoad(urlString);
    }

    /**
     * 根据URL载入网络中的流
     *
     * @param urlString
     * @return
     */
    private byte[] httpLoad(String urlString) {
        LogUtils.d(TAG, "shouldInterceptRequest url=" + urlString);
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                LogUtils.d(TAG, "HttpURLConnection  OK :"+url.toString());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int b;
                while ((b = in.read()) != -1) {
                    baos.write(b);
                }
                byte[] data = baos.toByteArray();
                return data;
            }
        } catch (final IOException e) {
            LogUtils.d(TAG, "Error in downloadBitmap - " + urlString + " : " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
            }
        }
        return null;
    }


    public class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int by_te = read();
                    if (by_te < 0) {
                        break; // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }


    private static final int IO_BUFFER_SIZE = 8 * 1024; // 8k


    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


}
