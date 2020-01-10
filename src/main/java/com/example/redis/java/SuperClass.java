package com.example.redis.java;

public class SuperClass {
    static {
        System.out.println("SuperClass init!");
    }

    public static int value = 123;

    public int cf = 345;

    public String hek = "hello world";

    public final static int pool = 789;
}
