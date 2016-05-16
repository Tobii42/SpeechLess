package de.ol.speechless.util;

import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * Created by Tobias on 02.03.14.
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
    public void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(path.getPath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {

        }
        mediaRecorder.start();
    }

    /**
     * Stops recording
     * @return Uri to the audio-file
     */
    public Uri stopRecording(Context context) {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        return getAudioUri(context);
    }

    /**
     *
     * @return Uri to the audio-file
     */
    private Uri getAudioUri(Context context) {
        Uri contentUri = FileProvider.getUriForFile(context, "de.ol.speechless.fileprovider", path);
        return contentUri;
    }
}
