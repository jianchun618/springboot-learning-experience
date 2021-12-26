package com.fmbank.remote_call.util;

import feign.HeaderMap;
import feign.Headers;
import feign.RequestLine;

import java.net.URI;
import java.util.Map;

/**
 * @ClassName: FeignClient
 * @Description: FeignClient客户端
 * @author: jc
 * @date: 2021/11/25 16:23
 */
public interface FeignHttpClient {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST")
    String login(URI uri, String req, @HeaderMap Map<String, String> map);


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST")
    String addJob(URI uri, String req, @HeaderMap Map<String, String> map);

}
