package com.eaglesakura.android.device.sound;

import com.eaglesakura.android.device.sound.mic.AudioContext;
import com.eaglesakura.android.device.sound.mic.MicBuffer;
import com.eaglesakura.android.util.AndroidThreadUtil;
import com.eaglesakura.collection.DataCollection;
import com.eaglesakura.lambda.CallbackUtils;
import com.eaglesakura.lambda.CancelCallback;
import com.eaglesakura.util.Util;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Androidのマイク入力を管理する
 *
 * 内部はリングバッファで実装されている。
 */
public class MicAudioBuffer {

    Context mContext;

    List<MicBuffer> mBuffers = new ArrayList<>();

    /**
     * 書込み先ポインタ
     */
    int mWritePointer = -1;

    int mSamplingRate;

    int mAudioSource;

    int mAudioEncoding;

    int mInput;

    int mBufferBytes;

    MicAudioBuffer(Builder builder) {
        mContext = builder.mContext;
        mSamplingRate = builder.mSamplingRate;
        mInput = builder.mInput;
        mBufferBytes = builder.mOnceBufferBytes;
        mAudioSource = builder.mSource;
        mAudioEncoding = builder.mAudioEncoding;

        for (int i = 0; i < builder.mBufferNum; ++i) {
            MicBuffer buffer = new MicBuffer(this, builder.mOnceBufferBytes);
            mBuffers.add(buffer);
        }
    }

    /**
     * 次の書込み先ポインタを得る
     */
    MicBuffer nextWriteBuffer() {
        ++mWritePointer;
        return mBuffers.get(mWritePointer % mBuffers.size());
    }

    /**
     * リングバッファを書込み時系列順にリスト化する
     */
    List<MicBuffer> listBuffers() {
        List<MicBuffer> result = new ArrayList<>();
        int index = mWritePointer - (mBuffers.size() - 1);
        for (int i = 0; i < mBuffers.size(); ++i) {
            if (index >= 0) {
                result.add(mBuffers.get(index % mBuffers.size()));
            }
            ++index;
        }
        return result;
    }

    /**
     * キャンセルされるまで内部で録音を続ける
     */
    public void record(RecordCallback callback, CancelCallback cancelCallback) {
        AndroidThreadUtil.assertBackgroundThread();

        AudioRecord audioRecord = null;
        try {
            mWritePointer = -1;

            audioRecord = new AudioRecord(mAudioSource, mSamplingRate, mInput, mAudioEncoding, mBufferBytes);
            audioRecord.startRecording();
            while (!CallbackUtils.isCanceled(cancelCallback)) {
                MicBuffer buffer = nextWriteBuffer();
                buffer.read(audioRecord);
                callback.onRecord(this, audioRecord, new AudioContext(new DataCollection<>(listBuffers())));
            }
        } finally {
            Util.safeIfPresent(audioRecord, it -> it.stop());
            Util.safeIfPresent(audioRecord, it -> it.release());
        }
    }

    public interface RecordCallback {
        /**
         * 1バッファの録音が完了したらコールバックされる
         *
         * @param audioContext 録音されたバッファリスト, 時系列順に並んでいるため、最新のバッファは一番最後となる。
         */
        void onRecord(MicAudioBuffer buffer, AudioRecord audio, AudioContext audioContext);
    }

    public static class Builder {
        Context mContext;

        /**
         * 1バッファの切り上げサイズ
         */
        int mOnceBufferBytes = 1024 * 4;

        /**
         * 使用するバッファ数
         */
        int mBufferNum = 4;

        /**
         * 入力ソース
         * 通情はマイク
         */
        int mSource = MediaRecorder.AudioSource.MIC;

        /**
         * 音声入力, 通常はモノラル入力
         */
        int mInput = AudioFormat.CHANNEL_IN_MONO;

        /**
         * 音声形式
         */
        int mAudioEncoding = AudioFormat.ENCODING_PCM_16BIT;

        /**
         * サンプリングレート
         * 通情は16khz
         */
        int mSamplingRate = 16 * 1000;

        public static Builder from(Context context) {
            Builder result = new Builder();
            result.mContext = context;
            return result;
        }

        /**
         * 書込み先バッファ数を指定する
         */
        public Builder bufferNum(@IntRange(from = 3) int buffers) {
            mBufferNum = buffers;
            return this;
        }

        /**
         * 入力機器を設定する
         *
         * @see AudioFormat#CHANNEL_IN_MONO
         */
        public Builder input(int audioFormat) {
            mInput = audioFormat;
            return this;
        }

        /**
         * 1バッファのサイズを指定する
         * このブロックが複数個生成され、リングバッファとして使用される。
         */
        public Builder onceBufferSize(@IntRange(from = 1024) int bytes) {
            mOnceBufferBytes = bytes;
            return this;
        }

        public MicAudioBuffer build() {
            return new MicAudioBuffer(this);
        }
    }
}
