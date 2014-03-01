package de.ol.speechless.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Tobias on 01.03.14.
 */
public class SpeechItem {

    Drawable picture;
    Uri audioUri;

    public SpeechItem(Drawable picture, Uri audioUri) {
        this.picture = picture;
        this.audioUri = audioUri;
    }

    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public Uri getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }
}
