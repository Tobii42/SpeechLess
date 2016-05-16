package de.ol.speechless.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

import de.ol.speechless.app.NewItemActivity;

/**
 * @author Tobias
 * @since 02.03.14.
 */
public class AudioRecorder {

    private MediaRecorder mediaRecorder;
    private File path;

    /**
     * Creates a new audio-recorder
     */
    public AudioRecorder(Context context) {
        path = DataHelper.getNextAudioFile(context);
    }

    /**
     * Starts recording (and saving) the audio from the microphone
     */
    public void startRecording(Activity activity) {

        // First we need the permission to use the microphone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // We don't have the permission
                activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, NewItemActivity.REQUEST_RECORD_AUDIO);
                // We have to return to go on when we have the permission
                return;
            }
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(path.getPath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    /**
     * Stops recording
     *
     * @return Uri to the audio-file
     */
    public Uri stopRecording(Context context) {
        if (mediaRecorder == null) return null;
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        return getAudioUri(context);
    }

    /**
     * @return Uri to the audio-file
     */
    private Uri getAudioUri(Context context) {
        return FileProvider.getUriForFile(context, "de.ol.speechless.fileprovider", path);
    }
}
