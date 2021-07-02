package com.example.ttester_paukov;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ttester_paukov.ExternalDB.DatabaseOpener;
import com.example.ttester_paukov.ExternalDB_worked.MainActivity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sp_1 on 27.03.2017.
 */

public class OpenFileActivity extends Activity {


    private static final int CHOOSE_FILE_REQUESTCODE = 101;
    public static final String LOG_TAG = "myLogs";
    public static Activity activity;
    static ListView lvMain =null;
    private static final int FILE_SELECT_CODE = 0;

    final String FILENAME = "file";

    public static final String DIR_SD = "MyFiles";
    public static final String FILENAME_SD = "тест короткий.txt";

        /** Called when the activity is first created. */

        private static final int MENU_ITEM_START = 1;
    private static final int MENU_ITEM_DELETE = 2;
    private static final int MENU_ITEM_IMPORT = 3;
    private static final int MENU_ITEM_EXTERNALDATABASE = 4;
    private static final int MENU_ITEM_ABOUT = 5;
    //private static final int MENU_ITEM_DELETE = 2;
    //Activity activity;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_START, Menu.NONE, "Начать тестирование");
        menu.add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "Удалить тесты");
        menu.add(Menu.NONE, MENU_ITEM_IMPORT, Menu.NONE, "Импортировать тесты");
        menu.add(Menu.NONE, MENU_ITEM_EXTERNALDATABASE, Menu.NONE, "внешняя база");
        menu.add(Menu.NONE, MENU_ITEM_ABOUT, Menu.NONE, "о приложении");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_START:

                return true;
            case MENU_ITEM_DELETE:
                deleteCheckedItems();
                //sba.
                return true;
            case MENU_ITEM_IMPORT:
            openFile();
                return true;
            case MENU_ITEM_EXTERNALDATABASE:
                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);
                return true;
            case MENU_ITEM_ABOUT:
                Intent intent1 = new Intent(this, Info_screen.class);
                startActivity(intent1);
                return true;
            default:
                return false;
        }
    }

    private void deleteCheckedItems() {
        SparseBooleanArray sba = lvMain.getCheckedItemPositions();
        DbHelper DB = new DbHelper(this);
        ArrayList<Integer> pagesToDelete = new ArrayList<>();
        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        DbHelper db = new DbHelper(activity);
        Allthemelist = db.getAllThemes();
        ArrayList<Integer> AllThemes_id = new ArrayList<>();
        for (int i = 0; i < Allthemelist.size(); i++) {
            allThemesClass = Allthemelist.get(i);
            AllThemes_id.add(allThemesClass.getTHEME_id());
        }
        for (int i = 0; i < sba.size(); ++i) {
            int key = sba.keyAt(i);
            if (sba.get(key)) {

                pagesToDelete.add(AllThemes_id.get(key));
               // System.out.print(lv.getItemAtPosition(key) + "|");
            }
        }
        DB.deleteTables(pagesToDelete);
    }

    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.openfile);
            activity=this;

        lvMain = (ListView) activity.findViewById(R.id.listTHEMES);
            //AllTheme.

        makeDataBaseCopy();

            makeRefresh();

            //DbHelper db=new DbHelper(this);
           // quesList=db.getAllQuestions();
        }

    private void makeDataBaseCopy() {
        //CopyDataBase copydatabase = new CopyDataBase(this);
        //copydatabase.openDatabase();
        //boolean copyed= copydatabase.copyDatabaseFrom``````````````````````Assets(this,"db",true);
        //Log.d(LOG_TAG, String.valueOf(copyed));
        //ContextWrapper cw =new ContextWrapper(this);
        //String DATABASE_PATH =cw.getFilesDir().getAbsolutePath()+ "/databases/"; //edited to databases
        //DBQuery db = new DBQuery(this);
        try {
            DatabaseOpener db = new DatabaseOpener(this);
            db.openDataBase();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        //CopyDataBase.movedb(this, getDatabasePath(getDbName()), new File(Environment.getExternalStorageDirectory(), getDatabaseBackupPath()));
        //copydatabase.movedb(getDatabasePath("db"),new File((Environment.getExternalStorageDirectory()),DATABASE_PATH+"db"));
    }


    public static void makeRefresh() {
        List<Question> quesList;
        Question currentQ;
        ArrayList<AllThemes> Allthemelist;
        AllThemes allThemesClass;
        DbHelper db = new DbHelper(activity);
        Allthemelist = db.getAllThemes();

        ArrayList ArrayTheme = new ArrayList();
        //ArrayList ArrayRowCount=new ArrayList();
        ArrayList<Integer> AllThemes_id = new ArrayList<>();
        for (int i = 0; i < Allthemelist.size(); i++) {
            allThemesClass = Allthemelist.get(i);
            int row_count = 0;
            quesList = db.getAllQuestions(-1);
            AllThemes_id.add(allThemesClass.getTHEME_id());
            for (int qid = 0; qid < quesList.size(); qid++) {
                currentQ = quesList.get(qid);

                if (allThemesClass.getTHEME_id() == currentQ.getTHEMEID()) {
                    row_count++;//получаем кол-во вопросов данной теме
                }
                //ArrayRowCount.add(allThemesClass.getRowCount());
            }
            ArrayTheme.add(allThemesClass.getTHEME() + " :" + row_count);
            //ArrayRowCount.add(allThemesClass.getRowCount());
        }

        //DbHelper db=new DbHelper(ac);

        //quesList.
        //quesList.


        lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvMain.setItemsCanFocus(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_multiple_choice, ArrayTheme);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                if (lvMain.isItemChecked(pos)){lvMain.setItemChecked(pos,false);}else{lvMain.setItemChecked(pos,true);}
                //Log.v(LOG_TAG,"long clicked pos: " + pos);
                //lvMain.setSelection();

                return true;
            }
        });
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(activity,QuizActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ThemeIndexList",(int)id);
                intent.putExtras(bundle);
                if (lvMain.isItemChecked(position)){lvMain.setItemChecked(position,false);}else{lvMain.setItemChecked(position,true);}
                activity.startActivity(intent);
            }
        });
    }

    public void openFile() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = null;
                    try {
                        path = FileUtils.getPath(this, uri);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d(LOG_TAG, "File Path: " + path);
                    ImportTXTfile ITXT = new ImportTXTfile();
                    ITXT.readFileSD(this, String.valueOf(path));

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





}
