package com.gongfuzhu.autotools;

import javassist.*;
import lombok.SneakyThrows;

import java.security.ProtectionDomain;
import java.util.Arrays;

public class JavassistDemo {

    public static void main(String[] args) {
//        demo1();
        demo2();

    }
    // 对构造方法添加代码
    @SneakyThrows
    private static void demo1( ) {
        final ClassLoader loader = JavassistDemo.class.getClassLoader();
        final ProtectionDomain domain = JavassistDemo.class.getProtectionDomain();
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.gongfuzhu.autotools.DemoClass");

        Arrays.stream(ctClass.getConstructors()).forEach((constructor)->{
            try {
//                constructor.insertBefore("System.err.println(123);");
//                constructor.insertAfter("System.err.println(456);");
                //替换内容
//                constructor.setBody("System.err.println(789);");
                //在指定行添加
                constructor.insertAt(6,"System.err.println(\"在第二行添加代码\");");
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        });

        pool.toClass(ctClass, loader, domain);
        ctClass.defrost();

        DemoClass demo = new DemoClass();
    }

    // 在指定方法后面添加代码
    @SneakyThrows
    public static void demo2(){
        // 获取类池
        ClassPool cp = ClassPool.getDefault();
        // 获取要修改的类
        CtClass cc = cp.get("com.gongfuzhu.autotools.DemoClass");
        // 获取要修改的方法
        CtMethod m = cc.getDeclaredMethod("sayHello");
        // 在方法体中插入新的代码
//        m.insertAfter("System.out.println(\"Hello from Javassist!\");");


        m.insertAt(2,"System.err.println(\"在第二行添加代码\");");
        // 将修改后的类保存到磁盘
//        cc.writeFile();
        cc.toClass();
        // 测试修改后的类
        DemoClass demo = new DemoClass();
        demo.sayHello();
    }

}

class DemoClass {
    public DemoClass() {
        System.out.println("无参构造方法");

    }
    public void sayHello() {
        System.out.println("Hello, world!");
        System.out.println("This is the second line.");
        System.out.println("This is the third line.");
    }
}
