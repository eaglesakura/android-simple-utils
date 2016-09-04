package com.eaglesakura.android.sql;

import com.eaglesakura.android.utils.CiJUnitTester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryFlagTest extends CiJUnitTester {

    @Test
    public void フラグが生成できる() throws Throwable {
        QueryFlag flag = new QueryFlag();
        flag.add("this");
        flag.add(2L);

        assertEquals(flag.toString(), ",2,this,");
    }

    @Test
    public void 追加された順序によらず同じフラグ文字列が生成される() throws Throwable {
        QueryFlag flag = new QueryFlag();
        flag.add(2L);
        flag.add("this");

        assertEquals(flag.toString(), ",2,this,");
    }

    @Test
    public void 検索文字列を生成できる() throws Throwable {
        QueryFlag flag = new QueryFlag();
        flag.add("this");
        assertEquals(flag.toLike(), "%,this,%");
    }

    @Test
    public void 複数個のフラグを適用した検索文字列を生成できる() throws Throwable {
        QueryFlag flag = new QueryFlag();
        flag.add("this");
        flag.add(2L);
        assertEquals(flag.toLike(), "%,2,%,this,%");
    }
}