package com.example.kongmin.Constant;

import android.Manifest;

import com.example.kongmin.network.Url;

public class Constant {
    //发布任务的类型
    //public static final String[] taskType = {"信息抽取", "文本分类", "文本关系", "文本配对","文本排序","文本类比排序"};
    public static final String[] taskType = {"信息抽取", "文本分类", "文本关系", "文本配对","文本排序"};
    //用户登录的URL
    public static final String userloginUrl = Url.port+"user/session?";
    //用户注册的URL
    public static final String userRegisterUrl = Url.port+"user?";
    //信息抽取和文本分类标注发任务的URL
    public static final String infoaddtaskUrl = Url.port+"task/paralabel";
    //文本关系类别标注发任务的URL
    public static final String twoitemsaddtaskUrl = Url.port+"task/relation";
    //文本配对标注发任务的URL
    public static final String matchcategoryaddtaskUrl = Url.port+"task/pairing";
    //单个文本配对标注发任务的URL
    public static final String onesortaddtaskUrl = Url.port+"task/sorting";

    //做任务模块查看所有发布的任务
    public static final String selectalltaskUrl = Url.port+"task/all";
    //做任务模块查看发布的任务详情
    public static final String taskdetailUrl = Url.port+"task/detail";

    //做任务模块信息抽取查看任务对应的文本的详细内容
    public static final String extrafileUrl = Url.port+"extraction";
    //做任务模块信息抽取做任务
    public static final String extradotaskUrl = Url.port+"extraction";
    //做任务模块信息抽取结束一个段落
    public static final String extradtparaUrl = Url.port+"dpara/status";
    //做任务模块信息抽取结束整篇文档
    public static final String extradtdocUrl = Url.port+"dpara/doc/status";

    //做任务模块文本分类查看任务对应的文本的详细内容
    public static final String classifyfileUrl = Url.port+"classify";
    //做文本分类任务
    public static final String doclassifytaskUrl = Url.port+"classify";



    //单个文本排序获取做任务根据文件文件ID获取instance+item的URL
    public static final String DotaskOneSortRequestUrl = Url.port+"sorting";
    //文本排序做任务的URL
    public static final String DotaskOneSortUrl = Url.port+"sorting";
    //文本排序结束一段
    public static final String DtOneSortcominstUrl = Url.port+"dinstance/status";
    //文本排序结束整个文档
    public static final String DtOneSortcomdocUrl = Url.port+"dinstance/doc/status";

    //任务管理查看我发布的任务列表
    public static final String TMshowmypublistUrl = Url.port+"task/my/pub";
    //任务管理查看我发布的任务有几个人做
    public static final String TMshowmypubdtlistUrl = Url.port+"dtask/detail";


    //做任务模块信息抽取查看任务对应的文本的详细内容
    public static final String relationfileUrl = Url.port+"relation";

    //做任务模块信息抽取做任务
    public static final String relationdotaskUrl = Url.port+"relation";

    //做任务模块信息抽取结束一个段落
    public static final String relationdtparaUrl = Url.port+"dinstance/status";

    //做任务模块信息抽取结束整篇文档
    public static final String relationdtdocUrl = Url.port+"dinstance/doc/status";

    //做任务模块文本配对查看任务对应的文本的详细内容
    public static final String pairingfileUrl = Url.port+"pairing";

    //任务管理模块查看我做的任务的任务列表
    public static final String selectmydotaskUrl = Url.port+"dtask/status";

    //查看信息抽取和文本分类类型的文件内容
    public static final String extractfilecontentUrl = Url.port+"paragraph";
    //查看文本关系、文本排序和文本类比排序类型的文件内容
    public static final String sortfilecontentUrl = Url.port+"instance/item";
    //查看文本配对类型的文件内容
    public static final String matchfilecontentUrl = Url.port+"instance/listitem";

    //查看文本配对类型的文件内容
    public static final String deletetaskUrl = Url.port+"task";



    //文本关系类别标注发任务的URL
    //public static final String twoitemsaddtaskUrl = Url.port+"task/addTaskTwoitems";
    //文本配对标注发任务的URL
    //public static final String matchcategoryaddtaskUrl = Url.port+"task/addTaskTMatchCategory";
    //单个文本配对标注发任务的URL
    //public static final String onesortaddtaskUrl = Url.port+"task/addSortingTask";
    //单个文本排序获取做任务根据文件文件ID获取instance+item的URL
    //public static final String DotaskOneSortRequestUrl = Url.port+"instance/getSortingInstanceItem";
    //文本排序做任务的URL
    //public static final String DotaskOneSortUrl = Url.port+"dotask/addSortingTask";


    public static final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.GET_ACCOUNTS,Manifest.permission.READ_CONTACTS,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};


    //加载页数
    public static int PageIndex = 1;
    //每页加载数量
    public static int PageCapacity = 8;


    public static String COMPLETED = "已完成" ;

    public static String UNCOMPLETED = "正在进行";
}
