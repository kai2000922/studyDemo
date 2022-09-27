package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Value("${testaa.aa}")
    private List<String> list;

    @GetMapping("/test")
    private int test(){
        return list.size();
    }

    public static void main(String[] args) {
        try {
            new Thread(TestController::run).start();
        }catch (Exception e){
            new Thread(TestController::run).start();
        }
    }

    public static void run(){
        System.out.println(Thread.currentThread().getName());
        int a = 5;
        int b = 3;
        while (true){
            try {
                Thread.sleep(1000);
                b--;
                int c = a/b;
                System.out.println(c);
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

}
