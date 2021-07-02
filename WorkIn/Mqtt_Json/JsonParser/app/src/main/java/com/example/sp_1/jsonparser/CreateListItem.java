package com.example.sp_1.jsonparser;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.R.attr.name;
import static com.example.sp_1.jsonparser.R.id.lMain;
import static com.example.sp_1.jsonparser.R.layout.item;


/**
 * Created by sp_1 on 12.01.2017.
 */

public class CreateListItem {
    private GlobalClass app;
    private Activity activity;
    private MainActivity getMainactivity2;
    LayoutInflater ltInflater;
    LinearLayout linLayout;
    //private MainActivity getMainactivity;
    public void makeList(String s) {
    }


public void createListInflater(ArrayList<HashMap<String, String>> contactList){
    getMainactivity2 = (MainActivity) GlobalClass.getInstance().getActivity();
    String[] name = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь" };
    String[] position = { "Программер", "Бухгалтер", "Программер",
            "Программер", "Бухгалтер", "Директор", "Программер", "Охранник" };
    int salary[] = { 13000, 10000, 13000, 13000, 10000, 15000, 13000, 8000 };

    int[] colors = new int[2];
    colors[0] = Color.parseColor("#559966CC");
    colors[1] = Color.parseColor("#55336699");

    LinearLayout linLayout = (LinearLayout) getMainactivity2.findViewById(R.id.myLiLa);

    LayoutInflater ltInflater = getMainactivity2.getLayoutInflater();

    for (int i = 0; i < name.length; i++) {
        //Log.d("myLogs", "i = " + i);
        View item = ltInflater.inflate(R.layout.item, linLayout, false);
        TextView tvName = (TextView) item.findViewById(R.id.tvName);
        tvName.setText(name[i]);
        TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
        tvPosition.setText("Должность: " + position[i]);
        TextView tvSalary = (TextView) item.findViewById(R.id.tvSalary);
        tvSalary.setText("Оклад: " + String.valueOf(salary[i]));
        item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        item.setBackgroundColor(colors[i % 2]);
        linLayout.addView(item);
     }
}
    public void createInflater(){

        linLayout = (LinearLayout) getMainactivity2.findViewById(lMain);
        ltInflater = getMainactivity2.getLayoutInflater();

    }
    public void printAllHashMap(ArrayList<HashMap<String, String>> contactList){
        getMainactivity2 = (MainActivity) GlobalClass.getInstance().getActivity();
        //getMainactivity2.myLog("-printing----------------------------------");

        if (ltInflater==null){createInflater();}
        ////////////////////////////////////////////////////////////
        View item = ltInflater.inflate(R.layout.item, linLayout, false);

        for(Map<String, String> map : contactList) {
                String widget = map.get("widget");
                switch (widget) {
                    case "toggle":
                        Button btnNew = new Button(getMainactivity2);
                        btnNew.setText(map.get("descr"));
                        btnNew.setOnClickListener(new ClickListener(btnNew.getId(),map.get("id")));

                        linLayout.addView(btnNew, item.getLayoutParams());
                        break;
                    case "range":

                        break;
                }


            TextView tvName = (TextView) item.findViewById(R.id.tvName);
            tvName.setText(map.get("descr"));
            TextView tvPosition = (TextView) item.findViewById(R.id.tvPosition);
            tvPosition.setText("page: " + (map.get("page")));
            TextView tvSalary = (TextView) item.findViewById(R.id.tvSalary);
            tvSalary.setText("widget: " + (map.get("widget")));
            item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            //item.setBackgroundColor(colors[i % 2]);
            linLayout.addView(item);
        }
        ////////////////////////////////////////////////////////////
        for(Map<String, String> map : contactList)
        {
            String tagName = map.get("id");
            getMainactivity2.myLog(tagName);
            //System.out.println(tagName);

        }
    }





}
