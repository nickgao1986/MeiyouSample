package nickgao.com.meiyousample;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nickgao.com.framework.utils.StringUtils;
import nickgao.com.meiyousample.skin.CacheDisc;
import nickgao.com.meiyousample.utils.EmojiConversionUtil;
import nickgao.com.meiyousample.utils.model.EmojiModel;
import nickgao.com.meiyousample.utils.model.ExpressionConfigModel;
import nickgao.com.meiyousample.utils.model.ExpressionModel;
import nickgao.com.meiyousample.utils.model.ExpressionSubModel;
import nickgao.com.meiyousample.utils.model.ExpressionTabType;

/**
 * 表情控制类
 * Created by lwh on 2015/7/14.
 */
public class ExpressionController {
    public static final String EXPRESSION_FOLDER = "expressions";
    private static ExpressionController mInstance;
    private String TAG = "ExpressionController";
    //private ExpressionHttpHelper expressionHttpHelper;
    private List<ExpressionConfigModel> listConfig = new ArrayList<>();
    private List<ExpressionConfigModel> listMine = new ArrayList<>();

    public static ExpressionController getInstance() {
        if (mInstance == null) {
            mInstance = new ExpressionController();
        }
        return mInstance;
    }

    public ExpressionController() {
      /*  if (expressionHttpHelper == null) {
            expressionHttpHelper = new ExpressionHttpHelper();
        }*/
    }

    /**
     * ******配置文件缓存操作写*************************
     *//*
    private void saveConfigToCache(Context context, List<ExpressionConfigModel> models) {
        try {
            FileUtils.saveObjectToLocal(context, models, "expression_config_file");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveMineConfigToCache(Context context, List<ExpressionConfigModel> models) {
        try {
            FileUtils.saveObjectToLocal(context, models, "expression_config_file_mine");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
*/
    public List<ExpressionConfigModel> getListConfig() {
        return listConfig;
    }

    public List<ExpressionConfigModel> getMineExpression() {
        return listMine;
    }

    /**
     * ******配置文件缓存操作读*************************
     */
   /* private List<ExpressionConfigModel> getConfigFromCache(Context context) {
        try {
            return (List<ExpressionConfigModel>) FileUtils.getObjectFromLocal(context, "expression_config_file");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private List<ExpressionConfigModel> getMineConfigFromCache(Context context) {
        try {
            return (List<ExpressionConfigModel>) FileUtils.getObjectFromLocal(context, "expression_config_file_mine");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }*/

    /**
     * 处理从服务端下载表情文件
     *
     * @param context
     */
   /* public void handleGetExpressionConfigureFile(final Context context) {
        if (!NetWorkStatusUtil.queryNetWork(context.getApplicationContext())) {
            ThreadUtil.addTaskForIO(context, "", new ThreadUtil.ITasker() {
                @Override
                public Object onExcute() {
                    List<ExpressionConfigModel> list = getConfigFromCache(context);
                    return list;
                }

                @Override
                public void onFinish(Object result) {
                    try {
                        if (result == null) {
                            return;
                        }
                        List<ExpressionConfigModel> list = (List<ExpressionConfigModel>) result;
                        if (list != null && list.size() > 0) {
                            listConfig.clear();
                            listConfig.addAll(list);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            return;
        }
        ThreadUtil.addTaskForCommunity(context.getApplicationContext(), false, "", new ThreadUtil.ITasker() {
            @Override
            public Object onExcute() {
                List<ExpressionConfigModel> listConfig = new ArrayList<>();
                try {
                    HttpResult httpResult = expressionHttpHelper.getExpressionConfigData(context);
                    if (httpResult.isSuccess()) {
                        String respone = httpResult.response;
                        JSONArray jsonArray = new JSONArray(respone);
                        listConfig.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ExpressionConfigModel model = new ExpressionConfigModel(jsonObject);
                            model.package_name = "com.lingan.expression_one";
                            listConfig.add(model);
                        }
                        LogUtils.d(TAG, "加载表情配置文件成功，大小为：" + listConfig.size());
                        saveConfigToCache(context, listConfig);
                    } else {
                        List<ExpressionConfigModel> list = getConfigFromCache(context);
                        if (list != null) {
                            listConfig.clear();
                            listConfig.addAll(list);
                        }
                        LogUtils.d(TAG, "加载表情配置文件失败");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return listConfig;
            }

            @Override
            public void onFinish(Object result) {
                try {
                    if (result == null) {
                        return;
                    }
                    List<ExpressionConfigModel> list = (List<ExpressionConfigModel>) result;
                    if (list != null && list.size() > 0) {
                        listConfig.clear();
                        listConfig.addAll(list);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }
*/
    /**
     * 根据内容 获取配置
     *
     * @param content
     * @return
     */
    private ExpressionConfigModel getExpressionConfig(String content) {
        if (listConfig == null || listConfig.size() == 0)
            return null;
        for (ExpressionConfigModel model : listConfig) {
            List<ExpressionSubModel> listSub = model.listConfig;
            if (listSub == null)
                return null;
            for (ExpressionSubModel subModel : listSub) {
                if (subModel.name.equals(content))
                    return model;
            }
        }
        return null;
    }

