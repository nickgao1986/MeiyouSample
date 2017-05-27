//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.meetyou.pullrefresh.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Scroller;
import android.widget.TextView;
import nickgao.com.meiyousample.R.styleable;


public class PtrFrameLayout extends ViewGroup {
    public static final byte PTR_STATUS_INIT = 1;
    private byte mStatus;
    public static final byte PTR_STATUS_PREPARE = 2;
    public static final byte PTR_STATUS_LOADING = 3;
    public static final byte PTR_STATUS_COMPLETE = 4;
    private static final boolean DEBUG_LAYOUT = true;
    public static boolean DEBUG = true;
    private static int ID = 1;
    protected final String LOG_TAG;
    private static final byte FLAG_AUTO_REFRESH_AT_ONCE = 1;
    private static final byte FLAG_AUTO_REFRESH_BUT_LATER = 2;
    private static final byte FLAG_ENABLE_NEXT_PTR_AT_ONCE = 4;
    private static final byte FLAG_PIN_CONTENT = 8;
    private static final byte MASK_AUTO_REFRESH = 3;
    protected View mContent;
    private int mHeaderId;
    private int mContainerId;
    private int mDurationToClose;
    private int mDurationToCloseHeader;
    private boolean mKeepHeaderWhenRefresh;
    private boolean mPullToRefresh;
    private View mHeaderView;
    private PtrUIHandlerHolder mPtrUIHandlerHolder;
    private PtrHandler mPtrHandler;
    private PtrFrameLayout.ScrollChecker mScrollChecker;
    private int mPagingTouchSlop;
    private int mHeaderHeight;
    private boolean mDisableWhenHorizontalMove;
    private int mFlag;
    private boolean mPreventForHorizontal;
    private MotionEvent mLastMoveEvent;
    private PtrUIHandlerHook mRefreshCompleteHook;
    private int mLoadingMinTime;
    private long mLoadingStartTime;
    private PtrIndicator mPtrIndicator;
    private boolean mHasSendCancelEvent;
    private Runnable mPerformRefreshCompleteDelay;

    public PtrFrameLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public PtrFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mStatus = 1;
        this.LOG_TAG = "ptr-frame-" + ++ID;
        this.mHeaderId = 0;
        this.mContainerId = 0;
        this.mDurationToClose = 200;
        this.mDurationToCloseHeader = 1000;
        this.mKeepHeaderWhenRefresh = true;
        this.mPullToRefresh = false;
        this.mPtrUIHandlerHolder = PtrUIHandlerHolder.create();
        this.mDisableWhenHorizontalMove = false;
        this.mFlag = 0;
        this.mPreventForHorizontal = false;
        this.mLoadingMinTime = 500;
        this.mLoadingStartTime = 0L;
        this.mHasSendCancelEvent = false;
        this.mPerformRefreshCompleteDelay = new Runnable() {
            public void run() {
                PtrFrameLayout.this.performRefreshComplete();
            }
        };
        this.mPtrIndicator = new PtrIndicator();
        TypedArray arr = context.obtainStyledAttributes(attrs, styleable.PtrFrameLayout, 0, 0);
        if(arr != null) {
            this.mHeaderId = arr.getResourceId(styleable.PtrFrameLayout_ptr_header, this.mHeaderId);
            this.mContainerId = arr.getResourceId(styleable.PtrFrameLayout_ptr_content, this.mContainerId);
            this.mPtrIndicator.setResistance(arr.getFloat(styleable.PtrFrameLayout_ptr_resistance, this.mPtrIndicator.getResistance()));
            this.mDurationToClose = arr.getInt(styleable.PtrFrameLayout_ptr_duration_to_close, this.mDurationToClose);
            this.mDurationToCloseHeader = arr.getInt(styleable.PtrFrameLayout_ptr_duration_to_close_header, this.mDurationToCloseHeader);
            float conf = this.mPtrIndicator.getRatioOfHeaderToHeightRefresh();
            conf = arr.getFloat(styleable.PtrFrameLayout_ptr_ratio_of_header_height_to_refresh, conf);
            this.mPtrIndicator.setRatioOfHeaderHeightToRefresh(conf);
            this.mKeepHeaderWhenRefresh = arr.getBoolean(styleable.PtrFrameLayout_ptr_keep_header_when_refresh, this.mKeepHeaderWhenRefresh);
            this.mPullToRefresh = arr.getBoolean(styleable.PtrFrameLayout_ptr_pull_to_fresh, this.mPullToRefresh);
            arr.recycle();
        }

