package com.coder.common.util;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @Description 负责发送Http请求，负责与python交换数据
 * @Author coder
 * @Date 2022/11/3 17:59
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Scope("singleton")
@Slf4j
public class HttpUtil {


    @Value("${flask.host}:${flask.port}${flask.deepfake.url}")
    private String detectUrl;

    @Value("${flask.host}:${flask.port}${flask.deepfake.quick_url}")
    private String detectQuick;

    /**
     * 发送Post请求
     * @param url 发送的url请求, 如果是null,则使用默认的url
     * @param params 请求参数
     * @return 返回POST请求的JSON结果
     */
    public JSONObject sendPost(String url, MultiValueMap<String, String>params, String mode){
        if(url==null && mode.equals("accuracy")){
            url = getDetectUrl();
        } else if (url == null && mode.equals("speed")) {
            url = getDetectQuick();
        }
        url = "http://" + url;
        RestTemplate client = new RestTemplate();
        client.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/x-www-form-urlencoded;charset=UTF-8"));
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        // 执行Http请求，将返回的结构使用JSONObject类格式化
        ResponseEntity<String> result = client.exchange(url, method, requestEntity, String.class);
        return JSONObject.parseObject(result.getBody());
    }

    /**
     * 发送 get请求
     * @param url get请求的url
     * @param params 发送的参数
     * @return 返回远程http请求结果
     */
    public JSONObject sendGet(String url, MultiValueMap<String, String>params){
        RestTemplate client = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.queryParams(params).build().encode().toUri();
        ResponseEntity<String> result = client.getForEntity(uri,String.class);
        return JSONObject.parseObject(result.getBody());
    }

}