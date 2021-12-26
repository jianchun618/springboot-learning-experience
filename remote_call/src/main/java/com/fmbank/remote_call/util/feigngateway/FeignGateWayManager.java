package com.fmbank.remote_call.util.feigngateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fmbank.remote_call.util.FeignHttpClient;
import feign.Feign;
import feign.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: FeignGateWayManager
 * @Description:
 * @author: jc
 * @date: 2021/12/9 16:12
 */
@Component
@Slf4j
public class FeignGateWayManager implements InitializingBean {

    private String httpUrl = "http://ip:8080/api";
    String loginPath = "/auth/login";
    String addJobPath = "/job/add/";

    private Map<String, String> headerMap;
    private FeignHttpClient client;

    private String username = "admin";
    private String password = "XXX";

    private volatile String authorization = "";

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    /**
     * 访问登录接口
     *
     * @param userName
     * @param passWord
     * @return
     */
    public String login(String userName, String passWord) {
        try {
            String loginMsg = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"rememberMe\":1}", userName, passWord);
            String result = client.login(new URI(loginPath), loginMsg, headerMap);
            log.info("调用[{}]成功. res：{}", httpUrl + loginPath, result);
            if (!ObjectUtils.isEmpty(result)) {
                String code = JSONObject.parseObject(result).get("code").toString();
                if ("200".equals(code)) {
                    authorization = JSONObject.parseObject(JSONObject.parseObject(result).get("content").toString()).get("data").toString();
                    log.info("登录成功-返回信息-{} ", authorization);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("调用[{}]登录接口异常. 异常信息：{}", httpUrl, e.getMessage());
            JSONObject error = new JSONObject();
            error.put("code", "500");
            error.put("msg", e.getMessage());
            return JSON.toJSONString(error);
        }
    }


    public String addJob(String req) {
        try {
            login(this.username, this.password);
            headerMap.put("Authorization", authorization);
            return client.addJob(new URI(addJobPath), req, headerMap);
        } catch (Exception e) {
            log.error("调用[{}]新增接口异常. 异常信息：{}", httpUrl, e.getMessage());
            JSONObject error = new JSONObject();
            error.put("code", "500");
            error.put("msg", e.getMessage());
            return JSON.toJSONString(error);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        client = Feign.builder()
                .client(new OkHttpClient())
                .target(FeignHttpClient.class, httpUrl);
        headerMap = new HashMap<>();
        headerMap.put("test", "test");
    }
}
