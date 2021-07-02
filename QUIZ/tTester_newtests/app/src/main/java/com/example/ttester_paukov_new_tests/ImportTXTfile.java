package com.example.ttester_paukov_new_tests;

import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ttester_paukov_new_tests.Utils.AllThemes;
import com.example.ttester_paukov_new_tests.Utils.Question;
import com.example.ttester_paukov_new_tests.dynamicviewpager.LogFragment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.example.ttester_paukov_new_tests.OpenFileActivity.LOG_TAG;

/**
 * Created by sp_1 on 30.03.2017.
 */

public class ImportTXTfile {
    boolean next_title = false;
    boolean next_themes = false;
    String theme = "", question = "", negative_answer = "", right_answer = "";
    String Question_is = "";
    ArrayList<String> NAnswers = new ArrayList();
    String RAnswer = "";
    int row = 0;
    int question_row = 0, Positive_row = 0, Negative_row = 0;
    boolean End_of_all_rows = false;
    Activity activity;

    public ImportTXTfile(FragmentActivity activity) {
        this.activity = activity;
    }


    public void openFile(String filePath) {
        FileInputStream fstream1 = null;
        try {
            fstream1 = new FileInputStream(filePath);
            DataInputStream in = new DataInputStream(fstream1);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "windows-1251"));
            //if (filePath.lastIndexOf("/") != -1) {
            String themename=filePath;
            if (themename.contains("/")) {
                themename = themename.substring(themename.lastIndexOf("/") + 1);
                themename=themename.contains(".")?themename.substring(0,themename.indexOf(".")):themename;
            }

            setTheme(themename);
            StringBuffer strBuffer = null;
            String str;

            String line = null;
            String message = new String();
            final StringBuilder buffer = new StringBuilder(2048);
            while ((line = br.readLine()) != null) {
                // buffer.append(line);
                buffer.append(line).append("\n");
            }
            message = buffer.toString();
            readFileSD(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFileSD(String filePath) {

        //br=filePath;
        String str = filePath;
        // читаем содержимое
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
                //System.out.println("\n Right answer:" + right_ansver + "\n");
                RAnswer = right_ansver;
                NAnswers.add(right_ansver);
                if (standby_for_question) {
                    //System.out.println("\nERROR to many right variants\n");
                }
                standby_for_question = true;
            } else if ((next_question) || (count_questions == 0)) {
                if (!str_n_found.isEmpty()) {
                    count_questions++;
                    standby_for_question = false;
                    //QUESTION
                    //System.out.println("\n Question:" + str_n_found + "END\n");
                    Question_is = str_n_found;
                    next_question = false;
                }
            } else if (str_n_found.isEmpty()) {
                next_question = true;
            } else {
                //NEGATIVE ANSWER
                NAnswers.add(str_n_found);
                //System.out.println("\n NAnswer:" + str_n_found + "END\n");
            }
            if ((NAnswers.size() > 0 && (!Question_is.equals("")) && (!RAnswer.equals("")))) {
                System.out.println("\n Question_is:" + Question_is + "\n");
                System.out.println("\n RAnswer:" + RAnswer + "\n");
                System.out.println("\n NAnswers:" + NAnswers.toString() + "\n");
                sendValues();
            }
        }

