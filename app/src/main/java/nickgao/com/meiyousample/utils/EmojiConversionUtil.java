package nickgao.com.meiyousample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.SparseArray;

import com.lingan.seeyou.ui.view.CustomImageSpan;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nickgao.com.meiyousample.R;
import nickgao.com.meiyousample.utils.model.EmojiModel;
import nickgao.com.meiyousample.utils.model.ExpressionModel;
import nickgao.com.meiyousample.utils.model.ExpressionType;


public class EmojiConversionUtil {

    private static final String ZHENGZE = "\\[[^\\]]+\\]";

    /**
     * 每一页表情的列数
     */
    public static final int PAGE_COLUMN_COUNT = 6;
    /**
     * 每一页表情的个数
     */
    private int PAGE_SIZE = 18;
    /**
     * 每一页限制表情的行数
     */
    public static final int PAGE_LINE_COUNT = 3;

    /**
     * 表情限制个数
     */
    public static final int LIMIT_NUM = 10;

    private int emojiNumInText = 0;

    private static EmojiConversionUtil mEmojiConversionUtil;

    /**
     * 保存于内存中的表情HashMap
     */
    private HashMap<String, String> emojiMap = new HashMap<String, String>();

    /**
     * 表情图片bitmapCachel,用于回复框的显示
     */
    private SparseArray<SoftReference<Bitmap>> emojiCache = new SparseArray<SoftReference<Bitmap>>();
    /**
     * 表情图片bitmapCachel 用于textview上边的显示，如果不另外开启缓存，回复框会受影响
     */
    private SparseArray<SoftReference<Bitmap>> emojiCacheWH = new SparseArray<SoftReference<Bitmap>>();

    /**
     * 保存于内存中的表情集合
     */
    private List<EmojiModel> emojis = new ArrayList<EmojiModel>();

    /**
     * 表情分页的结果集合
     */
    private List<List<EmojiModel>> emojiLists = new ArrayList<List<EmojiModel>>();

    private EmojiConversionUtil() {

    }

    public static EmojiConversionUtil getInstace() {
        if (mEmojiConversionUtil == null) {
            mEmojiConversionUtil = new EmojiConversionUtil();
        }
        return mEmojiConversionUtil;
    }

    public int getPageSize() {
        return PAGE_SIZE;
    }

