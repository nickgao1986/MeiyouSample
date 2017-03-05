package nickgao.com.meiyousample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.activity.Wukong;
import dagger.components.DaggerXiYouComponent;
import dagger.components.XiYouComponent;
import dagger.modules.XiYouModule;
import nickgao.com.meiyousample.utils.LogUtils;

public class MainActivity extends Activity {

    @Inject
    Wukong wukong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        XiYouComponent xiYouComponent = DaggerXiYouComponent.builder().xiYouModule(new XiYouModule()).build();
        xiYouComponent.inject(this);
        xiYouComponent.inject(wukong);
        TextView textView = (TextView)findViewById(R.id.mytext);
        LogUtils.d("wukong="+wukong);
        if(wukong != null) {
            textView.setText(wukong.useJingGubang());

        }

    }


}
