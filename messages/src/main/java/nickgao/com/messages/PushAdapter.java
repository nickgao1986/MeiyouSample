package nickgao.com.messages;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by gaoyoujian on 2017/5/16.
 */

public interface PushAdapter {

    public static final String KEY_APP_KEY = "app-key";
    public static final String KEY_APP_ID = "app-id";

    public static final String KEY_VERSION = "version";
    public static final String KEY_CLIENT_VERSION = "client-version";
    public static final String KEY_DEVICE_TYPE = "device-type";
    public static final String KEY_SERVER_URL = "server-url";
    public static final String KEY_SERVER_PORT = "server-port";


    public void init(Context context, Bundle bundle);

    @Deprecated
    public void registerUser(long userId);

    public void registerUser(long userId, boolean isTest);

    public void unRegister();

    public int getType();
}
