package de.ol.speechless.data;

import android.provider.BaseColumns;

/**
 * Created by Tobias on 02.03.14.
 */
public class SpeechDataContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public SpeechDataContract() {}

    /* Inner class that defines the table contents */
    public static abstract class SpeechEntry implements BaseColumns {
        public static final String TABLE_NAME = "speech_items";
        public static final String _ID = "_ID";
        public static final String COLUMN_NAME_COL_IMAGE_URI = "image_uri";
        public static final String COLUMN_NAME_AUDIO_URI = "audio_uri";
    }
}
