package org.hulei.util.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageInfo<T> {
    // 当前页
    private int pageNum;
    // 每页的数量
    private int pageSize;
    // 总页数
    private int pages;

    // 前一页
    private int prePage;
    // 下一页
    private int nextPage;

    // 是否有前一页
    private boolean hasPreviousPage = false;
    // 是否有下一页
    private boolean hasNextPage = false;

    // 总记录数
    protected long total;
    // 结果集
    protected List<T> list;
}