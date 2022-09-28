package com.example.demo.design;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.jndi.support.SimpleJndiBeanFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 单例模式
 * @author: hanguokai1
 * @create: 2022-09-20 17:08
 **/
public class SingletonDemo {

    private static volatile Map<String, Object> allUsers = null;

    public static synchronized Map<String, Object> getAllUser(){
        BeanFactory beanFactory = new SimpleJndiBeanFactory();
        beanFactory.getBean("com.aa");
        if (allUsers == null){
            allUsers = new HashMap<>();
            allUsers.put("hello", "world");
        }
        return allUsers;
    }

}
