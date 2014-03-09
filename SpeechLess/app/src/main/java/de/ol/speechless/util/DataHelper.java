package de.ol.speechless.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
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
    public static File getDirectoryToSaveAudio(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
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
    public static File getNextAudioFile(Context context) {
        Calendar c = Calendar.getInstance();
        String date = c.getTime().toString();
        String dir = getDirectoryToSaveAudio(context).getAbsolutePath();
        // Create folders if necessary
        File file = new File(dir);
        file.mkdirs();
        return new File(dir, "/sl_" + date + ".3gp");
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

    /**
     * Returns the image in a lower quality
     * @param context
     * @param uri
     * @param width The width of the image the method returns (e.g. 300)
     * @return
     */
    public static Drawable getImageFromFile(Context context, Uri uri, int width) {
        Drawable drawable = null;
        try {
            // Determining the original size of the image:
            BitmapFactory.Options optionsSizeOnly = new BitmapFactory.Options();
            optionsSizeOnly.inJustDecodeBounds = true;
            InputStream inputStreamSizeOnly = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(inputStreamSizeOnly, null, optionsSizeOnly);
            int widthOriginal = optionsSizeOnly.outWidth;

            // Determining the scale ratio.
            // Note, it's just an example, you should use more sophisticated algorithm:
            int ratio = widthOriginal / width; // widthView is supposed to be known

            // Now loading the scaled image:
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = ratio;

            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            drawable = new BitmapDrawable(context.getResources(), bitmap);
            inputStream.close();
        } catch (Exception e) {
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

        db.close();
        databaseHelper.close();
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
        String sortOrder = SpeechDataContract.SpeechEntry._ID + " DESC";

        String selection = "";
        String[] selectionArgs = {""};

        Cursor cursor = db.query(
                SpeechDataContract.SpeechEntry.TABLE_NAME,  // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // The sort order
        );

        // Go through the cursor and get the values into an ArrayList
        ArrayList<SpeechItem> speechItems = new ArrayList<SpeechItem>();

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            int imageColumn = cursor.getColumnIndex(SpeechDataContract.SpeechEntry.COLUMN_NAME_COL_IMAGE_URI);
            Uri imageUri = Uri.parse(cursor.getString(imageColumn));

            int audioColumn = cursor.getColumnIndex(SpeechDataContract.SpeechEntry.COLUMN_NAME_AUDIO_URI);
            Uri audioUri = Uri.parse(cursor.getString(audioColumn));

            SpeechItem newItem = new SpeechItem(imageUri, audioUri);
            speechItems.add(newItem);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        databaseHelper.close();

        return speechItems;
    }
}
