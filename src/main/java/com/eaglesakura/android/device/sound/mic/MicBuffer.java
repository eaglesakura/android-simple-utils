package com.eaglesakura.android.device.sound.mic;

import com.eaglesakura.android.device.sound.MicAudioBuffer;

import android.media.AudioRecord;
import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * マイク入力された1バッファを管理する。
 */
public class MicBuffer {

    /**
     * 親バッファ
     */
    @NonNull
    final MicAudioBuffer mParent;

    /**
     * オンメモリ処理用バッファ
     */
    short[] mCacheBuffer;

    /**
     * 正規化済みのバッファ
     */
    double[] mNormalizedBuffer;

    /**
     * 読み込み用バッファ
     */
    @NonNull
    ByteBuffer mNativeByteBuffer;

    /**
     * 書込み用バッファ
     */
    @NonNull
    ShortBuffer mNativeShortBuffer;

    /**
     * 計算済みのRMS値
     * リードを行なうと更新される
     */
    double mRMS;

    public MicBuffer(@NonNull MicAudioBuffer parent, int bufferBytes) {
        mParent = parent;
        mCacheBuffer = new short[bufferBytes / 2];
        mNormalizedBuffer = new double[mCacheBuffer.length];
        mNativeByteBuffer = ByteBuffer.allocateDirect(bufferBytes).order(ByteOrder.LITTLE_ENDIAN);
        mNativeShortBuffer = mNativeByteBuffer.asShortBuffer();
    }

    /**
     * バッファに読み込む
     */
    public void read(AudioRecord audioRecord) {
        audioRecord.read(mCacheBuffer, 0, mCacheBuffer.length);
        mNativeShortBuffer.position(0);
        mNativeShortBuffer.put(mCacheBuffer).position(0);
        mNativeByteBuffer.position(0);

        // 波形正規化を行なう
        for (int i = 0; i < mCacheBuffer.length; ++i) {
            mNormalizedBuffer[i] = (double) mCacheBuffer[i] / (double) Short.MAX_VALUE;
        }

        // RMS計算を行なう
        {
            double sum = 0;
            for (double buf : mNormalizedBuffer) {
                sum += buf;
            }
            double average = sum / mCacheBuffer.length;

            double sumSquare = 0;
            for (double buf : mNormalizedBuffer) {
                sumSquare += Math.pow(buf - average, 2);
            }

            double avgSumSquare = sumSquare / mNormalizedBuffer.length;
            // 正規化する
            mRMS = Math.pow(avgSumSquare, 0.5) + 0.5;
        }
    }

    /**
     * nio配下のバッファを取得する。
     * このバッファはLittle Endianで管理される
     */
    @NonNull
    public ByteBuffer getNativeBuffer() {
        return mNativeByteBuffer;
    }

    /**
     * 計算済みのRMSを取得する
     */
    public double getRMS() {
        return mRMS;
    }
}
