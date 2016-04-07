package com.eaglesakura.android.util;

import android.app.Dialog;

public class DialogUtil {
    public static void dismiss(Dialog dialog) {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {

        }
    }
}
