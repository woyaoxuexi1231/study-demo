package org.hulei.keeping.server.dubbo.provider;

import org.hulei.keeping.server.dubbo.api.GreetingsService;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.dubbo.provider
 * @className: GreetingsServiceImpl
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/16 16:13
 */

public class GreetingsServiceImpl implements GreetingsService {

    @Override
    public String sayHi(String name) {
        return "hi, " + name;
    }
}
