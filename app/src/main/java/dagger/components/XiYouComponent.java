package dagger.components;

import javax.inject.Singleton;

import dagger.Component;
import dagger.activity.Wukong;
import dagger.modules.XiYouModule;
import nickgao.com.meiyousample.MainActivity;

/**
 * Created by gaoyoujian on 2017/3/5.
 */
@Component(modules = {XiYouModule.class})
@Singleton
public interface XiYouComponent {

    void inject(Wukong wk);
    void inject(MainActivity a);
}
