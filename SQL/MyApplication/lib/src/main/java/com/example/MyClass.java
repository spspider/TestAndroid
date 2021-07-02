package com.example;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;
import static javafx.scene.input.KeyCode.H;

public class MyClass {




        public static void main(String args[]) {

            String RAnswer = "від земної( водної)поверхні до висоти 1500м( включно) та вище ешелону польоту 660(виключно)";
            String NegativeAnswer = "від земної( водної)поверхні до висоти 2900м( виключно) та вище ешелону польоту 660";
            //System.out.print("\n not found:"+String.valueOf(getValue(RAnswer,NegativeAnswer)));
            //getValue(RAnswer,NegativeAnswer).size()
            Map<Integer, Integer> _map= new HashMap<Integer, Integer>();
            //_map = getValue(RAnswer,NegativeAnswer);
            //Iterator<Map.Entry<Integer,Integer>> itr=  getValue(RAnswer,NegativeAnswer).entrySet().iterator();
            //please check
            //getValue(RAnswer,NegativeAnswer).get(0);
            ArrayList<HashMap<Integer, Integer>> mylist = new ArrayList<HashMap<Integer, Integer>>();
            mylist=getValue(RAnswer,NegativeAnswer);
            for(HashMap<Integer, Integer> map: mylist) {
                for(Map.Entry<Integer, Integer> mapEntry: map.entrySet()) {
                    Integer key = mapEntry.getKey();
                    Integer value = mapEntry.getValue();
                    System.out.println(key+" "+value);
                    //здесь добавить sb.setSpan
                }
            }
        }

    public static ArrayList<HashMap<Integer, Integer>> getValue(String RAnswer, String NegativeAnswer) {

        String[] splitted;
        String[] splitted2;
        splitted=NegativeAnswer.split(" ");
        splitted2=RAnswer.split(" ");
        ArrayList<HashMap<Integer,Integer>> NotMatched=new ArrayList<>();
       //HashMap<Integer,Integer> NegativeWord=new HashMap<>();
        for (int i=0;i<splitted.length;i++){
            if ((i<splitted2.length)) {
                    boolean founded=false;
                    for (int i1 = 0; i1 < splitted2.length; i1++) {
                            if (splitted[i].equals(splitted2[i1])) {
                                if ((i-i1<4)||(i+i1<4)) {
                                    founded=true;
                                }
                            }
                    }
                    if (!founded){
                        int Start=NegativeAnswer.indexOf(splitted[i]);
                        int Finish = NegativeAnswer.indexOf(splitted[i])+splitted[i].length();
                        HashMap<Integer, Integer> my = new HashMap();
                        my.put(Start,Finish);
                        NotMatched.add(my);
                    }
            }

        }

        return NotMatched;
    }

}

            //}



