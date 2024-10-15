package org.hulei.common.security;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;

/**
 * @author hulei
 * @since 2024/10/14 15:46
 */

@Slf4j
@MapperScan(basePackages = {"org.hulei.common.security.mapper"})
public class SecurityAutoConfig {


}
