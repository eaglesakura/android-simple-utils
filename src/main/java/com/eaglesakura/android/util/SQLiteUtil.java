package com.eaglesakura.android.util;

import com.eaglesakura.util.EncodeUtil;
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
        value = StringUtil.replaceAllSimple(value, " ", "_");
        value = EncodeUtil.toUrl(value);
        return getInjectionGuard(value);
    }

    /**
     *
     * @param value
     * @return
     */
    public static String getInjectionGuard(String value) {
        value = StringUtil.replaceAllSimple(value, "'", "_qt_");
        value = StringUtil.replaceAllSimple(value, "%", "_pr_");
        value = StringUtil.replaceAllSimple(value, "#", "_sh_");
        value = StringUtil.replaceAllSimple(value, "?", "_qn_");
        return value;
    }
}
