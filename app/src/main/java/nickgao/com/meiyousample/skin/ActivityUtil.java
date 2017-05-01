package nickgao.com.meiyousample.skin;

import android.content.Context;
import android.content.Intent;

import nickgao.com.meiyousample.firstPage.SeeyouActivity;

/**
 * Created by gaoyoujian on 2017/5/1.
 */

public class ActivityUtil {

    public static void backToMainActivity(Context context) {
        Intent intent = new Intent(context, SeeyouActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
