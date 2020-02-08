package com.example.kongmin.myapplication;

import org.junit.Test;

public class UserLoginTest {
    @Test
    public void testHttp() {

        String urlParams = "name=Bob&age=18&weight=60"; //也可是 "{\"name\":\"Bob\",\"age\":18}"
        String url = "http://lo";//也可是https://XXX

        /*HttpUtils h = HttpUtils.getHttpUtil(url, urlParams, LogoActivity.class, new IHttpCallback() {

            public void onResponse(String result) {
                // TODO Auto-generated method stub
                //Log.d(TAG, "string from server: " + result);

            }

        });

        h.httpPost();//也可是h.httpGet()*/
    }
}
