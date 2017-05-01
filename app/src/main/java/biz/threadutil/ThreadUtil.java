package biz.threadutil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/4/30.
 */

public class ThreadUtil {

    public static void addTaskForDynamic(final Context context, boolean isIO, final String msg, final ITasker iHttpTasker) {
        TaskForAll task = new TaskForAll(context,msg,iHttpTasker);
        task.execute();
        //TaskManager.getInstance().sumbit(task);
    }

    public static void addTask(final Context context, final ITasker iHttpTasker) {
        TaskForAll task = new TaskForAll(context,"",iHttpTasker);
        task.execute();
        //TaskManager.getInstance().sumbit(task);
    }

    public interface ITasker {
        // 执行
        public Object onExcute();
        // 完成
        public void onFinish(Object result);

    }


    private static class TaskForAll extends AsyncTask<Void, Void, Object> {

        public ITasker itasker;
        public String mMessage;
        private ProgressDialog mProgressDialog;
        public Context mContext;
        public Activity mActivity;
        public TaskForAll(Context context,String msg,ITasker itasker){
            super();
            this.itasker =itasker;
            mMessage = msg;
            if(context instanceof Activity)
                mActivity = (Activity)context;
            mContext = context;
            if(!StringUtils.isNull(mMessage) && mActivity!=null){
                mProgressDialog = new ProgressDialog(mActivity);

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
            try {
                if (!StringUtils.isNull(mMessage)&& mActivity!=null && mProgressDialog!=null) {
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.setMessage(mMessage);
                    mProgressDialog.show();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Void[] objects) {
            try{
                return itasker.onExcute();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            try {
                super.onPostExecute(result);
                if (mProgressDialog!=null) {
                    mProgressDialog.dismiss();
                }

                itasker.onFinish(result);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        protected void onCancelled() {
            try{
                super.onCancelled();
                if (mProgressDialog!=null)
                    mProgressDialog.dismiss();
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }
    }

}
