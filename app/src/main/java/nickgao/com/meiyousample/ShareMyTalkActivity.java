package nickgao.com.meiyousample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.CustomEditText;
import com.lingan.seeyou.ui.view.ResizeLayout;
import com.lingan.seeyou.ui.view.widget.EmojiLayout;

import activity.PeriodBaseActivity;
import nickgao.com.framework.utils.DeviceUtils;
import nickgao.com.meiyousample.event.NewsWebViewEvent;
import nickgao.com.meiyousample.model.reply.TopicModelItem;
import nickgao.com.okhttpexample.view.ImageLoadParams;
import nickgao.com.okhttpexample.view.LoaderImageView;

/**
 * Created by gaoyoujian on 2017/5/17.
 */

public class ShareMyTalkActivity extends PeriodBaseActivity {

    private static final String TAG = "ShareMyTalkActivity";
    private Activity mActivity;
    private CustomEditText editContent;
    private ResizeLayout resizeLayout;
    private View viewBottom;
    private View linearEmptyPhoto;
    private ImageView ivEmoji;
    private LoaderImageView ivSharePic;
    private TextView tvShareContent;
    private EmojiLayout emojiLayout;
    private TopicModelItem topicModel = null;
    //private ToolsTipModel tipModel = null;
    private TextView tvShareCategoryTitle;
    public int type;//0代表是帖子分享 1代表是贴士分享
    private String mBlockIconUrl;
    //
    private ImageLoadParams imageLoadParams;


    @Override
    public int getLayoutId() {
        return R.layout.layout_share_my_talk;

    }


    public void onEventMainThread(NewsWebViewEvent webViewEvent) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) {
            overridePendingTransition(R.anim.activity_bottom_in,
                    R.anim.activity_animation_none);
        }
        mActivity = this;
//        getIntentData();
        intUI();
//        intLogic();
        setListenner();

    }

    private void intUI() {

        getTitleBar().setLeftTextViewString(R.string.cancel);
        getTitleBar().setRightTextViewString(R.string.publish);
        getTitleBar().setTitle("分享到我的动态");
        getTitleBar().setLeftButtonRes(-1);

        tvShareCategoryTitle = (TextView) findViewById(R.id.tvShareCategoryTitle);
        tvShareContent = (TextView) findViewById(R.id.tvShareContent);
        //ivEmoji = (ImageView) findViewById(R.id.ivEmoji);
        ivSharePic = (LoaderImageView) findViewById(R.id.ivSharePic);
        editContent = (CustomEditText) findViewById(R.id.publish_et_content);
        resizeLayout = (ResizeLayout) findViewById(R.id.rootContainer);
        viewBottom = findViewById(R.id.viewBottom);
        viewBottom.setVisibility(View.GONE);
        linearEmptyPhoto = findViewById(R.id.linearEmptyPhoto);
        linearEmptyPhoto.setVisibility(View.VISIBLE);

        emojiLayout = (EmojiLayout) findViewById(R.id.emojiLayout);
        emojiLayout.setAnimation(true);
        emojiLayout.setShowCustomExpression(false);
        tvShareContent.requestFocus();
        //
        imageLoadParams = new ImageLoadParams();
        imageLoadParams.height = DeviceUtils.dip2px(mActivity, 60);
        imageLoadParams.width = DeviceUtils.dip2px(mActivity, 60);
        imageLoadParams.defaultholder = R.drawable.apk_meetyou_two;
    }



    private void setListenner() {
        resizeLayout.setOnKeyboardListener(new ResizeLayout.OnKeyboardListener() {
            @Override
            public void onKeyboardHide() {
                viewBottom.setVisibility(View.GONE);
            }

            @Override
            public void onKeyboardShow() {
                viewBottom.setVisibility(View.GONE);
                if (null != emojiLayout) {
                    emojiLayout.hideEmojiView();
                }
            }
        });
        getTitleBar().setLeftTextViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // handleFinish();
            }
        });

        getTitleBar().setRightTextViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  handleShareToMyTalk();
            }
        });

        //emojiLayout.setIbEmojiKeyboard(ivEmoji);
        emojiLayout.setEtContent(editContent);
        emojiLayout.setActivity(this);
        emojiLayout.setOnEmojiViewShowListener(new EmojiLayout.OnEmojiViewShowListener() {
            @Override
            public void onShow() {
                try {
                    ivEmoji.setImageResource(R.drawable.apk_sent_keyboard_new);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHide() {
                try {
                    ivEmoji.setImageResource(R.drawable.apk_sent_emotion_new);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



}
