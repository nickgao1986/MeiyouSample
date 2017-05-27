package nickgao.com.meiyousample;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingan.seeyou.ui.view.skin.SkinManager;
import com.lingan.seeyou.ui.view.skin.ViewFactory;
import com.meiyou.framework.share.ShareListDialog;
import com.meiyou.framework.share.ShareType;
import com.meiyou.framework.share.ShareTypeChoseListener;
import com.meiyou.framework.share.data.BaseShareInfo;

import java.util.List;

import nickgao.com.framework.utils.DeviceUtils;

/**
 * 帖子详情分享对话框
 * Author: hyx
 * Date: 9/23/16 09:47.
 */
public class TopicDetailShareDialog extends ShareListDialog {
    private List<TopicDetailAction> mActions;
    private TopicActionChooseListener mActionChooseListener;

    /**
     * @param activity             activity
     * @param actions              操作帖子models，删帖追帖等
     * @param shareInfoDO          分享信息
     * @param actionChooseListener 操作帖子回调
     */
    public TopicDetailShareDialog(Activity activity, List<TopicDetailAction> actions, BaseShareInfo shareInfoDO, ShareTypeChoseListener choseListener, TopicActionChooseListener actionChooseListener) {
        super(activity, shareInfoDO, choseListener);
        this.mActions = actions;
        this.mActionChooseListener = actionChooseListener;
        initBottomViews();
    }

    @Override
    protected ShareType[] getShareTypeList() {
        return new ShareType[]{ShareType.WX_CIRCLES, ShareType.WX_FRIENDS, ShareType.QQ_FRIENDS, ShareType.QQ_ZONE, ShareType.SINA};

    }

    /**
     * 初始化底部View控件，不去Override父控件里的initBottomView()，执行顺序有问题
     */
    private void initBottomViews() {
        if(mActions == null || mActions.size() == 0){
            hsViewBottom.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            return;
        }
        hsViewBottom.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);

        if (mActions != null) {
            for (int i = 0; i < mActions.size(); i++) {
                final TopicDetailAction action = mActions.get(i);
                View view = ViewFactory.from(activity).getLayoutInflater().inflate(R.layout.layout_share_text_view, null);
                ImageView ivShare = (ImageView) view.findViewById(R.id.ivShare);
                SkinManager.getInstance().setDrawableBackground(ivShare, action.getIconId());
                final TextView tvShare_top = (TextView) view.findViewById(R.id.tvShare);
                SkinManager.getInstance().setTextColor(tvShare_top, R.color.black_b);
                tvShare_top.setText(action.getTitleId());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.rightMargin = DeviceUtils.dip2px(activity, 10);
                layoutBottom.addView(view, params);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        if (mActionChooseListener != null) {
                            mActionChooseListener.onChoose(action);
                        }
                    }
                });
            }
        }
    }

    public interface TopicActionChooseListener {
        void onChoose(TopicDetailAction action);
    }
}


