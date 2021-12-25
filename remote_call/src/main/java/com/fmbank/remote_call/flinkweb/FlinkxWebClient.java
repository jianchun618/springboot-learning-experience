package com.fmbank.remote_call.flinkweb;


import com.alibaba.fastjson.JSON;
import com.fmbank.remote_call.flinkweb.dto.CommonRes;
import com.fmbank.remote_call.flinkweb.dto.JobAddReq;
import com.fmbank.remote_call.flinkweb.dto.JobAddRes;
import com.fmbank.remote_call.flinkweb.dto.LoginRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * @ClassName: FlinkxWebClient
 * @Description: 向flinkx提交任务使用
 * @author: jc
 * @date: 2021/12/25 18:56
 */
@Component
@Slf4j
public class FlinkxWebClient {
    private String host = "http://ip:8080/api";
    private String username = "admin";
    private String password = "123456";
    private volatile String authorization = "";
    private int jobGroup = 1;

    @Test
    public void test() throws IOException {
        login();
    }

    // 登录
    private boolean login() {
        String loginURL = host + "/auth/login";
        String method = "POST";
        String loginMsg = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"rememberMe\":1}", this.username, this.password);

        log.info("登录flinkx-web, {}, {}.", loginURL, loginMsg);
        String data = requestData(loginURL, method, loginMsg);

        LoginRes loginRes = JSON.parseObject(data, LoginRes.class);
        if (loginRes.success()) {
            log.info("登录flinkx-web成功.");
            this.authorization = loginRes.getContent().getData();
            return true;
        }

