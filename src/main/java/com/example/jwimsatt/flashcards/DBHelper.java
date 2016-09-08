package com.example.jwimsatt.flashcards;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Creates SQLite database and queries
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "FlashcardDB.db";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SIDE1 = "side1";
    public static final String COLUMN_SIDE2 = "side2";
    public static final String COLUMN_CARD_NUMBER = "card_number";
    public static final int database_version = 1;

    public DBHelper(Context context){

        super(context, DATABASE_NAME, null, database_version);
        Log.d("Database operations", "Database created");
    }

    //Creates table in database
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE sets " +
                "(" + COLUMN_ID + " INTEGER primary key, " + COLUMN_TITLE + " TEXT, " +
                COLUMN_SIDE1 + " TEXT, " + COLUMN_SIDE2 + " TEXT, " + COLUMN_CARD_NUMBER + " INTEGER );");

        Log.d("Database operations", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS sets");
        //onCreate(db);
    }

    //Inserts into sets table
    public boolean insertFlashcard(String title, String side1, String side2, int card_number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("side1", side1);
        contentValues.put("side2", side2);
        contentValues.put("card_number", card_number);
        db.insert("sets", null, contentValues);
        return true;
    }

    //Updates card in set table
    public boolean updateFlashcard(Integer id, String title, String side1, String side2, int card_number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("side1", side1);
        contentValues.put("side2", side2);
        contentValues.put("card_number", card_number);
        db.update("sets", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    //Deletes one card in set table
    //******* Will this be needed? *******
    public Integer deleteFlashcard(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("sets", "id = ? ",
                new String[] {Integer.toString(id)} );
    }

    //Deletes flashcard set based on title
    public Integer deleteFlashcardSet(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("sets", "title = ?", new String[] {title} );
    }
    //Get all titles
    public ArrayList<String> getAllTitles(){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT title FROM sets", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_TITLE)));
            res.moveToNext();
        }
        Log.d("All titles", "Titles returned");
        return array_list;
    }

    //Checks if title is already in use
    public boolean checkTitle(String title){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT title FROM sets WHERE title =" + '"' + title +'"' +";" , null);
        int count = res.getCount();

        if(count > 0){
            return true;
        }

        return false;
    }

    //Get flashcard set
    public ArrayList<Card> getFlashcardSet(String title){
        Log.d("Get cards", title);
        ArrayList<Card> array_list = new ArrayList<Card>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT side1, side2, card_number, id FROM sets WHERE title = "+ '"'+title +'"' +";", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Card card = new Card();
            card.setNumber(Integer.parseInt(res.getString(2)));
            card.setSide1(res.getString(0));
            card.setSide2(res.getString(1));
            card.setId(Integer.parseInt(res.getString(3)));
            array_list.add(card);
            //array_list.add(res.getString(res.getColumnIndex(COLUMN_CARD_NUMBER)));
            res.moveToNext();
        }
        return array_list;
    }
}