        this.mScrollChecker = new PtrFrameLayout.ScrollChecker();
        ViewConfiguration var6 = ViewConfiguration.get(this.getContext());
        this.mPagingTouchSlop = var6.getScaledTouchSlop() * 2;
    }

    protected void onFinishInflate() {
        int childCount = this.getChildCount();
        if(childCount > 2) {
            throw new IllegalStateException("PtrFrameLayout can only contains 2 children");
        } else {
            if(childCount == 2) {
                if(this.mHeaderId != 0 && this.mHeaderView == null) {
                    this.mHeaderView = this.findViewById(this.mHeaderId);
                }

                if(this.mContainerId != 0 && this.mContent == null) {
                    this.mContent = this.findViewById(this.mContainerId);
                }

                if(this.mContent == null || this.mHeaderView == null) {
                    View errorView = this.getChildAt(0);
                    View child2 = this.getChildAt(1);
                    if(errorView instanceof PtrUIHandler) {
                        this.mHeaderView = errorView;
                        this.mContent = child2;
                    } else if(child2 instanceof PtrUIHandler) {
                        this.mHeaderView = child2;
                        this.mContent = errorView;
                    } else if(this.mContent == null && this.mHeaderView == null) {
                        this.mHeaderView = errorView;
                        this.mContent = child2;
                    } else if(this.mHeaderView == null) {
                        this.mHeaderView = this.mContent == errorView?child2:errorView;
                    } else {
                        this.mContent = this.mHeaderView == errorView?child2:errorView;
                    }
                }
            } else if(childCount == 1) {
                this.mContent = this.getChildAt(0);
            } else {
                TextView errorView1 = new TextView(this.getContext());
                errorView1.setClickable(true);
                errorView1.setTextColor(-39424);
                errorView1.setGravity(17);
                errorView1.setTextSize(20.0F);
                errorView1.setText("The content view in PtrFrameLayout is empty. Do you forget to specify its id in xml layout file?");
                this.mContent = errorView1;
                this.addView(this.mContent);
            }

            if(this.mHeaderView != null) {
                this.mHeaderView.bringToFront();
            }

            super.onFinishInflate();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(this.mScrollChecker != null) {
            this.mScrollChecker.destroy();
        }

        if(this.mPerformRefreshCompleteDelay != null) {
            this.removeCallbacks(this.mPerformRefreshCompleteDelay);
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(this.isDebug()) {
            PtrCLog.d(this.LOG_TAG, "onMeasure frame: width: %s, height: %s, padding: %s %s %s %s", new Object[]{Integer.valueOf(this.getMeasuredHeight()), Integer.valueOf(this.getMeasuredWidth()), Integer.valueOf(this.getPaddingLeft()), Integer.valueOf(this.getPaddingRight()), Integer.valueOf(this.getPaddingTop()), Integer.valueOf(this.getPaddingBottom())});
        }

        MarginLayoutParams lp;
        if(this.mHeaderView != null) {
            this.measureChildWithMargins(this.mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            lp = (MarginLayoutParams)this.mHeaderView.getLayoutParams();
            this.mHeaderHeight = this.mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            this.mPtrIndicator.setHeaderHeight(this.mHeaderHeight);
        }

        if(this.mContent != null) {
            this.measureContentView(this.mContent, widthMeasureSpec, heightMeasureSpec);
            if(this.isDebug()) {
                lp = (MarginLayoutParams)this.mContent.getLayoutParams();
                PtrCLog.d(this.LOG_TAG, "onMeasure content, width: %s, height: %s, margin: %s %s %s %s", new Object[]{Integer.valueOf(this.getMeasuredWidth()), Integer.valueOf(this.getMeasuredHeight()), Integer.valueOf(lp.leftMargin), Integer.valueOf(lp.topMargin), Integer.valueOf(lp.rightMargin), Integer.valueOf(lp.bottomMargin)});
                PtrCLog.d(this.LOG_TAG, "onMeasure, currentPos: %s, lastPos: %s, top: %s", new Object[]{Integer.valueOf(this.mPtrIndicator.getCurrentPosY()), Integer.valueOf(this.mPtrIndicator.getLastPosY()), Integer.valueOf(this.mContent.getTop())});
            }
        }

    }

    private void measureContentView(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom() + lp.topMargin, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    protected void onLayout(boolean flag, int i, int j, int k, int l) {
        this.layoutChildren();
    }

    private void layoutChildren() {
        int offset = this.mPtrIndicator.getCurrentPosY();
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        MarginLayoutParams lp;
        int left;
        int top;
        int right;
        int bottom;
        if(this.mHeaderView != null) {
            lp = (MarginLayoutParams)this.mHeaderView.getLayoutParams();
            left = paddingLeft + lp.leftMargin;
            top = -(this.mHeaderHeight - paddingTop - lp.topMargin - offset);
            right = left + this.mHeaderView.getMeasuredWidth();
            bottom = top + this.mHeaderView.getMeasuredHeight();
            this.mHeaderView.layout(left, top, right, bottom);
            if(this.isDebug()) {
                PtrCLog.d(this.LOG_TAG, "onLayout header: %s %s %s %s", new Object[]{Integer.valueOf(left), Integer.valueOf(top), Integer.valueOf(right), Integer.valueOf(bottom)});
            }
        }

        if(this.mContent != null) {
            if(this.isPinContent()) {
                offset = 0;
            }

            lp = (MarginLayoutParams)this.mContent.getLayoutParams();
            left = paddingLeft + lp.leftMargin;
            top = paddingTop + lp.topMargin + offset;
            right = left + this.mContent.getMeasuredWidth();
            bottom = top + this.mContent.getMeasuredHeight();
            if(this.isDebug()) {
                PtrCLog.d(this.LOG_TAG, "onLayout content: %s %s %s %s", new Object[]{Integer.valueOf(left), Integer.valueOf(top), Integer.valueOf(right), Integer.valueOf(bottom)});
            }

            this.mContent.layout(left, top, right, bottom);
        }

    }

    private boolean isDebug() {
        return DEBUG;
    }

    public boolean dispatchTouchEventSupper(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    public boolean dispatchTouchEvent(MotionEvent e) {
        if(this.isEnabled() && this.mContent != null && this.mHeaderView != null) {
            int action = e.getAction();
            switch(action) {
                case 0:
                    this.mHasSendCancelEvent = false;
                    this.mPtrIndicator.onPressDown(e.getX(), e.getY());
                    this.mScrollChecker.abortIfWorking();
                    this.mPreventForHorizontal = false;
                    this.dispatchTouchEventSupper(e);
                    return true;
                case 1:
                case 3:
                    this.mPtrIndicator.onRelease();
                    if(this.mPtrIndicator.hasLeftStartPosition()) {
                        if(DEBUG) {
                            PtrCLog.d(this.LOG_TAG, "call onRelease when user release");
                        }

                        this.onRelease(false);
                        if(this.mPtrIndicator.hasMovedAfterPressedDown()) {
                            this.sendCancelEvent();
                            return true;
                        } else {
                            return this.dispatchTouchEventSupper(e);
                        }
                    } else {
                        return this.dispatchTouchEventSupper(e);
                    }
                case 2:
                    this.mLastMoveEvent = e;
                    this.mPtrIndicator.onMove(e.getX(), e.getY());
                    float offsetX = this.mPtrIndicator.getOffsetX();
                    float offsetY = this.mPtrIndicator.getOffsetY();
                    if(this.mDisableWhenHorizontalMove && !this.mPreventForHorizontal && Math.abs(offsetX) > (float)this.mPagingTouchSlop && Math.abs(offsetX) > Math.abs(offsetY) && this.mPtrIndicator.isInStartPosition()) {
                        this.mPreventForHorizontal = true;
                    }

                    if(this.mPreventForHorizontal) {
                        return this.dispatchTouchEventSupper(e);
                    } else {
                        boolean moveDown = offsetY > 0.0F;
                        boolean moveUp = !moveDown;
                        boolean canMoveUp = this.mPtrIndicator.hasLeftStartPosition();
                        if(DEBUG) {
                            boolean canMoveDown = this.mPtrHandler != null && this.mPtrHandler.checkCanDoRefresh(this, this.mContent, this.mHeaderView);
                            PtrCLog.v(this.LOG_TAG, "ACTION_MOVE: offsetY:%s, currentPos: %s, moveUp: %s, canMoveUp: %s, moveDown: %s: canMoveDown: %s", new Object[]{Float.valueOf(offsetY), Integer.valueOf(this.mPtrIndicator.getCurrentPosY()), Boolean.valueOf(moveUp), Boolean.valueOf(canMoveUp), Boolean.valueOf(moveDown), Boolean.valueOf(canMoveDown)});
                        }

                        if(moveDown && this.mPtrHandler != null && !this.mPtrHandler.checkCanDoRefresh(this, this.mContent, this.mHeaderView)) {
                            return this.dispatchTouchEventSupper(e);
                        } else if(moveUp && canMoveUp || moveDown) {
                            this.movePos(offsetY);
                            return true;
                        }
                    }
                default:
                    return this.dispatchTouchEventSupper(e);
            }
        } else {
            return this.dispatchTouchEventSupper(e);
        }
    }

    private void movePos(float deltaY) {
        if(deltaY < 0.0F && this.mPtrIndicator.isInStartPosition()) {
            if(DEBUG) {
                PtrCLog.e(this.LOG_TAG, String.format("has reached the top", new Object[0]));
            }

        } else {
            int to = this.mPtrIndicator.getCurrentPosY() + (int)deltaY;
            if(this.mPtrIndicator.willOverTop(to)) {
                if(DEBUG) {
                    PtrCLog.e(this.LOG_TAG, String.format("over top", new Object[0]));
                }

                to = 0;
            }

            this.mPtrIndicator.setCurrentPos(to);
            int change = to - this.mPtrIndicator.getLastPosY();
            this.updatePos(change);
        }
    }

    private void updatePos(int change) {
        if(change != 0) {
            boolean isUnderTouch = this.mPtrIndicator.isUnderTouch();
            if(isUnderTouch && !this.mHasSendCancelEvent && this.mPtrIndicator.hasMovedAfterPressedDown()) {
                this.mHasSendCancelEvent = true;
                this.sendCancelEvent();
            }

            if(this.mPtrIndicator.hasJustLeftStartPosition() && this.mStatus == 1 || this.mPtrIndicator.goDownCrossFinishPosition() && this.mStatus == 4 && this.isEnabledNextPtrAtOnce()) {
                this.mStatus = 2;
                this.mPtrUIHandlerHolder.onUIRefreshPrepare(this);
                if(DEBUG) {
                    PtrCLog.i(this.LOG_TAG, "PtrUIHandler: onUIRefreshPrepare, mFlag %s", new Object[]{Integer.valueOf(this.mFlag)});
                }
            }

            if(this.mPtrIndicator.hasJustBackToStartPosition()) {
                this.tryToNotifyReset();
                if(isUnderTouch) {
                    this.sendDownEvent();
                }
            }

            if(this.mStatus == 2) {
                if(isUnderTouch && !this.isAutoRefresh() && this.mPullToRefresh && this.mPtrIndicator.crossRefreshLineFromTopToBottom()) {
                    this.tryToPerformRefresh();
                }

                if(this.performAutoRefreshButLater() && this.mPtrIndicator.hasJustReachedHeaderHeightFromTopToBottom()) {
                    this.tryToPerformRefresh();
                }
            }

            if(DEBUG) {
                PtrCLog.v(this.LOG_TAG, "updatePos: change: %s, current: %s last: %s, top: %s, headerHeight: %s", new Object[]{Integer.valueOf(change), Integer.valueOf(this.mPtrIndicator.getCurrentPosY()), Integer.valueOf(this.mPtrIndicator.getLastPosY()), Integer.valueOf(this.mContent.getTop()), Integer.valueOf(this.mHeaderHeight)});
            }

            this.mHeaderView.offsetTopAndBottom(change);
            if(!this.isPinContent()) {
                this.mContent.offsetTopAndBottom(change);
            }

            this.invalidate();
            if(this.mPtrUIHandlerHolder.hasHandler()) {
                this.mPtrUIHandlerHolder.onUIPositionChange(this, isUnderTouch, this.mStatus, this.mPtrIndicator);
            }

            this.onPositionChange(isUnderTouch, this.mStatus, this.mPtrIndicator);
        }
    }

    protected void onPositionChange(boolean isInTouching, byte status, PtrIndicator mPtrIndicator) {
    }

    public int getHeaderHeight() {
        return this.mHeaderHeight;
    }

    private void onRelease(boolean stayForLoading) {
        this.tryToPerformRefresh();
        if(this.mStatus == 3) {
            if(this.mKeepHeaderWhenRefresh) {
                if(this.mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading() && !stayForLoading) {
                    this.mScrollChecker.tryToScrollTo(this.mPtrIndicator.getOffsetToKeepHeaderWhileLoading(), this.mDurationToClose);
                }
            } else {
                this.tryScrollBackToTopWhileLoading();
            }
        } else if(this.mStatus == 4) {
            this.notifyUIRefreshComplete(false);
        } else {
            this.tryScrollBackToTopAbortRefresh();
        }

    }

    public void setRefreshCompleteHook(PtrUIHandlerHook hook) {
        this.mRefreshCompleteHook = hook;
        hook.setResumeAction(new Runnable() {
            public void run() {
                if(PtrFrameLayout.DEBUG) {
                    PtrCLog.d(PtrFrameLayout.this.LOG_TAG, "mRefreshCompleteHook resume.");
                }

                PtrFrameLayout.this.notifyUIRefreshComplete(true);
            }
        });
    }

    private void tryScrollBackToTop() {
        if(!this.mPtrIndicator.isUnderTouch()) {
            this.mScrollChecker.tryToScrollTo(0, this.mDurationToCloseHeader);
        }

    }

    private void tryScrollBackToTopWhileLoading() {
        this.tryScrollBackToTop();
    }

    private void tryScrollBackToTopAfterComplete() {
        this.tryScrollBackToTop();
    }

    private void tryScrollBackToTopAbortRefresh() {
        this.tryScrollBackToTop();
    }

    private boolean tryToPerformRefresh() {
        if(this.mStatus != 2) {
            return false;
        } else {
            if(this.mPtrIndicator.isOverOffsetToKeepHeaderWhileLoading() && this.isAutoRefresh() || this.mPtrIndicator.isOverOffsetToRefresh()) {
                this.mStatus = 3;
                this.performRefresh();
            }

            return false;
        }
    }

    private void performRefresh() {
        this.mLoadingStartTime = System.currentTimeMillis();
        if(this.mPtrUIHandlerHolder.hasHandler()) {
            this.mPtrUIHandlerHolder.onUIRefreshBegin(this);
            if(DEBUG) {
                PtrCLog.i(this.LOG_TAG, "PtrUIHandler: onUIRefreshBegin");
            }
        }

        if(this.mPtrHandler != null) {
            this.mPtrHandler.onRefreshBegin(this);
        }

    }

    private boolean tryToNotifyReset() {
        if((this.mStatus == 4 || this.mStatus == 2) && this.mPtrIndicator.isInStartPosition()) {
            if(this.mPtrUIHandlerHolder.hasHandler()) {
                this.mPtrUIHandlerHolder.onUIReset(this);
                if(DEBUG) {
                    PtrCLog.i(this.LOG_TAG, "PtrUIHandler: onUIReset");
                }
            }

            this.mStatus = 1;
            this.clearFlag();
            return true;
        } else {
            return false;
        }
    }

    protected void onPtrScrollAbort() {
        if(this.mPtrIndicator.hasLeftStartPosition() && this.isAutoRefresh()) {
            if(DEBUG) {
                PtrCLog.d(this.LOG_TAG, "call onRelease after scroll abort");
            }

            this.onRelease(true);
        }

    }

    protected void onPtrScrollFinish() {
        if(this.mPtrIndicator.hasLeftStartPosition() && this.isAutoRefresh()) {
            if(DEBUG) {
                PtrCLog.d(this.LOG_TAG, "call onRelease after scroll finish");
            }

            this.onRelease(true);
        }

    }

    public boolean isRefreshing() {
        return this.mStatus == 3;
    }

    public final void refreshComplete() {
        if(DEBUG) {
            PtrCLog.i(this.LOG_TAG, "refreshComplete");
        }

        if(this.mRefreshCompleteHook != null) {
            this.mRefreshCompleteHook.reset();
        }

        int delay = (int)((long)this.mLoadingMinTime - (System.currentTimeMillis() - this.mLoadingStartTime));
        if(delay <= 0) {
            if(DEBUG) {
                PtrCLog.d(this.LOG_TAG, "performRefreshComplete at once");
            }

            this.performRefreshComplete();
        } else {
            this.postDelayed(this.mPerformRefreshCompleteDelay, (long)delay);
            if(DEBUG) {
                PtrCLog.d(this.LOG_TAG, "performRefreshComplete after delay: %s", new Object[]{Integer.valueOf(delay)});
            }
        }

    }

    private void performRefreshComplete() {
        this.mStatus = 4;
        if(this.mScrollChecker.mIsRunning && this.isAutoRefresh()) {
            if(DEBUG) {
                PtrCLog.d(this.LOG_TAG, "performRefreshComplete do nothing, scrolling: %s, auto refresh: %s", new Object[]{Boolean.valueOf(this.mScrollChecker.mIsRunning), Integer.valueOf(this.mFlag)});
            }

        } else {
            this.notifyUIRefreshComplete(false);
        }
    }

    private void notifyUIRefreshComplete(boolean ignoreHook) {
        if(this.mPtrIndicator.hasLeftStartPosition() && !ignoreHook && this.mRefreshCompleteHook != null) {
            if(DEBUG) {
                PtrCLog.d(this.LOG_TAG, "notifyUIRefreshComplete mRefreshCompleteHook run.");
            }

            this.mRefreshCompleteHook.takeOver();
        } else {
            if(this.mPtrUIHandlerHolder.hasHandler()) {
                if(DEBUG) {
                    PtrCLog.i(this.LOG_TAG, "PtrUIHandler: onUIRefreshComplete");
                }

                this.mPtrUIHandlerHolder.onUIRefreshComplete(this);
            }

            this.mPtrIndicator.onUIRefreshComplete();
            this.tryScrollBackToTopAfterComplete();
            this.tryToNotifyReset();
        }
    }

    public void autoRefresh() {
        this.autoRefresh(true, this.mDurationToCloseHeader);
    }

    public void autoRefresh(boolean atOnce) {
        this.autoRefresh(atOnce, this.mDurationToCloseHeader);
    }

    private void clearFlag() {
        this.mFlag &= -4;
    }

    public void autoRefresh(boolean atOnce, int duration) {
        if(this.mStatus == 1) {
            this.mFlag |= atOnce?1:2;
            this.mStatus = 2;
            if(this.mPtrUIHandlerHolder.hasHandler()) {
                this.mPtrUIHandlerHolder.onUIRefreshPrepare(this);
                if(DEBUG) {
                    PtrCLog.i(this.LOG_TAG, "PtrUIHandler: onUIRefreshPrepare, mFlag %s", new Object[]{Integer.valueOf(this.mFlag)});
                }
            }

            this.mScrollChecker.tryToScrollTo(this.mPtrIndicator.getOffsetToRefresh(), duration);
            if(atOnce) {
                this.mStatus = 3;
                this.performRefresh();
            }

        }
    }

    public boolean isAutoRefresh() {
        return (this.mFlag & 3) > 0;
    }

    private boolean performAutoRefreshButLater() {
        return (this.mFlag & 3) == 2;
    }

    public boolean isEnabledNextPtrAtOnce() {
        return (this.mFlag & 4) > 0;
    }

    public void setEnabledNextPtrAtOnce(boolean enable) {
        if(enable) {
            this.mFlag |= 4;
        } else {
            this.mFlag &= -5;
        }

    }

    public boolean isPinContent() {
        return (this.mFlag & 8) > 0;
    }

    public void setPinContent(boolean pinContent) {
        if(pinContent) {
            this.mFlag |= 8;
        } else {
            this.mFlag &= -9;
        }

    }

    public void disableWhenHorizontalMove(boolean disable) {
        this.mDisableWhenHorizontalMove = disable;
    }

    public void setLoadingMinTime(int time) {
        this.mLoadingMinTime = time;
    }

    /** @deprecated */
    @Deprecated
    public void setInterceptEventWhileWorking(boolean yes) {
    }

    public View getContentView() {
        return this.mContent;
    }

    public void setPtrHandler(PtrHandler ptrHandler) {
        this.mPtrHandler = ptrHandler;
    }

    public void addPtrUIHandler(PtrUIHandler ptrUIHandler) {
        PtrUIHandlerHolder.addHandler(this.mPtrUIHandlerHolder, ptrUIHandler);
    }

    public void removePtrUIHandler(PtrUIHandler ptrUIHandler) {
        this.mPtrUIHandlerHolder = PtrUIHandlerHolder.removeHandler(this.mPtrUIHandlerHolder, ptrUIHandler);
    }

    public void setPtrIndicator(PtrIndicator slider) {
        if(this.mPtrIndicator != null && this.mPtrIndicator != slider) {
            slider.convertFrom(this.mPtrIndicator);
        }

        this.mPtrIndicator = slider;
    }

    public float getResistance() {
        return this.mPtrIndicator.getResistance();
    }

    public void setResistance(float resistance) {
        this.mPtrIndicator.setResistance(resistance);
    }

    public float getDurationToClose() {
        return (float)this.mDurationToClose;
    }

    public void setDurationToClose(int duration) {
        this.mDurationToClose = duration;
    }

    public long getDurationToCloseHeader() {
        return (long)this.mDurationToCloseHeader;
    }

    public void setDurationToCloseHeader(int duration) {
        this.mDurationToCloseHeader = duration;
    }

    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        this.mPtrIndicator.setRatioOfHeaderHeightToRefresh(ratio);
    }

    public int getOffsetToRefresh() {
        return this.mPtrIndicator.getOffsetToRefresh();
    }

    public void setOffsetToRefresh(int offset) {
        this.mPtrIndicator.setOffsetToRefresh(offset);
    }

    public float getRatioOfHeaderToHeightRefresh() {
        return this.mPtrIndicator.getRatioOfHeaderToHeightRefresh();
    }

    public int getOffsetToKeepHeaderWhileLoading() {
        return this.mPtrIndicator.getOffsetToKeepHeaderWhileLoading();
    }

    public void setOffsetToKeepHeaderWhileLoading(int offset) {
        this.mPtrIndicator.setOffsetToKeepHeaderWhileLoading(offset);
    }

    public boolean isKeepHeaderWhenRefresh() {
        return this.mKeepHeaderWhenRefresh;
    }

    public void setKeepHeaderWhenRefresh(boolean keepOrNot) {
        this.mKeepHeaderWhenRefresh = keepOrNot;
    }

    public boolean isPullToRefresh() {
        return this.mPullToRefresh;
    }

    public void setPullToRefresh(boolean pullToRefresh) {
        this.mPullToRefresh = pullToRefresh;
    }

    public View getHeaderView() {
        return this.mHeaderView;
    }

    public void setHeaderView(View header) {
        if(this.mHeaderView != null && header != null && this.mHeaderView != header) {
            this.removeView(this.mHeaderView);
        }

        android.view.ViewGroup.LayoutParams lp = header.getLayoutParams();
        if(lp == null) {
            PtrFrameLayout.LayoutParams lp1 = new PtrFrameLayout.LayoutParams(-1, -2);
            header.setLayoutParams(lp1);
        }

        this.mHeaderView = header;
        this.addView(header);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p != null && p instanceof PtrFrameLayout.LayoutParams;
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new PtrFrameLayout.LayoutParams(-1, -1);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new PtrFrameLayout.LayoutParams(p);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new PtrFrameLayout.LayoutParams(this.getContext(), attrs);
    }

    private void sendCancelEvent() {
        if(DEBUG) {
            PtrCLog.d(this.LOG_TAG, "send cancel event");
        }

        if(this.mLastMoveEvent != null) {
            MotionEvent last = this.mLastMoveEvent;
            MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + (long)ViewConfiguration.getLongPressTimeout(), 3, last.getX(), last.getY(), last.getMetaState());
            this.dispatchTouchEventSupper(e);
        }
    }

    private void sendDownEvent() {
        if(DEBUG) {
            PtrCLog.d(this.LOG_TAG, "send down event");
        }

        MotionEvent last = this.mLastMoveEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), 0, last.getX(), last.getY(), last.getMetaState());
        this.dispatchTouchEventSupper(e);
    }

    class ScrollChecker implements Runnable {
        private int mLastFlingY;
        private Scroller mScroller = new Scroller(PtrFrameLayout.this.getContext());
        private boolean mIsRunning = false;
        private int mStart;
        private int mTo;

        public ScrollChecker() {
        }

        public void run() {
            boolean finish = !this.mScroller.computeScrollOffset() || this.mScroller.isFinished();
            int curY = this.mScroller.getCurrY();
            int deltaY = curY - this.mLastFlingY;
            if(PtrFrameLayout.DEBUG && deltaY != 0) {
                PtrCLog.v(PtrFrameLayout.this.LOG_TAG, "scroll: %s, start: %s, to: %s, currentPos: %s, current :%s, last: %s, delta: %s", new Object[]{Boolean.valueOf(finish), Integer.valueOf(this.mStart), Integer.valueOf(this.mTo), Integer.valueOf(PtrFrameLayout.this.mPtrIndicator.getCurrentPosY()), Integer.valueOf(curY), Integer.valueOf(this.mLastFlingY), Integer.valueOf(deltaY)});
            }

            if(!finish) {
                this.mLastFlingY = curY;
                PtrFrameLayout.this.movePos((float)deltaY);
                PtrFrameLayout.this.post(this);
            } else {
                this.finish();
            }

        }

        private void finish() {
            if(PtrFrameLayout.DEBUG) {
                PtrCLog.v(PtrFrameLayout.this.LOG_TAG, "finish, currentPos:%s", new Object[]{Integer.valueOf(PtrFrameLayout.this.mPtrIndicator.getCurrentPosY())});
            }

            this.reset();
            PtrFrameLayout.this.onPtrScrollFinish();
        }

        private void reset() {
            this.mIsRunning = false;
            this.mLastFlingY = 0;
            PtrFrameLayout.this.removeCallbacks(this);
        }

        private void destroy() {
            this.reset();
            if(!this.mScroller.isFinished()) {
                this.mScroller.forceFinished(true);
            }

        }

        public void abortIfWorking() {
            if(this.mIsRunning) {
                if(!this.mScroller.isFinished()) {
                    this.mScroller.forceFinished(true);
                }

                PtrFrameLayout.this.onPtrScrollAbort();
                this.reset();
            }

        }

        public void tryToScrollTo(int to, int duration) {
            if(!PtrFrameLayout.this.mPtrIndicator.isAlreadyHere(to)) {
                this.mStart = PtrFrameLayout.this.mPtrIndicator.getCurrentPosY();
                this.mTo = to;
                int distance = to - this.mStart;
                if(PtrFrameLayout.DEBUG) {
                    PtrCLog.d(PtrFrameLayout.this.LOG_TAG, "tryToScrollTo: start: %s, distance:%s, to:%s", new Object[]{Integer.valueOf(this.mStart), Integer.valueOf(distance), Integer.valueOf(to)});
                }

                PtrFrameLayout.this.removeCallbacks(this);
                this.mLastFlingY = 0;
                if(!this.mScroller.isFinished()) {
                    this.mScroller.forceFinished(true);
                }

                this.mScroller.startScroll(0, 0, 0, distance, duration);
                PtrFrameLayout.this.post(this);
                this.mIsRunning = true;
            }
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
