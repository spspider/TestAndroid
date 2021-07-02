package com.example.ttester_paukov;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ttester_paukov.Utils.AllThemes;
import com.example.ttester_paukov.Utils.FileUtils;
import com.example.ttester_paukov.Utils.Question;
import com.example.ttester_paukov.dynamicviewpager.LogFragment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;

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
//            BufferedReader br = new BufferedReader(new InputStreamReader(in, "windows-1251"));
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
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
            //readFileSD_Meteo(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void OpenFileFromReadText(Uri uri){
        FileUtils fileUtils = new FileUtils();
        Log.d(LOG_TAG,uri.getPath());
        String  message = fileUtils.readFile(uri.getPath());
        Log.d(LOG_TAG,message);
    }

    private static String Substring_brakets(String str){
        str = str.substring(str.indexOf(")") + 1);
        while (str.startsWith(" ")) {
            str = str.substring(1);
        }
        return str;
    }
    private void readFileSD_Meteo(String filepath2) {
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
                str = str.substring(str.indexOf("\n") + 1);
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
                    if ((NAnswers.size() > 0 && (!Question_is.equals("")) && (!RAnswer.equals("")))) {
                        System.out.println("\n Question_is:" + Question_is + "\n");
                        System.out.println("\n RAnswer:" + RAnswer + "\n");
                        System.out.println("\n NAnswers:" + NAnswers.toString() + "\n");
                        sendValues();
                    }
                }
            } else {
                System.out.println("\nQuestion:" + str_n_found + "\n");
                Question_is = str_n_found;
                Answers.clear();

            }

            //int n_index = str.indexOf(")");
        }
        //System.out.println("\nEND:" + "\n");
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
                    NAnswers.clear();
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

        Question q1 = new Question(themeid, Question_is, NAnswers.get(0), NAnswers.get(1), NAnswers.get(2), NAnswers.get(3), ID_right_answer,NAnswers);

        Log.d(LOG_TAG,q1.getOPT1());
        Log.d(LOG_TAG,q1.getOPT2());
        Log.d(LOG_TAG,q1.getOPT3());
        Log.d(LOG_TAG,q1.getOPT4());
        Log.d(LOG_TAG, String.valueOf(q1.getANSWER()));
        Log.d(LOG_TAG, String.valueOf(q1.getID()));

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

    public void OpenFileAsManual(Context context, Intent data) {
        String fullerror ="";
        String  actualfilepath="";
        FileUtils fileUtils = new FileUtils();

        try {
            Uri imageuri = data.getData();
            InputStream stream = null;
            String tempID= "", id ="";
            Uri uri = data.getData();
            Log.e(LOG_TAG, "file auth is "+uri.getAuthority());
            fullerror = fullerror +"file auth is "+uri.getAuthority();
            if (imageuri.getAuthority().equals("media")){
                tempID =   imageuri.toString();
                tempID = tempID.substring(tempID.lastIndexOf("/")+1);
                id = tempID;
                Uri contenturi = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String selector = MediaStore.Images.Media._ID+"=?";
                actualfilepath = fileUtils.getColunmData(context,contenturi, selector, new String[]{id}  );
            }else if (imageuri.getAuthority().equals("com.android.providers.media.documents")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    tempID = DocumentsContract.getDocumentId(imageuri);
                }
                String[] split = tempID.split(":");
                String type = split[0];
                id = split[1];
                Uri contenturi = null;
                if (type.equals("image")){
                    contenturi = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }else if (type.equals("video")){
                    contenturi = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }else if (type.equals("audio")){
                    contenturi = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selector = "_id=?";
                actualfilepath = fileUtils.getColunmData( context,contenturi, selector, new String[]{id}  );
            } else if (imageuri.getAuthority().equals("com.android.providers.downloads.documents")){
                tempID =   imageuri.toString();
                tempID = tempID.substring(tempID.lastIndexOf("/")+1);
                id = tempID;
                Uri contenturi = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                // String selector = MediaStore.Images.Media._ID+"=?";
                actualfilepath = fileUtils.getColunmData( context,contenturi, null, null  );
            }else if (imageuri.getAuthority().equals("com.android.externalstorage.documents")){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    tempID = DocumentsContract.getDocumentId(imageuri);
                }
                String[] split = tempID.split(":");
                String type = split[0];
                id = split[1];
                Uri contenturi = null;
                if (type.equals("primary")){
                    actualfilepath=  Environment.getExternalStorageDirectory()+"/"+id;
                }
            }
            File myFile = new File(actualfilepath);
            // MessageDialog dialog = new MessageDialog(Home.this, " file details --"+actualfilepath+"\n---"+ uri.getPath() );
            // dialog.displayMessageShow();
            String temppath =  uri.getPath();
            if (temppath.contains("//")){
                temppath = temppath.substring(temppath.indexOf("//")+1);
            }
            Log.e(LOG_TAG, " temppath is "+ temppath);
            fullerror = fullerror +"\n"+" file details -  "+actualfilepath+"\n --"+ uri.getPath()+"\n--"+temppath;
            if ( actualfilepath.equals("") || actualfilepath.equals(" ")) {
                myFile = new File(temppath);
            }else {
                myFile = new File(actualfilepath);
            }
            //File file = new File(actualfilepath);
            //Log.e(TAG, " actual file path is "+ actualfilepath + "  name ---"+ file.getName());
//                    File myFile = new File(actualfilepath);
            Log.e(LOG_TAG, " myfile is "+ myFile.getAbsolutePath());
            fileUtils.readfile(myFile);
            // lyf path  - /storage/emulated/0/kolektap/04-06-2018_Admin_1528088466207_file.xls
        } catch (Exception e) {
            Log.e(LOG_TAG, " read errro "+ e.toString());
        }
        //------------  /document/primary:kolektap/30-05-2018_Admin_1527671367030_file.xls
    }


    }

