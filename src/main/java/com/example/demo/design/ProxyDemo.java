package com.example.demo.design;

import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @description: 代理模式
 * @author: hanguokai1
 * @create: 2022-09-20 22:13
 **/
public class ProxyDemo {

    public static void main(String[] args) {
        Student student = new Student();
        Monitor monitor = new Monitor(student);
        monitor.giveMoney();

        ProxyDemo proxyDemo = new ProxyDemo();
        Person proxyStu = proxyDemo.getProxyStu("Bob");
        proxyStu.giveWork();
        proxyStu.giveMoney();

        byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy0",Student.class.getInterfaces());
        String path = "/Users/hanguokai1/Documents/StuProxy.class";
        try(FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(classFile);
            fos.flush();
            System.out.println("代理类class文件写入成功");
        } catch (Exception e) {
            System.out.println("写文件错误");
        }
    }

    public Person getProxyStu(String stuName){
        Student student = new Student(stuName);

        InvocationHandler handler = new StuInvocationHandler<>(student);
        return (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class[]{Person.class}, handler);
    }

}

interface Person{
    void giveMoney();
    void giveWork();
}

class Student implements Person{

    private String stuName;

    public Student() {
    }

    public Student(String stuName){
        this.stuName = stuName;
    }

    @Override
    public void giveMoney() {
        System.out.println(stuName + " give 5 yuan");
    }

    @Override
    public void giveWork() {
        System.out.println(stuName + " give work");
    }

}

class Monitor implements Person{

    Student student;

    @Override
    public void giveMoney() {
        student.giveMoney();
    }

    @Override
    public void giveWork() {
        student.giveWork();
    }

    public Monitor(Student student){
        if (student.getClass() == Student.class){
            this.student = student;
        }
    }
}

class StuInvocationHandler<T> implements InvocationHandler {

    //InvocationHandler所持有的被代理对象
    private T t;

    //通过构造函数赋值
    public StuInvocationHandler(T t){
        this.t = t;
    }

    @Override
    //最主要的方法，所有的被代理的对象的被代理方法都会进入此方法，很像AOP的理念
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("the execute method name is " + method.getName() + " args is" + Arrays.toString(args));

        System.out.println("before doing sth");
        Object result = method.invoke(t, args);
        System.out.println("after doing sth");

        return result;
    }
}