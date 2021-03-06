package com.example.textannotation.util;

import java.util.Random;

public class ColorUtil {
    /**
     * 获取十六进制的颜色代码.例如  "#6E36B4" , For HTML ,
     * 该方法用于随机生成一个颜色
     * @return String
     */
    public static String getRandColorCode(){
        String r,g,b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length()==1 ? "0" + r : r ;
        g = g.length()==1 ? "0" + g : g ;
        b = b.length()==1 ? "0" + b : b ;

        return "#"+r+g+b;
    }
}
