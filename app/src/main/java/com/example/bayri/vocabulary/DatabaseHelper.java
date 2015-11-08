package com.example.bayri.vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Works with database
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "vocabulary.sqlite";
    private static final int VERSION = 1;

    public static final String TABLE_MAIN = "main";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ENGLISH = "english";
    public static final String COLUMN_RUSSIAN = "russian";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_USED = "used";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create "main"
        db.execSQL("CREATE TABLE main (" +
                "_id integer primary key autoincrement, " +
                "english varchar(200), " +
                "russian varchar(200), " +
                "category int, " +
                "used int);");
        // Create "categories"
        db.execSQL("CREATE TABLE categories (" +
                "_id integer primary key autoincrement, " +
                "category varchar(200));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insertCategory(String name) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CATEGORY, name);
        return getWritableDatabase().insert(TABLE_CATEGORIES, null, cv);
    }

    public String getCategory(int id) {
        id++;
        String condition = "_id=\'" + id + "\'";
        Cursor wrapped = getReadableDatabase().query(TABLE_CATEGORIES,
                null, condition, null, null, null, null, null);
        wrapped.moveToFirst();
        return wrapped.getString(wrapped.getColumnIndex(COLUMN_CATEGORY));
    }

    public boolean isExsistWord(String name) {
        String condition = "english = \"" + name + "\"";
        Cursor wrapped = getReadableDatabase().query(TABLE_MAIN,
                null, condition, null, null, null, null, null);
        return wrapped.getCount() > 0;
    }

    public long isExsistCategory(String name) {
        String condition = "category = \"" + name + "\"";
        Cursor wrapped = getWritableDatabase().query(TABLE_CATEGORIES,
                null, condition, null, null, null, null, null);
        if(wrapped.getCount() > 0) {
            wrapped.moveToFirst();
            return wrapped.getInt(wrapped.getColumnIndex(COLUMN_CATEGORY));
        } else {
            return insertCategory(name);
        }
    }

    public long insertRecord(Record record) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ENGLISH, record.getEnglish());
        cv.put(COLUMN_RUSSIAN, record.getRussian());
        cv.put(COLUMN_CATEGORY, record.getCategory());
        cv.put(COLUMN_USED, record.getUsed());
        return getWritableDatabase().insert(TABLE_MAIN, null, cv);
    }

    public void insertFromFile(ContentValues cv) {
        String english_word = (String) cv.get(COLUMN_ENGLISH);
        if(!isExsistWord(english_word)) {
            String category_word = (String) cv.get(COLUMN_CATEGORY);
            long category = isExsistCategory(category_word);
            String russian_word = (String) cv.get(COLUMN_RUSSIAN);
            Record record = new Record(english_word, russian_word, (int)category);
            insertRecord(record);
        }
    }

    public RecordCursor queryRecord(int id) {
        Cursor wrapped = getWritableDatabase().query(TABLE_MAIN,
                null, "_id=" + id, null, null, null, null, null);
        return new RecordCursor(wrapped);
    }

    public int getRecordsCount() {
        Cursor wrapped = getReadableDatabase().query(TABLE_MAIN,
                null, null, null, null, null, null);
        return wrapped.getCount();
    }

    public ArrayList<String> getCategories() {
        Cursor cursor = getWritableDatabase().query(TABLE_CATEGORIES,
                null, null, null, null, null, null);
        ArrayList<String> categories = new ArrayList<String>(){};
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String category = cursor.getString(cursor
                        .getColumnIndex(COLUMN_CATEGORY));
                categories.add(category);
                cursor.moveToNext();
            }
        }
        return categories;
    }

    /**
     *
     */
    public static class RecordCursor extends CursorWrapper implements Serializable {

        public RecordCursor(Cursor c) {
            super(c);
        }

        /**
         * Возвращает объект Run, представляющий текущую строку,
         * или null, если текущая строка недействительна.
         */
        public Record getRecord() {
            if(!moveToFirst())
                return null;
            if (isBeforeFirst() || isAfterLast())
               return null;
            Record record = new Record();
            long id = getLong(getColumnIndex(COLUMN_ID));
            record.setId(id);
            String english = getString(getColumnIndex(COLUMN_ENGLISH));
            record.setEnglish(english);
            String russian = getString(getColumnIndex(COLUMN_RUSSIAN));
            record.setRussian(russian);
            int category = getInt(getColumnIndex(COLUMN_CATEGORY));
            record.setCategory(category);
            return record;
        }
    }
}
