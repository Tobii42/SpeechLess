package de.ol.speechless.util;

import android.media.MediaRecorder;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

/**
 * Created by Tobias on 02.03.14.
 */
public class AudioRecorder {

    private MediaRecorder mediaRecorder;
    private String path;

    /**
     * Creates a new audio-recorder
     */
    public AudioRecorder() {
        path = DataHelper.getNextAudioFileName();
    }

    /**
     * Starts recording (and saving) the audio from the microphone
     */
    public void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(path);
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
    public Uri stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        return getAudioUri();
    }

    /**
     *
     * @return Uri to the audio-file
     */
    private Uri getAudioUri() {
        Uri uri = Uri.parse(path);
        return uri;
    }
}
