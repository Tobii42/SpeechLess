package de.ol.speechless.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.ol.speechless.model.SpeechItem;
import de.ol.speechless.util.AudioRecorder;
import de.ol.speechless.util.DataHelper;

public class NewItemActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_PHOTO = 2;
    private static final int SELECT_AUDIO = 3;

    private Drawable image;
    private Uri audio;
    private AudioRecorder audioRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle results from intents
     * e.g. choosing a picture from the gallery or taking a picture with the camera
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap imageBitmap = null;
        Uri selectedAudio = null;

        // Get the image from the camera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
        } else if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            // Get the image from the gallery
            Uri selectedImage = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                imageBitmap = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException fileException) {
                // Do nothing, imageBitmap is and stays null
            }
        } else if(requestCode == SELECT_AUDIO && resultCode == RESULT_OK) {
            // Get audio from audio-picker
            selectedAudio = data.getData();
        }

        if(imageBitmap != null) {
            // We got a picture
            image = new BitmapDrawable(getResources(), imageBitmap); // save for use in list

            // Show the image in the preview
            ImageView preview = (ImageView) findViewById(R.id.imagePreview);
            preview.setImageDrawable(image);
        }

        if(selectedAudio != null) {
            // We got an audio-file-uri
            audio = selectedAudio;
        }

    }

    /**
     * Creates a new item (with picture and sound) and adds it to the ItemList
     *
     * @param view
     */
    public void createNewItem(View view) {
        if(image != null && audio != null) {
            SpeechItem speechItem = new SpeechItem(image, audio);
            DataHelper.addItemToList(speechItem);
        }
    }

    /**
     * Let the user choose a picture from the gallery
     *
     * @param view
     */
    public void choosePicture(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        // Check if there's an app which can handle this intent
        if (photoPickerIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
    }

    /**
     * Let the user take a picture with the camera
     *
     * @param view
     */
    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check if there's an app which can handle this intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Let the user choose an audio file
     *
     * @param view
     */
    public void chooseAudio(View view) {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        // Let him choose with that automatically generated chooser
        startActivityForResult(Intent.createChooser(intent, getString(R.string.new_item_audio_chooser)), SELECT_AUDIO);
    }

    /**
     * Let the user record an audio file
     *
     * @param view
     */
    public void startRecordingAudio(View view) {
        audioRecorder = new AudioRecorder();
        audioRecorder.startRecording();
    }

    /**
     * Stop recording
     *
     * @param view
     */
    public void stopRecordingAudio(View view) {
        if(audioRecorder != null) {
            audio = audioRecorder.stopRecording();
        }
    }
}
