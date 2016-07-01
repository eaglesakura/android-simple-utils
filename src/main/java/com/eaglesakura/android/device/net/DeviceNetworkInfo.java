package com.eaglesakura.android.device.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DeviceNetworkInfo {
    ConnectivityManager mConnectivityManager;

    NetworkInfo mNetworkInfo;

    public DeviceNetworkInfo(Context context) {
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        sync();
    }

    public NetworkInfo getNetworkInfo() {
        return mNetworkInfo;
    }

    /**
     * 何かしらのネットワークに接続されているならばtrue
     */
    public boolean isConnectedNetwork() {
        NetworkInfo info = getNetworkInfo();
        if (info == null) {
            return false;
        }

        return info.isConnected();
    }

    /**
     * Wi-Fi接続されているならばtrue
     */
    public boolean isWiFiNetwork() {
        NetworkInfo info = getNetworkInfo();
        if (info == null) {
            return false;
        }

        return ConnectivityManager.TYPE_WIFI == info.getType();
    }

    /**
     * モバイルネットワークに接続されているならばtrue
     */
    public boolean isMobileNetwork() {
        NetworkInfo info = getNetworkInfo();
        if (info == null) {
            return false;
        }

        return ConnectivityManager.TYPE_MOBILE == info.getType();
    }

    /**
     * 情報を同期する
     */
    public DeviceNetworkInfo sync() {
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return this;
    }

    private static DeviceNetworkInfo sInstance;

    public static DeviceNetworkInfo getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DeviceNetworkInfo(context);
        }

        return sInstance;
    }
}
