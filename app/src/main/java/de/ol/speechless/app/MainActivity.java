package de.ol.speechless.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import de.ol.speechless.util.DataHelper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link the GridView to the data with custom adapter
        SpeechItemListAdapter adapter = new SpeechItemListAdapter(this,
                R.layout.speech_grid_element, DataHelper.getSpeechItemList(this));
        GridView speechGrid = (GridView) findViewById(R.id.itemGridView);
        speechGrid.setAdapter(adapter);

        // Link the GridView to the functionality with custom ClickListener
        SpeechItemClickListener listener = new SpeechItemClickListener();
        speechGrid.setOnItemClickListener(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows the Activity to add a new item (with picture and sound)
     * @param item
     */
    public void addItem(MenuItem item) {
        Intent startNewItemActivity = new Intent(this, NewItemActivity.class);
        startActivity(startNewItemActivity);
    }
}
