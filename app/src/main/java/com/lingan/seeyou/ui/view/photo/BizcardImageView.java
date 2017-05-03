package com.lingan.seeyou.ui.view.photo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;

import nickgao.com.meiyousample.R;

/**
 * 查看图像View
 */
public class BizcardImageView extends ImageView implements OnTouchListener {

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private Bitmap bitmap;
    private int viewHeight;
    private int viewWidth;

    private static final int NONE = 0;// 初始状态
    private static final int DRAG = 1;// 拖动
    private static final int ZOOM = 2;// 缩放
    private int mode = NONE;

    private PointF prev = new PointF();
    private PointF mid = new PointF();
    private float dist = 1f;

    private static final int VERTICAL_LOCATION_TOP = 0;
    private static final int VERTICAL_LOCATION_CENTER = 1;
    private static final int VERTICAL_LOCATION_BOTTOM = 2;
    private static final int VERTICAL_LOCATION_DEFAULT = 3;
    private static final int TYPE_FOR_SCALE_MIN_ZOOM = 0;
    private static final int TYPE_FOR_SCALE_MAX_ZOOM = 1;
    /**
     * 垂直位置
     */
    private int verticalLocation;

    /**
     * 缩放模式
     */
    private int typeForScale;

    public BizcardImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(this);
    }

    public BizcardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        setScaleType(ScaleType.MATRIX);
        initAttributeSet(attrs);
        setOnTouchListener(this);
    }

    public BizcardImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        setScaleType(ScaleType.MATRIX);
        initAttributeSet(attrs);
        setOnTouchListener(this);
    }

    private void initAttributeSet(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BizcardImageView);
        typeForScale = typedArray.getColor(R.styleable.BizcardImageView_typeForScale, TYPE_FOR_SCALE_MIN_ZOOM);
        verticalLocation = typedArray.getColor(R.styleable.BizcardImageView_verticalLocation, VERTICAL_LOCATION_DEFAULT);
        typedArray.recycle();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 主点按下
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    prev.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                // 副点按下
                case MotionEvent.ACTION_POINTER_DOWN:
                    dist = spacing(event);
                    // 如果连续两点距离大于10，则判定为多点模式
                    if (spacing(event) > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - prev.x, event.getY()
                                - prev.y);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float tScale = newDist / dist;
                            matrix.postScale(tScale, tScale, mid.x, mid.y);
                        }
                    }
                    break;
            }
            setImageMatrix(matrix);
            checkView();
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {

        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && (viewWidth == 0 || viewHeight == 0)) {
            viewWidth = getWidth();
            viewHeight = getHeight();
            // minZoom();
            // center();
            setImageMatrix(matrix);
        }
    }

    /**
     * 载入图像
     *
     * @param path    图像路径
     * @param degress 旋转角度值
     */
    public void load(String path, int degress) {
        try {
            File file = new File(path);
            if (file.exists()) {
                release();
                matrix = new Matrix();
                savedMatrix = new Matrix();
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, opts);
                if (opts.outWidth > 1000 || opts.outHeight > 2500) {
                    opts.inSampleSize = 2;
                }
                opts.inJustDecodeBounds = false;


                FileInputStream is = new FileInputStream(path);
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeFile(path, opts);
                }
                //bitmap = BitmapFactory.decodeFile(path, opts);
                if (degress != 0) {
                    Matrix m = new Matrix();
                    m.setRotate(degress);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), m, true);
                }
                setImageBitmap(bitmap);
                if (viewWidth < 1 || viewHeight < 1) {
                    viewWidth = getWidth();
                    viewHeight = getHeight();
                }
                if (viewWidth != 0 && viewHeight != 0) {
                    setScale();
                    setLocation(true, VERTICAL_LOCATION_DEFAULT);
                    setImageMatrix(matrix);
                }
                is.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 载入图像
     *
     * @param path    图像路径
     * @param degress 旋转角度值
     * @param width   view寬
     * @param height  view高
     */
    public void load(String path, int degress, int width, int height) {
        try {
            File file = new File(path);
            if (file.exists()) {
                release();
                matrix = new Matrix();
                savedMatrix = new Matrix();
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, opts);
                if (opts.outWidth > 1000 || opts.outHeight > 2500) {
                    opts.inSampleSize = 2;
                }
                opts.inJustDecodeBounds = false;

                FileInputStream is = new FileInputStream(path);
                bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeFile(path, opts);
                }
                //bitmap = BitmapFactory.decodeFile(path, opts);
                if (degress != 0) {
                    Matrix m = new Matrix();
                    m.setRotate(degress);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), m, true);
                }
                setImageBitmap(bitmap);
                viewWidth = width;
                viewHeight = height;
                if (viewWidth != 0 && viewHeight != 0) {
                    setScale();
                    setLocation(true, VERTICAL_LOCATION_DEFAULT);
                    setImageMatrix(matrix);
                }
                is.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 设置View 大小
     *
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        viewWidth = width;
        viewHeight = height;
        // minZoom();
        // center();
        setImageMatrix(matrix);
    }

    /**
     * 限制最大最小缩放比例，自动居中
     */
    private void checkView() {
        float p[] = new float[9];
        matrix.getValues(p);
        if (mode == ZOOM) {
        }
    }

    /**
     * 最小缩放比例，最大为100%
     */
    private void setScale() {
        float scale = 1.0f;
        float cacheWidth = (float) viewWidth / (float) bitmap.getWidth();
        float cacheHeight = (float) viewHeight / (float) bitmap.getHeight();
        if (typeForScale == TYPE_FOR_SCALE_MIN_ZOOM) {
            scale = Math.min(cacheWidth, cacheHeight);
        } else if (typeForScale == TYPE_FOR_SCALE_MAX_ZOOM) {
            scale = Math.max(cacheWidth, cacheHeight);
        }
        matrix.postScale(scale, scale);
    }

    private void setVerticalLocation() {
        setLocation(true, verticalLocation);
    }

    /**
     * 横向居中、纵向设置位置
     */
    protected void setLocation(boolean horizontal, int verticalType) {
        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        switch (verticalType) {
            case VERTICAL_LOCATION_DEFAULT:
                // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
                if (height < viewHeight) {
                    deltaY = (viewHeight - height) / 2 - rect.top;
                } else if (rect.top > 0) {
                    deltaY = -rect.top;
                } else if (rect.bottom < viewHeight) {
                    deltaY = getHeight() - rect.bottom;
                }
                break;
            case VERTICAL_LOCATION_TOP:
                if (rect.top > 0) {
                    deltaY = -rect.top;
                }
                break;
            case VERTICAL_LOCATION_CENTER:
                    deltaY = (viewHeight - height) / 2 - rect.top;
                break;
            case VERTICAL_LOCATION_BOTTOM:
                if (rect.bottom < viewHeight) {
                    deltaY = getHeight() - rect.bottom;
                }
                break;
        }
        if (horizontal) {
            if (width < viewWidth) {
                deltaX = (viewWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < viewWidth) {
                deltaX = viewWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 两点的距离
     */
    private float spacing(MotionEvent event) {
        try {

            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float)Math.sqrt(x * x + y * y);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;

    }

    /**
     * 两点的中点
     */
    private void midPoint(PointF point, MotionEvent event) {
        try {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    /**
     * 釋放bitmap
     */
    public void release() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public void setImageBitmapss(Bitmap mBitmap, int width, int height) {
        release();
        bitmap = mBitmap;
        setImageBitmap(bitmap);
        viewWidth = width;
        viewHeight = height;
        if (viewWidth != 0 && viewHeight != 0) {
            setScale();
            setVerticalLocation();
            setImageMatrix(matrix);
        }

    }

}
