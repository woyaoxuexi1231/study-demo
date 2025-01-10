package org.hulei.basic.jdk.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.spi
 * @className: TranslateEngineLoad
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 2:12
 */

public class TranslateEngineLoad {

    public static void main(String[] args) {

        // 通过ServiceLoader来获取第三方的Translate接口的实现类
        ServiceLoader<Translate> translateEngines = ServiceLoader.load(Translate.class);
        Iterator<Translate> iterator = translateEngines.iterator();
        Translate translateEngine = iterator.next();
        // 这里只加载一种
        if (iterator.hasNext()) {
            System.out.println("WARN! More than one engine");
        }
        // 使用第三方提供的具体服务
        translateEngine.translate();
    }
}
