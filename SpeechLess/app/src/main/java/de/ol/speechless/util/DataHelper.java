package de.ol.speechless.util;

import java.util.ArrayList;

import de.ol.speechless.model.SpeechItem;

/**
 * Created by Tobias on 01.03.14.
 */
public class DataHelper {

    // The list of all speechitems
    private static ArrayList<SpeechItem> speechItemList;

    /**
     * Returns the list of all speechitems
     * @return
     */
    public static ArrayList<SpeechItem> getSpeechItemList() {
        if(speechItemList == null) {
            // Later we have to load this from a file or so
            speechItemList = new ArrayList<SpeechItem>();
        }
         return speechItemList;
    }

    /**
     * Adds a single speechitem to the list
     * @param speechItem
     */
    public static void addItemToList(SpeechItem speechItem) {
        getSpeechItemList().add(speechItem);
    }
}
