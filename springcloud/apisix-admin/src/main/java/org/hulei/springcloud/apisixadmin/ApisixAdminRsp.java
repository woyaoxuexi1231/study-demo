package org.hulei.springcloud.apisixadmin;

import lombok.Data;

import java.util.List;

/**
 * @author hulei
 * @since 2024/10/28 17:04
 */

@Data
public class ApisixAdminRsp {
    /**
     * 总数
     */
    private Integer total;
    /**
     *
     */
    private List<ApisixRouteRsp> list;
}

@Data
class ApisixRouteRsp{
    /**
     * 创建下标
     */
    private Integer createdIndex;
    /**
     * 路由信息
     */
    private ApisixAdminReq value;
    private String key;
    private String modifiedIndex;
}
