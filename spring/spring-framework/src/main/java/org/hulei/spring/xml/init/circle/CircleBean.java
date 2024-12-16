package org.hulei.spring.xml.init.circle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.init.circle
 * @className: CircleBean
 * @description:
 * @author: h1123
 * @createDate: 2023/2/8 0:40
 */

@Getter
@Setter
public class CircleBean implements ApplicationContextAware {

    /**
     * 自己注入自己
     */
    private ApplicationContextAware circleBean;

    private String name;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
