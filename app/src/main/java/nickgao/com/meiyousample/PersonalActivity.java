package nickgao.com.meiyousample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.meetyou.crsdk.util.ImageLoader;


public class PersonalActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoader.initialize(this, false);
        android.os.Debug.startMethodTracing("");
        setContentView(R.layout.activity_main);
        android.os.Debug.stopMethodTracing();
    }


}
