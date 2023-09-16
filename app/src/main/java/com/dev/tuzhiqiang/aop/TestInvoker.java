package com.dev.tuzhiqiang.aop;

public class TestInvoker {

    public static void main(String[] args) {

        Tester.testStaticHook();
        new Tester().testMemberHook();

    }

}
