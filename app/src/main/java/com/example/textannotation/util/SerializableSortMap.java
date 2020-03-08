package com.example.textannotation.util;

import java.io.Serializable;
import java.util.Map;

public class SerializableSortMap implements Serializable {

    private Map<Integer,String> map;

    public Map<Integer,String> getMap() {
        return map;
    }

    public void setMap(Map<Integer,String> map) {
        this.map = map;
    }
}
