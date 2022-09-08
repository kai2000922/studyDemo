package com.example.demo.thread;

import com.example.demo.domain.Student;

import java.util.ArrayList;
import java.util.List;

public class Demo {

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().freeMemory() / (double) 1024 / 1024 + "MB");

        int num = 500000;
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Student student = new Student();
            student.setId(i);
            student.setName(i+"");
            list.add(student);
        }

        System.out.println(Runtime.getRuntime().freeMemory() / (double) 1024 / 1024 + "MB");

        num = 510000;
        for (int i = 0; i < num; i++) {
            Student student = new Student();
            student.setId(i);
            student.setName(i+"");
            list.add(student);
        }
        System.out.println(Runtime.getRuntime().freeMemory() / (double) 1024 / 1024 + "MB");

    }

}
