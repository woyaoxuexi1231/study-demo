package org.hulei.jdk.rtjar.generic.work;

import lombok.Data;

import java.util.List;

/**
 * 这是一个中间上下文处理类
 * 1. 包含了原始的请求数据
 * 2. 包含了清理后的数据
 */
@Data
public class DataTransfer<S, T> {
    private List<S> sourceList;
    private List<T> targetList;
}