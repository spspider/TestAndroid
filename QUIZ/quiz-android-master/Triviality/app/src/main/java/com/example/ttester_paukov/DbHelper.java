package com.example.ttester_paukov;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.ttester_paukov.OpenFileActivity.LOG_TAG;

public class DbHelper extends SQLiteOpenHelper{

	private static final String ANSWERS="answers_table";
	private static final String ID_THEME="id_theme";
	private static final String ID_QUESTION="id_question";
	private static final String ID_RIGHT_ANSWERS="id_right_answer";
	private static final String ID_YOUR_ANSWERS="id_your_answer";

	private static final int DATABASE_VERSION = 1;
	// Database Name
	public static final String DATABASE_NAME = "db";
	// tasks table name
	public static final String TABLE_QUEST = "quest";
	private static final String TABLE_THEME = "THEME_TABLE";
	private static final String KEY_NAME_THEME = "theme_name";
	// tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_THEME="theme";
	private static final String KEY_QUES = "question";
	private static final String KEY_ANSWER = "answer"; //correct option
	private static final String KEY_OPT1= "opt1"; //option a
	private static final String KEY_OPT2= "opt2"; //option b
	private static final String KEY_OPT3= "opt3"; //option c
	private static final String KEY_OPT4= "opt4"; //option c
	private SQLiteDatabase dbase;
	String selectQuery;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	//@Override
	public void onCreate(SQLiteDatabase db) {

		dbase=db;
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+KEY_THEME + " INTEGER, "
				+ KEY_QUES+ " TEXT, "
				+ KEY_ANSWER+ " INTEGER, "
				+KEY_OPT1 +" TEXT, "
				+KEY_OPT2 +" TEXT, "
				+KEY_OPT3+" TEXT, "
				+KEY_OPT4+" TEXT" +
				")";
		db.execSQL(sql);
		Log.d(LOG_TAG,sql);
		//addQuestions();
		//db.close();

		String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_THEME + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_NAME_THEME+ " TEXT" +
				")";
		db.execSQL(sql2);
		Log.d(LOG_TAG,sql2);
		String sql3 = "CREATE TABLE IF NOT EXISTS " + ANSWERS + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ID_THEME+ " INTEGER,"
				+ ID_QUESTION+ " INTEGER,"
				+ ID_YOUR_ANSWERS+ " INTEGER,"
				+ ID_RIGHT_ANSWERS+ " INTEGER"
				+")";
		db.execSQL(sql3);


		Log.d(LOG_TAG,sql3);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
		// Create tables again
		onCreate(db);
	}
	// Adding new question
	public void addTheme(AllThemes theme) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME_THEME, theme.getTHEME());
		// Inserting Row
		Log.d(LOG_TAG, String.valueOf(values));
		db.insert(TABLE_THEME, null, values);
	}
	public void addQuestion(Question quest) {
		//dbase.
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_THEME, quest.getTHEMEID());
		values.put(KEY_QUES, quest.getQUESTION()); 
		values.put(KEY_OPT1, quest.getOPT1());
		values.put(KEY_OPT2, quest.getOPT2());
		values.put(KEY_OPT3, quest.getOPT3());
		values.put(KEY_OPT4, quest.getOPT4());
		values.put(KEY_ANSWER, quest.getANSWER());
		// Inserting Row
		Log.d(LOG_TAG, String.valueOf(values));
		db.insert(TABLE_QUEST, null, values);
	}
	public List<Question> getAllQuestions(Integer QuestionID) {
		List<Question> quesList = new ArrayList<Question>();
		// Select All Query

		if (QuestionID==-1){
			selectQuery = "SELECT  * FROM " + TABLE_QUEST;

		}
		else {
			selectQuery = "SELECT  * FROM " + TABLE_QUEST + " WHERE " + KEY_THEME + " = " + QuestionID;
		}
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Question quest = new Question();
				quest.setID(cursor.getInt(0));
				//quest.se(cursor.getString(1));
				quest.setTHEMEID(cursor.getInt(1));
				quest.setQUESTION(cursor.getString(2));
				quest.setANSWER(cursor.getInt(3));
				quest.setOPT1(cursor.getString(4));
				quest.setOPT2(cursor.getString(5));
				quest.setOPT3(cursor.getString(6));
				quest.setOPT4(cursor.getString(7));
				quesList.add(quest);
			} while (cursor.moveToNext());
		}
		// return quest list
		return quesList;
	}
	public ArrayList<AllThemes> getAllThemes(){
		ArrayList<AllThemes> AllThemes_list = new ArrayList();

		String selectQuery = "SELECT  * FROM " + TABLE_THEME;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do{
			AllThemes themes= new AllThemes();
			themes.setTHEME_id(cursor.getInt(0));
			themes.setTHEME(cursor.getString(1));
			themes.setRowCount(getRowCount(themes.getTHEME()));
			AllThemes_list.add(themes);
			} while (cursor.moveToNext());
		}
		return AllThemes_list;
	}


public int getRowCount(String theme){
/*
	String selectQueryTheme = "SELECT  * FROM " + TABLE_THEME+" WHERE "+KEY_NAME_THEME+" = " + theme.trim();
	dbase=this.getReadableDatabase();
	Cursor cursorTheme = dbase.rawQuery(selectQueryTheme, null);
	int KeyTheme = Integer.parseInt(cursorTheme.getString(0));
*/

	int row_count=0;
	String selectQuery = "SELECT  * FROM " + TABLE_QUEST+" WHERE "+KEY_THEME+" = "+0;
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	row_count=cursor.getCount();
	return row_count;
}

	public int rowcount()
	{
		int row=0;
		selectQuery = "SELECT  * FROM " + TABLE_QUEST;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		row=cursor.getCount();
		return row;
	}


	public void deleteTables(ArrayList<Integer> pagesToDelete) {
		SQLiteDatabase db = this.getWritableDatabase();

		Log.d(LOG_TAG,"key:"+String.valueOf(pagesToDelete));
		for (int i=0;i<pagesToDelete.size();i++) {
			db.execSQL("DELETE FROM " + TABLE_QUEST + " WHERE " + KEY_THEME + "='" + pagesToDelete.get(i) + "'");
		}
		for (int i=0;i<pagesToDelete.size();i++) {
			db.execSQL("DELETE FROM " + TABLE_THEME + " WHERE " + KEY_ID + "='" + pagesToDelete.get(i) + "'");
		}
		db.close();
		OpenFileActivity.makeRefresh();
	}
}