    /**
     * 程序启动，读取emoji表情
     */
    public void handleGetEmojiFromCache(Context context) {
        try {
            EmojiModel emojEentry;
            String[] emojiCodeArray = context.getResources().getStringArray(R.array.emoji_code);
            String[] emojiFileArray = context.getResources().getStringArray(R.array.emoji_file);
            if (null == emojiCodeArray || emojiCodeArray.length <= 0 || null == emojiFileArray || emojiFileArray.length <= 0 || emojiCodeArray.length != emojiFileArray.length) {
                return;
            }
            for (int i = 0; i < emojiCodeArray.length; i++) {
                String emojiCode = emojiCodeArray[i];
                String emojiFile = emojiFileArray[i];
                emojiMap.put(emojiCode, emojiFile);
                int resID = context.getResources().getIdentifier(emojiFile, "drawable", context.getPackageName());

                if (resID != 0) {
                    emojEentry = new EmojiModel();
                    emojEentry.setId(resID);
                    emojEentry.setCharacter(emojiCode);
                    emojEentry.setEmojiName(emojiFile);
                    emojis.add(emojEentry);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取系统表情列表
     *
     * @param bShowDeleteBtn 是否携带删除项
     * @return
     */
    public List<List<EmojiModel>> getEmojiLists(boolean bShowDeleteBtn) {
        try {
            //每次都重新组建，因为有些需要delete有些不需要
            PAGE_SIZE = bShowDeleteBtn ? 17 : 18;
            //计算page count
            int pageCount = emojis.size() % PAGE_SIZE == 0 ? emojis.size() / PAGE_SIZE : (int) Math.ceil(emojis.size() / PAGE_SIZE + 0.1);
            emojiLists.clear();
            //填充数据
            for (int index = 0; index < pageCount; index++) {
                //逐页填充
                List<EmojiModel> list = new ArrayList<EmojiModel>();
                int startIndex = index * PAGE_SIZE;
                int endIndex = startIndex + PAGE_SIZE;
                if (endIndex > emojis.size()) {
                    endIndex = emojis.size();
                }
                list.addAll(emojis.subList(startIndex, endIndex));

                //未满一页，填充到一页
                if (list.size() < PAGE_SIZE) {
                    for (int i = list.size(); i < PAGE_SIZE; i++) {
                        EmojiModel object = new EmojiModel();
                        list.add(object);
                    }
                }
                //是否添加删除键
                if (list.size() == PAGE_SIZE && bShowDeleteBtn) {
                    EmojiModel object = new EmojiModel();
                    object.setId(R.drawable.btn_emoji_del_selector);
                    list.add(object);
                }
                //加入此页数据
                emojiLists.add(list);
            }
           /*
            //防止异步预加载过慢冲突，导致数据重复，现在总页数是4
            if (null != emojiLists && emojiLists.size() > 4) {
                emojiLists = emojiLists.subList(0, 4);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emojiLists;
    }


    /**
     * 类型转换
     *
     * @param listDatas
     * @return
     */
    public List<List<ExpressionModel>> convertData(List<List<EmojiModel>> listDatas) {
        List<List<ExpressionModel>> listResult = new ArrayList<>();
        try {
            for (List<EmojiModel> list : listDatas) {
                List<ExpressionModel> listExpression = new ArrayList<>();
                for (EmojiModel model : list) {
                    ExpressionModel model1 = new ExpressionModel();
                    if (model.getId() == 0) {
                        model1.type = ExpressionType.NONE;
                    } else if (model.getId() == R.drawable.btn_emoji_del_selector) {
                        model1.type = ExpressionType.DELETE;
                    } else {
                        model1.type = ExpressionType.EMOJI;
                    }
                    model1.emojiModel = model;
                    listExpression.add(model1);
                }
                listResult.add(listExpression);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listResult;
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param str
     * @return
     */
    public SpannableString getExpressionString(Context context, String str, int emojiWidth, int emojiHeight) {
        SpannableString spannableString = new SpannableString(str);
        return getExpressionString(context, spannableString, emojiWidth, emojiHeight);

    }

    public SpannableString getExpressionString(Context context, SpannableString str, int emojiWidth, int emojiHeight) {
        SpannableString spannableString = str;
        Pattern patten = Pattern.compile(ZHENGZE, Pattern.CASE_INSENSITIVE);
        emojiNumInText = 0;
        try {
            dealExpression(context, spannableString, patten, 0, emojiWidth, emojiHeight);
        } catch (Exception e) {

        }
        return spannableString;
    }

    /**
     * 转换表情
     *
     * @param context
     * @param imgId
     * @param spannableString
     * @return
     */
    public SpannableString convertEmojiToSpannableString(Context context, int imgId,
                                                         String spannableString) {
        if (TextUtils.isEmpty(spannableString)) {
            return null;
        }
        SpannableString spannable = new SpannableString(spannableString);
        try {
            Bitmap bitmap = getEmojiFromCache(context, imgId, (int) context.getResources().getDimension(R.dimen.list_icon_height_22), (int) context.getResources().getDimension(R.dimen.list_icon_height_22));
            if(bitmap == null || bitmap.isRecycled()){
                return spannable;
            }
            ImageSpan imageSpan = new ImageSpan(context, bitmap);
            spannable.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return spannable;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws Exception
     */
    private void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start, int emojiWidth, int emojiHeight)
            throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }

            String value = emojiMap.get(key);
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            if (emojiNumInText >= LIMIT_NUM) {
                break;
            }


            int resId = context.getResources().getIdentifier(value, "drawable",
                    context.getPackageName());

            if (resId != 0) {
                final Bitmap bitmap = getEmojiFromCache(context, resId, emojiWidth, emojiHeight);
                CustomImageSpan imageSpan = new CustomImageSpan(context, bitmap);
                //ImageSpan imageSpan = new ImageSpan(context, bitmap);
                // 计算该图片名字的长度，替换的字符串的长度
                int end = matcher.start() + key.length();
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                emojiNumInText++;
                if (end < spannableString.length()) {
                    dealExpression(context, spannableString, patten, end, emojiWidth, emojiHeight);
                }
                break;
            }
        }
    }

    /**
     * 是否是emoji
     *
     * @param character
     * @return
     */
    public boolean isEmoji(String character) {
        try {
            for (EmojiModel emojiModel : emojis) {
                if (character.equals(emojiModel.getCharacter())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 解析字符串中含表情数量
     *
     * @param text
     * @return
     */
    public int getEmojiCount(String text) {
        int nums = 0;
        Pattern pattern = Pattern.compile(ZHENGZE, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        try {
            while (matcher.find()) {
                String key = matcher.group();
                String value = emojiMap.get(key);
                if (TextUtils.isEmpty(value)) {
                    continue;
                }
                nums++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nums;
    }

    /**
     * 解析字符串中含表情  除去表情的字符长度
     *
     * @param text
     * @return
     */
    public String getTextHanZiLen(String text) {
        List<String> stringList = new ArrayList<>();
        String strContent = text;
        Pattern pattern = Pattern.compile(ZHENGZE, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(strContent);
        try {
            while (matcher.find()) {
                String key = matcher.group();
                String value = emojiMap.get(key);
                if (TextUtils.isEmpty(value)) {
                    continue;
                } else {
                    stringList.add(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String string : stringList) {
            strContent = strContent.replaceAll("\\"+string, "");
        }
        return strContent;
    }

    /**
     * 过滤超过10个的表情
     *
     * @param text
     * @return
     */
    public String filterEmoji(String text) {
        try {
            int nums = 0;
            String textPart1 = "";
            String textPart2 = "";
            Pattern pattern = Pattern.compile(ZHENGZE, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String key = matcher.group();
                String value = emojiMap.get(key);
                if (TextUtils.isEmpty(value)) {
                    continue;
                }
                nums++;
                if (nums >= LIMIT_NUM) {
                    textPart1 = text.substring(0, matcher.end());
                    if (text.length() > matcher.end()) {
                        textPart2 = text.substring(matcher.end());
                    }
                    break;
                }
            }
            Matcher matcherFilter = pattern.matcher(textPart2);
            return textPart1 + matcherFilter.replaceAll("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Bitmap getEmojiFromCache(Context context, int imgId, int emojiWidht, int emojiHeight) {
        Bitmap bitmap = null;
        try {
            if (emojiWidht > 0 && emojiHeight > 0) {
                SoftReference<Bitmap> sfBitmap = emojiCacheWH.get(imgId);
                if (null != sfBitmap) {
                    bitmap = emojiCacheWH.get(imgId).get();
                }
                if (null == bitmap) {
                    bitmap = getBitmapByResId(context, imgId, emojiWidht, emojiHeight);
                    emojiCacheWH.put(imgId, new SoftReference<Bitmap>(bitmap));
                }
            } else {
                SoftReference<Bitmap> sfBitmap = emojiCache.get(imgId);
                if (null != sfBitmap) {
                    bitmap = emojiCache.get(imgId).get();
                }
                if (null == bitmap) {
                    bitmap = getBitmapByResId(context, imgId, emojiWidht, emojiHeight);
                    emojiCache.put(imgId, new SoftReference<Bitmap>(bitmap));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public Bitmap getBitmapByResId(Context context, int resId, int width, int height) {
        try {
            if (resId == 0)
                return null;
            InputStream is = context.getResources().openRawResource(resId);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            if (width > 0 && height > 0) {
               // Bitmap target = BitmapUtil.zoomImage(bitmap, width, height);
                Bitmap target = null;
                return target;
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}