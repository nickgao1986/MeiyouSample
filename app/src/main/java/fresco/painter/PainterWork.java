package fresco.painter;


import fresco.view.FrescoPainterPen;
import fresco.view.IFrescoImageView;

/**
 * Created by gaoyoujian on 2017/4/26.
 */

public interface PainterWork {
    void paint(IFrescoImageView draweeView, FrescoPainterPen pen);
}
