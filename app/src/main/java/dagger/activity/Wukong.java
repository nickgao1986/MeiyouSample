package dagger.activity;

import javax.inject.Inject;

/**
 * Created by gaoyoujian on 2017/3/5.
 */

public class Wukong {

    @Inject
    JinGuBang mJinGuBang;

    @Inject
    public Wukong() {

    }


    public String useJingGubang() {
        return this.mJinGuBang.use();
    }
}
