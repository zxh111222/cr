package com.example.cr.common.util;

import cn.hutool.core.util.IdUtil;

public class SnowflakeUtil {
    // 数据中心
    private static final long dataCenterID = 1L;

    // 机器标识
    private static final long workerId = 1L;

    public static long getId() {
        return IdUtil.getSnowflake(workerId, dataCenterID).nextId();
    }

    public static String getIdToStr() {
        return String.valueOf(IdUtil.getSnowflake(dataCenterID, workerId).nextId());
    }
}
