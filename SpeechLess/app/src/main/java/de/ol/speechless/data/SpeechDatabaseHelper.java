package de.ol.speechless.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tobias on 02.03.14.
 */
public class SpeechDatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Speech.db";

    // SQL-Statements
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SpeechDataContract.SpeechEntry.TABLE_NAME + " (" +
                    SpeechDataContract.SpeechEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    SpeechDataContract.SpeechEntry.COLUMN_NAME_COL_IMAGE_URI + TEXT_TYPE + COMMA_SEP +
                    SpeechDataContract.SpeechEntry.COLUMN_NAME_AUDIO_URI + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SpeechDataContract.SpeechEntry.TABLE_NAME;

    public SpeechDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing for now
    }
}
