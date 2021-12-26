package com.fmbank.remote_call.controller;

import com.alibaba.fastjson.JSON;
import com.fmbank.remote_call.flinkweb.dto.JobAddReq;
import com.fmbank.remote_call.util.feigngateway.FeignGateWayManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;

@Controller
@RequestMapping(value = "feign")
public class FeignController {

    @Resource
    FeignGateWayManager feignGateWayManager;

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam String userName, @RequestParam String password) {
        return feignGateWayManager.login(userName, password);
    }

    @RequestMapping(value = "/addJob", method = {RequestMethod.POST})
    @ResponseBody
    public String addJobInfo() {
        //新增
        String jobDesc = UUID.randomUUID().toString();
        String jobJson = "{\n  \"job\": {\n    \"content\": [\n      {\n        \"reader\": {\n          \"parameter\": {\n            \"path\": \"hdfs://nameservice1/user/hive/warehouse/cdip_data.db/cdip_cust_data_daleizi/000000_0\",\n            \"defaultFS\": \"hdfs://nameservice1\",\n            \"hadoopConfig\": {\n              \"dfs.ha.namenodes.ns1\": \"namenode221,namenode185\",\n              \"dfs.namenode.rpc-address.nameservice1.namenode221\": \"cdhnode02:8020\",\n              \"dfs.namenode.rpc-address.nameservice1.namenode185\": \"cdhmaster02:8020\",\n              \"dfs.client.failover.proxy.provider.ns1\": \"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider\",\n              \"dfs.nameservices\": \"nameservice1\"\n            },\n            \"column\": [\n              {\n                \"name\": \"cust_no\",\n                \"index\": 0,\n                \"type\": \"string\"\n              }\n            ],\n            \"fileType\": \"parquet\"\n          },\n          \"name\": \"hdfsreader\"\n        },\n        \"writer\": {\n          \"parameter\": {\n            \"path\": \"/home/fmftp/ftp/cdip_data/cdip_cust_data_daleizi\",\n            \"fileName\": \"data.csv\",\n            \"protocol\": \"sftp\",\n            \"port\": 22,\n            \"writeMode\": \"append\",\n            \"host\": \"192.168.214.25\",\n            \"column\": [\n              {\n                \"name\": \"cust_no\",\n                \"type\": \"string\"\n              }\n            ],\n            \"password\": \"fmftp\",\n            \"fieldDelimiter\": \",\",\n            \"encoding\": \"utf-8\",\n            \"username\": \"fmftp\"\n          },\n          \"name\": \"ftpwriter\"\n        }\n      }\n    ],\n    \"setting\": {\n      \"restore\": {\n        \"maxRowNumForCheckpoint\": 0,\n        \"isRestore\": false,\n        \"restoreColumnName\": \"\",\n        \"restoreColumnIndex\": 0\n      },\n      \"errorLimit\": {\n        \"record\": 100\n      },\n      \"speed\": {\n        \"bytes\": 0,\n        \"channel\": 1\n      }\n    }\n  }\n}";
        JobAddReq jobAddReq = new JobAddReq();
        jobAddReq.setJobJson(jobJson);
        jobAddReq.setJobDesc(jobDesc + "描述");
        jobAddReq.setJobGroup(1);
        String jobAddMsg = JSON.toJSONString(jobAddReq);
        return feignGateWayManager.addJob(jobAddMsg);
    }
}