        return false;
    }

    @Test
    public void testAddTask() {
        String jobDesc = UUID.randomUUID().toString();
        String jobJson = "{\n  \"job\": {\n    \"content\": [\n      {\n        \"reader\": {\n          \"parameter\": {\n            \"path\": \"hdfs://nameservice1/user/hive/warehouse/cdip_data.db/cdip_cust_data_daleizi/000000_0\",\n            \"defaultFS\": \"hdfs://nameservice1\",\n            \"hadoopConfig\": {\n              \"dfs.ha.namenodes.ns1\": \"namenode221,namenode185\",\n              \"dfs.namenode.rpc-address.nameservice1.namenode221\": \"cdhnode02:8020\",\n              \"dfs.namenode.rpc-address.nameservice1.namenode185\": \"cdhmaster02:8020\",\n              \"dfs.client.failover.proxy.provider.ns1\": \"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider\",\n              \"dfs.nameservices\": \"nameservice1\"\n            },\n            \"column\": [\n              {\n                \"name\": \"cust_no\",\n                \"index\": 0,\n                \"type\": \"string\"\n              }\n            ],\n            \"fileType\": \"parquet\"\n          },\n          \"name\": \"hdfsreader\"\n        },\n        \"writer\": {\n          \"parameter\": {\n            \"path\": \"/home/fmftp/ftp/cdip_data/cdip_cust_data_daleizi\",\n            \"fileName\": \"data.csv\",\n            \"protocol\": \"sftp\",\n            \"port\": 22,\n            \"writeMode\": \"append\",\n            \"host\": \"192.168.214.25\",\n            \"column\": [\n              {\n                \"name\": \"cust_no\",\n                \"type\": \"string\"\n              }\n            ],\n            \"password\": \"fmftp\",\n            \"fieldDelimiter\": \",\",\n            \"encoding\": \"utf-8\",\n            \"username\": \"fmftp\"\n          },\n          \"name\": \"ftpwriter\"\n        }\n      }\n    ],\n    \"setting\": {\n      \"restore\": {\n        \"maxRowNumForCheckpoint\": 0,\n        \"isRestore\": false,\n        \"restoreColumnName\": \"\",\n        \"restoreColumnIndex\": 0\n      },\n      \"errorLimit\": {\n        \"record\": 100\n      },\n      \"speed\": {\n        \"bytes\": 0,\n        \"channel\": 1\n      }\n    }\n  }\n}";
        System.out.println(jobAdd(jobDesc, jobJson));
    }

    /**
     * 新增任务
     *
     * @param jobDesc
     * @param jobJson
     * @return
     */
    public String jobAdd(String jobDesc, String jobJson) {
        if (!checkLogin()) {
            return "";
        }

        String jobAddURL = host + "/job/add/";
        String method = "POST";

        JobAddReq jobAddReq = new JobAddReq();
        jobAddReq.setJobJson(jobJson);
        jobAddReq.setJobDesc(jobDesc);
        jobAddReq.setJobGroup(jobGroup);
        String jobAddMsg = JSON.toJSONString(jobAddReq);

        log.info("执行flinkx-web任务新增接口, {} .", jobAddMsg);
        String data = requestData(jobAddURL, method, jobAddMsg);
        log.info("执行flinkx-web任务新返回调用-data:" + data);
        JobAddRes jobAddRes = JSON.parseObject(data, JobAddRes.class);
        if (jobAddRes.success()) {
            log.info("执行flinkx-web任务新增接口成功.");
            return jobAddRes.getContent();
        }

        if (jobAddRes.forbidden()) {
            log.warn("执行flinkx-web任务新增接口,没有权限.");
            if (login()) {
                data = requestData(jobAddURL, method, jobAddMsg);
                jobAddRes = JSON.parseObject(data, JobAddRes.class);
            }
        }

        if (jobAddRes.success()) {
            log.info("执行flinkx-web任务新增接口,再次执行成功..");
            return jobAddRes.getContent();
        }

        log.warn("执行flinkx-web任务新增接口,失败..");
        return "";
    }

    /**
     * 执行一次任务
     *
     * @param jobId
     * @return
     */
    public boolean jobTrigger(Integer jobId) {
        if (!checkLogin()) {
            return false;
        }

        String jobTriggerURL = host + "/job/trigger/";
        String method = "POST";

        String jobTriggerMsg = String.format("{\"jobId\":%d,\"executorParam\":\"\"}", jobId);

        log.info("执行flinkx-web任务-执行一次任务接口,请求数据: {} .", jobTriggerMsg);
        String data = requestData(jobTriggerURL, method, jobTriggerMsg);
        log.info("执行flinkx-web任务-执行一次任务接口,返回响应: {} .", data);
        CommonRes commonRes = JSON.parseObject(data, CommonRes.class);

        return commonRes.success();
    }

    // 获取执行状态
    public void getExecuteResult() {
    }

    private boolean checkLogin() {
        if ("".equals(authorization)) {
            synchronized (FlinkxWebClient.class) {
                if ("".equals(authorization)) {
                    login();
                }
            }
        }
        return !"".equals(authorization);
    }

    private String requestData(String urlStr, String method, String line) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        HttpURLConnection connection = null;
        boolean connecteSuccess = false;

        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod(method);
            connection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
            if (!"".equals(authorization)) {
                connection.addRequestProperty("Authorization", authorization);
            }

            bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(line);
            bw.flush();

            connection.connect();

            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connecteSuccess = true;
            StringBuilder sb = new StringBuilder();
            int i = 0;
            String resLine = null;
            while ((resLine = br.readLine()) != null) {
                if (i++ > 0) {
                    sb.append("\n");
                }
                sb.append(resLine);
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("FlinkxWeb访问异常", e);
            if (null != connection && !connecteSuccess) {
                return readError(connection);
            }
        } finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    log.error("FlinkxWeb访问异常,关闭bw异常", e);
                }
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("FlinkxWeb访问异常,关闭br异常", e);
                }
            }
        }

        return "";
    }

    private String readError(HttpURLConnection connection) {
        int code = 0;
        try {
            code = connection.getResponseCode();
        } catch (IOException e) {
            log.error("获取异常返回状态码", e);
        }

        String res = String.format("{\"code\":\"%s\"}", code + "");

        return res;
    }


}
