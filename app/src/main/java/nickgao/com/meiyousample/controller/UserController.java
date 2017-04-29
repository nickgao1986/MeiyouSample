package nickgao.com.meiyousample.controller;

import android.content.Context;

/**
 * Created by gaoyoujian on 2017/4/29.
 */

public class UserController {

    private static UserController instance;

    public UserController() {
    }

    public static UserController getInstance() {
        if(instance == null) {
            instance = new UserController();
        }

        return instance;
    }

    public int getUserId(Context context) {
        return 129746620;
    }
}
