package com.eaglesakura.android.sql;

import com.eaglesakura.android.utils.CiJUnitTester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void フラグリストを解析する() throws Throwable {
        QueryFlag flag = QueryFlag.parse(",2,this,");
        assertTrue(flag.contains(2L));
        assertTrue(flag.contains("this"));
        assertFalse(flag.contains(","));
    }

    @Test
    public void フラグリストを加算する() throws Throwable {
        QueryFlag flag = QueryFlag.parse(",this,").add(QueryFlag.parse(",2,"));
        assertTrue(flag.contains(2L));
        assertTrue(flag.contains("this"));
        assertFalse(flag.contains(","));
    }

    @Test
    public void フラグの和を取得する() throws Throwable {
        assertEquals(QueryFlag.or(",this,", ",2,"), ",2,this,");
        assertEquals(QueryFlag.or(",2,this,", null), ",2,this,");
        assertEquals(QueryFlag.or(null, null), "");
        assertEquals(QueryFlag.or("2", "this"), ",2,this,");
    }
}