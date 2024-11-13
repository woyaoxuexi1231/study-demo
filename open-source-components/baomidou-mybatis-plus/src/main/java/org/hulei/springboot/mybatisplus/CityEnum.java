package org.hulei.springboot.mybatisplus;

/**
 * @author hulei
 * @since 2024/11/11 17:14
 */

public enum CityEnum {

    Aachen(459);

    CityEnum(Integer code) {
        this.code = code;
    }

    private final Integer code;
}
