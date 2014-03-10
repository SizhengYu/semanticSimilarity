package com.dichen.semanticSim;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.netlib.util.doubleW;


import com.dichen.semanticSim.InputParser.TaskType;
import com.dichen.semanticSim.wordNet.WordNet_DescriptionToDescription;
import com.dichen.semanticSim.wordNet.WordNet_measurement;
import com.dichen.semanticSim.wordNet.WordNet_phraseToWord;
import com.dichen.semanticSim.wordNet.WordNet_wordToDescription;
import com.dichen.semanticSim.wordNet.WordNet_wordToWord;
import com.dichen.semanticSim.wordNet.WordNet_wordToWord.SimilarityAlgorithm;

public class SemanticSimRunner implements Callable<Double> {

    private static WordNet_measurement measure;
    private SimilarityAlgorithm algorithm;
    private String word;
    private String sense;
    
    private void setMeasure(TaskType taskType) {
        if (measure != null) {
            return;
        }
        
        if (taskType == TaskType.word2sense) {
            // three approach here
            measure = new WordNet_DescriptionToDescription(algorithm);
//            measure = new WordNet_wordToDescription(algorithm);
//            measure = new WordNet_wordToWord(algorithm);
        }
        else if (taskType == TaskType.phrase2word){
            // two approach here.
            measure = new WordNet_phraseToWord(algorithm);
        }
    }
    
    public SemanticSimRunner(TaskType taskType, SimilarityAlgorithm inputAlgorithm, String word1, String sense2) {
        algorithm = inputAlgorithm;
        setMeasure(taskType);
        word = word1;
        sense = sense2;
    }

    @Override
    public Double call() throws Exception {
        System.out.println("start value!");
        try {
            double score = measure.getWordNetSimilarity(word, sense);
            score = score < 0 ? 0 : score;
            System.out.println("get value!");
            return new Double(score);
        } catch (Exception e) {
            System.out.println("get error!");
            return 0.0;
        }

    }

}
