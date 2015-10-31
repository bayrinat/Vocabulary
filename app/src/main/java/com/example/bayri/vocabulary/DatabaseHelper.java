package com.example.bayri.vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

/**
 * Created by bayri on 31.10.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "vocabulary.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_MAIN = "main";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ENGLISH = "english";
    private static final String COLUMN_RUSSIAN = "russian";
    private static final String COLUMN_USED = "used";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы "main"
        db.execSQL("CREATE TABLE main (" +
                "_id integer primary key autoincrement, " +
                "english varchar(200), " +
                "russian varchar(200), " +
                "used int);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insertRecord(Record record) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ENGLISH, record.getEnglish());
        cv.put(COLUMN_RUSSIAN, record.getRussian());
        cv.put(COLUMN_USED, record.getUsed());
        return getWritableDatabase().insert(TABLE_MAIN, null, cv);
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
            if( !moveToFirst())
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
            return record;
        }
    }
}
