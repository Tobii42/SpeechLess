package de.ol.speechless.app;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.IOError;
import java.io.IOException;

import de.ol.speechless.util.DataHelper;

/**
 * Created by Tobias on 01.03.14.
 */
public class SpeechItemClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();
        String text = "Position: " + position + ", id: " + id;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MediaPlayer mediaPlayer = new MediaPlayer();
        Uri uri = DataHelper.getItem(position).getAudioUri();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
           // MediaPlayer mediaPlayer = MediaPlayer.create(view.getContext(), DataHelper.getItem(position).getAudioUri());
            mediaPlayer.setDataSource(view.getContext(), uri);
            mediaPlayer.prepare();
        } catch (Exception e) {
            toast = Toast.makeText(context, "error preparing playback", duration);
            toast.show();
            e.printStackTrace();
        }
        mediaPlayer.start();
    }
}
