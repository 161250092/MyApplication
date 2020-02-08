package com.example.kongmin.util;

import java.io.Serializable;
import java.util.Map;
/**
 * 序列化map供Bundle传递map使用
 */

public class SerializableMap implements Serializable {

    private Map<String,Integer> map;

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}

