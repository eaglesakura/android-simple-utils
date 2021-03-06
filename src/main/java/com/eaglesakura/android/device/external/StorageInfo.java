package com.eaglesakura.android.device.external;

import com.eaglesakura.util.IOUtil;
import com.eaglesakura.util.StringUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 端末のストレージ情報を管理する
 *
 * API整理とAndroid 6.0対応のため、Storageクラスを推奨。
 */
@Deprecated
@SuppressLint("all")
public class StorageInfo {
    /**
     * 外部接続されたストレージであればtrue
     */
    private final boolean sdcard;

    /**
     * ストレージのRootパス
     */
    private final File path;


    /**
     * ストレージの空き容量
     */
    private long freeSize = -1;

    /**
     * ストレージの最大容量
     */
    private long maxSize = -1;

    private StorageInfo(boolean external, File path) {
        this.sdcard = external;
        this.path = path;
    }

    public boolean isSdcard() {
        return sdcard;
    }

    public File getPath() {
        return path;
    }

    public long getMaxSize() {
        if (maxSize < 0) {
            throw new IllegalStateException("call loadStorageInfo()");
        }
        return maxSize;
    }

    public long getFreeSize() {
        if (freeSize < 0) {
            throw new IllegalStateException("call loadStorageInfo()");
        }
        return freeSize;
    }

    /**
     * ストレージ情報を読み込む
     */
    public synchronized void loadStorageInfo() {
        StatFs stat = new StatFs(path.getAbsolutePath());
        if (Build.VERSION.SDK_INT >= 18) {
            if (this.maxSize < 0) {
                this.maxSize = stat.getBlockSizeLong() * stat.getBlockCountLong();
            }
            this.freeSize = stat.getBlockSizeLong() * stat.getFreeBlocksLong();
        } else {
            if (this.maxSize < 0) {
                this.maxSize = ((long) stat.getBlockSize() & 0xFFFFFFFFL) * ((long) stat.getBlockCount() & 0xFFFFFFFFL);
            }
            this.freeSize = ((long) stat.getBlockSize() & 0xFFFFFFFFL) * ((long) stat.getFreeBlocks() & 0xFFFFFFFFL);
        }
    }

    /**
     * 外部ストレージ一覧を取得する
     */
    public synchronized static List<StorageInfo> listExternalStorages() {
        List<StorageInfo> result = new ArrayList<>();
        File check = null;
        if ((check = new File("/storage/sdcard1")).exists()) {
            // Android 4.4標準パスから検索
            result.add(new StorageInfo(true, check));
            result.add(new StorageInfo(true, new File("/storage/sdcard0")));
        } else if ((check = new File("/mnt/sdcard/external_sd")).exists()) {
            // for Xperia GX
            result.add(new StorageInfo(true, check));
            result.add(new StorageInfo(true, Environment.getExternalStorageDirectory()));
        } else {
            String path = null;
            final String[] ENV_LIST = {
                    "EXTERNAL_ALT_STORAGE",
                    "EXTERNAL_STORAGE2",
                    "EXTERNAL_STORAGE"
            };
            for (String env : ENV_LIST) {
                String envPath = System.getenv(env);
                if (!StringUtil.isEmpty(envPath)) {
                    path = envPath;
                }
            }

            if (!StringUtil.isEmpty(path)
                    && !"/storage/emulated/legacy".equals(path)
                    && !"/sdcard".equals(path)
                    && (check = new File(path)).exists()) {
                // emulatedじゃないパスがひっかかれば、それがSDカードパスになる
                result.add(new StorageInfo(true, check));
                result.add(new StorageInfo(false, Environment.getExternalStorageDirectory()));
            }
        }

        if (result.isEmpty()) {
            // 例外的な機種や状況の場合、標準の外部パスを設定する
            result.add(new StorageInfo(false, Environment.getExternalStorageDirectory()));
        }
        return result;
    }

    public static File getExternalStorageRoot(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                return context.getExternalFilesDir(null);
            } catch (SecurityException e) {
                e.printStackTrace();
                return IOUtil.mkdirs(Environment.getExternalStorageDirectory());
            }
        } else {
            return IOUtil.mkdirs(Environment.getExternalStorageDirectory());
        }
    }
}
