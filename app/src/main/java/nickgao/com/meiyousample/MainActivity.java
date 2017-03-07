package nickgao.com.meiyousample;

import android.app.Activity;
import android.os.Bundle;

import javax.inject.Inject;

import dagger.activity.Wukong;

public class MainActivity extends Activity {

    @Inject
    Wukong wukong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal_head);


//        XiYouComponent xiYouComponent = DaggerXiYouComponent.builder().xiYouModule(new XiYouModule()).build();
//        xiYouComponent.inject(this);
//        xiYouComponent.inject(wukong);
//        TextView textView = (TextView)findViewById(R.id.mytext);
//        LogUtils.d("wukong="+wukong);
//        if(wukong != null) {
//            textView.setText(wukong.useJingGubang());
//
//        }

    }


}
