package com.example.textannotation.view.doTask.sort;

public class DataBean {
    private String text;
    private int itemid;
    private boolean isCollapsed=true;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setCollapsed(boolean collapsed) {
        isCollapsed = collapsed;
    }
}