        Toast.makeText(activity, "Новые вопросы импортированы" + count_questions, Toast.LENGTH_SHORT).show();

    }

    void readFileSD(Activity activity, String filePath) {


        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(OpenFileActivity.LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        //sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // формируем объект File, который содержит путь к файлу
        //Log.d(LOG_TAG, "sdPath:"+String.valueOf(sdPath));
        //File sdFile = new File(sdPath, FILENAME_SD);
        //Log.d(LOG_TAG, "sdFile:"+String.valueOf(sdFile));
        try {
            // открываем поток для чтения
            //InputStreamReader isr = new InputStreamReader(sdFile, "windows-1251");
            //BufferedReader br = new BufferedReader(isr);
            FileInputStream fstream1 = new FileInputStream(filePath);
            DataInputStream in = new DataInputStream(fstream1);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "windows-1251"));
            //BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            // читаем содержимое


            while ((str = br.readLine()) != null) {
                if (row != 0) {
                    if (str.equals("-") || str.equals("+") || str.contains("##time") || str.contains("##theme")) {
                        Negative_row = Positive_row = question_row = 0;
                        if (!question.equals("")) {
                            //Log.d(LOG_TAG, "!Question_is:" + question);
                            Question_is = question;
                            question = "";
                        }
                        if (!right_answer.equals("")) {
                            //Log.d(LOG_TAG, "!Positive_row:" + right_answer);
                            RAnswer = right_answer;
                            NAnswers.add(right_answer);
                            right_answer = "";
                        }
                        if (!negative_answer.equals("")) {
                            //Log.d(LOG_TAG, "!Negative_row:" + negative_answer);
                            NAnswers.add(negative_answer);
                            negative_answer = "";
                        }
                        //Log.d(LOG_TAG, "!Question_is:" + question);
                        //Log.d(LOG_TAG, "!Positive_row:" + right_answer);
                        //Log.d(LOG_TAG, "!Negative_row:" + negative_answer);
                    }
                    if (!str.equals("-") || !str.equals("+") || !str.contains("##time") || !str.contains("##theme")) {
                        //if (!str.equals("-")||!str.equals("+")||!str.contains("##time")) {
                        //Log.d(LOG_TAG,"OR");
                        if (question_row == row) {
                            question += str;
                            //Log.d(LOG_TAG, "!Question_is:" + question);
                            question_row = row + 1;
                        }
                        if (Positive_row == row) {
                            right_answer += str;
                            //Log.d(LOG_TAG, "!Positive_row:" + right_answer);
                            Positive_row = row + 1;
                        }
                        if (Negative_row == row) {
                            negative_answer += str;
                            //Log.d(LOG_TAG, "!Negative_row:" + negative_answer);
                            Negative_row = row + 1;
                        }
                    }

                }
                if (next_title) {
                    Log.d(LOG_TAG, "!TITLE:" + str);
                    next_title = false;
                    setTheme(str);
                }
                if (next_themes) {
                    Log.d(LOG_TAG, "!THEMES:" + str);
                    next_themes = false;
                }
                if (str.equals("###TITLE###")) {
                    next_title = true;
                }
                if (str.equals("###THEMES###")) {
                    next_themes = true;
                }

                if (str.contains("##time")) {
                    //если ##time найден, значит впереди - слудющий вопрос
                    question = negative_answer = right_answer = "";
                    question_row = row + 1;

                }
                if (str.equals("+")) {
                    question = negative_answer = right_answer = "";
                    Positive_row = row + 1;

                }
                if (str.equals("-")) {
                    question = negative_answer = right_answer = "";
                    Negative_row = row + 1;

                }


                if ((str.contains("##") && (!Question_is.equals("")) && (!RAnswer.equals("")))) {
                    sendValues();

                    //Log.d(LOG_TAG, "!question:"+question);
                }


                //if((RAnswer!="")&&(NAnswers.size()>3)&&(Question_is!="")) {
                //DbHelper DB = new DbHelper(activity);
                //SQLiteDatabase db;
                //db = DB.getWritableDatabase();
                //db.insert()
                //int ID_right_answer=NAnswers.indexOf(RAnswer);
                //Question q1 = new Question(1,Question_is, NAnswers.get(0), NAnswers.get(1), NAnswers.get(2), NAnswers.get(3),ID_right_answer);
                //Log.d(LOG_TAG,Question_is);
                //Question q1=new Question("What is JP?","Jalur Pesawat", "Jack sParrow", "Jasa Programmer", "Jasa Programmer");
                //Log.d(LOG_TAG, q1.getANSWER()+q1.getOPT1()+q1.getQUESTION());

                //DB.addQuestion(q1);
                //DB.close();
                //Question_is = RAnswer = "";
                //NAnswers.clear();
                //}
                //Log.d(LOG_TAG, str);
                row++;
            }
            if (br.readLine() == null) {
                if (!question.equals("")) {
                    //Log.d(LOG_TAG, "!Question_is:" + question);
                    Question_is = question;
                    question = "";
                }
                if (!right_answer.equals("")) {
                    //Log.d(LOG_TAG, "!Positive_row:" + right_answer);
                    RAnswer = right_answer;
                    NAnswers.add(right_answer);
                    right_answer = "";
                }
                if (!negative_answer.equals("")) {
                    //Log.d(LOG_TAG, "!Negative_row:" + negative_answer);
                    NAnswers.add(negative_answer);
                    negative_answer = "";
                }
                sendValues();
                Toast.makeText(activity, "Новые вопросы импортированы", Toast.LENGTH_SHORT).show();
                //OpenFileActivity.makeRefresh();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendValues() {

        //Log.d(LOG_TAG, "!Question_is:" + Question_is);
        //Log.d(LOG_TAG, "!Positive_row:" + (NAnswers.indexOf(RAnswer)+1));
        //Log.d(LOG_TAG, "!Negative_row:" + String.valueOf(NAnswers));


        DbHelper DB = new DbHelper(OpenFileActivity.activity);
        int ID_right_answer = -1;
        ID_right_answer = NAnswers.indexOf(RAnswer);
        //NAnswers.size()

        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        Allthemelist = DB.getAllThemes();
        int themeid = -1;
        for (int i = 0; i < Allthemelist.size(); i++) {
            allThemesClass = Allthemelist.get(i);
            if (allThemesClass.getTHEME().equals(theme)) {
                themeid = allThemesClass.getTHEME_id();
            }
        }

        for (int i = 0; NAnswers.size() < 4; i++) {
            NAnswers.add("");
        }
        Question q1 = new Question(themeid, Question_is, NAnswers.get(0), NAnswers.get(1), NAnswers.get(2), NAnswers.get(3), ID_right_answer);
        /*
        Log.d(LOG_TAG,q1.getOPT1());
        Log.d(LOG_TAG,q1.getOPT2());
        Log.d(LOG_TAG,q1.getOPT3());
        Log.d(LOG_TAG,q1.getOPT4());
        Log.d(LOG_TAG, String.valueOf(q1.getANSWER()));
        Log.d(LOG_TAG, String.valueOf(q1.getID()));
*/
        LogFragment.Text += "вопрос:" + q1.getQUESTION() + "\n";
        LogFragment.Text += "ответ0:" + q1.getOPT1() + "\n";
        LogFragment.Text += "ответ1:" + q1.getOPT2() + "\n";
        LogFragment.Text += "ответ2:" + q1.getOPT3() + "\n";
        LogFragment.Text += "ответ3:" + q1.getOPT4() + "\n";
        LogFragment.Text += "верно:" + q1.getANSWER() + "\n";
        LogFragment.Text += "\n";
        DB.addQuestion(q1);


        Question_is = RAnswer = "";
        NAnswers.clear();
    }

    public void setTheme(String theme) {
        this.theme = theme;
        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        DbHelper DB = new DbHelper(activity);
        Allthemelist = DB.getAllThemes();
        boolean found_that_theme = false;
        ArrayList ArrayTheme = new ArrayList();
        for (int i = 0; i < Allthemelist.size(); i++) {
            allThemesClass = Allthemelist.get(i);
            if (allThemesClass.getTHEME().equals(theme)) {
                found_that_theme = true;
                Toast.makeText(activity, "Эта тема уже добавлена", Toast.LENGTH_SHORT).show();
                break;
            }

            //ArrayTheme.add(allThemesClass.getTHEME());
        }
        found_that_theme = false;

        if (found_that_theme == false) {

            //DbHelper DB = new DbHelper(OpenFileActivity.activity);
            //DB.addTheme(new AllThemes(theme).setTHEME(theme));
            //Question q1 = new Question(1,Question_is, NAnswers.get(0), NAnswers.get(1), NAnswers.get(2), NAnswers.get(3),ID_right_answer);
            AllThemes All = new AllThemes(theme);
            DB.addTheme(All);
            Toast.makeText(activity, "Добавлена новая тема", Toast.LENGTH_SHORT).show();

        }
    }
}
