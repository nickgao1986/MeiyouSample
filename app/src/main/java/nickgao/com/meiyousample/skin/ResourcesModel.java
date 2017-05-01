package nickgao.com.meiyousample.skin;

import android.content.res.Resources;

/**
 * Created by Administrator on 2014/9/24.
 */
public class ResourcesModel {
    public Resources resources;
    public String packageName;
    public String apkName;

    public ResourcesModel(Resources resources,String packageName,String apkName){
        this.resources = resources;
        this.packageName = packageName;
        this.apkName = apkName;
    }
}
