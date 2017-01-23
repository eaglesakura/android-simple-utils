package com.eaglesakura.android.util;

import com.eaglesakura.util.StringUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
     */
    public static String getInjectionGuard(String value) {
        value = StringUtil.replaceAllSimple(value, "%", "[[pr]]");
        value = StringUtil.replaceAllSimple(value, "_", "[[us]]");
        return value;
    }

    /**
     * SQLiteバージョンを取得する
     */
    public static String getVersion(Context context) {
        try {
            String query = "select sqlite_version() AS sqlite_version";
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToNext()) {
                return cursor.getString(0);
            } else {
                return null;
            }
        } catch (Throwable e) {
            return null;
        }
    }
}
