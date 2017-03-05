package dagger.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.activity.JinGuBang;
import dagger.activity.Wukong;

/**
 * Created by gaoyoujian on 2017/3/5.
 */
@Module
public class XiYouModule {

    @Provides
    @Singleton
    Wukong provideWuKong() {
        return new Wukong();
    }

    @Provides
    @Singleton
    JinGuBang provideJinGuBang() {
        return new JinGuBang();
    }
}
