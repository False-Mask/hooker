package com.dev.tuzhiqiang.aop;

import android.util.Log;

/** @noinspection unused*/
public class TesterHooker {

    public static void testStaticHook() {
        Log.e("TAG", "testStaticHook: ");
    }

    public static void testMemberHook(Tester t) {
        Log.e("TAG", "testMemberHook: ");
    }


}
