package com.lingan.seeyou.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import nickgao.com.meiyousample.R;


/**
 * 小圆点 【自动修正数量】
 *
 * @author ziv
 */
public class YiPageIndicator extends View {
    private static final String TAG = "YiPageIndicator";
    private int mCurrentPage = -1;
    private int mTotalPage = 0;
    // private int mNormalResId = R.drawable.banner_dot;
    // private int mCurrentResId = R.drawable.banner_dot_up;
    private int mSpace = 8;
    //private int mItemHeight;
    //private int mItemWight;
    //public Bitmap bitmap;
    private Context mContext;
    private int normalColor;
    private int pressColor;
    private boolean normalKongxin, selectKongxin;
    private int mIconWH = 15;

    public YiPageIndicator(Context context) {
        super(context);
        mContext = context;
        //如果没有设置attr属性，就是dot的颜色就是这样；
        normalColor = mContext.getResources().getColor(R.color.white_a);
        pressColor = mContext.getResources().getColor(R.color.red_b);//SkinEngine.getInstance().getResouceColor(mContext, R.color.red_b);
        normalKongxin = false;
        selectKongxin = false;
        init(context);
    }

    public YiPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initStyleable(context, attrs);
        init(context);
    }

    public YiPageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initStyleable(context, attrs);
        init(context);
    }

    private void initStyleable(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.indicator);
        //mNormalResId = a.getResourceId(R.styleable.indicator_normalIndicator, R.drawable.apk_leading_dot_grey);
        //mCurrentResId = a.getResourceId(R.styleable.indicator_currentIndicator, R.drawable.apk_leading_dot_white);

        normalColor = a.getColor(R.styleable.indicator_normalColor, mContext.getResources().getColor(R.color.white_a));
        pressColor = a.getColor(R.styleable.indicator_selectColor, mContext.getResources().getColor(R.color.red_b));

        normalKongxin = a.getBoolean(R.styleable.indicator_normalKongxin, false);
        selectKongxin = a.getBoolean(R.styleable.indicator_selectKongxin, false);

        mIconWH = (int) a.getDimension(R.styleable.indicator_icon_w_h, 15f);

        a.recycle();

       // setKongxin(true, false);
      //  setDotColor(mContext.getResources().getColor(R.color.white_a), mContext.getResources().getColor(R.color.white_a));
    }

    /**
     * 设置是否空心
     *
     * @param normal
     * @param select
     */
    public void setKongxin(boolean normal, boolean select) {
        normalKongxin = normal;
        selectKongxin = select;
    }

    public void setDotColor(int normal, int select) {
        normalColor = normal;
        pressColor = select;
    }

    /**
     * 刷新
     */
    public void update() {
        invalidate();
    }

    private void init(Context context) {
        mSpace = (int) (8 * context.getResources().getDisplayMetrics().density);
        if (isInEditMode()) {
            setTotalPage(4);
            setCurrentPage(2);
        }
       /* Drawable currentDrawable = getContext().getResources().getDrawable(mCurrentResId);
        Drawable normailDrawable = getContext().getResources().getDrawable(mNormalResId);
        int ch = currentDrawable.getIntrinsicHeight();
        int cw = currentDrawable.getIntrinsicWidth();
        int nh = normailDrawable.getIntrinsicHeight();
        int nw = normailDrawable.getIntrinsicWidth();
        mItemHeight = Math.max(ch, nh);
        mItemWight = Math.max(cw, nw);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // heightMeasureSpec = MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.AT_MOST);
        // int w = mItemWight * mTotalPage + (mTotalPage - 1) * mSpace;
        // widthMeasureSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension(w, mItemHeight);
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = mIconWH * mTotalPage + (mTotalPage - 1) * mSpace + getPaddingLeft() + getPaddingRight();
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = mIconWH + getPaddingTop() + getPaddingBottom();
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    public void setTotalPage(int nPageNum) {
        mTotalPage = nPageNum;
        if (mCurrentPage >= mTotalPage)
            mCurrentPage = mTotalPage - 1;
        requestLayout();
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int nPageIndex) {
        try {
            if (nPageIndex < 0 || nPageIndex >= mTotalPage)
                return;

            if (mCurrentPage != nPageIndex) {
                mCurrentPage = nPageIndex;
                this.invalidate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Paint paint, mPaintEmpty;
    private Rect r, r1;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (paint == null)
            paint = new Paint();
        if (mPaintEmpty == null)
            mPaintEmpty = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        mPaintEmpty.setStyle(Paint.Style.STROKE);
        mPaintEmpty.setAntiAlias(true);
        //paint.setColor(Color.BLACK);


        if (r == null)
            r = new Rect();
        this.getDrawingRect(r);

        int iconWidth = mIconWH;
        int iconHeight = mIconWH;

        int x = (r.width() - (iconWidth * mTotalPage + mSpace * (mTotalPage - 1))) / 2;
        int y = (r.height() - iconHeight) / 2;

        for (int i = 0; i < mTotalPage; i++) {
            //int resid = mNormalResId;
            if (i == mCurrentPage) {
                paint.setColor(pressColor);
                mPaintEmpty.setColor(pressColor);
                //resid = mCurrentResId;
            } else {
                paint.setColor(normalColor);
                mPaintEmpty.setColor(pressColor);
            }
            if (r1 == null)
                r1 = new Rect();
            r1.left = x;
            r1.top = y;
            r1.right = x + iconWidth;
            r1.bottom = y + iconHeight;
            //bitmap = BitmapFactory.decodeResource(getResources(), resid);
            //canvas.drawBitmap(bitmap, null, r1, paint);
            //canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
            if (i == mCurrentPage) {
                if (selectKongxin) {
                    canvas.drawCircle(r1.centerX(), r1.centerY(), r1.height() / 2, mPaintEmpty);
                } else {
                    canvas.drawCircle(r1.centerX(), r1.centerY(), r1.height() / 2, paint);
                }
            } else {
                if (normalKongxin) {
                    canvas.drawCircle(r1.centerX(), r1.centerY(), r1.height() / 2, mPaintEmpty);
                } else {
                    canvas.drawCircle(r1.centerX(), r1.centerY(), r1.height() / 2, paint);
                }
            }

            x += iconWidth + mSpace;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    listener.onPageClick((int) (event.getX() / mIconWH));
                }
                break;
        }
        return true;
    }

    public static interface OnPageClickListener {
        public void onPageClick(int page);
    }

    private OnPageClickListener listener;

    public void setOnPageClickListener(OnPageClickListener listener) {
        this.listener = listener;
    }


}
