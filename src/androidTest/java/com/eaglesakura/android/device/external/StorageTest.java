package com.eaglesakura.android.device.external;

import com.eaglesakura.util.LogUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class StorageTest {

    @Test
    public void 容量が取得できる() throws Throwable {
        Storage storage = Storage.getExternalStorage(InstrumentationRegistry.getContext());
        assertNotNull(storage);

        LogUtil.out(getClass().getName(), "Storage path[%s] size[%.1f GB Free / %.1f GB Max]", storage.getPath().getAbsolutePath(), storage.getFreeSizeGB(), storage.getMaxSizeGB());
        assertNotNull(storage.getPath());
        assertTrue(storage.getMaxSize() > 0);
        assertTrue(storage.getFreeSize() >= 0);
        assertTrue(storage.getMaxSize() >= storage.getFreeSize());
    }
}