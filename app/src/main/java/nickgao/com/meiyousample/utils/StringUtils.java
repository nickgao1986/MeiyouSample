package nickgao.com.meiyousample.utils;

import android.graphics.Paint;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by gaoyoujian on 2017/3/5.
 */

public class StringUtils {

    public StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null?str2 == null:str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null?str2 == null:str1.equalsIgnoreCase(str2);
    }

    public static int indexOf(String str, char searchChar) {
        return isEmpty(str)?-1:str.indexOf(searchChar);
    }

    public static String getJsonString(JSONObject jsonObject, String key) {
        try {
            if(jsonObject.has(key)) {
                return jsonObject.getString(key);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return "";
    }

    public static String getString(int value) {
        try {
            return String.valueOf(value);
        } catch (Exception var2) {
            var2.printStackTrace();
            return "";
        }
    }

    public static int getJsonInt(JSONObject jsonObject, String key) {
        try {
            if(isNull(key)) {
                return 0;
            }

            if(jsonObject.has(key)) {
                return jsonObject.getInt(key);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return 0;
    }

    public static boolean getJsonBoolean(JSONObject object, String key) {
        Boolean strValue = Boolean.valueOf(false);

        try {
            if(object != null && key != null) {
                if(object.has(key)) {
                    strValue = Boolean.valueOf(object.getBoolean(key));
                }

                return strValue.booleanValue();
            } else {
                return strValue.booleanValue();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            return strValue.booleanValue();
        }
    }


    public static boolean isNull(String str) {
        try {
            return str == null?true:(str != null?(!str.equals("") && !str.equals("null") && !str.equals("[]")?str.trim().equals("") || str.trim().equals("null"):true):false);
        } catch (Exception var2) {
            var2.printStackTrace();
            return true;
        }
    }

    public static JSONObject getJsonObejct(JSONObject object, String key) {
        JSONObject jsonObject = null;

        try {
            if(object == null || key == null) {
                return null;
            }

            if(object.has(key)) {
                JSONObject obj = object.getJSONObject(key);
                if(obj != null) {
                    jsonObject = (JSONObject)obj;
                }
            }
        } catch (Exception var4) {
            ;
        }

        return jsonObject;
    }

    public static JSONArray getJsonArray(JSONObject object, String key) {
        JSONArray isonArray = new JSONArray();

        try {
            if(object == null || key == null) {
                return null;
            }

            if(object.has(key)) {
                JSONArray obj = object.getJSONArray(key);
                if(obj != null) {
                    isonArray = (JSONArray)obj;
                }
            }
        } catch (Exception var4) {
            ;
        }

        return isonArray;
    }

    /**
     * Tests if a string is blank: null, emtpy, or only whitespace (" ", \r\n, \t, etc)
     * @param string string to test
     * @return if string is blank
     */
    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0)
            return true;

        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!StringUtil.isWhitespace(string.codePointAt(i)))
                return false;
        }
        return true;
    }

    public static int getInt(String value) {
        try {
            return value != null && !value.equals("") && !value.equals("null")?Integer.valueOf(value).intValue():0;
        } catch (Exception var2) {
            var2.printStackTrace();
            return 0;
        }
    }

    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int)Math.ceil((double)(fm.descent - fm.top)) + 2;
    }

    public static String getDynamicnum(int dynamicnum) {
        if (StringUtils.sizeOfInt(dynamicnum) > 5) {
            int value = dynamicnum / 10000;
            return value + "w";
        } else {
            return dynamicnum + "";
        }
    }

    public static int sizeOfInt(int x) {
        for (int i = 0; ; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

    final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};


}
