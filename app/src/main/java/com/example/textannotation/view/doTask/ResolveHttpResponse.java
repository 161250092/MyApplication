package com.example.textannotation.view.doTask;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.textannotation.Constant.Constant;

public class ResolveHttpResponse {

    public static void showHttpResponse(String str, Context context){
        JSONObject jsonObject= (JSONObject) JSON.parse(str);
        Integer data = (Integer) jsonObject.getInteger("status");
        if(data.toString().equals("0")){
            Looper.prepare();
            Toast.makeText(context,Constant.SUBMIT_SUCCESS,Toast.LENGTH_LONG).show();
            Looper.loop();
        }else{
            Looper.prepare();
            Toast.makeText(context,Constant.SUBMIT_FAIL,Toast.LENGTH_LONG).show();
            Looper.loop();
        }
    }





}
