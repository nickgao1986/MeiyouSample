package com.meetyou.pullrefresh.lib;

/**
 * Created by gaoyoujian on 2017/5/26.
 */

class PtrUIHandlerHolder implements PtrUIHandler {
    private PtrUIHandler mHandler;
    private PtrUIHandlerHolder mNext;

    private boolean contains(PtrUIHandler handler) {
        return this.mHandler != null && this.mHandler == handler;
    }

    private PtrUIHandlerHolder() {
    }

    public boolean hasHandler() {
        return this.mHandler != null;
    }

    private PtrUIHandler getHandler() {
        return this.mHandler;
    }

    public static void addHandler(PtrUIHandlerHolder head, PtrUIHandler handler) {
        if(null != handler) {
            if(head != null) {
                if(null == head.mHandler) {
                    head.mHandler = handler;
                } else {
                    for(PtrUIHandlerHolder current = head; !current.contains(handler); current = current.mNext) {
                        if(current.mNext == null) {
                            PtrUIHandlerHolder newHolder = new PtrUIHandlerHolder();
                            newHolder.mHandler = handler;
                            current.mNext = newHolder;
                            return;
                        }
                    }

                }
            }
        }
    }

    public static PtrUIHandlerHolder create() {
        return new PtrUIHandlerHolder();
    }

    public static PtrUIHandlerHolder removeHandler(PtrUIHandlerHolder head, PtrUIHandler handler) {
        if(head != null && handler != null && null != head.mHandler) {
            PtrUIHandlerHolder current = head;
            PtrUIHandlerHolder pre = null;

            do {
                if(current.contains(handler)) {
                    if(pre == null) {
                        head = current.mNext;
                        current.mNext = null;
                        current = head;
                    } else {
                        pre.mNext = current.mNext;
                        current.mNext = null;
                        current = pre.mNext;
                    }
                } else {
                    pre = current;
                    current = current.mNext;
                }
            } while(current != null);

            if(head == null) {
                head = new PtrUIHandlerHolder();
            }

            return head;
        } else {
            return head;
        }
    }

    public void onUIReset(PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;

        do {
            PtrUIHandler handler = current.getHandler();
            if(null != handler) {
                handler.onUIReset(frame);
            }
        } while((current = current.mNext) != null);

    }

    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        if(this.hasHandler()) {
            PtrUIHandlerHolder current = this;

            do {
                PtrUIHandler handler = current.getHandler();
                if(null != handler) {
                    handler.onUIRefreshPrepare(frame);
                }
            } while((current = current.mNext) != null);

        }
    }

    public void onUIRefreshBegin(PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;

        do {
            PtrUIHandler handler = current.getHandler();
            if(null != handler) {
                handler.onUIRefreshBegin(frame);
            }
        } while((current = current.mNext) != null);

    }

    public void onUIRefreshComplete(PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;

        do {
            PtrUIHandler handler = current.getHandler();
            if(null != handler) {
                handler.onUIRefreshComplete(frame);
            }
        } while((current = current.mNext) != null);

    }

    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        PtrUIHandlerHolder current = this;

        do {
            PtrUIHandler handler = current.getHandler();
            if(null != handler) {
                handler.onUIPositionChange(frame, isUnderTouch, status, ptrIndicator);
            }
        } while((current = current.mNext) != null);

    }
}
