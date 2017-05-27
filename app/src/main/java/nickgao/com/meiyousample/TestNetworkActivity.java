package nickgao.com.meiyousample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.meiyou.framework.share.Config;
import com.meiyou.framework.share.ShareType;
import com.meiyou.framework.share.ShareTypeChoseListener;
import com.meiyou.framework.share.SocialService;
import com.meiyou.framework.share.data.BaseShareInfo;
import com.meiyou.framework.share.data.ShareImage;

import org.reactivestreams.Subscription;

import nickgao.com.framework.utils.LogUtils;
import nickgao.com.framework.utils.StringUtils;


/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class TestNetworkActivity extends Activity {

    private Activity mActivity;
    private static final String TAG = "TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_network_layout);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        mActivity = this;
        initSocialService();
        showDialog();
    }


    private void initSocialService() {
        try {

            Config config = SocialService.getInstance().getConfig();
            config.initAppConf(ShareType.SINA, GlobalConfig.SINA_APPKEY, GlobalConfig.SINA_SECRET, GlobalConfig.SINA_REDIRECT_URI);
            config.initAppConf(ShareType.QQ_ZONE, GlobalConfig.QZONE_CLIENT_ID, GlobalConfig.QZONE_CLIENT_SECRET);
            config.initAppConf(ShareType.WX_CIRCLES, GlobalConfig.WX_APP_ID, GlobalConfig.WX_APP_SECRET);
            config.initAppConf(ShareType.WX_FRIENDS, GlobalConfig.WX_APP_ID, GlobalConfig.WX_APP_SECRET);
            SocialService.getInstance().prepare(this);
            LogUtils.d(TAG, "--->initNoNeedAtApplicationOrRecovery initSocialService ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showDialog() {
        final BaseShareInfo shareInfo = new BaseShareInfo(); //先随便给一个，onChoose的时候再确定
        TopicDetailShareDialog dialog = new TopicDetailShareDialog(mActivity, null, shareInfo, new ShareTypeChoseListener() {
            @Override
            public BaseShareInfo onChoose(ShareType shareType, BaseShareInfo shareInfoDO) {
                shareInfoDO.setContent("测试图片");
                ShareImage image = new ShareImage();
                image.setImageUrl("http://sc.seeyouyima.com/android-forumPublish-10310211-1495866710284_720_1280.jpg");
                shareInfo.setShareMediaInfo(new ShareImage());
                shareInfoDO.setLocation("003");
                String umengEventString = "";
                switch (shareType) {

                    default:
                        break;
                }
                if (!StringUtils.isNull(umengEventString)) {
                }
                return shareInfoDO;
            }
        }, null);
        dialog.show();
    }


    private Subscription mSubscription;

    public void request(long n) {
        mSubscription.request(n); //在外部调用request请求上游
    }

//    public void demo3() {
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                Log.d(TAG, "emit 1");
//                emitter.onNext(1);
//                Log.d(TAG, "emit 2");
//                emitter.onNext(2);
//                Log.d(TAG, "emit 3");
//                emitter.onNext(3);
//                Log.d(TAG, "emit complete");
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Integer>() {
//
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
//                        mSubscription = s;  //把Subscription保存起来
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.d(TAG, "onNext: " + integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });
//    }

//    private void test() {
//        List<Course> courses = new ArrayList<>();
//        Course course1 = new Course("c1");
//        Course course2 = new Course("c2");
//        courses.add(course1);
//        courses.add(course2);
//
//        Student student1 = new Student(11, "aa", courses);
//
//
//        List<Course> courses11 = new ArrayList<>();
//        Course course3 = new Course("c3");
//        courses11.add(course3);
//
//        Student student2 = new Student(22, "bb", courses11);
//
//
//        Student[] students = {student1, student2};
//
//    }


    private void sendRequest() {
        //NewsReviewService.sendRequest(this);
//        DynamicService service = (DynamicService) ServiceFactory.getInstance().getService(DynamicService.class.getName());
//        service.sendRequest();
//        UserHomeService service1 = (UserHomeService) ServiceFactory.getInstance().getService(UserHomeService.class.getName());
//        service1.sendRequest();
    }
}
