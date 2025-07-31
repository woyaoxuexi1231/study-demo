package org.hulei.springboot.spring.typefilter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

@Slf4j
public class CustomTypeFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader,
                         MetadataReaderFactory metadataReaderFactory) throws IOException {
        // 获取类名
        String className = metadataReader.getClassMetadata().getClassName();
        if (className.endsWith("Stub")) {
            log.info("找到以 Stub 结尾的类：{}", className);
        }
        // 示例逻辑：排除类名以 "Stub" 结尾的类
        return !className.endsWith("Stub");
    }
}