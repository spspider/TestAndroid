package com.example;

import java.util.ArrayList;

public class MyClass_import {

    static int row = 0;
    static int question_row = 0;
    static int Positive_row = 0;
    static int Negative_row = 0;
    static String theme = "", question = "", negative_answer = "", right_answer = "";
    static String Question_is = "";
    static ArrayList<String> NAnswers = new ArrayList();
    static String RAnswer = "";
    static boolean next_title = false;
    static boolean next_themes = false;
    private final Object LOG_TAG = "LOG_IMPORT";

    public static void main(String[] arg) {
        String filePath = "�� ������� ���������� ���������� ������������� �������� ��� ������ ��� (���������� ������������� �����, ��������������� ������ ������, �� ��������� ���������� �� ������� �� ������������� ������� ���������� �� �������) ��� ������ � ��������� ����������� ������� ���� ����������� ������� ��������������� �� ������������� ������� ��������� �� �����:\n" +
                "30 ������\n" +
                "+1 ������\n" +
                "2 �����\n" +
                "3 �����\n" +
                "\n" +
                "���������� ������� ���� ���������� ������ ��� �������������:\n" +
                " +�������������� ��������\n" +
                "������������ ��������\n" +
                "�������� ��������\n" +
                "�������� ������������ ��������\n";
        //readFileSD(filePath);
        filePath="content://com.estrongs.files/storage/sdcard0/PC/����������.txt";
       // while(filePath.contains("/")){

       // }
        if (filePath.contains("/")) {
            filePath = filePath.substring(filePath.lastIndexOf("/") + 1);
            filePath=filePath.contains(".")?filePath.substring(0,filePath.indexOf(".")):filePath;
        }
        System.out.println("\n Right answer:" + filePath + "\n");
    }

    // convert UTF-8 to internal Java String format
    public static String convertUTF8ToString(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("ISO-8859-1"), "windows-1251");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    // convert internal Java String format to UTF-8
    public static String convertStringToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("windows-1251"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }


    static void readFileSD(String filePath) {

        //br=filePath;
        String str = filePath;
        // ������ ����������
        String AllText;
        boolean next_question = false;
        boolean standby_for_question = false;
        int count_questions = 0;
        while (!str.isEmpty()) {
            int n_index = str.indexOf("\n");
            String str_n_found = str.substring(0, n_index);
            str = str.substring(n_index + 1);
            while (str.startsWith(" ")) {//��� �� ������ ��� ������� ����� ��������
                str = str.substring(1);
            }
            if (str_n_found.startsWith("+")) {
                //RIGHT ANSWER
                String right_ansver = str_n_found.substring(1);
                System.out.println("\n Right answer:" + right_ansver + "\n");

                if (standby_for_question) {
                    System.out.println("\nERROR to many right variants\n");
                }
                standby_for_question = true;
            } else if ((next_question) || (count_questions == 0)) {
                if (!str_n_found.isEmpty()) {
                    count_questions++;
                    standby_for_question = false;
                    //QUESTION
                    System.out.println("\n Question:" + str_n_found + "END\n");
                    next_question = false;
                }
            } else if (str_n_found.isEmpty()) {
                next_question = true;
            } else {
                //NEGATIVE ANSWER
                System.out.println("\n NAnswer:" + str_n_found + "END\n");
            }
        }
    }


}
