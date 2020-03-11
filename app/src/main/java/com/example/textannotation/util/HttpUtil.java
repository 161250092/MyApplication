package com.example.textannotation.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.List;
import com.example.textannotation.view.login.LoginActivity;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//HTTP通用类
public class HttpUtil {
    static String TAG = LoginActivity.class.getCanonicalName();
    private static final String CHARSET = "utf-8"; //设置编码
    /**
     * get提交数据
     * @param baseurl
     * @param paramurl
     *
     */
    public static String requestGet(String baseurl,String paramurl) {
        String result = "";
        try {
            String requestUrl = baseurl + paramurl;
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                result = streamToString(urlConn.getInputStream());
                Log.e(TAG, "Get方式请求成功，result--->" + result);
                return result;
            } else {
                Log.e(TAG, "Get方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
        return result;
    }

    /**
     * post提交数据
     * @param baseurl
     */
    public static String requestPost(String baseurl,String paramurl) {
        String requestUrl = baseurl+paramurl;
        String result = "";
        try{
                // 新建一个URL对象
                URL url = new URL(requestUrl);
                // 打开一个HttpURLConnection连接
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                // 设置连接主机超时时间
                urlConn.setConnectTimeout(5 * 1000);
                //设置从主机读取数据超时
                urlConn.setReadTimeout(5 * 1000);
                // 设置是否使用缓存  默认是true
                urlConn.setUseCaches(true);
                // 设置为Post请求
                urlConn.setRequestMethod("POST");
                //urlConn设置请求头信息
                //设置请求中的媒体类型信息。
                //post请求不能设置这个
                //urlConn.setRequestProperty("Content-Type", "application/json");
                //设置客户端与服务连接类型
                urlConn.addRequestProperty("Connection", "Keep-Alive");
                // 开始连接
                urlConn.connect();
                // 判断请求是否成功
                if (urlConn.getResponseCode() == 200) {
                    // 获取返回的数据
                    result = streamToString(urlConn.getInputStream());
                    Log.e("success", "Post方式请求成功，result--->" + result);
                    return result;
                } else {
                    Log.e("fail", "Post方式请求失败");
                }
                urlConn.disconnect();
        } catch (Exception e) {
                Log.e("exceptioninfo", e.toString());
                return null;
        }
        return result;
    }

    /**
     * delete提交数据
     * @param baseurl
     */
    public static String requestDelete(String baseurl,String paramurl) {
        String requestUrl = baseurl+paramurl;
        String result = "";
        try{
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("DELETE");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            //post请求不能设置这个
            //urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                result = streamToString(urlConn.getInputStream());
                Log.e("success", "Delete方式请求成功，result--->" + result);
                return result;
            } else {
                Log.e("fail", "Delete方式请求失败");
            }
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e("exceptioninfo", e.toString());
            return null;
        }
        return result;
    }

    /**
     * post提交数据
     * @param baseurl
     */
    public static String requestPostandUploadfiles(String baseurl,String paramurl,List<File> files) {
        String BOUNDARY = UUID.randomUUID().toString(); //边界标识 随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; //内容类型
        String requestUrl = baseurl+paramurl;
        String result = "";
        for(int i=0;i<files.size();i++){
            Log.e("tag", "addtaskfilename-->>" + files.get(i).getName());
        }

        try{
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            //设置客户端与服务连接类型
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            urlConn.setDoInput(true); //允许输入流
            urlConn.setDoOutput(true); //允许输出流
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);//不允许使用缓存
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            urlConn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            urlConn.setRequestProperty("Charset", CHARSET);
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            //post请求不能设置这个
            //urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接

            //urlConn.connect();
            if(files.size()!= 0) {
                //** * 当文件不为空，把文件包装并且上传 *//*
                OutputStream outputSteam = urlConn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);
                for (int i = 0; i < files.size(); i++) {
                    final StringBuffer sb = new StringBuffer();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINE_END);
                    sb.append("Content-Disposition:form-data; name=\"files[]\";filename=\"" + files.get(i).getName() + "\"" + LINE_END);
                    sb.append("Content-Type:application/octet-stream; charset="+ CHARSET +LINE_END);
                    sb.append(LINE_END);
                    dos.write(sb.toString().getBytes());
                    InputStream is = new FileInputStream(files.get(i));
                    byte[] bytes = new byte[1024];
                    int len = -1;
                    while ((len = is.read(bytes)) != -1) {
                        dos.write(bytes, 0, len);
                    }
                    is.close();
                    dos.write(LINE_END.getBytes());
                }
                final byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
            }
            int res = urlConn.getResponseCode();
            Log.e(TAG, "response code:"+res);
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                result = streamToString(urlConn.getInputStream());
                Log.e("success", "Post方式请求成功，addtaskresult--->" + result);
                return result;
            } else {
                Log.e("fail", "Post方式请求失败");
            }
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e("exceptioninfo", e.toString());
            return null;
        }
        return result;
    }

    /**
     * 使用HttpURLConnection通过POST方式提交请求，并上传文件。
     *
     * @param actionUrl  访问的url
     * @param textParams 文本类型的POST参数(key:value)，其中value的类型为object
     * @param filePaths  文件集合
     * @return 服务器返回的数据，出现异常时返回 null
     */
    public static String postWithFilesTwoitems(String actionUrl, Map<String,Object> textParams, List<String> filePaths) {
        try {
            final String BOUNDARY = UUID.randomUUID().toString();
            final String PREFIX = "--";
            final String LINE_END = "\r\n";

            final String MULTIPART_FROM_DATA = "multipart/form-data";
            final String CHARSET = "UTF-8";

            URL uri = new URL(actionUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

            //缓存大小
            conn.setChunkedStreamingMode(1024 * 64);
            //超时
            conn.setReadTimeout(5 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            // 拼接文本类型的参数
            StringBuilder textSb = new StringBuilder();
            if (textParams != null) {
                for (Map.Entry<String, Object> entry : textParams.entrySet()) {
                    textSb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    textSb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
                    textSb.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);
                    textSb.append("Content-Transfer-Encoding: 8bit" + LINE_END);
                    textSb.append(LINE_END);
                    textSb.append(entry.getValue());
                    textSb.append(LINE_END);
                }
            }

            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(textSb.toString().getBytes());

            //参数POST方式
            //outStream.write("userId=1&cityId=26".getBytes());

            // 发送文件数据
           if (filePaths != null) {
                for (String file : filePaths) {
                    StringBuilder fileSb = new StringBuilder();
                    fileSb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    fileSb.append("Content-Disposition: form-data; name=\"files[]\"; filename=\"" +
                            file.substring(file.lastIndexOf("/") + 1) + "\"" + LINE_END);
                    fileSb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                    fileSb.append(LINE_END);
                    outStream.write(fileSb.toString().getBytes());

                    InputStream is = new FileInputStream(file);
                    byte[] buffer = new byte[1024 * 8];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }

                    is.close();
                    outStream.write(LINE_END.getBytes());
                }
            }

            // 请求结束标志
            outStream.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());
            outStream.flush();

            // 得到响应码
            int responseCode = conn.getResponseCode();
            Log.e("success", "Post方式请求，responseCode--->" + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));

            StringBuilder resultSb = null;
            String line;
            if (responseCode == 200) {
                resultSb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    resultSb.append(line).append("\n");
                }
            }

            br.close();
            outStream.close();
            conn.disconnect();
            Log.e("success", "Post方式请求成功，addtaskresult--->" + resultSb);
            return resultSb == null ? null : resultSb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    /**
     * 使用HttpURLConnection通过POST方式提交请求，并上传文件。
     *
     * @param actionUrl  访问的url
     * @param textParams 文本类型的POST参数(key:value)
     * @param filePaths  文件路径的集合
     * @return 服务器返回的数据，出现异常时返回 null
     */
    public static String postWithFiles(String actionUrl, Map<String, String> textParams, List<String> filePaths) {
        try {
            final String BOUNDARY = UUID.randomUUID().toString();
            final String PREFIX = "--";
            final String LINE_END = "\r\n";

            final String MULTIPART_FROM_DATA = "multipart/form-data";
            final String CHARSET = "UTF-8";

            URL uri = new URL(actionUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

            //缓存大小
            conn.setChunkedStreamingMode(1024 * 64);
            //超时
            conn.setReadTimeout(5 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            // 拼接文本类型的参数
            StringBuilder textSb = new StringBuilder();
            if (textParams != null) {
                for (Map.Entry<String, String> entry : textParams.entrySet()) {
                    textSb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    textSb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
                    textSb.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);
                    textSb.append("Content-Transfer-Encoding: 8bit" + LINE_END);
                    textSb.append(LINE_END);
                    textSb.append(entry.getValue());
                    textSb.append(LINE_END);
                }
            }

            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(textSb.toString().getBytes());

            //参数POST方式
            //outStream.write("userId=1&cityId=26".getBytes());

            // 发送文件数据
            if (filePaths != null) {
                for (String file : filePaths) {
                    StringBuilder fileSb = new StringBuilder();
                    fileSb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    fileSb.append("Content-Disposition: form-data; name=\"files[]\"; filename=\"" +
                            file.substring(file.lastIndexOf("/") + 1) + "\"" + LINE_END);
                    fileSb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                    fileSb.append(LINE_END);
                    outStream.write(fileSb.toString().getBytes());

                    InputStream is = new FileInputStream(file);
                    byte[] buffer = new byte[1024 * 8];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }

                    is.close();
                    outStream.write(LINE_END.getBytes());
                }
            }

            // 请求结束标志
            outStream.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());
            outStream.flush();

            // 得到响应码
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));

            StringBuilder resultSb = null;
            String line;
            if (responseCode == 200) {
                resultSb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    resultSb.append(line).append("\n");
                }
            }

            br.close();
            outStream.close();
            conn.disconnect();
            Log.e("success", "Post方式请求成功，addtaskresult--->" + resultSb);
            return resultSb == null ? null : resultSb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