   /* public ExpressionModel getExpressionModel(Context context, String name) {
        if (listConfig == null || listConfig.size() == 0)
            return null;
        for (ExpressionConfigModel model : listConfig) {
            List<ExpressionSubModel> listSub = model.listConfig;
            if (listSub == null)
                return null;
            for (ExpressionSubModel subModel : listSub) {
                if (subModel.name.equals(name)) {
                    ExpressionModel expressionModel = new ExpressionModel();
                    expressionModel.type = ExpressionType.GIF;
                    expressionModel.iconurl = model.getGifUrl(subModel.gif_name);
                    boolean isExisted = isLocalExsited(context,model.id);
                    if(isExisted){
                        expressionModel.packagename = getExpressionFileName(context, model.id);
                    }else
                        expressionModel.packagename = "";
                    expressionModel.expressionSubModel = new ExpressionSubModel();
                    expressionModel.expressionSubModel.name = name;
                    expressionModel.expressionSubModel.gif_name = name;
                    return expressionModel;
                }
            }
        }
        return null;
    }*/

    private ExpressionSubModel getExpressionSubConfig(String content) {
        if (listConfig == null || listConfig.size() == 0)
            return null;
        for (ExpressionConfigModel model : listConfig) {
            List<ExpressionSubModel> listSub = model.listConfig;
            if (listSub == null)
                return null;
            for (ExpressionSubModel subModel : listSub) {
                if (subModel.name.equals(content))
                    return subModel;
            }
        }
        return null;
    }





    /**
     * 正则表达式 匹配类似[自定义表情包名123_表情名称]
     */
    private static final String ZHENGZE_EXPRESSION = "\\[[\u4e00-\u9fa5_a-zA-Z0-9]+\\_[\u4e00-\u9fa5_a-zA-Z0-9]+\\]";


    /**
     * 是否是自定义表情
     *
     * @param content
     * @return
     */
    public boolean isCustomExpression(String content) {
        int count = 0;
        Pattern patten = Pattern.compile(ZHENGZE_EXPRESSION, Pattern.CASE_INSENSITIVE);
        Matcher matcher = patten.matcher(content);
        if (matcher != null) {
            while (matcher.find()) {
                count++;
            }
        }
        return count == 1 ? true : false;
    }

    /**
     * 去除自定义表情字符串
     *
     * @param content
     * @return
     */
    public String customExpressionText(String content) {
        List<String> stringList = new ArrayList<>();
        String strContent = content;
        Pattern patten = Pattern.compile(ZHENGZE_EXPRESSION, Pattern.CASE_INSENSITIVE);
        Matcher matcher = patten.matcher(content);
        if (matcher != null) {
            while (matcher.find()) {
                String key = matcher.group();
                if (!StringUtils.isNull(key)) {
                    stringList.add(key);
                }
            }
        }
        for (String string : stringList) {
            strContent = strContent.replaceAll("\\"+string, "");
        }
        return strContent;
    }

