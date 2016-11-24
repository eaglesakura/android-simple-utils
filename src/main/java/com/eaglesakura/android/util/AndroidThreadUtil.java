package com.eaglesakura.android.util;

import android.os.Handler;
import android.os.Looper;

public class AndroidThreadUtil {
    /**
     * @return UIスレッドだったらtrue
     */
    public static boolean isUIThread() {
        return Thread.currentThread().equals(Looper.getMainLooper().getThread());
    }

    /**
     * UIスレッドでなければ例外を投げる。
     */
    public static void assertUIThread() {
        if (!isUIThread()) {
            throw new Error("is not ui thread!!");
        }
    }

    /**
     * Background Threadであればtrueを返却する
     */
    public static void assertBackgroundThread() {
        if (isUIThread()) {
            throw new Error("is not background thread!!");
        }
    }

    /**
     * Handlerに関連付けられていたThreadで動作している場合はtrueを返す。
     *
     * @param handler 確認対象のHandler
     * @return Handlerに関連付けられていたThreadで動作している場合はtrueを返す。
     */
    public static boolean isHandlerThread(Handler handler) {
        return Thread.currentThread().equals(handler.getLooper().getThread());
    }

    /**
     * 指定したハンドラのスレッドである場合は処理を実行し、異なるスレッドである場合は処理を投げる
     *
     * @param handler  対象ハンドラ
     * @param runnable 実行処理
     */
    public static void postOrRun(Handler handler, Runnable runnable) {
        if (handler == null || isHandlerThread(handler)) {
            runnable.run();
        } else {
            handler.post(runnable);
        }
    }
}
