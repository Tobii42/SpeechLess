package de.ol.speechless.app;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

import de.ol.speechless.util.DataHelper;

/**
 * Created by Tobias on 01.03.14.
 */
public class SpeechItemClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Play the sound
        MediaPlayer mediaPlayer = new MediaPlayer();
        Uri uri = DataHelper.getItem(position).getAudioUri();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            ParcelFileDescriptor parcelFileDescriptor = view.getContext().getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            mediaPlayer.setDataSource(fileDescriptor);
            mediaPlayer.prepare();
        } catch (Exception e) {
            Context context = view.getContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, context.getString(R.string.error_play_sound), duration);
            toast.show();
        }
        mediaPlayer.start();
    }
}
