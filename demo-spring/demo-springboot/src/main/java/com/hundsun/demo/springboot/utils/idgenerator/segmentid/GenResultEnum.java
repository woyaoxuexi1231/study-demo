package com.hundsun.demo.springboot.utils.idgenerator.segmentid;

public enum GenResultEnum {

    INIT_NOT_COMPLETE(-1L, "初始化尚未完成!"),
    KEY_NOT_FOUND(-2L, "未找到需要生成id的key!"),
    NOT_READY(-3L, "生成分布式ID的工具都尚未准备好!");

    /**
     * id(这里也是错误码)
     */
    private final long id;

    public long getId() {
        return id;
    }

    /**
     * 结果
     */
    private final String result;

    public String getResult() {
        return result;
    }

    GenResultEnum(long id, String result) {
        this.id = id;
        this.result = result;
    }
}