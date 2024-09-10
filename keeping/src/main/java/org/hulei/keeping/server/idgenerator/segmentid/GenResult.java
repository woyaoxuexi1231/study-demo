package org.hulei.keeping.server.idgenerator.segmentid;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GenResult {
    /**
     * 生成的 id 编号
     */
    private long id;
    /**
     * 结果
     */
    private String result;
}