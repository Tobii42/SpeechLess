package de.ol.speechless.util;

import android.content.Context;
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
    public static ArrayList<SpeechItem> getSpeechItemList() {
        if(speechItemList == null) {
            // Later we have to load this from a file or so
            speechItemList = new ArrayList<SpeechItem>();
        }
         return speechItemList;
    }

    /**
     * Adds a single speech-item to the list
     * @param speechItem
     */
    public static void addItemToList(SpeechItem speechItem) {
        getSpeechItemList().add(speechItem);
    }

    /**
     * Returns a single speech-item
     * @param id
     * @return
     */
    public static SpeechItem getItem(int id) {
        return getSpeechItemList().get(id);
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
    public static int getNumberOfEntries() {
        return getSpeechItemList().size();
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

        }
        return drawable;
    }

    /**
     * Saves the list in a SQLite-database so that it can be accessed even if the app was closed
     */
    private static void saveList() {

    }
}
