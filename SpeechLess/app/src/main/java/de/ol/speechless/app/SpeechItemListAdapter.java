package de.ol.speechless.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import de.ol.speechless.model.SpeechItem;

/**
 * Created by Tobias on 01.03.14.
 */
public class SpeechItemListAdapter extends ArrayAdapter<SpeechItem> {


    public SpeechItemListAdapter(Context context, int resource, ArrayList<SpeechItem> objects) {
        super(context, resource, objects);
    }

    /**
     * Returns the view for the gridView
     * Contains just the image we want to click on
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.speech_grid_element, parent, false);
        ImageButton imageButton = (ImageButton) rowView.findViewById(R.id.speechImageButton);
        imageButton.setImageResource(R.drawable.santa_claus);
        return rowView;
    }
}
