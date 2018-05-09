package com.bigdata.task;


import com.bigdata.entity.AreaUrl;
import com.bigdata.factory.DriverFactory;
import com.bigdata.factory.HbaseFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class VegenetSchedule {

    @Autowired
    private HbaseFactory hbaseFactory;

    @Autowired
    private DriverFactory driverFactory;

    private ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(2);
    private BlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<Object>();

    @Scheduled(cron = "0 0 3 * * ?")
    public void index() {

        String chengdu = "http://www.vegnet.com.cn/Market/369.html";
        String chongqing = "http://www.vegnet.com.cn/Market/497.html";
        String guiyang = "http://www.vegnet.com.cn/Market/495.html";
        Map<String, String> map = new LinkedHashMap<String,String>();
        map.put("chongqing", chongqing);
        map.put("guiyang", guiyang);
        map.put("chengdu", chengdu);
        map.forEach((k, v) -> {
            String area = k;
            String url = v;
            try {

                WebDriver driver = driverFactory.getPhantomJSDriver();
                driver.get(url);
                System.out.println(driver.getCurrentUrl());
                //延时等待js加载完毕
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<WebElement> href = driver.findElement(By.className("price_list")).findElements(By.tagName("a"));
                href.stream().forEach(p -> {
                    String ahref = p.getAttribute("href");
                    try {
                        AreaUrl areaUrl = new AreaUrl();
                        areaUrl.setArea(area);
                        areaUrl.setUrl(ahref);
                        blockingQueue.put(areaUrl);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                newFixedThreadPool.execute(new VegenetSpiderTask(blockingQueue, driver, hbaseFactory));
                //newFixedThreadPool.execute(new VegenetSpiderTask(blockingQueue, driver));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
