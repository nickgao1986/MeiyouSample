//package nickgao.com.meiyousample.love;
//
//import android.content.Context;
//import android.os.Handler;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.lingan.seeyou.ui.view.skin.ViewFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import activity.PeriodBaseActivity;
//import nickgao.com.meiyousample.R;
//
///**
// * Created by gaoyoujian on 2017/5/23.
// */
//
//public abstract class AnalysisBaseActivity extends PeriodBaseActivity {
//
//
//    private static final int[] ANALYSIS_IDS = new int[]{R.id.advice11,
//            R.id.advice12, R.id.advice13, R.id.advice14};
//    private static final int[] ADVICE_IDS = new int[]{R.id.advice21,
//            R.id.advice22, R.id.advice23, R.id.advice24, R.id.advice25, R.id.advice26};
//
//    /**
//     * 加建议的 方法
//     *
//     * @param res
//     * @param isShowTitle
//     * @param llAdviceContainer
//     */
//    public void fillAnalysisResult(int res, boolean isShowTitle, LinearLayout llAdviceContainer) {
//        List<Integer> list = new ArrayList<Integer>();
//        list.add(res);
//        fillAnalysisResult(list, isShowTitle, llAdviceContainer);
//    }
//
//    public void fillAnalysisResult(final List<Integer> res, final boolean showTitle, final LinearLayout llAdviceContainer) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    llAdviceContainer.removeAllViews();
//                    for (int i = 0; i < res.size(); i++) {
//                        View view = ViewFactory.from(getApplicationContext())
//                                .getLayoutInflater()
//                                .inflate(R.layout.analy_t3item, null);
//                        if (res.size() == 1 && (!showTitle || res.get(0) == 8)) {
//                            view.findViewById(R.id.advice_about_container).setVisibility(View.GONE);
//                        } else {
//                            ((TextView) view.findViewById(R.id.advice_about_count)).setText((i + 1) + "");
//                        }
//                        fillAnalysisResult(view, AnalysisConstants.getAdivices(AnalysisBaseActivity.this)
//                                .get(res.get(i)));
//                        llAdviceContainer.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
//            }
//        }, 200);
//    }
//
//    public void fillAnalysisResult(View view, String[][] res) {
//        if (res[0].length > 0) {
//            fillAnalysisContent(view, res[0]);
//        } else {
//            view.findViewById(R.id.ll_Analysis).setVisibility(View.GONE);
//            //view.findViewById(R.id.line).setVisibility(View.GONE);
//        }
//        fillAdviceContent(view, res[1]);
//        if (res.length == 3) {
//            fillAnalysisTitle(view, res[2]);
//        }
//    }
//
//    /**
//     * 填充健康分析标题
//     *
//     * @param view
//     * @param title
//     */
//    public void fillAnalysisTitle(View view, String... title) {
//        if (title.length > 0) {
//            String titleStr = title[0];
//            setText(view, R.id.advice_about, titleStr);
//        }
//    }
//
//    /**
//     * 填充健康分析
//     *
//     * @param content
//     */
//    public void fillAnalysisContent(View view, String... content) {
//        if (content.length > 0) {
//            for (int i = 0; i < ANALYSIS_IDS.length; i++) {
//                String text = i >= content.length ? null : content[i];
//                setText(view, ANALYSIS_IDS[i], text);
//            }
//        }
//    }
//
//    /**
//     * 填充健康建议
//     *
//     * @param content
//     */
//    public void fillAdviceContent(View view, String... content) {
//        if (content.length > 0) {
//            for (int i = 0; i < ADVICE_IDS.length; i++) {
//                String text = i >= content.length ? null : content[i];
//                setText(view, ADVICE_IDS[i], text);
//            }
//        }
//    }
//
//    /**
//     * 填充分析建议的内容【具有爱心图标~】
//     *
//     * @param id
//     * @param text
//     */
//    public void setText(View view, int id, String text) {
//        View container;
//        if (view == null) {
//            container = findViewById(id);
//        } else {
//            container = view.findViewById(id);
//        }
//        if (text == null) {
//            container.setVisibility(View.GONE);
//        } else {
//            if (container instanceof TextView) {
//                ((TextView) container).setText(text);
//                // // SkinEngine.getInstance().setViewTextColor(getApplicationContext(), (TextView) container, R.color.white_a);
//            } else {
//                ((TextView) container.findViewById(R.id.top_tv_count)).setText(text);
//                // // SkinEngine.getInstance().setViewTextColor(getApplicationContext(), (TextView) container.findViewById(R.id.top_tv_count), R.color.black_b);
//            }
//        }
//    }
//
//    void fillSymptomAnalysisResult(int res) {
//        try {
//            fillAnalysisResult(null, AnalysisConstants.getAdivices(this).get(res));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//    private String[] mTitles = new String[]{"趋势", "详情", "建议"};
//
//    public String[] getTabTitle() {
//        return mTitles;
//    }
//
//    public interface OnCoverDismissListener {
//        public void onDismimss();
//    }
//
//    public boolean isAnalysismengban(Context mContext, LinearLayout mCover) {
//        return true;//不显示蒙层
//       /* boolean analysismengban = AnalysisController.getInstance().IsAnalysisMengban();
//        ImageView iv = new ImageView(getApplicationContext());
//        iv.setImageResource(R.drawable.apk_anlysis_remind);
//        if (!analysismengban) {
//            setCoverView(mCover, iv, true, true, false, Gravity.CENTER, null);
//            AnalysisController.getInstance().setAnalysisMengban(true);
//        }
//        return analysismengban;*/
//    }
//
//    /**
//     * 设置蒙版层
//     */
//    private void setCoverView(final LinearLayout mCover, View v, boolean showGrayLayer, boolean clickToHide, boolean belowTitleBar, int gravity, final OnCoverDismissListener listener) {
//        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) mCover.getLayoutParams();
//        mCover.setVisibility(View.VISIBLE);
//        if (showGrayLayer) {
//            mCover.setBackgroundColor(getResources().getColor(R.color.xiyou_translucent));
//        } else {
//            mCover.setBackgroundColor(getResources().getColor(R.color.trans_color));
//        }
//        if (clickToHide) {
//            mCover.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null)
//                        listener.onDismimss();
//                    disableCover(mCover);
//                }
//            });
//        }
//        if (belowTitleBar) {
//            mCover.setLayoutParams(p);
//        }
//        mCover.setGravity(gravity);
//        mCover.addView(v);
//    }
//
//    /**
//     * 关闭蒙版层
//     */
//    public void disableCover(LinearLayout mCover) {
//        mCover.removeAllViews();
//        mCover.setVisibility(View.GONE);
//    }
//
//    /**
//     * 透明度渐变
//     */
//    public void handleTitleAlpha(View view, int mScrollY, int totalHeight) {
//        if (view == null)
//            return;
//        float alpha = (float) mScrollY / (float) totalHeight;
//        if (alpha > 1.0f) {
//            alpha = 1.0f;
//        }
//        view.setAlpha(alpha);
//    }
//
//}