    /**
     * 解析和切割文字
     *
     * @param content   内容
     * @param privilege 是否需要转换url
     * @param blockId   圈子ID
     * @return
     */
   /* public List<ExpressionSplitModel> handleParseExpression(Context context, String content, int privilege, int blockId) {

        //用于html展示
        //public int privilege;//是否可转换url
        //public int blockId;//圈子ID
        //用于自定义表情展示
        //public boolean is_local_exist =false;//本地是否存在自定义表情
        //public String icon_packagename;//自定义表情包名 如:com.lingan.expression_cat;
        //public String icon_name;//自定义表情本地名字 如 hello_kitty.gif
        //public String icon_url;//自定义表情网络url
        List<ExpressionSplitModel> listExpression = new ArrayList<ExpressionSplitModel>();
        try {
           *//* if (Contants.DEBUG) {
                //content = "这是一个测试表情内容[自定义表情包名_表情名称][自定义表情包名_表情名称][自定义表情包名_表情名称]后缀文案[自定义表情包名_表情名称]sdfdsf[自定义表情包名_表情名称]";
                content = "这是一个测试表情内容[asdgsd][自定义表情包名123_表情名称][自定义表情包名_表情名称]sdfdsf[自定义表情包名_表情名称]";
                //content="这是一个测试表情内容";
            }*//*
            Pattern patten = Pattern.compile(ZHENGZE_EXPRESSION, Pattern.CASE_INSENSITIVE);
            Matcher matcher = patten.matcher(content);
            int totalLength  = content.length();
            int length =0;
            if (matcher != null) {
                int count = 0;
                while (matcher.find()) {
                    count++;
                    //计算起始长度
                    int start = 0;
                    for (ExpressionSplitModel model : listExpression) {
                        start += model.content.length();
                    }

                    //匹配字符串
                    String key = matcher.group();
                    int index = content.indexOf(key, start);

                    //添加切割文案
                    ExpressionSplitModel expressionSplitModel = new ExpressionSplitModel();
                    expressionSplitModel.type = ExpressionSplitModel.TYPE_TEXT;
                    expressionSplitModel.content = content.substring(start, index);
                    expressionSplitModel.privilege = privilege;
                    expressionSplitModel.blockId = blockId;
                    if (!StringUtils.isNull(expressionSplitModel.content)) {
                        listExpression.add(expressionSplitModel);
                        length+=expressionSplitModel.content.length();
                        //LogUtils.d(TAG, "加入内容：" + expressionSplitModel.content);
                    }
                    //加入自定义表情内容
                    ExpressionSplitModel expressionSplitModelTwo = new ExpressionSplitModel();
                    expressionSplitModelTwo.type = ExpressionSplitModel.TYPE_EXPRESSION;
                    expressionSplitModelTwo.content = key;
                    ExpressionConfigModel model = getExpressionConfig(key);
                    ExpressionSubModel subModel = getExpressionSubConfig(key);
                    if (model != null && subModel != null) {
                        expressionSplitModelTwo.is_local_exist = isLocalExsited(context, model.id);//TODO
                        expressionSplitModelTwo.package_name = getExpressionFileName(context, model.id);//TODO
                        expressionSplitModelTwo.gif_name = subModel.gif_name;//TODO
                        expressionSplitModelTwo.gif_url = model.getGifUrl(subModel.gif_name);//TODO
                        if (expressionSplitModelTwo.is_local_exist) {
                            expressionSplitModel.resource = SkinEngine.getInstance().getApkFileResources(context, model.package_name);
                        }
                    } else {
                        expressionSplitModelTwo.is_local_exist = false;//TODO
                        expressionSplitModelTwo.package_name = "";//TODO
                        expressionSplitModelTwo.gif_name = "";//TODO
                        expressionSplitModelTwo.gif_url = "";//TODO
                        //LogUtils.d(TAG, "从配置文件中查找不到 ：" + key);
                    }
                    listExpression.add(expressionSplitModelTwo);
                    length+=expressionSplitModelTwo.content.length();
                    //LogUtils.d(TAG, "加入内容：" + expressionSplitModelTwo.content);
                    //LogUtils.d(TAG, "表情文案:"+key);
                }
                if (count == 0) {
                    ExpressionSplitModel expressionSplitModelTwo = new ExpressionSplitModel();
                    expressionSplitModelTwo.type = ExpressionSplitModel.TYPE_TEXT;
                    expressionSplitModelTwo.content = content;
                    expressionSplitModelTwo.privilege = privilege;
                    expressionSplitModelTwo.blockId = blockId;
                    if (!StringUtils.isNull(expressionSplitModelTwo.content)) {
                        listExpression.add(expressionSplitModelTwo);
                    }
                }
                if(count>0 && length<totalLength){
                    ExpressionSplitModel expressionSplitModelTwo = new ExpressionSplitModel();
                    expressionSplitModelTwo.type = ExpressionSplitModel.TYPE_TEXT;
                    expressionSplitModelTwo.content = content.substring(length,totalLength);
                    expressionSplitModelTwo.privilege = privilege;
                    expressionSplitModelTwo.blockId = blockId;
                    if (!StringUtils.isNull(expressionSplitModelTwo.content)) {
                        //LogUtils.d(TAG, "加入内容：" + expressionSplitModelTwo.content);
                        listExpression.add(expressionSplitModelTwo);
                    }
                }
            } else {
                ExpressionSplitModel expressionSplitModelTwo = new ExpressionSplitModel();
                expressionSplitModelTwo.type = ExpressionSplitModel.TYPE_TEXT;
                expressionSplitModelTwo.content = content;
                expressionSplitModelTwo.privilege = privilege;
                expressionSplitModelTwo.blockId = blockId;
                if (!StringUtils.isNull(expressionSplitModelTwo.content)) {
                    listExpression.add(expressionSplitModelTwo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //LogUtils.d(TAG,"listExpression.size: "+listExpression.size());
        return listExpression;
    }*/


