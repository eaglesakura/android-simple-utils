package com.eaglesakura.android.sql;

import com.eaglesakura.util.CollectionUtil;
import com.eaglesakura.util.IOUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.RawRes;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * SQLite クエリを記述するためのラッパー
 */
@Deprecated
public class SQLQuery {
    /**
     * SELECT文に対応する
     */
    StringBuilder mSelect = new StringBuilder();

    /**
     * JOIN文に対応する
     */
    StringBuilder mJoin = new StringBuilder();

    /**
     * LEFT JOIN文に対応する
     */
    StringBuilder mLeftJoin = new StringBuilder();


    /**
     * ON文に対応する
     */
    StringBuilder mOn = new StringBuilder();

    /**
     * FROM文に対応する
     */
    StringBuilder mFrom = new StringBuilder();

    /**
     * WHERE文に対応する
     */
    StringBuilder mWhere = new StringBuilder();

    /**
     * ORDER文に対応する
     */
    StringBuilder mOrder = new StringBuilder();

    /**
     * 引数リスト
     */
    List<String> mArgments = new LinkedList<>();

    /**
     * テンプレートとして最初に適用される
     */
    StringBuilder mTemplates = new StringBuilder();

    SQLiteDatabase mDatabase;

    public SQLQuery() {
    }


    public SQLQuery from(String tableName) {
        mFrom.append(tableName);
        return this;
    }

    /**
     * データベースをバインドする
     */
    public SQLQuery database(SQLiteDatabase db) {
        mDatabase = db;
        return this;
    }

    /**
     * クエリを実行する
     */
    public Cursor query() {
        return mDatabase.rawQuery(build(), args());
    }

    /**
     * テンプレートを追加する
     */
    public SQLQuery template(String string, String... argments) {
        if (mTemplates.length() > 0) {
            mTemplates.append(" ");
        }
        mTemplates.append(string);
        for (String arg : argments) {
            mArgments.add(arg);
        }
        return this;
    }

    /**
     * SQLTemplateを追加する
     */
    public SQLQuery templateFromRaw(Context context, @RawRes int resId, String... argments) {
        InputStream is = null;
        try {
            return template(IOUtil.toString(context.getResources().openRawResource(resId), false), argments);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            IOUtil.close(is);
        }
    }

    /**
     * SQLTemplateを追加する
     */
    public SQLQuery templateFromAsset(Context context, String path, String... argments) {
        InputStream is = null;
        try {
            return template(IOUtil.toString(context.getResources().getAssets().open(path), false), argments);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            IOUtil.close(is);
        }
    }

    public SQLQuery select(String column) {
        if (mSelect.length() > 0) {
            mSelect.append(", ");
        }
        mSelect.append(column);
        return this;
    }

    public SQLQuery select(String table, String column) {
        return select(table + "." + column);
    }

    public SQLQuery join(String table) {
        if (mJoin.length() > 0) {
            mJoin.append(" ");
        }
        mJoin.append(table);
        return this;
    }

    public SQLQuery leftJoin(String table) {
        if (mLeftJoin.length() > 0) {
            mLeftJoin.append(" ");
        }
        mLeftJoin.append(table);
        return this;
    }

    public SQLQuery where(String op, String sql, String match, Object argment) {
        if (mWhere.length() > 0) {
            mWhere.append(" ");
            mWhere.append(op);
            mWhere.append(" ");
        }
        mWhere.append(sql);
        mWhere.append(" ");
        mWhere.append(match);
        mWhere.append(" ");
        if (argment instanceof String) {
            mWhere.append("?");
            mArgments.add((String) argment);
        } else {
            mWhere.append(argment.toString());
        }
        return this;
    }

    public SQLQuery orderBy(String column, String order) {
        if (mOrder.length() > 0) {
            mOrder.append(", ");
        }
        mOrder.append(column);
        mOrder.append(" ");
        mOrder.append(order);
        return this;
    }

    public SQLQuery on(String tableA, String columnA, String tableB, String columnB) {
        if (mOn.length() > 0) {
            mOn.append(" AND ");
        }
        mOn.append(tableA + "." + columnA);
        mOn.append("=");
        mOn.append(tableB + "." + columnB);
        return this;
    }

    public SQLQuery on(String tableColumnA, String tableColumnB) {
        if (mOn.length() > 0) {
            mOn.append(" AND ");
        }
        mOn.append(tableColumnA);
        mOn.append("=");
        mOn.append(tableColumnB);
        return this;
    }

    private void append(StringBuilder result, String sql, StringBuilder append) {
        if (append.length() > 0) {
            result.append(sql)
                    .append(append);
        }
    }

    public String[] args() {
        return CollectionUtil.asArray(mArgments, new String[mArgments.size()]);
    }

    public String build() {
        StringBuilder result = new StringBuilder();

        if (mTemplates.length() > 0) {
            result.append(mTemplates);
        }
        append(result, " SELECT ", mSelect);
        append(result, " FROM ", mFrom);
        append(result, " JOIN ", mJoin);
        append(result, " LEFT JOIN ", mLeftJoin);
        append(result, " ON ", mOn);
        append(result, " WHERE ", mWhere);
        append(result, " ORDER BY ", mOrder);

        return result.toString();
    }
}
