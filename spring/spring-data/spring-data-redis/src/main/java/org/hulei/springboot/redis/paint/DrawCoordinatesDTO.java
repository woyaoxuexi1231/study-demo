package org.hulei.springboot.redis.paint;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hulei
 * @since 2024/12/12 23:34
 */

@Accessors(chain = true)
@Data
public class DrawCoordinatesDTO {

    private int row;

    private int col;

    private String color;
}
