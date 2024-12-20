package org.hulei.basic.jdk.generic.work;

import lombok.Data;

import java.util.List;

/**
 * 这是一个请求参数类,他内部包裹了一些原始的数据类型的数据
 */
@Data
public class DataTransferReq<S> {
    private List<S> sourceList;
}
