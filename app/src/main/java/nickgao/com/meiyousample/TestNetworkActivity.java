package nickgao.com.meiyousample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by gaoyoujian on 2017/3/23.
 */

public class TestNetworkActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_network_layout);
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

    }


    private void sendRequest() {
//        DynamicService service = (DynamicService) ServiceFactory.getInstance().getService(DynamicService.class.getName());
//        service.sendRequest();
//        UserHomeService service1 = (UserHomeService) ServiceFactory.getInstance().getService(UserHomeService.class.getName());
//        service1.sendRequest();
    }
}
