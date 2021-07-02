package com.example;

import java.util.ArrayList;
import java.util.List;

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
    static String filepath2 = "У якому шарі атмосфери знаходиться  практично уся водяна пара, при конденсації якої утворюються хмари і опади? \n" +
            "а) стратосфера;\n" +
            "б) мезосфера;\n" +
            "в) тропосфера;\n" +
            "г) термосфера.\n" +
            "в) тропосфера\n" +
            "\n" +
            "Що називається тропопаузою?\n" +
            "а) це перехідний шар між шаром тертя та вільною атмосферою;\n" +
            "б) це шар повітря, температура повітря в якому залишається незмінною; \n" +
            "в) це перехідний шар між тропосферою та мезосферою;\n" +
            "г) це перехідний шар між тропосферою та стратосферою.\n" +
            "г) перехідний шар між тропосферою та стратосферою\n" +
            "\n" +
            "Що найбільше впливає на погодні умови в нижньому шарі тропосфери?\n" +
            "а) підстильна поверхня;\n" +
            "б) конденсація водяного пару;\n" +
            "в) перемішування повітря внаслідок турбулентності;\n" +
            "г) запиленість повітря.\n" +
            "а) підстильна поверхня\n" +
            "\n" +
            "\n";
    private final Object LOG_TAG = "LOG_IMPORT";

    public static void main(String[] arg) {
        String filePath = "На каналах авіаційного повітряного електрозв’язку діапазону ДВЧ органів ОПР (аеродромної диспетчерської вишки, диспетчерського органу підходу, що забезпечує заходження на посадку за радіолокатором точного заходження на посадку) для одного з комплектів радіостанцій повинно бути передбачено аварійне електроживлення від акумуляторних батарей тривалістю не менше:\n" +
                "30 хвилин\n" +
                "+1 години\n" +
                "2 годин\n" +
                "3 годин\n" +
                "\n" +
                "\n" +
                "Обладнання робочих місць диспетчерів органів ОПР забезпечується:\n" +
                " +ненавантаженим резервом\n" +
                "навантаженим резервом\n" +
                "подвійним резервом\n" +
                "частково навантаженим резервом\n";
        //readFileSD_Meteo(filepath2);
        readFileSD(filePath);
    }
    static void readFileSD(String filePath) {

        //br=filePath;
        String str = filePath;
        // читаем содержимое
        String AllText;
        boolean next_question = false;
        boolean standby_for_question = false;
        int count_questions = 0;
        while (!str.isEmpty()) {
            int n_index = str.indexOf("\n");
            String str_n_found = str.substring(0, n_index);
            str = str.substring(n_index + 1);
            while (str.startsWith(" ")) {//что бы убрать все пробелы перед вопросом
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
    private static String Substring_brakets(String str) {
        str = str.substring(str.indexOf(")") + 1);
        while (str.startsWith(" ")) {
            str = str.substring(1);
        }
        return str;
    }

    private static void readFileSD_Meteo(String filepath2) {
        //br=filePath;
        String str = filepath2;
        // читаем содержимое
        String AllText;
        boolean next_question = false;
        boolean standby_for_question = false;
        int count_questions = 0;
        List<String> Answers = new ArrayList<>();
        while ((!str.isEmpty())) {
            String str_n_found;
            int n_index = str.indexOf("\n");
            if (n_index < 0) {
                break;
            } else {
                str_n_found = str.substring(0, n_index);
            }
            str = str.substring(n_index + 1);

            if (str_n_found.isEmpty()) {
                str = str.substring(str.indexOf("\n")+1);
                continue;
            }
            while (str_n_found.startsWith(" ")) {
                str_n_found = str_n_found.substring(1);
            }
            String str_question_begin = str_n_found.length() > 2 ? str_n_found.substring(0, 2) : "";
            //System.out.println("\nAnswers:" + str_question_begin + "\n");
            boolean RA_found = false;
            if (str_question_begin.contains(")")) {
                for (int i = 0; i < Answers.size(); i++) {
                    String check = Answers.get(i);
                    if (check.substring(0, 2).equals(str_question_begin)) {
                        String right_answer = str_n_found;
                        Answers.remove(Answers.get(i));
                        right_answer = Substring_brakets(right_answer);


                        Answers.add(right_answer);
                        System.out.println("\nRAnswer:" + right_answer + "\n");
                        RAnswer = right_answer;
                        //Answers.clear();
                        RA_found = true;
                    }
                }
                if (!RA_found) {
                    Answers.add(str_n_found);
                    //System.out.println("\nAnswers:" + str_n_found + "\n");
                } else {
                    List<String> Answers_List = new ArrayList<>();
                    for (int i = 0; i < Answers.size(); i++) {
                        String answer = Substring_brakets(Answers.get(i));
                        Answers_List.add(answer);
                    }
                    Answers.clear();
                    System.out.println("\nAnswers_List:" + Answers_List + "\n");
                    NAnswers.clear();
                    NAnswers.addAll(Answers_List);
                    Answers_List.clear();
                    ///

                }
            } else {
                //System.out.println("\nQuestion:" + str_n_found + "\n");
                Question_is = str_n_found;
                if ((NAnswers.size() > 0 && (!Question_is.equals("")) && (!RAnswer.equals("")))) {
                    System.out.println("\n Question_is:" + Question_is + "\n");
                    System.out.println("\n RAnswer:" + RAnswer + "\n");
                    System.out.println("\n NAnswers:" + NAnswers.toString() + "\n");
                    //sendValues();
                }else{
                    System.out.println("\n EEEEEEEEEERRRROR Question_is:" + Question_is + "\n");
                    System.out.println("\n EEEEEEEEEERRRROR RAnswer:" + RAnswer + "\n");
                    System.out.println("\n EEEEEEEEEERRRROR NAnswers:" + NAnswers.toString() + "\n");
                }
                //System.out.println("\n countQuestion:" + count_questions+ "\n");
                count_questions++;
                Answers.clear();

            }

            //int n_index = str.indexOf(")");
        }
        //System.out.println("\nEND:" + "\n");
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





}
