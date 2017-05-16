package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.EmojiConversionUtil;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-3-5
 * Time: 下午2:15
 * To change this template use File | Settings | File Templates.
 */
public class CustomUrlTextView extends TextView {
    //        public static final String REGEX_URL = "(http|ftp|https):\\/\\/[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
//    public static final String REGEX_URL = "([http|https:\\/\\/]*)(([0-9]{1,3}\\.){3}[0-9]{1,3}|([0-9a-z_!~*\\'()-]+\\.)*([0-9a-zA-Z-]*)\\.(aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|cz|de|dj|dk|dm|do|dz|ec|ee|eg|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mn|mn|mo|mp|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|nom|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ra|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sj|sk|sl|sm|sn|so|sr|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw|arpa))(\\/[0-9a-zA-Z\\.\\?\\@\\&\\=\\#\\%\\_\\:\\$]*)*";
    public static final String REGEX_URL = "([[Hh][Tt][Tt][Pp]|[Hh][Tt][Tt][Pp][Ss]:\\/\\/]*)(([0-9]{1,3}\\.){3}[0-9]{1,3}|([0-9a-zA-Z_!~*\\'()-]+\\.)*([0-9a-zA-Z-]*)\\.(aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cx|cy|cz|cz|de|dj|dk|dm|do|dz|ec|ee|eg|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mn|mn|mo|mp|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|nom|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ra|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sj|sk|sl|sm|sn|so|sr|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw|arpa))(\\/[0-9a-zA-Z\\.\\?\\@\\&\\=\\#\\%\\_\\:\\$]*)*";
    public static final String REGEX_STYLE = "<a[^>]*?>[\\s\\S]*?<\\/a>";

    private int blockId;
    private boolean isLinkClick;
    private int urlColorId = 0; // url链接的颜色id

    public CustomUrlTextView(Context context) {
        super(context);
    }

