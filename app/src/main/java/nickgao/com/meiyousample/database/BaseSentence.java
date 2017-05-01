package nickgao.com.meiyousample.database;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class BaseSentence {

    public static  String PRIMARY_KEY = "tid";
    protected String mTableName;
    protected HashMap<String, String> mColumnMap;
    public BaseSentence(String tableName){
        this.mTableName = tableName;
        this.mColumnMap = new HashMap<String, String>();
    }

    public void add(String columnName, Object columnType){
        String value = "varchar";
        if(columnType instanceof Integer){
            value = "integer";
        }else if(columnType instanceof Long){
            value = "long";
        }
        mColumnMap.put(columnName, value);
    }

    public void add(String columnName, String columnType){
        mColumnMap.put(columnName, columnType);
    }

    public String create(){
        String sql = "create table "+ mTableName +"(";
        sql += PRIMARY_KEY+ " integer primary key";

        Set<Entry<String, String>> set = mColumnMap.entrySet();
        for (Entry<String, String> entry : set) {
            sql += ","+ entry.getKey()+ " " + entry.getValue();
        }
        sql += ")";
        return sql;
    }

    public String createIfNotExists(){
        String sql = "create table if not exists "+ mTableName +"(";
        sql += PRIMARY_KEY+ " integer primary key";

        Set<Entry<String, String>> set = mColumnMap.entrySet();
        for (Entry<String, String> entry : set) {
            sql += ","+ entry.getKey()+ " " + entry.getValue();
        }
        sql += ")";
        return sql;
    }


}
