package com.fmbank.remote_call.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fmbank.remote_call.flinkweb.dto.JobAddReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * @ClassName: HttpClientUtil
 * @Description: httpclient 工具类
 * @author: jc
 * @date: 2021/12/25 17:56
 */
@Component
@Slf4j
public class HttpClientUtil {
    private String host = "http://120.76.205.7:8080/api";
    private String username = "admin";
    private String password = "123456";

    private volatile String authorization = "";

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public static String doGet(String url, Map<String, Object> header, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            if (header != null) {
                header.forEach((k, v) -> {
                    httpGet.addHeader(k, String.valueOf(v));
                });
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doGet(String url, Map<String, Object> header) {
        return doGet(url, header, null);
    }

    public static String doPost(String url, Map<String, Object> header, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
                httpPost.setEntity(entity);
            }
            if (header != null) {
                header.forEach((k, v) -> {
                    httpPost.addHeader(k, String.valueOf(v));
                });
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doPost(String url, Map<String, Object> header) {
        return doPost(url, header, null);
    }

    public static String doPostJson(String url, String json) {
        return doPostJson(url, null, json);
    }

    public static String doPostJson(String url, Map<String, Object> header, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            if (header != null) {
                header.forEach((k, v) -> {
                    httpPost.addHeader(k, String.valueOf(v));
                });
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 登录功能
     */
    @Test
    public void login() {
        String loginURL = host + "/auth/login";
        String loginMsg = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"rememberMe\":1}", this.username, this.password);
        log.info("登录flinkx-web, {}, {}.", loginURL, loginMsg);
        String result = doPostJson(loginURL, loginMsg);
        if(!ObjectUtils.isEmpty(result)){
            String code = JSONObject.parseObject(result).get("code").toString();
            if("200".equals(code)){
                authorization= JSONObject.parseObject(JSONObject.parseObject(result).get("content").toString()).get("data").toString();
                System.out.println("authorization-"+authorization);
            }

        }
    }

    /**
     * 测试新增功能
     */
    @Test
    public void testAddTask() {
        //执行登录
        login();

        HashMap<String, Object> headParams = new HashMap<>();
        headParams.put("Authorization",authorization);
        //任务的调度
        String jobDesc = UUID.randomUUID().toString();
        String jobJson = "{\n  \"job\": {\n    \"content\": [\n      {\n        \"reader\": {\n          \"parameter\": {\n            \"path\": \"hdfs://nameservice1/user/hive/warehouse/cdip_data.db/cdip_cust_data_daleizi/000000_0\",\n            \"defaultFS\": \"hdfs://nameservice1\",\n            \"hadoopConfig\": {\n              \"dfs.ha.namenodes.ns1\": \"namenode221,namenode185\",\n              \"dfs.namenode.rpc-address.nameservice1.namenode221\": \"cdhnode02:8020\",\n              \"dfs.namenode.rpc-address.nameservice1.namenode185\": \"cdhmaster02:8020\",\n              \"dfs.client.failover.proxy.provider.ns1\": \"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider\",\n              \"dfs.nameservices\": \"nameservice1\"\n            },\n            \"column\": [\n              {\n                \"name\": \"cust_no\",\n                \"index\": 0,\n                \"type\": \"string\"\n              }\n            ],\n            \"fileType\": \"parquet\"\n          },\n          \"name\": \"hdfsreader\"\n        },\n        \"writer\": {\n          \"parameter\": {\n            \"path\": \"/home/fmftp/ftp/cdip_data/cdip_cust_data_daleizi\",\n            \"fileName\": \"data.csv\",\n            \"protocol\": \"sftp\",\n            \"port\": 22,\n            \"writeMode\": \"append\",\n            \"host\": \"192.168.214.25\",\n            \"column\": [\n              {\n                \"name\": \"cust_no\",\n                \"type\": \"string\"\n              }\n            ],\n            \"password\": \"fmftp\",\n            \"fieldDelimiter\": \",\",\n            \"encoding\": \"utf-8\",\n            \"username\": \"fmftp\"\n          },\n          \"name\": \"ftpwriter\"\n        }\n      }\n    ],\n    \"setting\": {\n      \"restore\": {\n        \"maxRowNumForCheckpoint\": 0,\n        \"isRestore\": false,\n        \"restoreColumnName\": \"\",\n        \"restoreColumnIndex\": 0\n      },\n      \"errorLimit\": {\n        \"record\": 100\n      },\n      \"speed\": {\n        \"bytes\": 0,\n        \"channel\": 1\n      }\n    }\n  }\n}";
        JobAddReq jobAddReq = new JobAddReq();
        jobAddReq.setJobJson(jobJson);
        jobAddReq.setJobDesc(jobDesc + "描述");
        jobAddReq.setJobGroup(1);
        String jobAddMsg = JSON.toJSONString(jobAddReq);
        //返回实例id
        System.out.println(doPostJson(host+"/job/add/",headParams, jobAddMsg));
    }
}
