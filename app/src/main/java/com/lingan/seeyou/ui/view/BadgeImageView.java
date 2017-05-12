package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TabWidget;

import nickgao.com.okhttpexample.view.FrescoImageView;


/**
 * Created by Administrator on 2014/9/3.
 * 角标控件
 */
public class BadgeImageView extends FrescoImageView {
    public final int POSITION_TOP_LEFT = 1;
    public final int POSITION_TOP_RIGHT = 2;
    public final int POSITION_BOTTOM_LEFT = 3;
    public static final int POSITION_BOTTOM_RIGHT = 4;
    public static final int POSITION_CENTER = 5;

    private final int DEFAULT_MARGIN_DIP = 0;
    private final int DEFAULT_LR_PADDING_DIP = 0;
    private final int DEFAULT_CORNER_RADIUS_DIP = 0;
    private final int DEFAULT_POSITION = POSITION_TOP_RIGHT;
    private final int DEFAULT_BADGE_COLOR = Color.parseColor("#CCFF0000"); //Color.RED;
    private final int DEFAULT_TEXT_COLOR = Color.WHITE;

    private Animation fadeIn;
    private Animation fadeOut;

    private Context context;
    private View target;

    private int badgePosition;
    private int badgeMarginH;
    private int badgeMarginV;
    private int badgeColor;

    private boolean isShown;

    private ShapeDrawable badgeBg;

    private int targetTabIndex;

    public FrameLayout container;

    public BadgeImageView(Context context) {
        this(context, (AttributeSet) null, android.R.attr.textViewStyle);
    }

    public BadgeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }


    public BadgeImageView(Context context, View target) {
        this(context, null, android.R.attr.textViewStyle, target, 0);
    }


    public BadgeImageView(Context context, TabWidget target, int index) {
        this(context, null, android.R.attr.textViewStyle, target, index);
    }

    public BadgeImageView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, null, 0);
    }

    public BadgeImageView(Context context, AttributeSet attrs, int defStyle, View target, int tabIndex) {
        super(context, attrs, defStyle);
        init(context, target, tabIndex);
    }

    private void init(Context context, View target, int tabIndex) {

        this.context = context;
        this.target = target;
        this.targetTabIndex = tabIndex;

        // defaults
        badgePosition = DEFAULT_POSITION;
        badgeMarginH = dipToPixels(DEFAULT_MARGIN_DIP);
        badgeMarginV = badgeMarginH;
        badgeColor = DEFAULT_BADGE_COLOR;

        int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
        setPadding(paddingPixels, 0, paddingPixels, 0);

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(200);

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(200);

        isShown = false;

        if (this.target != null) {
            applyTo(this.target);
        } else {
            show();
        }

    }

    private void applyTo(View target) {
        try {

            ViewGroup.LayoutParams lp = target.getLayoutParams();
            ViewParent parent = target.getParent();
            container = new FrameLayout(context);

            if (target instanceof TabWidget) {
                target = ((TabWidget) target).getChildTabViewAt(targetTabIndex);
                this.target = target;

                ((ViewGroup) target).addView(container,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

                this.setVisibility(View.GONE);
                container.addView(this);

            } else {
                ViewGroup group = (ViewGroup) parent;
                int index = group.indexOfChild(target);
                group.removeView(target);
                group.addView(container, index, lp);
                container.addView(target);
                this.setVisibility(View.GONE);
                container.addView(this);
                group.invalidate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void show() {
        show(false, null);
    }

    public void show(boolean animate) {
        show(animate, fadeIn);
    }

    public void show(Animation anim) {
        show(true, anim);
    }

    public void hide() {
        hide(false, null);
    }

    public void hide(boolean animate) {
        hide(animate, fadeOut);
    }

    public void hide(Animation anim) {
        hide(true, anim);
    }

    public void toggle() {
        toggle(false, null, null);
    }

    public void toggle(boolean animate) {
        toggle(animate, fadeIn, fadeOut);
    }

    public void toggle(Animation animIn, Animation animOut) {
        toggle(true, animIn, animOut);
    }

    private void show(boolean animate, Animation anim) {
//        if (getBackground() == null) {
//            if (badgeBg == null) {
//                badgeBg = getDefaultBackground();
//            }
//            setBackgroundDrawable(badgeBg);
//        }
        applyLayoutParams();

        if (animate) {
            this.startAnimation(anim);
        }
        this.setVisibility(View.VISIBLE);
        isShown = true;
    }

    private void hide(boolean animate, Animation anim) {
        this.setVisibility(View.GONE);
        if (animate) {
            this.startAnimation(anim);
        }
        isShown = false;
    }

    private void toggle(boolean animate, Animation animIn, Animation animOut) {
        if (isShown) {
            hide(animate && (animOut != null), animOut);
        } else {
            show(animate && (animIn != null), animIn);
        }
    }

    private ShapeDrawable getDefaultBackground() {

        int r = dipToPixels(DEFAULT_CORNER_RADIUS_DIP);
        float[] outerR = new float[]{r, r, r, r, r, r, r, r};

        RoundRectShape rr = new RoundRectShape(outerR, null, null);
        ShapeDrawable drawable = new ShapeDrawable(rr);
        drawable.getPaint().setColor(badgeColor);

        return drawable;

    }

    private void applyLayoutParams() {
        try {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            switch (badgePosition) {
                case POSITION_TOP_LEFT:
                    lp.gravity = Gravity.LEFT | Gravity.TOP;
                    lp.setMargins(badgeMarginH, badgeMarginV, 0, 0);
                    break;
                case POSITION_TOP_RIGHT:
                    lp.gravity = Gravity.RIGHT | Gravity.TOP;
                    lp.setMargins(0, badgeMarginV, badgeMarginH, 0);
                    break;
                case POSITION_BOTTOM_LEFT:
                    lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
                    lp.setMargins(badgeMarginH, 0, 0, badgeMarginV);
                    break;
                case POSITION_BOTTOM_RIGHT:
                    lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
                    lp.setMargins(0, 0, badgeMarginH, badgeMarginV);
                    break;
                case POSITION_CENTER:
                    lp.gravity = Gravity.CENTER;
                    lp.setMargins(0, 0, 0, 0);
                    break;
                default:
                    break;
            }

            setLayoutParams(lp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public View getTarget() {
        return target;
    }


    @Override
    public boolean isShown() {
        return isShown;
    }


    public int getBadgePosition() {
        return badgePosition;
    }


    public void setBadgePosition(int layoutPosition) {
        this.badgePosition = layoutPosition;
    }


    public int getHorizontalBadgeMargin() {
        return badgeMarginH;
    }


    public int getVerticalBadgeMargin() {
        return badgeMarginV;
    }


    public void setBadgeMargin(int badgeMargin) {
        this.badgeMarginH = badgeMargin;
        this.badgeMarginV = badgeMargin;
    }


    public void setBadgeMargin(int horizontal, int vertical) {
        this.badgeMarginH = horizontal;
        this.badgeMarginV = vertical;
    }


    public int getBadgeBackgroundColor() {
        return badgeColor;
    }


    public void setBadgeBackgroundColor(int badgeColor) {
        this.badgeColor = badgeColor;
        badgeBg = getDefaultBackground();
    }

    private int dipToPixels(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }

    /**
     * 隐藏整个控件
     */
    public void hideContainer() {
        if (container != null) {
            container.setVisibility(View.GONE);
        }
    }
    /**
     * 显示整个控件
     */
    public void showContainer() {
        if (container != null) {
            container.setVisibility(View.VISIBLE);
        }
    }
}
