package com.eaglesakura.android.utils;

import com.eaglesakura.util.LogUtil;

import org.junit.Before;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import android.content.Context;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, packageName = BuildConfig.APPLICATION_ID, sdk = 21)
public abstract class CiJUnitTester {

    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    private void initializeLogger() {
        ShadowLog.stream = System.out;
        LogUtil.setLogger(
                new LogUtil.Logger() {
                    @Override
                    public void out(int level, String tag, String msg) {
                        try {
                            StackTraceElement[] trace = new Exception().getStackTrace();
                            StackTraceElement elem = trace[Math.min(trace.length - 1, 3)];
                            System.out.println(String.format("%s[%d] : %s", elem.getFileName(), elem.getLineNumber(), msg));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Before
    public void onSetup() {
        mContext = RuntimeEnvironment.application;
        initializeLogger();
    }
}
