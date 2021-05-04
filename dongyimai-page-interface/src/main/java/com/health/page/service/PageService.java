package com.health.page.service;


/**
 * 页面生成接口
 */
public interface PageService {

    /**
     * 生成静态页面
     * @param goodsId
     * @return
     */
    public boolean getHtml(Long goodsId);

    /**
     * 删除静态页面
     */
    public boolean deleteHtml(Long[] goodsIds);
}
