package com.example.kongmin.network;

/**
 *
 *
 */
public class Url {

    public static final String port = "http://192.168.0.104:8082/";
    //用户登录的URL
    public static final String userloginUrl = "http://192.168.0.104:8082/user/session";

    //用户注册的URL
    public static final String userRegisterUrl = "http://192.168.0.104:8082/user/registerInApp";

    //做任务模块查看所有发布的任务
    public static final String selectalltaskUrl = "http://192.168.0.104:8082/task/all";



    //做任务模块信息抽取查看任务对应的文本的详细内容
    public static final String extrafileUrl = "http://192.168.0.104:8082/extraction";
    //做任务模块信息抽取做任务
    public static final String extradotaskUrl = "http://192.168.0.104:8082/extraction";
    //做任务模块信息抽取结束一个段落
    public static final String extradtparaUrl = "http://192.168.0.104:8082/dpara/status";
    //做任务模块信息抽取结束整篇文档
    public static final String extradtdocUrl = "http://192.168.0.104:8082/dpara/doc/status";


    //做任务模块文本分类查看任务对应的文本的详细内容
    public static final String classifyfileUrl = "http://192.168.0.104:8082/classify";
    //做文本分类任务
    public static final String doclassifytaskUrl = "http://192.168.0.104:8082/classify";


    //单个文本排序获取做任务根据文件文件ID获取instance+item的URL
    public static final String DotaskOneSortRequestUrl = "http://192.168.0.104:8082/sorting";
    //文本排序做任务的URL
    public static final String DotaskOneSortUrl = "http://192.168.0.104:8082/sorting";
    //文本排序结束一段
    public static final String DtOneSortcominstUrl = "http://192.168.0.104:8082/dinstance/status";
    //文本排序结束整个文档
    public static final String DtOneSortcomdocUrl = "http://192.168.0.104:8082/dinstance/doc/status";




    //做任务模块信息抽取查看任务对应的文本的详细内容
    public static final String relationfileUrl = "http://192.168.0.104:8082/relation";
    //做任务模块信息抽取做任务
    public static final String relationdotaskUrl = "http://192.168.0.104:8082/relation";
    //做任务模块信息抽取结束一个段落
    public static final String relationdtparaUrl = "http://192.168.0.104:8082/dpara/status";
    //做任务模块信息抽取结束整篇文档
    public static final String relationdtdocUrl = "http://192.168.0.104:8082/dpara/doc/status";



    //做任务模块文本配对查看任务对应的文本的详细内容
    public static final String pairingfileUrl = "http://192.168.0.104:8082/pairing";

    //任务管理模块查看我做的任务的任务列表
    public static final String selectmydotaskUrl = "http://192.168.0.104:8082/dtask/status";



    //查看信息抽取和文本分类类型的文件内容
    public static final String extractfilecontentUrl = "http://192.168.0.104:8082/paragraph";
    //查看文本关系、文本排序和文本类比排序类型的文件内容
    public static final String sortfilecontentUrl = "http://192.168.0.104:8082/instance/item";
    //查看文本配对类型的文件内容
    public static final String matchfilecontentUrl = "http://192.168.0.104:8082/instance/listitem";

    //查看文本配对类型的文件内容
    public static final String deletetaskUrl = "http://192.168.0.104:8082/task";



}
