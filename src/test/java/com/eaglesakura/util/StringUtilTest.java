package com.eaglesakura.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class StringUtilTest {

    @Test
    public void base64変換が行える() throws Exception {
        byte[] buffer = "this is test".getBytes();
        String encoded = StringUtil.toString(buffer);
        assertNotNull(encoded);
        assertNotEquals(encoded, "");
        byte[] decoded = StringUtil.toByteArray(encoded);
        assertArrayEquals(buffer, decoded);
    }
}
