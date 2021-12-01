package com.example.thoitiet_tongcaocuong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {
    static private final String DB_NAME="HistoryList.db";
    static private final String DV_STRING="string";
    static private final String TB_HISTORYLIST="historyList";


    public SQLHelper(Context context){
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE historylist("
                +"string TEXT NOT NULL PRIMARY KEY"
                +")";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion){
            String strQuery ="DROP TABLE IF EXISTS "+DB_NAME;
            db.execSQL(strQuery);
            onCreate(db);
        }
    }

    public void onAddList(String string){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("string", string );
        sqLiteDatabase.insert(TB_HISTORYLIST,null,contentValues);
        sqLiteDatabase.close();
        contentValues.clear();

    }

    public void onDeleteAll(int string){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TB_HISTORYLIST,null,null);

    }
//    public int onDelete(int id){
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        return sqLiteDatabase.delete(TB_HISTORYLIST,"id=?",
//                new String[]{String.valueOf(id)});
//    }
    public List<SearchHistoryList> onGetList(){
        List<SearchHistoryList> searchHistoryLists = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(false,TB_HISTORYLIST,
                null,
                null,
                null,
                null,
                null,
                null,
                null

                );

        while (cursor.moveToNext()){
            String string = cursor.getString(cursor.getColumnIndex(DV_STRING));
            SearchHistoryList searchHistoryList=new SearchHistoryList(string);
            searchHistoryLists.add(searchHistoryList);

        }
        return searchHistoryLists;
    }


}
