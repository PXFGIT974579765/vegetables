package com.bigdata.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigdata.entity.Weather;
import com.bigdata.factory.HbaseFactory;
import com.bigdata.util.Util;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeatherSpiderTask {


    @Autowired
    private HbaseFactory hbaseFactory;

    private static String TABLENAME = "weather";
    private static String FAMILYNAME = "info";

    /**
     * 用okhttp获取http接口数据
     *
     * @param client
     * @param cityMap
     */
    public void getWeather(OkHttpClient client, Map<String, String> cityMap) {
        cityMap.forEach((k, v) -> {
            try {
                String startDate = "201601";
                DateFormat df = new SimpleDateFormat("yyyyMM");
                Calendar cl = Calendar.getInstance();
                Date date = df.parse(startDate);
                cl.setTime(date);
                //List<Weather> weatherList = new ArrayList<Weather>();
                List<Map<String,Object>> weatherList = new ArrayList<>();
                for (int i = 0; i < 30; i++) {
                    cl.add(Calendar.MONTH, 1);
                    String temp = "";
                    String city = v;
                    DateFormat df1 = new SimpleDateFormat("yyyyMM");
                    temp = df1.format(cl.getTime());
                    String url = String.format("http://tianqi.2345.com/t/wea_history/js/%s/%s_%s.js", temp, city, temp);
                    System.out.println(url);
                    Request request = new Request.Builder().url(url).build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        byte[] bytes = response.body().bytes();
                        String json = new String(bytes, "gbk");
                        if (!json.isEmpty()) {
                            json = json.replace("var weather_str=", "").replace(";", "");
                            JSONObject jsonObject = JSONObject.parseObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("tqInfo");
                            jsonArray.forEach((o) -> {
                                JSONObject jo = (JSONObject) o;
                                Weather weather = jo.toJavaObject(Weather.class);
                                weather.setArea(k);
                                //weatherList.add(weather);
                                String randoms = Util.getFourRandom();
                                if (weather.getYmd() != null) {
                                    weather.setRowKey(randoms.concat("_").concat(k).concat("_").concat(weather.getYmd()));
                                    weatherList.add(Util.transBean2Map(weather));
                                }
                            });
                        }
                    }
                }
                if (!weatherList.isEmpty()) {
                    String path = "d:\\documents\\weather.txt";
                    hbaseFactory.batchInsert(TABLENAME, FAMILYNAME, weatherList);
                }
                //Util.saveData(JSONObject.toJSONString(weatherList), path);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        });
    }

}