    /**
     * 过滤不符合条件的表情内容
     * @param context
     * @param content
     * @return
     */
 /*   public String fiterExpression(Context context, String content) {
        List<ExpressionSplitModel> listExpression = new ArrayList<ExpressionSplitModel>();
        try {

            Pattern patten = Pattern.compile(ZHENGZE_EXPRESSION, Pattern.CASE_INSENSITIVE);
            Matcher matcher = patten.matcher(content);
            int totalLength  = content.length();
            int length =0;
            if (matcher != null) {
                int count = 0;
                while (matcher.find()) {
                    count++;
                    //计算起始长度
                    int start = 0;
                    for (ExpressionSplitModel model : listExpression) {
                        start += model.content.length();
                    }

                    //匹配字符串
                    String key = matcher.group();
                    int index = content.indexOf(key, start);

                    //添加切割文案
                    ExpressionSplitModel expressionSplitModel = new ExpressionSplitModel();
                    expressionSplitModel.type = ExpressionSplitModel.TYPE_TEXT;
                    expressionSplitModel.content = content.substring(start, index);
                    if (!StringUtils.isNull(expressionSplitModel.content)) {
                        listExpression.add(expressionSplitModel);
                        length+=expressionSplitModel.content.length();
                        //LogUtils.d(TAG, "加入内容：" + expressionSplitModel.content);
                    }
                    //加入自定义表情内容
                    ExpressionSplitModel expressionSplitModelTwo = new ExpressionSplitModel();
                    expressionSplitModelTwo.type = ExpressionSplitModel.TYPE_EXPRESSION;
                    expressionSplitModelTwo.content = key;
                    ExpressionConfigModel model = getExpressionConfig(key);
                    ExpressionSubModel subModel = getExpressionSubConfig(key);
                    if (model != null && subModel != null) {
                        expressionSplitModelTwo.is_local_exist = isLocalExsited(context, model.id);//TODO
                        expressionSplitModelTwo.package_name = getExpressionFileName(context, model.id);//TODO
                        expressionSplitModelTwo.gif_name = subModel.gif_name;//TODO
                        expressionSplitModelTwo.gif_url = model.getGifUrl(subModel.gif_name);//TODO
                        if (expressionSplitModelTwo.is_local_exist) {
                            expressionSplitModel.resource = SkinEngine.getInstance().getApkFileResources(context, model.package_name);
                        }
                    } else {
                        expressionSplitModelTwo.is_local_exist = false;//TODO
                        expressionSplitModelTwo.package_name = "";//TODO
                        expressionSplitModelTwo.gif_name = "";//TODO
                        expressionSplitModelTwo.gif_url = "";//TODO
                        //LogUtils.d(TAG, "从配置文件中查找不到 ：" + key);
                    }
                    listExpression.add(expressionSplitModelTwo);
                    length+=expressionSplitModelTwo.content.length();
                    //LogUtils.d(TAG, "加入内容：" + expressionSplitModelTwo.content);
                    //LogUtils.d(TAG, "表情文案:"+key);
                }
                if (count == 0) {
                    ExpressionSplitModel expressionSplitModelTwo = new ExpressionSplitModel();
                    expressionSplitModelTwo.type = ExpressionSplitModel.TYPE_TEXT;
                    expressionSplitModelTwo.content = content;
                    if (!StringUtils.isNull(expressionSplitModelTwo.content)) {
                        listExpression.add(expressionSplitModelTwo);
                    }
                }
                if(count>0 && length<totalLength){
                    ExpressionSplitModel expressionSplitModelTwo = new ExpressionSplitModel();
                    expressionSplitModelTwo.type = ExpressionSplitModel.TYPE_TEXT;
                    expressionSplitModelTwo.content = content.substring(length,totalLength);
                    if (!StringUtils.isNull(expressionSplitModelTwo.content)) {
                        //LogUtils.d(TAG, "加入内容：" + expressionSplitModelTwo.content);
                        listExpression.add(expressionSplitModelTwo);
                    }
                }
            } else {
                ExpressionSplitModel expressionSplitModelTwo = new ExpressionSplitModel();
                expressionSplitModelTwo.type = ExpressionSplitModel.TYPE_TEXT;
                expressionSplitModelTwo.content = content;
                if (!StringUtils.isNull(expressionSplitModelTwo.content)) {
                    listExpression.add(expressionSplitModelTwo);
                }
            }
            //LogUtils.d(TAG,"listExpression.size: "+listExpression.size());
            StringBuilder stringBuilder = new StringBuilder();
            for(ExpressionSplitModel model:listExpression){
                if(model.type == ExpressionSplitModel.TYPE_TEXT ){
                    stringBuilder.append(model.content);
                }
               *//* if(model.type == ExpressionSplitModel.TYPE_EXPRESSION && model.is_local_exist){
                    stringBuilder.append(model.content);
                }*//*
            }
            return stringBuilder.toString();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }*/

