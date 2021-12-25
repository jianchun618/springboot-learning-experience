package com.fmbank.remote_call.flinkweb.dto;

import java.io.Serializable;

/**
 * @ClassName: CommonRes
 * @Description:
 * @author: jc
 * @date: 2021/12/25 18:56
 */
public class CommonRes implements Serializable {
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean success() {
        return 200 == code;
    }

    public boolean forbidden() {
        return 403 == code;
    }
}
