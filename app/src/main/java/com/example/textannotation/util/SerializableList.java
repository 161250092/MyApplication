package com.example.textannotation.util;

import java.io.Serializable;
import java.util.List;

/**
 * 序列化map供Bundle传递map使用
 */

public class SerializableList implements Serializable {

    private List<Integer> list;

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }
}

