package com.hundsun.demo.spring.service.impl;

import com.hundsun.demo.spring.model.dto.FXNewsBean;
import com.hundsun.demo.spring.service.IFXNewsListener;
import com.hundsun.demo.spring.service.IFXNewsPersister;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.service.impl
 * @className: FXNewsProvider
 * @description:
 * @author: h1123
 * @createDate: 2022/10/20 21:16
 * @updateUser: h1123
 * @updateDate: 2022/10/20 21:16
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class FXNewsProvider {

    // IFXNewsListener来帮助抓取新闻内容
    private IFXNewsListener newsListener;

    // IFXNewsPersister存储抓取的新闻
    private IFXNewsPersister newsPersister;

    public void getAndPersistNews() {

        String[] newsIds = newsListener.getAvailableNewsIds();
        if (ArrayUtils.isEmpty(newsIds)) {
            return;
        }

        for (String newsId : newsIds) {

            FXNewsBean newsBean = newsListener.getNewsByPK(newsId);

            // 存储消息
            newsPersister.persistNews(newsBean);

            // 后续处理
            newsListener.postProcessIfNecessary(newsId);
        }
    }
}
