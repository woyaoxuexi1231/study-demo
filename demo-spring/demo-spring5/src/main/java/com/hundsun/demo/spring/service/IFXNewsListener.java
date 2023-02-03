package com.hundsun.demo.spring.service;

import com.hundsun.demo.spring.model.dto.FXNewsBean;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.service.impl
 * @className: IFXNewsListener
 * @description:
 * @author: h1123
 * @createDate: 2022/10/20 21:16
 * @updateUser: h1123
 * @updateDate: 2022/10/20 21:16
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public interface IFXNewsListener {

    /**
     * 获取可用的新闻id
     *
     * @return result
     */
    String[] getAvailableNewsIds();

    /**
     * 通过id获取新闻
     *
     * @param newsId id
     * @return news
     */
    FXNewsBean getNewsByPK(String newsId);

    /**
     * 后续处理
     *
     * @param newsId id
     */
    void postProcessIfNecessary(String newsId);
}