    /**
     * 是否有自定义表情
     *
     * @param content
     * @return
     */
    public boolean hasCustomExpression(String content) {
        try {
            Pattern patten = Pattern.compile(ZHENGZE_EXPRESSION, Pattern.CASE_INSENSITIVE);
            Matcher matcher = patten.matcher(content);
            if (matcher != null) {
                while (matcher.find()) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 获取自定义表情数据
     *
     * @param content
     * @return
     */
    public int getExpressionCount(String content) {
        int count = 0;
        try {
            Pattern patten = Pattern.compile(ZHENGZE_EXPRESSION, Pattern.CASE_INSENSITIVE);
            Matcher matcher = patten.matcher(content);
            if (matcher != null) {
                while (matcher.find()) {
                    count++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return count;
    }

    /**
     * 是否在尾部有自定义表情
     * @param content
     * @return
     */
 /*   public boolean hasCustomExpressionInEnd(String content) {
        List<String> listString = new ArrayList<String>();
        List<String> listExpression = new ArrayList<String>();
        try {
            Pattern patten = Pattern.compile(ZHENGZE_EXPRESSION, Pattern.CASE_INSENSITIVE);
            Matcher matcher = patten.matcher(content);
            int count = 0;
            if (matcher != null) {
                while (matcher.find()) {
                    count++;
                    //计算起始长度
                    int start = 0;
                    for (String str : listString) {
                        start += str.length();
                    }
                    //匹配字符串
                    String key = matcher.group();
                    int index = content.indexOf(key, start);
                    //添加切割后的普通文案
                    String  str1 = content.substring(start, index);
                    if (!StringUtils.isNull(str1)) {
                        listString.add(str1);
                        LogUtils.d(TAG, "加入内容：" + str1);
                    }
                    //添加切割后的自定义表情内容
                    String str2 = key;
                    listString.add(str2);
                    listExpression.add(str2);
                    LogUtils.d(TAG, "加入内容2：" + str2);
                    //LogUtils.d(TAG, "表情文案:"+key);
                }
            }
            LogUtils.d(TAG,"-->count:"+count);
            //如果没有匹配到,删除最后一个字符
            if (count == 0) {
                return false;
            }else{
                //如果最后一个是自定义表情
                if(listExpression.size()>0){
                    String lastExpress = listExpression.get(listExpression.size()-1);
                    int length = lastExpress.length();
                    if(content.substring(content.length()-length,content.length()).equals(lastExpress)){
                        return true;
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

    }*/

    /**
     * 处理回退删除 （系统回退删除键不管）
     *
     * [小猴子_大哭][小猴子_疑问]-->[小猴子_大哭]哭]
     * @param content
     * @return
     */
   /* public String handlePressDelete(String content) {
        LogUtils.d(TAG,"content:"+content);
        List<String> listString = new ArrayList<String>();
        List<String> listExpression = new ArrayList<String>();
        try {
            *//*if (Contants.DEBUG) {
                //content = "这是一个测试表情内容[自定义表情包名_表情名称][自定义表情包名_表情名称][自定义表情包名_表情名称]后缀文案[自定义表情包名_表情名称]sdfdsf[自定义表情包名_表情名称]";
                content = "这是一个测试表情内容[asdgsd][自定义表情包名123_表情名称][自定义表情包名_表情名称]sdfdsf好";
                //content="这是一个测试表情内容";
            }*//*
            Pattern patten = Pattern.compile(ZHENGZE_EXPRESSION, Pattern.CASE_INSENSITIVE);
            Matcher matcher = patten.matcher(content);
            int count = 0;
            if (matcher != null) {
                while (matcher.find()) {
                    count++;
                    //计算起始长度
                    int start = 0;
                    for (String str : listString) {
                        start += str.length();
                    }
                    //匹配字符串
                    String key = matcher.group();
                    int index = content.indexOf(key, start);
                    LogUtils.d(TAG, "key:"+key+"start：" + start+"-->index:"+index);
                    //添加切割后的普通文案
                    String  str1 = content.substring(start, index);
                    if (!StringUtils.isNull(str1)) {
                        listString.add(str1);
                        LogUtils.d(TAG, "加入内容：" + str1);
                    }
                    //添加切割后的自定义表情内容
                    String str2 = key;
                    listString.add(str2);
                    listExpression.add(str2);
                    LogUtils.d(TAG, "加入内容2：" + str2);
                    //LogUtils.d(TAG, "表情文案:"+key);
                }
            }
            LogUtils.d(TAG,"-->count:"+count);
            //如果没有匹配到,删除最后一个字符
            if (count == 0) {
                if(StringUtils.isNull(content))
                    return content;
                listString.add(content);
                return content.substring(0,content.length()-1);
            }else{
                //如果最后一个是自定义表情
                if(listExpression.size()>0){
                    String lastExpress = listExpression.get(listExpression.size()-1);
                    int length = lastExpress.length();
                    if(content.substring(content.length()-length,content.length()).equals(lastExpress)){
                        StringBuilder stringBuilder = new StringBuilder();
                        for(String str:listString){
                            if(!str.equals(lastExpress)){
                                stringBuilder.append(str);
                            }
                        }
                        return stringBuilder.toString();
                    }else{
                        return content.substring(0,content.length()-1);
                    }
                }else{
                    return content.substring(0,content.length()-1);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }*/

    /**
     * 查询data目录下相对应id的表情文件名
     *
     * @param expression_id 不存在@return 空字符串
     */
  /*  public String getExpressionFileName(Context context, int expression_id) {
        String vagueName = "expression_" + expression_id;
        File dir = context.getApplicationContext().getDir(SkinUtil.SKIN_FOLDER, Activity.MODE_PRIVATE);
        String[] fileNames = dir.list();
        for (String fileName : fileNames) {
            if (fileName.contains(vagueName)) {
                return fileName;
            }
        }
        return "";
    }*/

   /* *//**
     * 获取显示gridview上的名字 如[小猴子_开心]--》开心
     *
     * @param gif_name
     * @return
     *//*
    private String getExpressionUIName(String gif_name) {
        try {
            String[] strings = gif_name.split("_");
            if (strings != null && strings.length == 2) {
                String value = strings[1];
                value = value.substring(0, value.length() - 1);
                return value;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    *//**
     * 是否已经存在data目录下。
     *
     * @param context
     * @param expression_id
     * @return
     *//*
    public boolean isLocalExsited(Context context, int expression_id) {
        String vagueName = "expression_" + expression_id;
        File dir = context.getApplicationContext().getDir(SkinUtil.SKIN_FOLDER, Activity.MODE_PRIVATE);
        String[] fileNames = dir.list();
        for (String fileName : fileNames) {
            if (fileName.contains(vagueName)) {
                return true;
            }
        }
        return false;
    }
*/

    /**
     * 为viewpager提供数据
     *
     * @param expression_id
     * @return
     */
    /*private List<List<ExpressionModel>> getExpressionDatasForViewPager(Context context, int expression_id) {
        List<List<ExpressionModel>> listResult = new ArrayList<>();
        try {
            ExpressionConfigModel expressionConfigModel = null;
            for (ExpressionConfigModel model : listMine) {
                if (model.id == expression_id && ExpressionController.getInstance().isLocalExsited(context, expression_id)) {
                    expressionConfigModel = model;
                    break;
                }
            }
            if (expressionConfigModel == null)
                return listResult;
            //组装表情
            List<ExpressionSubModel> listConfig = expressionConfigModel.listConfig;
            List<ExpressionModel> listExpression = new ArrayList<>();
            for (int i = 0; i < listConfig.size(); i++) {
                ExpressionSubModel model = listConfig.get(i);
                ExpressionModel expressionModel = new ExpressionModel();
                expressionModel.type = ExpressionType.GIF;
                expressionModel.expressionSubModel = model;
                expressionModel.packagename = getExpressionFileName(context, expression_id);//expressionConfigModel.package_name;
                expressionModel.gif_ui_name = getExpressionUIName(model.name);
                expressionModel.iconurl =expressionConfigModel.getGifUrl(model.gif_name);
                listExpression.add(expressionModel);
            }

            //int pageCount = listExpression.size() / ExpressionEngine.PAGE_SIZE + listExpression.size() % ExpressionEngine.PAGE_SIZE == 0 ? 0 : 1;//总共有几页表情
            int yu = listExpression.size() / ExpressionEngine.PAGE_SIZE;
            int mo = listExpression.size() % ExpressionEngine.PAGE_SIZE;
            int moCount = (mo == 0 ? 0 : 1);
            int pageCount = yu + moCount;
            for (int i = 0; i < pageCount; i++) {
                int startIndex = i * ExpressionEngine.PAGE_SIZE;
                int endIndex = startIndex + ExpressionEngine.PAGE_SIZE;
                endIndex = endIndex > listExpression.size() ? listExpression.size() : endIndex;
                List<ExpressionModel> sub = new ArrayList<>();
                sub.addAll(listExpression.subList(startIndex, endIndex));
                //未满一页，填充到一页
                if (sub.size() < ExpressionEngine.PAGE_SIZE) {
                    for (int j = sub.size(); j < ExpressionEngine.PAGE_SIZE; j++) {
                        ExpressionModel expressionModel = new ExpressionModel();
                        expressionModel.type = ExpressionType.NONE;
                        sub.add(expressionModel);
                    }
                }
                listResult.add(sub);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listResult;
    }
*/

    //expression layout datas
    private List<ExpressionConfigModel> listExpressionTabDatas = new ArrayList<>();
    //viewpager的总页数
    private int mViewPagerCount = 3;
    //所有tab的数据
    private List<List<ExpressionModel>> listViewPagerDatas = new ArrayList<>();
    //每个tab对应的数据
    private HashMap<Integer, List<List<ExpressionModel>>> hashMapViewPager = new HashMap<>();
    //每个gridview对应的类型
    private List<Integer> listTabType = new ArrayList<>();//HashMap<Integer,Integer> hashMapExpressionTabType = new HashMap<>();
    //每个viewpager的index对应gridview的index
    private HashMap<Integer, List<Integer>> hashMapViewPagerIndex = new HashMap<>();

    public List<ExpressionConfigModel> getExpressionLayoutDatas() {
        return listExpressionTabDatas;
    }

   /* */

    /**
     * 初始化 表情控件的数据
     *//*
    public void initExpressionLayoutData(Context context){
        LogUtils.d(TAG, "initExpressionLayoutData");
        //初始化表情菜单：自定义表情等
        listExpressionTabDatas.clear();
        listExpressionTabDatas.addAll(listMine);

        //加入默认表情
        ExpressionConfigModel modelDefault = new ExpressionConfigModel();
        modelDefault.name="默认";
        modelDefault.id=0;
        listExpressionTabDatas.add(0,modelDefault);
        //计算viewpager页数
        ExpressionController.getInstance().handleCaculateData(context,listExpressionTabDatas, !bShowCustomExpression);
    }*/

    //计算viewpager数据,提供每页数据和类型
    public void handleCaculateData(Context context, List<ExpressionConfigModel> listDatas, boolean bShowDeleteBtn) {
        try {
            hashMapViewPager.clear();
            listViewPagerDatas.clear();
            listTabType.clear();
            hashMapViewPagerIndex.clear();
            mViewPagerCount = 0;
            for (int i = 0; i < listDatas.size(); i++) {
                ExpressionConfigModel model = listDatas.get(i);
                //默认表情
                if (model.id == 0) {
                    List<List<EmojiModel>> listEmoji = EmojiConversionUtil.getInstace().getEmojiLists(bShowDeleteBtn);
                    List<List<ExpressionModel>> listTemp = EmojiConversionUtil.getInstace().convertData(listEmoji);
                    listViewPagerDatas.addAll(listTemp);
                    //加入数据映射
                    hashMapViewPager.put(i, listTemp);
                    //加入tab
                    for (int j = 0; j < listTemp.size(); j++) {
                        listTabType.add(ExpressionTabType.DEFAULT);
                    }
                    //加入index对应
                    int startIndex = getStartIndex();
                    List<Integer> listIndex = new ArrayList<>();
                    for (int j = startIndex; j < listTemp.size() + startIndex; j++) {
                        listIndex.add(j);
                    }
                    hashMapViewPagerIndex.put(i, listIndex);

                    //计算页
                    mViewPagerCount += listTemp.size();
                }/* else if (model.id > 0 && !bShowDeleteBtn) {
                    List<List<ExpressionModel>> listTemp = getExpressionDatasForViewPager(context, model.id);
                    listViewPagerDatas.addAll(listTemp);
                    //加入数据映射
                    hashMapViewPager.put(i, listTemp);
                    //加入tab
                    for (int j = 0; j < listTemp.size(); j++) {
                        listTabType.add(ExpressionTabType.GIF);
                    }

                    //加入index对应
                    int startIndex = getStartIndex();
                    List<Integer> listIndex = new ArrayList<>();
                    for (int j = startIndex; j < listTemp.size() + startIndex; j++) {
                        listIndex.add(j);
                    }
                    hashMapViewPagerIndex.put(i, listIndex);
                    //计算页
                    mViewPagerCount += listTemp.size();
                }*/
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取gridview 数据
     *
     * @param position
     * @return
     */
    public List<ExpressionModel> getGridViewDatas(int position) {
        if (position < listViewPagerDatas.size())
            return listViewPagerDatas.get(position);
        return new ArrayList<>();
    }

    /**
     * 获取grdiview的tab type
     *
     * @param position
     * @return
     */
    public int getGridViewTabType(int position) {
        if (position < listTabType.size())
            return listTabType.get(position);
        return ExpressionTabType.DEFAULT;
    }


    /**
     * 获取tab的索引
     *
     * @param viewpagetIndex
     * @return
     */
    public int getTabIndex(int viewpagetIndex) {
        int index = 0;
        Iterator iter = hashMapViewPagerIndex.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int pos = (int) entry.getKey();
            List<Integer> list = (List<Integer>) entry.getValue();
            for (Integer value : list) {
                if (value == viewpagetIndex) {
                    return pos;
                }
            }
        }
        return index;
    }

    private int getStartIndex() {
        int index = 0;
        Iterator iter = hashMapViewPagerIndex.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int pos = (int) entry.getKey();
            List<Integer> list = (List<Integer>) entry.getValue();
            for (Integer value : list) {
                index++;
            }
        }
        return index;

    }

    /**
     * 获取索引游标总数
     *
     * @param viewpagerIndex
     * @return
     */
    public int getIndicatorTotalCount(int viewpagerIndex) {
        int index = 1;
        Iterator iter = hashMapViewPagerIndex.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int pos = (int) entry.getKey();
            List<Integer> list = (List<Integer>) entry.getValue();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == viewpagerIndex) {
                    return list.size();
                }
            }
        }
        return index;
    }

    /**
     * 获取索引游标 index
     *
     * @param viewpagerIndex
     * @return
     */
    public int getIndicatorIndex(int viewpagerIndex) {
        int index = 0;
        Iterator iter = hashMapViewPagerIndex.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int pos = (int) entry.getKey();
            List<Integer> list = (List<Integer>) entry.getValue();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == viewpagerIndex) {
                    return i;
                }
            }
        }
        return index;
    }

    public int getPageCount() {
        return mViewPagerCount;
    }

    /**
     * 获取viewpager的选中页面
     *
     * @param position
     * @return
     */
    public int getViewPagerSelectIndex(int position) {
        int index = 0;
        Iterator iter = hashMapViewPager.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int pos = (int) entry.getKey();
            List<List<ExpressionModel>> list = (List<List<ExpressionModel>>) entry.getValue();
            if (pos < position) {
                index += list.size();
            }
        }
        return index;
    }


    /**
     * 查询SD卡目录下对应id的表情文件名
     *
     * @return
     */
    public boolean isExpressionFileExistInSDCard(Context context, String fileName) {
        File file = new File(getDownloadPath(context) + File.separator + fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除data目录中对应id的表情包
     */
   /* public void deleteExpressionFile(Context context, int id) {
        String vagueName = "expression_" + id;
        File dir = context.getDir(SkinUtil.SKIN_FOLDER, Activity.MODE_PRIVATE);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().contains(vagueName)) {
                file.delete();
            }
        }
    }
*/
    /**
     * 删除SD卡目录中对应id的表情包
     */
    public void deleteExpressionFileSDCard(Context context, int id) {
        String vagueName = "expression_" + id;
        File dir = new File(getDownloadPath(context));
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().contains(vagueName)) {
                file.delete();
            }
        }
    }

    /**
     * 表情包SD卡下载路径
     *
     * @param context
     * @return
     */
    public String getDownloadPath(Context context) {
        File skinDir = CacheDisc.getInstance(context).getCacheFileName(EXPRESSION_FOLDER);
        if (!skinDir.exists()) {
            skinDir.mkdirs();
        }
        return skinDir.getAbsolutePath();
    }
}
