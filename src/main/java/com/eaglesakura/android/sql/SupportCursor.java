package com.eaglesakura.android.sql;

import android.database.Cursor;

import java.io.Closeable;
import java.io.IOException;

/**
 * カーソル
 */
public class SupportCursor implements Closeable {
    int mIndex = 0;

    private Cursor mCursor;

    public SupportCursor(Cursor cursor) {
        mCursor = cursor;
    }

    public String nextString() {
        try {
            return mCursor.isNull(mIndex) ? null : mCursor.getString(mIndex);
        } finally {
            ++mIndex;
        }
    }

    public Long nextLong() {
        try {
            return mCursor.isNull(mIndex) ? null : mCursor.getLong(mIndex);
        } finally {
            ++mIndex;
        }
    }

    public Integer nextInt() {
        try {
            return mCursor.isNull(mIndex) ? null : mCursor.getInt(mIndex);
        } finally {
            ++mIndex;
        }
    }

    public Float nextFloat() {
        try {
            return mCursor.isNull(mIndex) ? null : mCursor.getFloat(mIndex);
        } finally {
            ++mIndex;
        }
    }

    public Double nextDouble() {
        try {
            return mCursor.isNull(mIndex) ? null : mCursor.getDouble(mIndex);
        } finally {
            ++mIndex;
        }
    }

    public byte[] nextBlob() {
        try {
            return mCursor.isNull(mIndex) ? null : mCursor.getBlob(mIndex);
        } finally {
            ++mIndex;
        }
    }

    public boolean moveToFirst() {
        try {
            return mCursor.moveToFirst();
        } finally {
            mIndex = 0;
        }
    }

    public boolean moveToNext() {
        try {
            return mCursor.moveToNext();
        } finally {
            mIndex = 0;
        }
    }

    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public void close() throws IOException {
        mCursor.close();
    }
}
