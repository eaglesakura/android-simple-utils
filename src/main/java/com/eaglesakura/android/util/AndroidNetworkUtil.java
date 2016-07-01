package com.eaglesakura.android.util;

import com.eaglesakura.android.error.NetworkNotConnectException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AndroidNetworkUtil {
    /**
     * ネットワークに接続済みであることを確認する
     */
    public static void assertNetworkConnected(Context context) throws NetworkNotConnectException {
        ConnectivityManager service = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = service.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            throw new NetworkNotConnectException();
        }
    }
}
