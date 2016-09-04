package com.eaglesakura.android.sql;

import com.eaglesakura.util.CollectionUtil;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SQL内部で文字列としてフラグを管理する
 *
 * 内部のセパレータに半角カンマ`,`を使用するため、フラグにカンマを利用することはできない。
 */
public class QueryFlag {
    static final char SEPARATOR = ',';

    Set<String> mFlags = new HashSet<>();

    public QueryFlag() {
    }

    public QueryFlag(Collection<String> flags) {
        mFlags.addAll(flags);
    }

    public QueryFlag(String... flags) {
        for (String flag : flags) {
            add(flag);
        }
    }

    /**
     * 文字列をフラグとして扱う
     */
    public QueryFlag add(@NonNull String flag) {
        if (flag.indexOf(SEPARATOR) >= 0) {
            throw new IllegalArgumentException(flag);
        }
        mFlags.add(flag);
        return this;
    }

    /**
     * 数値をフラグに変換する
     */
    public QueryFlag add(long flag) {
        return add(String.valueOf(flag));
    }

    private List<String> toList() {
        List<String> list = CollectionUtil.asOtherList(mFlags, it -> it);
        Collections.sort(list, (a, b) -> a.compareTo(b));
        return list;
    }

    /**
     * LIKEで検索するためのキーワードを取得する
     */
    public String toLike() {
        if (mFlags.isEmpty()) {
            return ",,,";   // フラグが設定されていない場合、絶対にヒットしないようにする。
        }

        StringBuilder builder = new StringBuilder();
        for (String flag : toList()) {
            builder.append("%").append(SEPARATOR).append(flag).append(SEPARATOR);
        }
        return builder.append("%").toString();
    }

    /**
     * フラグ文字列に変換する
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String flag : toList()) {
            builder.append(SEPARATOR).append(flag);
        }
        builder.append(SEPARATOR);
        return builder.toString();
    }
}
