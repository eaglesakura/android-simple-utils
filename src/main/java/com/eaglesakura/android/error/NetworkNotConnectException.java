package com.eaglesakura.android.error;

import java.io.IOException;

/**
 * Android本体がネットワーク接続されていない場合の例外
 */
public class NetworkNotConnectException extends IOException {
    public NetworkNotConnectException() {
    }

    public NetworkNotConnectException(String detailMessage) {
        super(detailMessage);
    }

    public NetworkNotConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkNotConnectException(Throwable cause) {
        super(cause);
    }
}
