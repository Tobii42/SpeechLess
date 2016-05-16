package de.ol.speechless.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Tobias on 01.03.14.
 */
public class SpeechItem {

    Uri pictureUri;
    Uri audioUri;

    public SpeechItem(Uri pictureUri, Uri audioUri) {
        this.pictureUri = pictureUri;
        this.audioUri = audioUri;
    }

    public Uri getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(Uri pictureUri) {
        this.pictureUri = pictureUri;
    }

    public Uri getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }
}
