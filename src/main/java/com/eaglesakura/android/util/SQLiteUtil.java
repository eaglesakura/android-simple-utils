package com.eaglesakura.android.util;

import com.eaglesakura.util.StringUtil;

public class SQLiteUtil {

    /**
     * SQLiteのキーとするためのシンプルな文字列を生成する
     *
     * 全角半角変換 -> URLエンコード -> クォート削除 -> %削除
     *
     * @return キーとする文字列
     */
    public static String makeSimpleKey(String value) {
        value = StringUtil.macStringToWinString(value);
        value = StringUtil.zenkakuEngToHankakuEng(value);
        value = StringUtil.replaceAllSimple(value, " ", "[sp]");
//        value = EncodeUtil.toUrl(value);
        return getInjectionGuard(value);
    }

    /**
     * LIKE句に使われる文字を置換する
     * @param value
     * @return
     */
    public static String getInjectionGuard(String value) {
        value = StringUtil.replaceAllSimple(value, "%", "[[pr]]");
        value = StringUtil.replaceAllSimple(value, "_", "[[us]]");
        return value;
    }
}
