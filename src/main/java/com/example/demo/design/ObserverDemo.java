package com.example.demo.design;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 观察者模式
 * @author: hanguokai1
 * @create: 2022-09-28 16:48
 **/
public class ObserverDemo {

    public static void main(String[] args) {
        User alice = new User("Alice");
        User bob = new User("Bob");

        PublicOwner publicOwner = new PublicOwner();
        publicOwner.addUser(alice);
        publicOwner.addUser(bob);

        publicOwner.notifyUser("hello!");
    }

}

//创建观察者抽象接口
interface Observer {
    void onMessage(String message);
}

//创建用户实现观察者接口
class User implements Observer {

    String userName;

    public User(String userName){
        this.userName = userName;
    }

    //当接收到消息时需要做的动作
    @Override
    public void onMessage(String message) {
        System.out.println(userName + " receive message: " + message);
    }

}

//被观察着抽象接口
interface Subject{
    //添加用户
    void addUser(Observer user);
    //删除用户
    void delUser(Observer user);
    //通知用户
    void notifyUser(String message);
}

class PublicOwner implements Subject {

    List<Observer> users = new ArrayList<>();

    @Override
    public void addUser(Observer user) {
        users.add(user);
    }

    @Override
    public void delUser(Observer user) {
        users.remove(user);
    }

    @Override
    public void notifyUser(String message) {
        for (Observer user : users) {
            user.onMessage(message);
        }
    }
}