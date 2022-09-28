package com.example.demo.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 策略模式demo
 * @author: hanguokai1
 * @create: 2022-09-28 15:41
 **/
public class StrategyDemo {

    private static Map<String, Pay> strategyRelation = new HashMap<>();

    static {
        strategyRelation.put("wx", new WXPay());
        strategyRelation.put("ali", new AliPay());
        strategyRelation.put("cash", new CashPay());
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("wx");
        list.add("ali");
        list.add("cash");
        list.add("haha");
        double money = 25.6;
        for (String s : list) {
            Pay pay = strategyRelation.get(s);
            if (pay != null){
                pay.pay(money);
            }else {
                System.out.println("don't exist pay module");
            }
        }
    }

}

interface Pay{
    void pay(double money);
}

class WXPay implements Pay{

    @Override
    public void pay(double money){
        System.out.println("use wx pay " + money + " yuan");
    }

}

class AliPay implements Pay{

    @Override
    public void pay(double money){
        System.out.println("use ali pay " + money + " yuan");
    }

}

class CashPay implements Pay{

    @Override
    public void pay(double money){
        System.out.println("use cash pay " + money + " yuan");
    }

}