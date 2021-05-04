package com.health.sms.service;

import com.health.sms.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SendMessage {

    private String host = "http://dingxin.market.alicloudapi.com";
    private String path = "/dx/sendSms";
    private String method = "POST";
    private String appcode = "3da9045d41a5470583372072aac68369";
    private String tpl_id = "TP1711063";

    public HttpResponse sendMessage(String mobile,String param) throws Exception {


        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        querys.put("param", "code:"+param);
        querys.put("tpl_id", tpl_id);
        Map<String, String> bodys = new HashMap<String, String>();

        HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);

        return response;

    }
}
