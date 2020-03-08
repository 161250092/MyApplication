package com.example.textannotation.pojo;

import com.example.textannotation.util.SerializableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件信息
 * 传入SerializableMap 对象后进行初始化
 * 可以获得文件ID、NAME的LIST 和数组
 */
public class FileInfo {

    private Map<String,Integer> file_map;

    private List<Integer> mFileId_list;
    private List<String> mFileName_list;
    private String[] mFileNames;
    private Integer[] mFileIds;


    public FileInfo(SerializableMap fileMap ){
        file_map = fileMap.getMap();

        mFileId_list = new ArrayList<>();
        mFileName_list = new ArrayList<>();

        for(String labelName : file_map.keySet()){
            mFileId_list.add(file_map.get(labelName));
            mFileName_list.add(labelName);
        }
        mFileNames = mFileName_list.toArray(new String[mFileName_list.size()]) ;
        mFileIds = mFileId_list.toArray(new Integer[mFileId_list.size()]);
    }


    public int getFileIdByName(String fileName){
        return file_map.get(fileName);
    }


    public List<Integer> getmFileId_list() {
        return mFileId_list;
    }

    public void setmFileId_list(List<Integer> mFileId_list) {
        this.mFileId_list = mFileId_list;
    }

    public List<String> getmFileName_list() {
        return mFileName_list;
    }

    public void setmFileName_list(List<String> mFileName_list) {
        this.mFileName_list = mFileName_list;
    }

    public String[] getmFileNames() {
        return mFileNames;
    }

    public void setmFileNames(String[] mFileNames) {
        this.mFileNames = mFileNames;
    }

    public Integer[] getmFileIds() {
        return mFileIds;
    }

    public void setmFileIds(Integer[] mFileIds) {
        this.mFileIds = mFileIds;
    }
}
