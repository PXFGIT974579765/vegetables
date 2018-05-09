package com.bigdata.task;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class WeatherSchedule {

    @Autowired
    private WeatherSpiderTask weatherSpiderTask;

    @Scheduled(cron = "0 0 3 * * ?")
    public void weather(){

        //设置一个起始时间，2345天气网只能获取一年内的数据。
        try {
            String chengdu = "56294";
            String chongqing = "57516";
            String guiyang = "57816";
            OkHttpClient client = new OkHttpClient();
            Map<String,String> cityMap = new LinkedHashMap<>();
            cityMap.put("chengdu",chengdu);
            cityMap.put("chongqing",chongqing);
            cityMap.put("guiyang",guiyang);
            weatherSpiderTask.getWeather(client,cityMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
