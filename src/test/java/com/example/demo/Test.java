package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: suxz
 * @Date: 2019/4/20 4:22 AM
 * @Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Test {
    @org.junit.Test
    public void main() throws Exception{
        doPostForHuaweiChannel();
    }
    public void doPostForHuaweiChannel() throws Exception {
        String title = "快应用提示消息";
        String content = "今天还没有签到，快来签到得奖励吧！";
        String fastAppUrl = "/Index";
        String apiUrl = "https://api.push.hicloud.com/pushsend.do";
        JSONObject nsp_ctx = new JSONObject();
        nsp_ctx.put("ver", "1");
        nsp_ctx.put("appId", "100573051");
        Map<String, String> heards = new HashMap<>();
        heards.put("Content-Type", "application/x-www-form-urlencoded");
        String url = apiUrl + "?nsp_ctx=" + URLEncoder.encode(nsp_ctx.toString(), "UTF-8");
        System.out.println(nsp_ctx.toString());
        System.out.println("url====" +url);
        Map<String, String> params = new HashMap<>();
        String accessToken = gainAccessToken();
        Long nsp_ts = System.currentTimeMillis() /1000;
        String nsp_svc = "openpush.message.api.send";
        params.put("access_token", accessToken);
        params.put("nsp_ts", String.valueOf(nsp_ts));
        params.put("nsp_svc", nsp_svc);
        //params.put("expire_time", URLEncoder.encode(expire_time,"UTF-8"));
        ArrayList<String> strings = new ArrayList<>();
        //strings.add(URLEncoder.encode("0867420039422201300001717400CN01","UTF-8"));
        strings.add(URLEncoder.encode("0867420039422201300003543100CN01","UTF-8"));
        params.put("device_token_list", JSON.toJSON(strings.toArray()).toString());

        JSONObject obj = new JSONObject();
        JSONObject hps = new JSONObject();
        JSONObject msg = new JSONObject();
        JSONObject body = new JSONObject();
        JSONObject pushbody = new JSONObject();
        msg.put("type", 1);
        //● 0：通知栏消息
        //● 1：透传消息
        body.put("pushtype", 0);
        pushbody.put("title", title);
        pushbody.put("description", content);
        pushbody.put("page", fastAppUrl);
        //扩展参数
        pushbody.put("params", new JSONObject());
        msg.put("body", body);
        body.put("pushbody", pushbody);
        hps.put("msg", msg);

        obj.put("hps",hps);

        params.put("payload", obj.toString());
        System.out.println("payload=========" + obj.toString());

        System.out.println(JSON.toJSON(params));
        HttpClientResult httpClientResult = HttpClientUtils.doPost(url, heards, params);
        System.out.println(httpClientResult + ">>>>>>>>>>>>>");

    }

    /**
     * 获取accessToken
     *
     * @return
     * @throws Exception
     */
    public String gainAccessToken() throws Exception {
        String apiUrl = "https://login.cloud.huawei.com/oauth2/v2/token";
        Map<String, String> heards = new HashMap<>();
        heards.put("Content-Type", "application/x-www-form-urlencoded");
        String grant_type = "client_credentials";
        //应用ID
        String client_id = "100573051";
        //应用秘钥
        String client_secret = "e283cf5a53e4cb17dd77a471e07284e9";
        //权限列表（不传此参数，默认权限）
        String scope = "";
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", grant_type);
        params.put("client_id", client_id);
        params.put("client_secret", client_secret);
        HttpClientResult httpClientResult = HttpClientUtils.doPost(apiUrl, heards, params);
        System.out.println("httpClientResult============" + httpClientResult);
        Integer code = httpClientResult.getCode();
        String content = httpClientResult.getContent();
        HwgainAccessTokenResponse accessTokenResponse = JSON.parseObject(content, HwgainAccessTokenResponse.class);
        String responseError = accessTokenResponse.getError();
        return accessTokenResponse.getAccess_token();

    }

    /**
     * 服务器请求时间戳 获取1970-1-1 0:0:0至今的秒数
     *
     * @return
     * @throws Exception
     */
    public Long getTime() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d h:m:s");
        long time = simpleDateFormat.parse("1970-1-1 0:0:0").getTime();
        long currentTime = System.currentTimeMillis();
        return currentTime - time;}

}
