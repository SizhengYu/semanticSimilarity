package com.dichen.semanticSim;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.ukp.dkpro.wsd.si.lsr.LsrSenseInventory;
import de.tudarmstadt.ukp.dkpro.wsd.si.wordnet.candidates.WordNetSenseKeyToSynset;

public class WordNetWorker {
    private static Map<String, String> wordNetMap;
    
    /**
     * 
     * @return
     * @throws IllegalArgumentException
     * @throws IOException
     */
    static Map<String, String> getWordNetMap() throws IllegalArgumentException, IOException{
        if (wordNetMap == null){
            URL wordNetSenseMapLocation;
            wordNetSenseMapLocation = new File(System.getenv("DKPRO_HOME") + "/de.tudarmstadt.ukp.dkpro.lexsemresource.core.ResourceFactory/wordnet3/dict/index.sense").toURI().toURL();
            wordNetMap = WordNetSenseKeyToSynset.getSenseMap(wordNetSenseMapLocation);

        }
        return wordNetMap;
    }
    
    /**
     * 
     * @param senseKey  The input should be the sense key in wordnet 3.1
     * @return  The sense set in wordnet3.1. return empty string if there is an exception.
     */
    static String getSense(String senseKey){
        // get sense map.
        try {
            // Get the sense number from the sense key.
            String temp = getWordNetMap().get(senseKey);
            String senseSyn = temp.substring(0, temp.length()-1);
            senseSyn = Integer.parseInt(senseSyn) + "";
            
            // Get the word from sense key, search by word.
            LsrSenseInventory senseInventory = new LsrSenseInventory("wordnet", "en");
            String word = senseKey.split("%")[0];
            List<String> senses = senseInventory.getSenses(word);

            // fileter the sense that match the sense number, return the description.
            for (String sense : senses) {
                if (sense.contains(senseSyn)){
                    return senseInventory.getSenseDescription(sense);
                }
            }
            
            // If no sense found, return empty string.
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }        
    }
}
