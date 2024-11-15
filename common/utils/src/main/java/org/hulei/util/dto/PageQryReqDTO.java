package org.hulei.util.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.commom.core.model.req
 * @className: PageQryReqDTO
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 14:25
 */

@Data
public class PageQryReqDTO implements Serializable {
    /**
     * 页数
     */
    @NotNull(message = "分页参数 pageNum 不能为 null")
    private Integer pageNum;
    /**
     * 数量
     */
    @NotNull(message = "分页参数 PageSize 不能为 null")
    private Integer pageSize;
}
