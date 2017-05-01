package nickgao.com.meiyousample.skin;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

import nickgao.com.meiyousample.R;

/**
 * Created by WenHui on 2015/6/26.
 * 处理toast相关
 */
public class ToastUtils {
    private static final java.lang.String TAG = "ToastUtils";
    private static Toast mToast;
    private static View toastRoot;
    private static Handler mHandler;


    public static void showToast(final Context context, final String content) {
        try {
            if (TextUtils.isEmpty(content))
                return;

            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            /*if (mToast == null) {
                                mToast = Toast.makeText(context.getApplicationContext(), content, Integer.valueOf(300));
                            }
                            mToast.setText(content);
                            mToast.setGravity(Gravity.CENTER, 0, 0);
                            mToast.show();*/

                            handleToast(context.getApplicationContext(), content);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                });
            } else {
                if(mHandler == null){
                    mHandler = new Handler(Looper.getMainLooper());
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        handleToast(context, content);
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void handleToast(Context context, String content) {
        //show(context,content);
        try {
            if(mToast == null){
                mToast = new Toast(context.getApplicationContext());
            }
            if (toastRoot == null) {
                toastRoot = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_toast, null);
            }
            mToast.setView(toastRoot);
            TextView tv = (TextView) toastRoot.findViewById(R.id.tvToast);
            tv.setText(content);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void handleToast(Context context, int content) {
        handleToast(context, context.getResources().getString(content));
    }

    public static void showToast(final Context context, final int content) {
        try {
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            /*if (mToast == null) {
                                mToast = Toast.makeText(context.getApplicationContext(), content, Integer.valueOf(300));
                            }
                            mToast.setText(content);
                            mToast.setGravity(Gravity.CENTER, 0, 0);
                            mToast.show();*/
                            handleToast(context, content);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                });
            } else {
                /*if (mToast == null) {
                    mToast = Toast.makeText(context.getApplicationContext(), content, Integer.valueOf(300));
                }
                mToast.setText(content);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();*/
                if(mHandler == null){
                    mHandler = new Handler(Looper.getMainLooper());
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        handleToast(context, content);
                    }
                });
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 自定义ToastView;为什么,因为要对ToastView做动画;
     */
    private static boolean mIsShow = false;
    private static WindowManager.LayoutParams mParams;
    private static Timer mTimer;
    private static WindowManager mWdm;
    private static View mToastView;



    /*private static void show(Context context,String text){
        try {
            if(Thread.currentThread() != Looper.getMainLooper().getThread()){
                LogUtils.e(TAG,"不在主线程,不进行Toast");
                return;
            }
            if(mIsShow && mWdm!=null && mToastView!=null &mTimer!=null){
                mWdm.removeView(mToastView);
                mTimer.cancel();
                mIsShow = false;
            }
            if(!mIsShow){//如果Toast没有显示，则开始加载显示
                if(mWdm==null)
                    mWdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                //通过Toast实例获取当前android系统的默认Toast的View布局
                mToastView = Toast.makeText(context, text, Toast.LENGTH_SHORT).getView();
                mTimer = new Timer();
                //设置布局参数
                setParams();

                mIsShow = true;
                mWdm.addView(mToastView, mParams);//将其加载到windowManager上
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            mWdm.removeView(mToastView);
                            mIsShow = false;
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }, 1500);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void setParams() {
        try {
            mParams = new WindowManager.LayoutParams();
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.format = PixelFormat.TRANSLUCENT;
            mParams.windowAnimations = R.style.ToastAnimationStyle;//设置进入退出动画效果
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            mParams.gravity = Gravity.CENTER_HORIZONTAL;
            mParams.y = 250;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }*/

}
