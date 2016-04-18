package com.eaglesakura.android.res;

import android.content.Context;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;

public class MenuId {
    @MenuRes
    final int mId;

    public MenuId(int id) {
        mId = id;
    }

    public MenuId(@NonNull Context context, @NonNull String name) {
        mId = context.getResources().getIdentifier(name, "menu", context.getPackageName());
        if (mId == 0) {
            throw new RuntimeException("ResourceName Error :: " + name);
        }
    }

    @MenuRes
    public int getId() {
        return mId;
    }
}
