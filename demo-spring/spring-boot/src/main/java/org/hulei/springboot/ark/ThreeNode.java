package org.hulei.springboot.ark;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hulei
 * @since 2025/7/18 13:31
 */

@AllArgsConstructor
@Data
public class ThreeNode {
    public String loopName;
    public String itemName;
    public Double value;
}
