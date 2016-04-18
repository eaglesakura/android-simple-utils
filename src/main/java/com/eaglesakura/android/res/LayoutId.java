package com.eaglesakura.android.res;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

public class LayoutId {
    @LayoutRes
    final int mId;

    public LayoutId(int id) {
        mId = id;
    }

    public LayoutId(@NonNull Context context, @NonNull String name) {
        mId = context.getResources().getIdentifier(name, "layout", context.getPackageName());
        if (mId == 0) {
            throw new RuntimeException("ResourceName Error :: " + name);
        }
    }

    @LayoutRes
    public int getId() {
        return mId;
    }
}
