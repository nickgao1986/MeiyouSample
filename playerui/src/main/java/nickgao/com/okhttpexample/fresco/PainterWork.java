package nickgao.com.okhttpexample.fresco;

import nickgao.com.okhttpexample.view.FrescoPainterPen;
import nickgao.com.okhttpexample.view.IFrescoImageView;

/**
 * Created by gaoyoujian on 2017/4/26.
 */

public interface PainterWork {
    void paint(IFrescoImageView draweeView, FrescoPainterPen pen);
}
