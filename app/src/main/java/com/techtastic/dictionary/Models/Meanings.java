package com.techtastic.dictionary.Models;

import java.util.List;

public class Meanings {

    String partOfSpeech = "";
    List<Definations> definitions = null;

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public List<Definations> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definations> definitions) {
        this.definitions = definitions;
    }
}
