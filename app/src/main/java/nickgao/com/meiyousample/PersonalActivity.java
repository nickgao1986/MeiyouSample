package nickgao.com.meiyousample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import fresco.view.ImageLoader;

public class PersonalActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoader.initialize(this, false);

        setContentView(R.layout.activity_main);

    }


}
