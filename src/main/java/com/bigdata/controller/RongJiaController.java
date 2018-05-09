package com.bigdata.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class RongJiaController {

    public Map<String,Integer> concurrentHashMap = new ConcurrentHashMap<String,Integer>();

    @GetMapping("/rj")
    public void RongJia() throws IOException {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
        String referer = "http://www.cdprice.cn/price/homePageAction!goProductDetail.do?productid=2139&random=0.16177586062316052&paramid=2139&saveParam=2";
        Headers.Builder headersBuilder = new Headers.Builder()
                .add("Referer", referer);
        Headers requestHeaders = headersBuilder.build();
        String url = String.format("http://tianqi.2345.com/t/wea_history/js/%s/56294_%s.js");
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            byte[] bytes = response.body().bytes();
            String json = new String(bytes, "gbk");
            json = json.replace("var weather_str=", "").replace(";", "");
            JSONObject jsonObject = JSONObject.parseObject(json);
            JSONArray jsonArray =  jsonObject.getJSONArray("tqInfo");
        }
    }


    public void GetProductId(){

    }


}
