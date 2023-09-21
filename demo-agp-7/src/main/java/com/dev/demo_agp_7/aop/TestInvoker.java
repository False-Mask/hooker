package com.dev.demo_agp_7.aop;

public class TestInvoker {

    public static void main(String[] args) {

        Tester.testStaticHook();
        new Tester().testMemberHook();

    }

}
