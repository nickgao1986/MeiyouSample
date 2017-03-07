package com.lingan.seeyou.ui.view;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import nickgao.com.meiyousample.utils.LogUtils;


/**
 * 重写LinkMovemenMethod 处理长按和ClickableSpan冲突 Created by Administrator on
 * 2014/8/9.
 */
public class LinkMovementClickMethod extends LinkMovementMethod {

	private long lastClickTime;

	private static final long CLICK_DELAY = 500l;

	@Override
	public boolean onTouchEvent(TextView widget, Spannable buffer,
			MotionEvent event) {
		int action = event.getAction();

		if (action == MotionEvent.ACTION_UP
				|| action == MotionEvent.ACTION_DOWN) {
			int x = (int) event.getX();
			int y = (int) event.getY();

			x -= widget.getTotalPaddingLeft();
			y -= widget.getTotalPaddingTop();

			x += widget.getScrollX();
			y += widget.getScrollY();

			Layout layout = widget.getLayout();
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line, x);

			ClickableSpan[] link = buffer.getSpans(off, off,
					ClickableSpan.class);

			if (link.length != 0) {
				if (action == MotionEvent.ACTION_UP) {
					if (System.currentTimeMillis() - lastClickTime < CLICK_DELAY) {
						if (widget instanceof CustomUrlTextView) {
							((CustomUrlTextView) widget).setLinkClick(true);
						}
						LogUtils.d("Clickable on click");
						link[0].onClick(widget);
					}
				} else if (action == MotionEvent.ACTION_DOWN) {
					Selection.setSelection(buffer,
							buffer.getSpanStart(link[0]),
							buffer.getSpanEnd(link[0]));
					lastClickTime = System.currentTimeMillis();
				}
				return true;
			} else {
				Selection.removeSelection(buffer);
			}
		}
		return super.onTouchEvent(widget, buffer, event);
	}

	public static LinkMovementClickMethod getInstance() {
		if (null == sInstance) {
			sInstance = new LinkMovementClickMethod();
		}
		return sInstance;
	}

	private static LinkMovementClickMethod sInstance;

}
