package com.meetyou.crsdk.util;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Author: lwh
 * Date: 4/6/16 10:28.
 */
public class ViewUtil {

    private static final java.lang.String TAG = "ViewUtil";

    /**
     * 设置textview最大显示几行
     *
     * @param textView
     * @param title
     * @param maxTextViewWidth
     * @param canSetTwoLine
     * @return
     */
    public static int setTextViewLineLimit(TextView textView, String title, int maxTextViewWidth, boolean canSetTwoLine) {
        try {
            TextPaint mTextPaint = textView.getPaint();
            float mTextViewWidth = mTextPaint.measureText(title);
            float lines = mTextViewWidth / maxTextViewWidth;
            if (lines > 1 && canSetTwoLine) {
                textView.setMaxLines(2);
                //textView.setEllipsize(TextUtils.TruncateAt.END);
                return 2;
            } else {
                textView.setMaxLines(1);
                //textView.setLines(1);
                //textView.setEllipsize(TextUtils.TruncateAt.END);
                return 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 2;
    }

    /**
     * 获取textview行数
     *
     * @param textView
     * @param title
     * @param maxTextViewWidth
     * @return
     */
    public static int getTextViewLineCount(TextView textView, String title, int maxTextViewWidth) {
        try {
            StaticLayout e = new StaticLayout(title, textView.getPaint(), maxTextViewWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            int lines = e.getLineCount();
            return lines;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static String convertStringIfOverTwoLine(TextView textView, String orginString, int maxTextViewWidth) {
        try {
            //计算单个字的宽度
            float sigleWidth = textView.getPaint().measureText("好");
            double tempWidth = Math.max(sigleWidth, 0.0);
            int width = (int) (tempWidth + 0.5);
            //单行字数
            int countForOneLine = maxTextViewWidth / width;
            int length = orginString.length();
           // LogUtils.d(TAG, "单个字宽度为:" + width + "-->最长宽度为:" + maxTextViewWidth + "-->单行字数为:" + countForOneLine + "-->原始长度字数为:" + length);
            //字符串截取
            if ((countForOneLine * 2) < length) {
                String result = orginString.substring(0, (countForOneLine * 2));
                //计算字符串中标题符号数量(为了不留明显的尾部空隙,必须把标点符号的位置补齐)
                String orginStringTest = result;
                orginStringTest = filterFuhao(orginStringTest);
                int fuhaoCount = result.length() - orginStringTest.length();
                //三个符号认为宽度认为是1个字符的宽度
                int addCount = fuhaoCount / 3;
               // LogUtils.d(TAG, "此字符串含有符号数:" + fuhaoCount + "-->追加字数为:" + addCount + "-->此时内容为:" + orginString.substring(0, (countForOneLine * 2)));
                if (fuhaoCount > 0 && (countForOneLine * 2 + addCount) < length) {
                    result = orginString.substring(0, (countForOneLine * 2 + addCount));
                }
                result += "...";
                return result;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return orginString;
    }

    private static String filterFuhao(String orginString) {
        try {
            String str = orginString.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
            if (str == null)
                str = orginString;
            return str;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return orginString;
    }

    /**
     * 获取列表某项显示区域的xy, 目前仅用于列表获取video显示区域
     *
     * @param listView     列表
     * @param position     第几项
     * @param itemHeight   video的高度
     * @param bottomMargin 距离bottom的距离
     * @return
     */
    public static int[] getListViewVisiableRect(ListView listView, View videoView, int position, int itemHeight, int titleHeight, int bottomMargin) {
        int start = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount();
        int end = listView.getLastVisiblePosition() - listView.getHeaderViewsCount();
        View current = null;
        boolean isInvisible = false;
        int x = 0;
        int y = 0;
        if (position >= start && position <= end) {
            isInvisible = true;
            current = listView.getChildAt(position - start);
        }
        if (isInvisible) {
            if (current != null) {
                x = 1;
                y = current.getBottom() - itemHeight - bottomMargin + titleHeight;
            } else {
                int[] location = new int[2];
                videoView.getLocationOnScreen(location);
                x = 1;
                y = location[1];
            }
        }
        return new int[]{x, y};
    }

    /**
     * 获取列表某项显示区域的xy, 目前仅用于列表获取video显示区域
     *
     * @param listView 列表
     * @param position 第几项
     * @return
     */
    public static boolean isListViewVideoVisiable(ListView listView, int position) {
        int start = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount();
        int end = listView.getLastVisiblePosition() - listView.getHeaderViewsCount();
        boolean isInvisible = false;
        if (position >= start && position <= end) {
            isInvisible = true;
        }
        return isInvisible;
    }

    /**
     * 获取列表某项显示区域的xy, 目前仅用于列表获取video显示区域
     *
     * @param recyclerView     列表
     * @param position     第几项
     * @param itemHeight   video的高度
     * @param bottomMargin 距离bottom的距离
     * @return
     */
//    public static int[] getRecyclerViewVisiableRect(RecyclerView recyclerView, View videoView, int position, int itemHeight, int titleHeight, int bottomMargin, int headerViewCount) {
//        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (layoutManager instanceof LinearLayoutManager) {
//            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//            int start = linearManager.findFirstVisibleItemPosition() - headerViewCount;
//            int end = linearManager.findLastVisibleItemPosition() - headerViewCount;
//            View current = null;
//            boolean isInvisible = false;
//            int x = 0;
//            int y = 0;
//            if (position >= start && position <= end) {
//                isInvisible = true;
//                current = recyclerView.getChildAt(position - start);
//            }
//            if (isInvisible) {
//                if (current != null) {
//                    x = 1;
//                    y = current.getBottom() - itemHeight - bottomMargin + titleHeight;
//                } else {
//                    int[] location = new int[2];
//                    videoView.getLocationOnScreen(location);
//                    x = 1;
//                    y = location[1];
//                }
//            }
//            return new int[]{x, y};
//        } else {
//            int[] location = new int[2];
//            videoView.getLocationOnScreen(location);
//            return new int[]{location[0], location[1]};
//        }
//    }

    /**
     * 获取列表某项显示区域的xy, 目前仅用于列表获取video显示区域
     *
     * @param recyclerView 列表
     * @param position 第几项
     * @param headerViewCount 头视图数量
     * @return
     */
//    public static boolean isListViewVideoVisiable(RecyclerView recyclerView, int position, int headerViewCount) {
//        boolean isInvisible = false;
//        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (layoutManager instanceof LinearLayoutManager) {
//            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//            int start = linearManager.findFirstVisibleItemPosition() - headerViewCount;
//            int end = linearManager.findLastVisibleItemPosition() - headerViewCount;
//            if (position >= start && position <= end) {
//                isInvisible = true;
//            }
//        }
//        return isInvisible;
//    }
}
