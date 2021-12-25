package com.fmbank.remote_call.flinkweb.dto;

/**
 * @ClassName: LoginRes
 * @Description:
 * @author: jc
 * @date: 2021/12/25 18:56
 */
public class LoginRes extends CommonRes {
    private LoginResContent content;

    public LoginResContent getContent() {
        return content;
    }

    public void setContent(LoginResContent content) {
        this.content = content;
    }

    public static class LoginResContent {
        String data;
        String[] roles;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }
    }
}
