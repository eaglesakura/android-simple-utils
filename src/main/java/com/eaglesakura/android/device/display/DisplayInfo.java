package com.eaglesakura.android.device.display;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * ディスプレイ情報を扱う
 */
public class DisplayInfo {

    /**
     * ディスプレイ解像度ピクセル
     */
    private int mWidthPixel;

    /**
     * ディスプレイ解像度ピクセル
     */
    private int mHeightPixel;

    private Type mDeviceType;

    /**
     * ディスプレイ解像度DP
     */
    private float mWidthDp;

    /**
     * ディスプレイ解像度DP
     */
    private float mHeightDp;

    /**
     * ディスプレイの物理サイズ
     */
    private float mWidthInch;

    private float mHeightInch;

    @NonNull
    private Inch mRoundInch;

    private DisplayMetrics mDisplayMetrics = new DisplayMetrics();

    private Dpi mDpi;

    public DisplayInfo(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getMetrics(mDisplayMetrics);

        loadDisplayPix(context, display);
        loadDisplayDensity(context);
    }

    /**
     * ディスプレイの実解像度をピクセル単位で取得する
     */
    public int getWidthPixel() {
        return mWidthPixel;
    }

    /**
     * ディスプレイの実解像度をピクセル単位で取得する
     */
    public int getHeightPixel() {
        return mHeightPixel;
    }

    /**
     * ディスプレイの実解像度をdp単位で取得する
     */
    public float getWidthDp() {
        return mWidthDp;
    }

    /**
     * ディスプレイの実解像度をdp単位で取得する
     */
    public float getHeightDp() {
        return mHeightDp;
    }

    /**
     * レイアウト切り分けに使用するvalues-sw${smallest-width}dpを取得する
     */
    public int getSmallestWidthDp() {
        return (((int) Math.min(getWidthDp(), getHeightDp())) / 10) * 10;
    }

    /**
     * ディスプレイの対角線インチ数を取得する。
     *
     * ユーザーがわかりやすい、購入時の端末ディスプレイサイズとなる。
     */
    public float getDiagonalInch() {
        return (float) Math.sqrt((mWidthInch * mWidthInch) + (mHeightInch * mHeightInch));
    }

    /**
     * おおまかにユーザーのわかりやすいインチ数を取得する
     *
     * インチ数の小数点第2位を四捨五入して返却する。
     *
     * 例えば、4.65inchであれば、4.7インチに切り上げられる。
     *
     * @return 丸められたインチ数
     */
    @NonNull
    public Inch getDiagonalInchRound() {
        return mRoundInch;
    }

    public float getWidthInch() {
        return mWidthInch;
    }

    public float getHeightInch() {
        return mHeightInch;
    }

    private void loadDisplayDensity(Context context) {
        mWidthDp = (float) mWidthPixel / mDisplayMetrics.density;
        mHeightDp = (float) mHeightPixel / mDisplayMetrics.density;

        mWidthInch = (float) mWidthPixel / mDisplayMetrics.xdpi;
        mHeightInch = (float) mHeightPixel / mDisplayMetrics.ydpi;
        mDpi = Dpi.toDpi(mDisplayMetrics.xdpi, mDisplayMetrics.ydpi);
        {
            int diagonal = (int) (getDiagonalInch() * 100.0f);
            diagonal += 5;
            diagonal /= 10;
            mRoundInch = new Inch(diagonal / 10, diagonal % 10);
        }

        // 丸められたインチ数で、デバイスを種類を指定する
        {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT_WATCH) {
                mDeviceType = Type.Watch;
            } else if (mRoundInch.major <= 5) {
                mDeviceType = Type.Phone;
            } else if (mRoundInch.major <= 6) {
                mDeviceType = Type.Phablet;
            } else if (mRoundInch.major <= 12) {
                mDeviceType = Type.Tablet;
            } else {
                mDeviceType = Type.Other;
            }
        }
    }

    private void loadDisplayPix(Context context, Display display) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Point point = new Point(0, 0);
            display.getRealSize(point);
            mWidthPixel = point.x;
            mHeightPixel = point.y;
        } else {
            try {
                Class clazz = Display.class;
                Method getRawWidth = clazz.getMethod("getRawWidth");
                Method getRawHeight = clazz.getMethod("getRawHeight");
                mWidthPixel = (int) getRawWidth.invoke(display);
                mHeightPixel = (int) getRawHeight.invoke(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Type getDeviceType() {
        return mDeviceType;
    }

    public Dpi getDpi() {
        return mDpi;
    }

    public enum Dpi {
        ldpi,
        mdpi,
        tvdpi,
        hdpi,
        xhdpi,
        xxhdpi,
        xxxhdpi;

        static Dpi toDpi(float xdpi, float ydpi) {
            final float dpi = Math.min(xdpi, ydpi);

            if (dpi > 480) {
                return xxxhdpi;
            }

            if (dpi > 320) {
                return xxhdpi;
            }

            if (dpi > 240) {
                return xhdpi;
            }

            if (dpi > 210) {
                return tvdpi;
            }

            if (dpi > 160) {
                return hdpi;
            }

            if (dpi > 120) {
                return mdpi;
            }

            return ldpi;
        }
    }

    public enum Type {
        /**
         * 時計
         */
        Watch,

        /**
         * 携帯電話
         */
        Phone,

        /**
         * ファブレット
         */
        Phablet,

        /**
         * タブレット
         */
        Tablet,

        /**
         * その他
         */
        Other,
    }

    /**
     * 表示用のインチ数を示す値
     */
    public static class Inch {
        public final int major;

        public final int minor;

        public Inch(int major, int minor) {
            this.major = major;
            this.minor = minor;
        }

        public float toFloat() {
            return (float) major + ((float) minor / 10.0f);
        }

        @Override
        public String toString() {
            return String.valueOf(major) + "." + String.valueOf(minor);
        }
    }
}
