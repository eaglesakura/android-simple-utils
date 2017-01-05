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
}
