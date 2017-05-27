package com.meetyou.pullrefresh.lib;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public abstract class PtrUIHandlerHook implements Runnable {
    private Runnable mResumeAction;
    private static final byte STATUS_PREPARE = 0;
    private static final byte STATUS_IN_HOOK = 1;
    private static final byte STATUS_RESUMED = 2;
    private byte mStatus = 0;

    public PtrUIHandlerHook() {
    }

    public void takeOver() {
        this.takeOver((Runnable)null);
    }

    public void takeOver(Runnable resumeAction) {
        if(resumeAction != null) {
            this.mResumeAction = resumeAction;
        }

        switch(this.mStatus) {
            case 0:
                this.mStatus = 1;
                this.run();
            case 1:
            default:
                break;
            case 2:
                this.resume();
        }

    }

    public void reset() {
        this.mStatus = 0;
    }

    public void resume() {
        if(this.mResumeAction != null) {
            this.mResumeAction.run();
        }

        this.mStatus = 2;
    }

    public void setResumeAction(Runnable runnable) {
        this.mResumeAction = runnable;
    }
}
