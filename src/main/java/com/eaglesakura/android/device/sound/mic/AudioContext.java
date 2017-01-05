package com.eaglesakura.android.device.sound.mic;

import com.eaglesakura.collection.DataCollection;

/**
 * 現在のマイク入力状態を管理する
 */
public class AudioContext {
    DataCollection<MicBuffer> mMicBufferList;

    public AudioContext(DataCollection<MicBuffer> micBufferList) {
        mMicBufferList = micBufferList;
    }

    /**
     * 全てのサウンドバッファを取得する
     */
    public DataCollection<MicBuffer> getMicBufferList() {
        return mMicBufferList;
    }

    /**
     * 最新のソースを取得する
     */
    public MicBuffer getCurrentBuffer() {
        return mMicBufferList.getSource().get(mMicBufferList.size() - 1);
    }

    /**
     * RMS平均値を取得する
     */
    public double getAverageRMS() {
        if (mMicBufferList.size() > 2) {
            // スパイクのチェックを行なうため、最新の値は除く
            double sum = 0;
            for (int i = 0; i < (mMicBufferList.size() - 1); ++i) {
                sum += mMicBufferList.getSource().get(i).getRMS();
            }
            return sum / (mMicBufferList.size() - 1);
        } else {
            double sum = 0;
            for (MicBuffer buf : mMicBufferList.getSource()) {
                sum += buf.getRMS();
            }
            return sum / mMicBufferList.size();
        }
    }

    /**
     * RMS値が最新で急激に変化していればtrueを返却する
     *
     * @param threshold しきい値, 0.1程度が良い
     */
    public boolean isSpikeRMS(double threshold) {
        double current = getCurrentBuffer().getRMS();
        double avg = getAverageRMS();
        return current >= (avg + threshold);
    }
}