    public CustomUrlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomUrlTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 显示html格式字符串
     * text = text + "<a href=\"http://www.baidu.com?__type=2\">百度内嵌</a>";
     * text = text + "<a href=\"http://www.baidu.com?__type=1\">百度外链</a>";
     *
     * @param text
     */
    public void setHtmlText(String text) {
        if (StringUtils.isNull(text)) {
            return;
        }
        try {
            if (!isContainCustomUrl(text)) {
                ArrayList<String> urls = getUrlFromString(text);
                if (null != urls && urls.size() > 0) {
                    for (String url : urls) {
                        text = replaceUrl(text, url, "网页链接");
                    }
                } else {
                    setText(text);
                    return;
                }
            }

            text = text.replaceAll("\n", "<br/>");//换行符 转换
            super.setText(Html.fromHtml(text));
            this.setMovementMethod(LinkMovementClickMethod.getInstance());
            CharSequence chars = this.getText();
            if (chars instanceof Spannable) {
                int end = chars.length();
                Spannable sp = (Spannable) this.getText();
                URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                SpannableString style = new SpannableString(chars);
                for (URLSpan url : urls) {
                    style.removeSpan(url);
                    MyURLSpan myURLSpan = new MyURLSpan(getContext(), url.getURL(), blockId, urlColorId);
                    style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                this.setText(EmojiConversionUtil.getInstace().getExpressionString(getContext(), style, 25, 25));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            super.setText(getText().toString());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 只解析动态url，服务端控制
     *
     * @param text
     */
    public void setHtmlUrlTextDynamic(String text, boolean isParseUrl) {
        if (StringUtils.isNull(text)) {
            return;
        }
        //判断是否需要解析url
        //if (!ApplicationController.getInstance().getIsParseShuoshuoUrl(getContext())) {
        if (!isParseUrl) {
            setText(text);
            return;
        }
        paraseUrl(text);
    }

    /**
     * @param text
     * @param isParase 是否需要解析url
     */
    public void setHtmlUrlText(String text, boolean isParase) {
        if (StringUtils.isNull(text)) {
            return;
        }
        if (isParase) {
            paraseUrl(text);
        } else {
            setText(text);
        }
    }


    public void setHtmlUrlText(String text) {
        if (StringUtils.isNull(text)) {
            return;
        }
        paraseUrl(text);
    }


    private void paraseUrl(String text) {
        try {
            if (!isContainCustomUrl(text)) {
                ArrayList<String> urls = getUrlFromString(text);

                if (null != urls && urls.size() > 0) {
                    for (String url : urls) {
                        text = replaceUrl(text, url);
                    }
                } else {
                    setText(text);
                    return;
                }
            }

            text = text.replaceAll("\n", "<br/>");//换行符 转换
            super.setText(Html.fromHtml(text));
            this.setMovementMethod(LinkMovementClickMethod.getInstance());
            CharSequence chars = this.getText();
            if (chars instanceof Spannable) {
                int end = chars.length();
                Spannable sp = (Spannable) this.getText();
                URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                SpannableString style = new SpannableString(chars);
                for (URLSpan url : urls) {
                    style.removeSpan(url);
                    MyURLSpan myURLSpan = new MyURLSpan(getContext(), url.getURL(), blockId, urlColorId);
                    style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                setText(style);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            super.setText(getText().toString());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析html标签和表情
     *
     * @param text
     */
    public void setOnlyHtmlText(String text) {
//        text = text + "<font color='#e18c96'>红色字</font>" + "哈哈";
        if (StringUtils.isNull(text)) {
            return;
        }
        try {
            text = text.replaceAll("\n", "<br/>");//换行符 转换
            super.setText(Html.fromHtml(text));
            this.setMovementMethod(LinkMovementClickMethod.getInstance());
            CharSequence chars = this.getText();
            if (chars instanceof Spannable) {
                int end = chars.length();
                Spannable sp = (Spannable) this.getText();
                URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                SpannableString style = new SpannableString(chars);
                for (URLSpan url : urls) {
                    style.removeSpan(url);

                    MyURLSpan myURLSpan = new MyURLSpan(getContext(), url.getURL(), blockId, urlColorId);
                    style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                setText(style);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            super.setText(getText().toString());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



   /* private OnClickListener onClickListener;

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }*/

    /**
     * 设置text并过滤url
     *
     * @param text
     */

    public void setTextFilterUrl(String text) {
        try {
            if (StringUtils.isNull(text)) {
                return;
            }
            setText(removeUrl(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setText(String text) {
        try {
            super.setText(EmojiConversionUtil.getInstace().getExpressionString(getContext(), text, (int) getResources().getDimension(R.dimen.list_icon_height_22), (int) getResources().getDimension(R.dimen.list_icon_height_22)));    //To change body of overridden methods use File | Settings | File Templates.
        } catch (ArrayIndexOutOfBoundsException e) {
            super.setText(getText().toString());
            e.printStackTrace();
        }
    }

    public void setText(SpannableString text) {
        try {
            super.setText(EmojiConversionUtil.getInstace().getExpressionString(getContext(), text, (int)getResources().getDimension(R.dimen.list_icon_height_22),(int)getResources().getDimension(R.dimen.list_icon_height_22)));
        } catch (ArrayIndexOutOfBoundsException e) {
            super.setText(getText().toString());
            e.printStackTrace();
        }
    }

    public void setStringText(String text) {
        super.setText(text);
    }

    public void setStringText(SpannableString text) {
        super.setText(text);
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public ArrayList getUrlFromString(String text) {
        ArrayList links = new ArrayList();
        Pattern p = Pattern.compile(REGEX_URL);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String urlStr = m.group();
            if (urlStr.endsWith(")")) {
                urlStr = urlStr.substring(0, urlStr.length() - 1);
            }
            links.add(urlStr);
        }
        return links;
    }

    /**
     * url格式化
     *
     * @param text
     * @param url
     * @param mark
     * @return
     */
    public String replaceUrl(String text, String url, String mark) {
        // <a href="http://www.baidu.com">百度</a>
//        String formater = "<a href=\"%s\">%s</a>";
//        String htmlUrl = String.format(formater,url,mark);
        String htmlUrl = "<a href=\"" + url + "\">" + mark + "</a>";
        text = text.replace(url, htmlUrl);
        return text;
    }

    private String replaceUrl(String text, String url) {

        String htmlUrl = "<a href=\"" + url + "\">" + url + "</a>";
        text = text.replace(url, htmlUrl);
        return text;
    }


    private String removeUrl(String text) {
        Pattern p = Pattern.compile(REGEX_STYLE);
        Matcher m = p.matcher(text);
        text = m.replaceAll("");

        Pattern pUrl = Pattern.compile(REGEX_URL);
        Matcher mUrl = pUrl.matcher(text);
        text = mUrl.replaceAll("");
        return text;
    }

    private boolean isContainSubText(String text, String subText) {
        return text.contains(subText);
    }


    /**
     * 判断是否包含自定义url格式
     *
     * @param text <a href=\"http://circle.seeyouyima.com/topic/detail-web?topic_id=2715128\">经期装备大调查，妇炎洁好礼等你拿</a>
     * @return
     */
    public boolean isContainCustomUrl(String text) {
        try {
            Pattern pattern = Pattern.compile(REGEX_STYLE);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
     @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } catch (Exception e) {
            super.setText(getText().toString());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            e.printStackTrace();
        }
    }

    @Override
    public void setGravity(int gravity) {
        try {
            super.setGravity(gravity);
        } catch (Exception e) {
            super.setText(getText().toString());
            super.setGravity(gravity);
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isLinkClick = false;
        return super.onTouchEvent(event);
    }


    public boolean isLinkClick() {
        return isLinkClick;
    }

    public void setLinkClick(boolean isLinkHit) {
        this.isLinkClick = isLinkHit;
    }

    public void setUrlColorId(int id) {
        urlColorId = id;
    }
}
