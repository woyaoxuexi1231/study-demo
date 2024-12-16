package org.hulei.jdk.jdk.jvm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hulei42031
 * @since 2024-04-08 18:25
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SimpleObject {

    private Integer integer;

    private String string;
}
