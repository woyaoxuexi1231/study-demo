package com.hundsun.demo.spring.service;

import com.hundsun.demo.spring.model.dto.FXNewsBean;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.service.impl
 * @className: IFXNewsPersister
 * @description:
 * @author: h1123
 * @createDate: 2022/10/20 21:16
 * @updateUser: h1123
 * @updateDate: 2022/10/20 21:16
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public interface IFXNewsPersister {

    /**
     * 存储这个消息
     *
     * @param newsBean news
     */
    void persistNews(FXNewsBean newsBean);

}
