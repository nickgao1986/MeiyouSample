package nickgao.com.meiyousample.skin;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import biz.threadutil.FileUtils;
import biz.threadutil.ThreadUtil;
import nickgao.com.meiyousample.controller.UserController;
import nickgao.com.meiyousample.database.SkinStastus_DataBase;
import nickgao.com.meiyousample.personal.ExtendOperationController;
import nickgao.com.meiyousample.settings.DataSaveHelper;
import nickgao.com.meiyousample.utils.LogUtils;
import nickgao.com.meiyousample.utils.StringUtils;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class SkinController {

    private static final String TAG = "SkinController";
    public static final String SKIN_FILE = "skin_file";
    public static final String MY_SKIN_FILE = "my_skin_file";
    public static final String CLASSIFY_SKIN_FILE = "classify_skin_file";
    public static final String BOUTIQUE_SKIN_FILE = "boutique_skin_file";
    public static final String SKIN_HOME_BANNER_DATA = "skin_home_banner_data";
    public static final String FROM_HOME = "001";
    public static final String FROM_BANNER = "002";
    public static final String FROM_NOTIFY = "003";
    public static final String FROM_UCOIN = "004";
    public static final String FROM_ZHUANGBAN = "005";
    public static final String FROM_TOPIC = "006";
    public static final String FROM_BLACK_SKIN = "007";
    public static final String FROM_LOAD_MORE = "008";
    public static final String FROM_UPDATE = "009";
    private static SkinController mInstance;
    private Context mContext;
    private SkinStastus_DataBase skinStatusDatabase;
    private SkinThread_DataBase skinThreadDatabase;
    private ExpressionStatusDatabase expressionStatusDatabase;

    public SkinController(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        if(this.skinStatusDatabase == null) {
            this.skinStatusDatabase = new SkinStastus_DataBase(mContext.getApplicationContext());
        }

        if(this.skinThreadDatabase == null) {
            this.skinThreadDatabase = new SkinThread_DataBase(mContext.getApplicationContext());
        }

        if(this.expressionStatusDatabase == null) {
            this.expressionStatusDatabase = new ExpressionStatusDatabase(mContext.getApplicationContext());
        }

    }

    public static SkinController getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new SkinController(context);
        }

        return mInstance;
    }
    //http://coin.seeyouyima.com/app/theme-skin?style=1&app_id=1&mode=2&v=6.0&platform=android&mode=2&device_id=990006202219704


    public void intDataBase() {
        this.skinStatusDatabase = new SkinStastus_DataBase(this.mContext.getApplicationContext());
        this.skinThreadDatabase = new SkinThread_DataBase(this.mContext.getApplicationContext());
        this.expressionStatusDatabase = new ExpressionStatusDatabase(this.mContext.getApplicationContext());
    }

    public void copyFileAndUseThemeNight(final Activity mContext, final SkinModel model, boolean isMyActivty, final OnNotifationListener listener) {
        ThreadUtil.addTask(mContext.getApplicationContext(),  new ThreadUtil.ITasker() {
            public Object onExcute() {
                boolean isSucess = false;
                if(SkinUtil.getInstance().isSkinFileExistsInData(mContext, model.getFileName())) {
                    isSucess = true;
                } else {
                    isSucess = SkinUtil.getInstance().copyFile2Data(mContext.getApplicationContext(), new File(SkinUtil.getInstance().getDownloadPath(mContext.getApplicationContext()) + "/" + model.getFileName()), model.getFileName());
                }

                return Boolean.valueOf(isSucess);
            }

            public void onFinish(Object result) {
                boolean isSucess = ((Boolean)result).booleanValue();
                if(isSucess) {
                    DataSaveHelper.getInstance(mContext.getApplicationContext()).saveSkinNightId(model.skinId);
                    DataSaveHelper.getInstance(mContext.getApplicationContext()).setIsNightMode(true);
                    DataSaveHelper.getInstance(mContext.getApplicationContext()).saveNightSkinApkName(model.getFileName());
                    DataSaveHelper.getInstance(mContext.getApplicationContext()).saveSkinNightName(model.skinName);
                    SkinEngine.getInstance().initResources();
                    ExtendOperationController.getInstance().notify(-1060003, Integer.valueOf(0));
                    if(listener != null) {
                        listener.onNitifation(Boolean.valueOf(true));
                    }
                } else {
                    ToastUtils.showToast(mContext.getApplicationContext(), "切换失败");
                }

            }
        });
    }

    public SkinModel getNightSkinModel(OnFollowListener listener) {
        Object nightSkinModel = null;
        ArrayList nightSkinModels = new ArrayList();

        try {
            if(!DataSaveHelper.getInstance(this.mContext).getNightSkinNew()) {
                List e = this.getListFromCache("skin_file");
                Iterator var5 = e.iterator();

                while(var5.hasNext()) {
                    SkinModel model = (SkinModel)var5.next();
                    if(model.skinPackageName.equals("com.meetyou.skin.night")) {
                        nightSkinModels.add(model);
                        break;
                    }
                }
            }

            if(nightSkinModels.size() != 0) {
                return (SkinModel)this.getSkinStatus((DecorationModel)nightSkinModels.get(0), true);
            }

            listener.OnFollow(0);
            nightSkinModels.addAll(this.getNetNightSkinModel());
            if(nightSkinModels.size() > 0) {
                return (SkinModel)this.getSkinStatus((DecorationModel)nightSkinModels.get(0), true);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return (SkinModel)nightSkinModel;
    }

    public List<SkinModel> getNetNightSkinModel() {
        ArrayList nightSkinModels = new ArrayList();
        String str = getFromAssets("nightmode.txt",mContext);
        if(!StringUtils.isNull(str)) {
            JSONObject obj = null;

            try {
                obj = new JSONObject(str);
                SkinModel e = new SkinModel(obj, 0);
                nightSkinModels.add(e);
                this.saveNightListToCache(nightSkinModels, "skin_file");
            } catch (JSONException var6) {
                var6.printStackTrace();
            }
        }

        return nightSkinModels;
    }

    public void saveNightListToCache(List<SkinModel> list, String file) {
        try {
            List ex = this.getListFromCache(file);
            ex.addAll(list);
            FileUtils.saveObjectToLocal(this.mContext, ex, file + UserController.getInstance().getUserId(this.mContext));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public String getFromAssets(String fileName,final Context context){
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public List<SkinModel> getListFromCache(String file) {
        try {
            List ex = (List) FileUtils.getObjectFromLocal(this.mContext, file + UserController.getInstance().getUserId(this.mContext));
            return (List)(ex != null?ex:new ArrayList());
        } catch (Exception var3) {
            var3.printStackTrace();
            return new ArrayList();
        }
    }


    public DecorationModel getSkinStatus(DecorationModel model, boolean isSkin) {
        List dbModels;
        if(isSkin) {
            dbModels = this.skinStatusDatabase.selectStatusModels();
        } else {
            dbModels = this.expressionStatusDatabase.selectStatusModels();
        }

        Iterator var4 = dbModels.iterator();

        while(true) {
            while(true) {
                while(true) {
                    DecorationModel dbModel;
                    do {
                        if(!var4.hasNext()) {
                            if((model.is_exchanged || model.is_prize) && (SkinUtil.getInstance().isSkinFileExistsInData(this.mContext, model.getFileName()) || isSkin && SkinUtil.getInstance().isSkinFileExistsInSDCard(this.mContext, model.getFileName())) && model.updateStastus != 5) {
                                model.updateStastus = 4;
                            }

                            return model;
                        }

                        dbModel = (DecorationModel)var4.next();
                    } while(model.skinId != dbModel.skinId);

                    LogUtils.d("SkinController", "model.version: " + model.version + "  dbModel.version: " + dbModel.version);
                    if(!SkinUtil.getInstance().isSkinFileExistsInData(this.mContext, model.getFileName()) && (!isSkin || !SkinUtil.getInstance().isSkinFileExistsInSDCard(this.mContext, model.getFileName()))) {
                        if(model.version > dbModel.version && (dbModel.updateStastus == 1 || dbModel.updateStastus == 2 || dbModel.updateStastus == 6 || dbModel.updateStastus == 3 || dbModel.updateStastus == 4)) {
                            this.skinStatusDatabase.deleteStatusmModel(dbModel);
                            this.skinThreadDatabase.deleteThreadData(dbModel);
                            model.updateStastus = 5;
                        } else if(dbModel.updateStastus != 4 && dbModel.updateStastus != 3) {
                            model.updateStastus = dbModel.updateStastus;
                            model.completeSize = dbModel.completeSize;
                        } else {
                            model.updateStastus = 0;
                        }
                    } else if(model.version > dbModel.version) {
                        model.updateStastus = 5;
                    } else {
                        model.updateStastus = 4;
                        model.completeSize = dbModel.completeSize;
                    }
                }
            }
        }
    }


//    public SkinListModel loadSkinData(int catalog_id, int activity_id, int page) {
//        SkinListModel skinListModel = new SkinListModel();
//        Object listResult = new ArrayList();
//
//        try {
//            if(!NetWorkStatusUtil.queryNetWork(this.mContext.getApplicationContext())) {
//                if(page == 0) {
//                    listResult = this.getListFromCache("skin_file");
//                }
//            } else {
//                HttpResult ex = MainController.getInstance().getLoadSkinCatalogActivityIdData(this.mContext.getApplicationContext(), catalog_id, activity_id, page);
//                if(!ex.isSuccess()) {
//                    if(page == 0) {
//                        listResult = this.getListFromCache("skin_file");
//                    }
//                } else {
//                    String response = ex.getResult().toString();
//                    if(StringUtils.isNull(response)) {
//                        if(page == 0) {
//                            listResult = this.getListFromCache("skin_file");
//                        }
//                    } else {
//                        JSONObject object = new JSONObject(response);
//                        if(object.has("mine")) {
//                            JSONObject page_value = object.getJSONObject("mine");
//                            int array = StringUtils.getJsonInt(page_value, "coin");
//                            DataSaveHelper.getInstance(this.mContext).setUserCoin(array);
//                        }
//
//                        int var15 = StringUtils.getJsonInt(object, "page");
//                        JSONArray var16 = object.getJSONArray("data");
//
//                        for(int activityObject = 0; activityObject < var16.length(); ++activityObject) {
//                            JSONObject obj = var16.getJSONObject(activityObject);
//                            SkinModel model = new SkinModel(obj, var15);
//                            ((List)listResult).add(model);
//                        }
//
//                        if(object.has("activity")) {
//                            JSONObject var17 = object.getJSONObject("activity");
//                            skinListModel.activity.description = StringUtils.getJsonString(var17, "description");
//                            skinListModel.activity.id = StringUtils.getJsonInt(var17, "id");
//                            skinListModel.activity.name = StringUtils.getJsonString(var17, "name");
//                        }
//
//                        this.saveListToCache((List)listResult, "skin_file");
//                    }
//                }
//            }
//
//            this.compareDatabase((List)listResult, true);
//            skinListModel.models = (List)listResult;
//        } catch (Exception var14) {
//            var14.printStackTrace();
//        }
//
//        return skinListModel;
//    }
}
