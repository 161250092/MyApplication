package com.example.kongmin.view.textcategory;

public interface IMatchCategory {

    /**
     * 初始化
     *
     *
     */


    /**
     * DoTask
     * 逻辑：
     * 点击 下方 fragment 的确定匹配， 获取上方 和 下方 的itemId, 存入selectedaboveitemids，selectedblowitemids
     * 确定匹配隐藏，取消匹配出现
     * 上方 fragment 添加匹配信息
     *
     *
     * 点击 下方的 取消匹配， 获取上方 和 下方 的itemId， 同时遍历selectedaboveitemids，selectedblowitemids，删除对应项
     * 取消匹配隐藏，确定匹配出现
     * 上方 fragment 更新匹配信息
     */


    /**
     * 获取当前 上方 viewpager 展示的 fragment 对应的 itemId;
     */
    public int getAboveItemId();


    public int getBelowItemId();

    /**
     * 获取下方index，以方便上方碎片的数据显示
     * @return
     */
    public int getBelowFragmentIndex();


    /**
     * ui显示，添加匹配信息
     */
    public void addMatchInfoInAboveFragment(int belowIndex);

    /**
     * ui显示，删除匹配信息
     */
    public void minusMatchInfoInAboveFragment(int belowIndex);


    /**
     * 增加匹配对
     */
    public void addMatchPair(int aboveItemId, int belowItemId);

    /**
     * 取消匹配对
     * @param aboveItemId
     * @param belowItemId
     */
    public void deleteMatchPair(int aboveItemId, int belowItemId);


    /**
     * 在滑动viewPage时改变匹配按钮状态
     */


    public void updateBtnStatus();

}
