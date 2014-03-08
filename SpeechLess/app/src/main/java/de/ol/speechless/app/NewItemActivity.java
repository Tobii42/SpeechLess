package de.ol.speechless.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import de.ol.speechless.model.SpeechItem;
import de.ol.speechless.util.AudioRecorder;
import de.ol.speechless.util.DataHelper;

public class NewItemActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_PHOTO = 2;
    private static final int SELECT_AUDIO = 3;

    private static final int IMAGE_WIDTH = 300;

    private Uri image;
    private Uri imageToCapture; // Uri of the image that has to be made with the camera-app
    private Uri audio;
    private AudioRecorder audioRecorder;

    private int takeFlags; // To "take" the permissions to access the files (persistent, after reboot, ...)

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
        Uri selectedImage = null;
        Uri selectedAudio = null;

        // Get the image from the camera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // The camera app does not return anything, but we know that we got a
            // picture in the Uri that we gave to the camera (imageToCapture)
            selectedImage = imageToCapture;

        } else if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            // Get the image from the gallery
             selectedImage = data.getData();

        } else if(requestCode == SELECT_AUDIO && resultCode == RESULT_OK) {
            // Get audio from audio-picker
            selectedAudio = data.getData();
        }

        if(selectedImage != null) {
            // We got a picture
            image = selectedImage;

            // Show the image in the preview
            ImageView preview = (ImageView) findViewById(R.id.imagePreview);
            preview.setImageDrawable(DataHelper.getImageFromFile(this, image, IMAGE_WIDTH));

            // Take permission
            //getContentResolver().takePersistableUriPermission(selectedImage, takeFlags);
        }

        if(selectedAudio != null) {
            // We got an audio-file-uri
            audio = selectedAudio;

            // Take permission
            //getContentResolver().takePersistableUriPermission(selectedAudio, takeFlags);
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
            DataHelper.addItemToList(speechItem, this);
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

            takeFlags = photoPickerIntent.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

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
        imageToCapture = Uri.fromFile(new File(DataHelper.getNextImageFileName()));
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageToCapture); // set the image file name

        // Check if there's an app which can handle this intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takeFlags = takePictureIntent.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

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

        takeFlags = intent.getFlags()
                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

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
