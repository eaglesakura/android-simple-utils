package com.eaglesakura.util;


import org.junit.Test;

import static org.junit.Assert.*;

public class EnvironmentUtilTest {
    @Test
    public void UnitTestでの環境チェックを行う() {
        assertTrue(EnvironmentUtil.isRunningAndroid());
        assertTrue(EnvironmentUtil.isRunningRobolectric());
    }
}
