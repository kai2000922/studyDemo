package com.example.demo.design;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 工厂模式
 * @author: hanguokai1
 * @create: 2022-09-20 18:05
 **/
public class FactoryDemo {

    public static void main(String[] args) {
        BeanFactory beanFactory = new BeanFactory();

        BeanA beanA = (BeanA) beanFactory.getBean("beanA");
        BeanB beanB = (BeanB) beanFactory.getBean("beanB");

        beanA.print();
        beanB.print();
    }

}

class BeanFactory{

    private Map<String, Bean> beanMap = new HashMap<>();

    public Bean getBean(String beanName){
        init();

        return beanMap.get(beanName);
    }

    public void init(){
        beanMap.put("beanA", new BeanA());
        beanMap.put("beanB", new BeanB());
    }

}

interface Bean{
    void print();
}

class BeanA implements Bean{

    @Override
    public void print() {
        System.out.println("this is beanA");
    }
}

class BeanB implements Bean{

    @Override
    public void print() {
        System.out.println("this is beanB");
    }
}