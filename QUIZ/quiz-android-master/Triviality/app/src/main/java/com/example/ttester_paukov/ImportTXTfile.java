package com.example.ttester_paukov;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;
import static com.example.ttester_paukov.OpenFileActivity.activity;

/**
 * Created by sp_1 on 30.03.2017.
 */

public class ImportTXTfile {
    boolean next_title=false;
    boolean next_themes=false;
    String theme="", question ="",negative_answer="",right_answer="";
    String Question_is="";
    ArrayList<String> NAnswers=new ArrayList();
    String RAnswer="";
    int row=0;
    int question_row=0,Positive_row=0,Negative_row=0;
    boolean End_of_all_rows=false;

    void readFileSD(Activity activity,String filePath) {


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
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"windows-1251"));
            //BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            // читаем содержимое


            while ((str = br.readLine()) != null) {
                if (row!=0){
                    if (str.equals("-")||str.equals("+")||str.contains("##time")||str.contains("##theme")) {
                        Negative_row=Positive_row=question_row=0;
                        if (question!=""){
                            //Log.d(LOG_TAG, "!Question_is:" + question);
                            Question_is=question;
                            question="";
                        }
                        if (right_answer!=""){
                            //Log.d(LOG_TAG, "!Positive_row:" + right_answer);
                            RAnswer=right_answer;
                            NAnswers.add(right_answer);
                            right_answer="";
                        }
                        if (negative_answer!=""){
                            //Log.d(LOG_TAG, "!Negative_row:" + negative_answer);
                            NAnswers.add(negative_answer);
                            negative_answer="";
                        }
                        //Log.d(LOG_TAG, "!Question_is:" + question);
                        //Log.d(LOG_TAG, "!Positive_row:" + right_answer);
                        //Log.d(LOG_TAG, "!Negative_row:" + negative_answer);
                    }
                    if (!str.equals("-")||!str.equals("+")||!str.contains("##time")||!str.contains("##theme")) {
                        //if (!str.equals("-")||!str.equals("+")||!str.contains("##time")) {
                        //Log.d(LOG_TAG,"OR");
                        if (question_row == row) {
                            question += str;
                            //Log.d(LOG_TAG, "!Question_is:" + question);
                            question_row=row+1;
                        }
                        if (Positive_row == row) {
                            right_answer += str;
                            //Log.d(LOG_TAG, "!Positive_row:" + right_answer);
                            Positive_row=row+1;
                        }
                        if (Negative_row == row) {
                            negative_answer += str;
                            //Log.d(LOG_TAG, "!Negative_row:" + negative_answer);
                            Negative_row=row+1;
                        }
                    }

                }
                if (next_title){Log.d(LOG_TAG, "!TITLE:"+str);next_title=false;setTheme(str);}
                if (next_themes){Log.d(LOG_TAG, "!THEMES:"+str);next_themes=false;}
                if(str.equals("###TITLE###")){
                    next_title=true;
                }
                if(str.equals("###THEMES###")){
                    next_themes=true;
                }

                if(str.contains("##time")){
                    //если ##time найден, значит впереди - слудющий вопрос
                    question =negative_answer=right_answer="";
                    question_row = row+1;

                }
                if(str.equals("+")){
                    question =negative_answer=right_answer="";
                    Positive_row=row+1;

                }
                if(str.equals("-")){
                    question =negative_answer=right_answer="";
                    Negative_row=row+1;

                }



                if((str.contains("##")&&(Question_is!="")&&(RAnswer!=""))){
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
            if (br.readLine() == null){
                if (question!=""){
                    //Log.d(LOG_TAG, "!Question_is:" + question);
                    Question_is=question;
                    question="";
                }
                if (right_answer!=""){
                    //Log.d(LOG_TAG, "!Positive_row:" + right_answer);
                    RAnswer=right_answer;
                    NAnswers.add(right_answer);
                    right_answer="";
                }
                if (negative_answer!=""){
                    //Log.d(LOG_TAG, "!Negative_row:" + negative_answer);
                    NAnswers.add(negative_answer);
                    negative_answer="";
                }
                sendValues();
                Toast.makeText(activity,"Новые вопросы импортированы",Toast.LENGTH_SHORT).show();
                OpenFileActivity.makeRefresh();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void sendValues(){

            //Log.d(LOG_TAG, "!Question_is:" + Question_is);
            //Log.d(LOG_TAG, "!Positive_row:" + (NAnswers.indexOf(RAnswer)+1));
            //Log.d(LOG_TAG, "!Negative_row:" + String.valueOf(NAnswers));


        DbHelper DB = new DbHelper(OpenFileActivity.activity);
        int ID_right_answer=-1;
        ID_right_answer= NAnswers.indexOf(RAnswer);
        //NAnswers.size()
        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        Allthemelist = DB.getAllThemes();
        int themeid=-1;
        for(int i = 0;i<Allthemelist.size();i++){
            allThemesClass = Allthemelist.get(i);
            if(allThemesClass.getTHEME().equals(theme)){
                themeid = allThemesClass.getTHEME_id();
            }
        }
        for (int i=0;NAnswers.size()<4;i++){ NAnswers.add("");}
        Question q1 = new Question(themeid,Question_is, NAnswers.get(0), NAnswers.get(1), NAnswers.get(2), NAnswers.get(3),ID_right_answer);
        DB.addQuestion(q1);
        //db.insert()


        Question_is = RAnswer = "";
            NAnswers.clear();

            //Log.d(LOG_TAG, "!Question_is:" + Question_is);
            //Log.d(LOG_TAG, "!Positive_row:" + right_answer);
            //Log.d(LOG_TAG, "!Negative_row:" + negative_answer);
    }

    public void setTheme(String theme) {
        this.theme = theme;
        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        DbHelper DB=new DbHelper(activity);
        Allthemelist = DB.getAllThemes();
        boolean found_that_theme=false;
        ArrayList ArrayTheme=new ArrayList();
        for(int i = 0;i<Allthemelist.size();i++){
            allThemesClass = Allthemelist.get(i);
            if(allThemesClass.getTHEME().equals(theme))
            {
                found_that_theme = true;
                Toast.makeText(activity,"Эта тема уже добавлена",Toast.LENGTH_SHORT).show();
                break;
            }

            //ArrayTheme.add(allThemesClass.getTHEME());
        }
        found_that_theme = false;

        if(found_that_theme==false) {

            //DbHelper DB = new DbHelper(OpenFileActivity.activity);
            //DB.addTheme(new AllThemes(theme).setTHEME(theme));
            //Question q1 = new Question(1,Question_is, NAnswers.get(0), NAnswers.get(1), NAnswers.get(2), NAnswers.get(3),ID_right_answer);
            AllThemes All = new AllThemes(theme);
            DB.addTheme(All);
            Toast.makeText(activity,"Добавлена новая тема",Toast.LENGTH_SHORT).show();

        }

    }
}
