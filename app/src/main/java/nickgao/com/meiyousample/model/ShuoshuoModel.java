package nickgao.com.meiyousample.model;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nickgao.com.framework.utils.KeyUtil;
import nickgao.com.framework.utils.StringUtils;

/**
 *
 * 发布说说的 model
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-13
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public class ShuoshuoModel implements Serializable {

    public long id;
    //内容
    public String content="";
    //压缩过的图片路径
    public List<String> listPictures = new ArrayList<String>();
    //时间
    public String time="";
    //图片是否已经上传过
    public boolean isImageUploaded = false;

    //相机原图
    public List<String> listUrl = new ArrayList<String>();
    //相机缩略图
    public List<String> listThumbUrl = new ArrayList<String>();
    //选中的图片id
    public List<String> listImageID = new ArrayList<String>();
    //来自相册还是拍照
    public List<String> listImageFrom = new ArrayList<String>();

    //只用于发送队列，其他key则采用 KeyUtil.getShuoshuoVerify(mContext,content);
    public String uuid;
    
   

    //public String verify_str;

    public ShuoshuoModel(Context context){

        if(uuid==null){
            uuid = KeyUtil.getShuoshuoVerify(context, "");
            //verify_str = uuid;
        }
    }

    public void setContent(Context context,String content){
    	this.content = content;
    	uuid = KeyUtil.getShuoshuoVerify(context, content);
    }

    public String getContent(){
    	return content;
    }

    public String getImageIDList(){
        try{
            StringBuilder stringBuilder = new StringBuilder();
            for(String string:listImageID){
                stringBuilder.append(string).append(";");
            }
            return stringBuilder.toString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    public void setImageIDList(String ids){
        try{
            String []strings = ids.split(";");
            if(strings==null)
                return;
            listImageID.clear();
            int len = strings.length;
            for (String conent : strings) {
                listImageID.add(conent);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    /**
     * 获取标记说说的唯一值
     * @return
     */
    public String getOnlyKey(){
        if(uuid==null){
            uuid = UUID.randomUUID().toString();
        }
        return uuid;
    }

    /**
     * 设置唯一值
     * @param uuids
     */
    public void setOnlyKey(String uuids){
        uuid = uuids;
    }



    public String getImageSrcList(){
        try{
            StringBuilder stringBuilder = new StringBuilder();
            for(String string:listUrl){
                if(!StringUtils.isNull(string))
                stringBuilder.append(string).append(";");
            }
            return stringBuilder.toString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";

    }
    public void setImageSrcList(String imageList){
        try{
            String []strings = imageList.split(";");
            if(strings==null)
                return;
            listUrl.clear();
            int len = strings.length;
            for (String conent : strings) {
                if (!StringUtils.isNull(conent))
                    listUrl.add(conent);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }




    public String getImageThumbList(){
        try{
            StringBuilder stringBuilder = new StringBuilder();
            for(String string:listThumbUrl){
                if(!StringUtils.isNull(string))
                stringBuilder.append(string).append(";");
            }
            return stringBuilder.toString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";

    }

    public void setImageThumbList(String imageList){
        try{
            String []strings = imageList.split(";");
            if(strings==null)
                return;
            listThumbUrl.clear();
            int len = strings.length;
            for (String conent : strings) {
                if (!StringUtils.isNull(conent))
                    listThumbUrl.add(conent);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }




    public String getImageList(){
        try{
            StringBuilder stringBuilder = new StringBuilder();
            for(String string:listPictures){
                if(!StringUtils.isNull(string)){
                    stringBuilder.append(string).append(";");
                }

            }
            return stringBuilder.toString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";

    }

    public void setImageList(String imageList){
        try{
           String []strings = imageList.split(";");
           if(strings==null || strings.length==0)
               return;
            listPictures.clear();
            int len = strings.length;
            for (String conent : strings) {
                if (!StringUtils.isNull(conent))
                    listPictures.add(conent);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


    public String getImageFromList(){
        try{
            StringBuilder stringBuilder = new StringBuilder();
            for(String string:listImageFrom){
                if(!StringUtils.isNull(string)){
                    stringBuilder.append(string).append(";");
                }

            }
            return stringBuilder.toString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";

    }

    public void setImageFromList(String imageList){
        try{
            String []strings = imageList.split(";");
            if(strings==null || strings.length==0)
                return;
            listImageFrom.clear();
            int len = strings.length;
            for (String conent : strings) {
                if (!StringUtils.isNull(conent))
                    listImageFrom.add(conent);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }



    //是否是空的说说
    public boolean isEmpty(){

        return !(!StringUtils.isNull(content) || listPictures.size() > 0);

    }
}
