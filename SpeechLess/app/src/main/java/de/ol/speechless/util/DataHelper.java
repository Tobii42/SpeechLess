package de.ol.speechless.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import de.ol.speechless.data.SpeechDataContract;
import de.ol.speechless.data.SpeechDatabaseHelper;
import de.ol.speechless.model.SpeechItem;

/**
 * Created by Tobias on 01.03.14.
 */
public class DataHelper {

    // The list of all speech-items
    private static ArrayList<SpeechItem> speechItemList;

    /**
     * Returns the list of all speech-items
     * @return
     */
    public static ArrayList<SpeechItem> getSpeechItemList(Context context) {
        if(speechItemList == null) {
            speechItemList = getDatabaseAsList(context);
            if(speechItemList == null) {
                // Maybe an error while reading from database
                speechItemList = new ArrayList<SpeechItem>();
            }
        }
         return speechItemList;
    }

    /**
     * Adds a single speech-item to the list
     * @param speechItem
     */
    public static void addItemToList(SpeechItem speechItem, Context context) {
        getSpeechItemList(context).add(speechItem);
        addSpeechItemToDatabase(speechItem, context);
    }

    /**
     * Returns a single speech-item
     * @param id
     * @return
     */
    public static SpeechItem getItem(int id, Context context) {
        return getSpeechItemList(context).get(id);
    }

    /**
     * Returns the directory where to save audio-files
     * @return
     */
    public static File getDirectoryToSaveAudio() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
    }

    /**
     * Returns the directory where to save image-files
     * @return
     */
    public static File getDirectoryToSaveImage() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    /**
     * Returns the path where the next audio-file has to be saved
     * @return
     */
    public static String getNextAudioFileName() {
        Calendar c = Calendar.getInstance();
        String date = c.getTime().toString();
        String dir = getDirectoryToSaveAudio().getAbsolutePath() + "/SpeechLess";
        // Create folders if necessary
        File file = new File(dir);
        file.mkdirs();
        return dir + "/sl_" + date + ".3gp";
    }

    /**
     * Returns the path where the next image-file has to be saved
     * @return
     */
    public static String getNextImageFileName() {
        Calendar c = Calendar.getInstance();
        String date = c.getTime().toString();
        String dir = getDirectoryToSaveImage().getAbsolutePath() + "/SpeechLess";
        // Create folders if necessary
        File file = new File(dir);
        file.mkdirs();
        return dir + "/sl_" + date + ".png";
    }

    /**
     * Returns the number of speech-items in the list
     * @return
     */
    public static int getNumberOfEntries(Context context) {
        return getSpeechItemList(context).size();
    }

    /**
     * Saves an image as png and returns the Uri
     * @param bitmap
     * @return Uri from the saved image
     */
    public static Uri saveImage(Bitmap bitmap) {

        FileOutputStream out = null;
        String path = getNextImageFileName();

        try {
            out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 99, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                out.close();
            } catch(Throwable ignore) {}
        }

        File file = new File(path);
        Uri uri = null;
        if(file.exists())
            uri = Uri.fromFile(file);

        return uri;
    }

    public static Drawable getImageFromFile(Context context, Uri uri) {
        Drawable drawable = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            drawable = Drawable.createFromStream(inputStream, uri.toString() );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     * Saves the list in a SQLite-database so that it can be accessed even if the app was closed
     */
    private static void addSpeechItemToDatabase(SpeechItem item, Context context) {
        SpeechDatabaseHelper databaseHelper = new SpeechDatabaseHelper(context);

        // Gets the data repository in write mode
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        //values.put(SpeechDataContract.SpeechEntry._ID, getNumberOfEntries()); // Should auto_increment
        values.put(SpeechDataContract.SpeechEntry.COLUMN_NAME_COL_IMAGE_URI, item.getPictureUri().toString());
        values.put(SpeechDataContract.SpeechEntry.COLUMN_NAME_AUDIO_URI, item.getAudioUri().toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                SpeechDataContract.SpeechEntry.TABLE_NAME,
                null,
                values);
    }

    /**
     * Returns the items in the database as an ArrayList
     */
    private static ArrayList<SpeechItem> getDatabaseAsList(Context context) {
        SpeechDatabaseHelper databaseHelper = new SpeechDatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                SpeechDataContract.SpeechEntry._ID,
                SpeechDataContract.SpeechEntry.COLUMN_NAME_COL_IMAGE_URI,
                SpeechDataContract.SpeechEntry.COLUMN_NAME_AUDIO_URI
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                SpeechDataContract.SpeechEntry._ID + " DESC";

        String selection = "";
        String[] selectionArgs = {""};

        Cursor cursor = db.query(
                SpeechDataContract.SpeechEntry.TABLE_NAME, // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // Go through the cursor and get the values into an ArrayList
        ArrayList<SpeechItem> speechItems = new ArrayList<SpeechItem>();

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            int imageColumn = cursor.getColumnIndex(SpeechDataContract.SpeechEntry.COLUMN_NAME_COL_IMAGE_URI);
            Uri imageUri = Uri.fromFile(new File(cursor.getString(imageColumn)));

            int audioColumn = cursor.getColumnIndex(SpeechDataContract.SpeechEntry.COLUMN_NAME_AUDIO_URI);
            Uri audioUri = Uri.fromFile(new File(cursor.getString(audioColumn)));

            SpeechItem newItem = new SpeechItem(imageUri, audioUri);
            speechItems.add(newItem);

            cursor.moveToNext();
        }

        return speechItems;
    }
}
