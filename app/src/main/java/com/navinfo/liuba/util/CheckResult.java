package com.navinfo.liuba.util;

import com.alibaba.fastjson.JSON;

/**
 * 数据校验结果类。用来记录数据校验的详细结果
 *
 * @author hb
 */
public class CheckResult {
    private boolean isSuccess;
    private String descript;

    public CheckResult(boolean isSuccess, String descript) {
        this.isSuccess = isSuccess;
        this.descript = descript;
    }

    /**
     * 校验结果的描述
     *
     * @return 描述
     */
    public String getDescript() {
        return descript;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getJsonString() {
        return JSON.toJSONString(this);
    }

    public static CheckResult getCheckResult(String jsonStr) {
        return JSON.parseObject(jsonStr, CheckResult.class);
    }

    public static final CheckResult RESULT_SUCCESS = new CheckResult(true, "成功");
    public static final CheckResult RESULT_FAIL = new CheckResult(false, "失败");
}
