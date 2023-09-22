# TODO

1.更新完善文档
2.修复暴露的bug

# 介绍

一个用于hook的插件

可以通过指定的语法对方法进行hook

> \[public|private|protected] \[static] \[final] \[synchronized] returnType classInternalName#methodName(paramType...)

整体包含信息如下

> 权限修饰符、返回值类型、类全路径名称、方法名称、参数列表

其中权限修饰符可选，返回值类型、类全路径名、方法名称、参数列表必选

# Hook规则

针对两种方法进行Hook

- 静态方法

Hook后的方法为static方法并且和原方法具有相同的参数列表(类名和方法名可自行配置)
如：

```java
// before
public class Tester {

    public static void testStaticHook() {}

}

// after
public class TesterHooker {

    public static void testStaticHook() {
        Log.e("TAG", "testStaticHook: ");
    }
    
}
```

- 成员方法

Hook后的方法变为**静态方法**，方法参数列表比Hook前多一个成员方法，其余保持不变(类名和方法名可自行配置)
如：

```java

public class Tester {
    
    public void testMemberHook() {}

}


public class TesterHooker {

    // 注意member必须是第一个参数，其他参数列表copy原函数
    public static void testMemberHook(Tester t) {
        Log.e("TAG", "testMemberHook: ");
    }

}

```

# 使用价绍

需要接入gradle plugin

```kotlin
plugins {
    // ......
    id("com.plugin.hook")
}

// dsl配置hook项
hook {

    // hook static方法
    element {
        hook = "void com.dev.tuzhiqiang.aop.Tester#testStaticHook()"
        target = "void com.dev.tuzhiqiang.aop.TesterHooker#testStaticHook(...)"
    }

    // hook 成员方法
    element {
        hook = "void com.dev.tuzhiqiang.aop.Tester#testMemberHook()"
        target = "void com.dev.tuzhiqiang.aop.TesterHooker#testMemberHook(...)"
    }
}

```

注：
1. element.hook可以必须配置返回值，方法名 & 参数列表。
并依照： 返回值全路径名称 类全路径名#方法名(参数列表)
参数列表只包含类型

> 如void com.dev.tuzhiqiang.Test#hello(java.lang.String)

2. target只需配置返回值类型、类名、方法名，参数列表可选。

# 效果呈现

> 插件生效前

```java
public class TestInvoker {

    public static void main(String[] args) {

        Tester.testStaticHook();
        new Tester().testMemberHook();

    }

}
```

> 生效后

```java
public class TestInvoker {
    public TestInvoker() {
    }

    public static void main(String[] args) {
        TesterHooker.testStaticHook();
        TesterHooker.testMemberHook(new Tester());
    }
}
```