package com.example.kongmin.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class UploadUtil {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10*1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码

    //读文件
    public static String readSDFile(String fileName) throws IOException {
       File file = new File(fileName);
       String fileContent = "";
        Log.e("readfilename", fileName);
        FileInputStream fis = new FileInputStream(file);
        int length = fis.available();
        byte [] buffer = new byte[length];
        fis.read(buffer);
        String res = new String(buffer, "UTF-8");
        /*System.out.println(new String(res.getBytes("ISO-8859-1"),"gb2312"));
        System.out.println(new String(res.getBytes("UTF8"),"gb2312"));
        System.out.println(new String(res.getBytes("GB2312"),"gb2312"));
        System.out.println(new String(res.getBytes("GBK"),"gb2312"));
        System.out.println(new String(res.getBytes("BIG5"),"gb2312"));*/
        Log.e("readfile", res);
        fis.close();
   /*try{
        if(file.isFile()&&file.exists()){
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader reader=new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent += line;
            }
            read.close();
        }
    } catch (Exception e) {
        System.out.println("读取文件内容操作出错");
        e.printStackTrace();
    }
  return fileContent;*/
        return res;
        /*StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readline = "";

            while ((readline = br.readLine()) != null) {
                System.out.println("readline:" + readline);
                sb.append(readline);
            }
                br.close();
                System.out.println("读取成功：" + sb.toString());
        } catch (Exception e) {
                e.printStackTrace();
        }
        return sb.toString();*/
    }

    /**
     * android查看文件内容
     * @param file  需要上传的文件
     * @param RequestURL  请求的rul
     * @return  返回响应的内容
     */
    public static String readFile(final File file,final String RequestURL)
    {
        String result = null;
        final String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识 随机生成
        final String PREFIX = "--" , LINE_END = "\r\n";
        final String CONTENT_TYPE = "multipart/form-data";   //内容类型

        try {
            final URL url = new URL(RequestURL);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("GET");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            if(file!=null)
            {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                final DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                final StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key才可以得到对应的文件,在这里这个key就是对应的files
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"files\"; filename=\""+file.getName()+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                final InputStream is = new FileInputStream(file);
                final byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1)
                {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                final byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */
                final int res = conn.getResponseCode();
                Log.e(TAG, "response code:"+res);
                if(res==200)
                {
                    Log.e(TAG, "request success");
                    final InputStream input =  conn.getInputStream();
                    final StringBuffer sb1= new StringBuffer();
                    int ss ;
                    while((ss=input.read())!=-1)
                    {
                        sb1.append((char)ss);
                    }
                    result = sb1.toString();
                    Log.e(TAG, "result : "+ result);
                }
                else{
                    Log.e(TAG, "request error");
                }
            }
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return result;
    }







    /**
     * android上传文件到服务器
     * @param file  需要上传的文件
     * @param RequestURL  请求的rul
     * @return  返回响应的内容
     */
    public static String uploadFile(final File file,final String RequestURL)
    {
        String result = null;
        final String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识 随机生成
        final String PREFIX = "--" , LINE_END = "\r\n";
        final String CONTENT_TYPE = "multipart/form-data";   //内容类型

        try {
            final URL url = new URL(RequestURL);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            if(file!=null)
            {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                final DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                final StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key才可以得到对应的文件,在这里这个key就是对应的files
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"files\"; filename=\""+file.getName()+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                final InputStream is = new FileInputStream(file);
                final byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1)
                {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                final byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */
                final int res = conn.getResponseCode();
                Log.e(TAG, "response code:"+res);
                if(res==200)
                {
                    Log.e(TAG, "request success");
                    final InputStream input =  conn.getInputStream();
                    final StringBuffer sb1= new StringBuffer();
                    int ss ;
                    while((ss=input.read())!=-1)
                    {
                        sb1.append((char)ss);
                    }
                    result = sb1.toString();
                    Log.e(TAG, "result : "+ result);
                }
                else{
                    Log.e(TAG, "request error");
                }
            }
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}


