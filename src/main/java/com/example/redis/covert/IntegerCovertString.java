package com.example.redis.covert;

public class IntegerCovertString {
    private String value;

    public IntegerCovertString(String value) {
        this.value = value;
    }

    public String add(Integer i){
        return String.valueOf(this.value()+i);
    }

    private Integer value(){
        return Integer.valueOf(this.value);
    }
}
