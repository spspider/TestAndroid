package com.example.simplyjava;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MyClass {
    public static void main(String[] arg){
        Map<String, List<String>> parms = null;
        parms.put("username",Collections.singletonList("list"));
        List<String> answerList = parms.get("username");
        System.out.println(answerList);
    }
}
