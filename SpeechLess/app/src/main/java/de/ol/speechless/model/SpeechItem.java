package de.ol.speechless.model;

import android.graphics.drawable.Drawable;
import android.provider.MediaStore;

/**
 * Created by Tobias on 01.03.14.
 */
public class SpeechItem {

    Drawable picture;
    MediaStore.Audio audio;

    public SpeechItem(Drawable picture, MediaStore.Audio audio) {
        this.picture = picture;
        this.audio = audio;
    }

    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public MediaStore.Audio getAudio() {
        return audio;
    }

    public void setAudio(MediaStore.Audio audio) {
        this.audio = audio;
    }
}
