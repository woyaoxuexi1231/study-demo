package org.hulei.springboot.spring.transactional;

/**
 * @author hulei
 * @since 2024/9/17 23:39
 */

public class CustomException extends Exception {

    public CustomException(String message) {
        super(message);
    }
}
