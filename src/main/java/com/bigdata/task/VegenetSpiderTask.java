package com.bigdata.task;

import com.bigdata.entity.AreaUrl;
import com.bigdata.entity.Vegetables;
import com.bigdata.factory.HbaseFactory;
import com.bigdata.util.ChangeToPinYin;
import com.bigdata.util.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class VegenetSpiderTask implements Runnable {

     private static Logger logger = LoggerFactory.getLogger(VegenetSpiderTask.class);

     private BlockingQueue<Object> blockingQueue;
     private WebDriver driver;
     private ChangeToPinYin changeToPinYin = new ChangeToPinYin();
     private static String TABLENAME = "vegetables";
     private static String FAMILYNAME = "info";
     private HbaseFactory hbaseFactory;

    public VegenetSpiderTask(BlockingQueue blockingQueue,WebDriver driver,HbaseFactory hbaseFactory){
        this.blockingQueue = blockingQueue;
        this.driver = driver;
        this.hbaseFactory = hbaseFactory;
    }

    public VegenetSpiderTask(BlockingQueue blockingQueue,WebDriver driver){
        this.blockingQueue = blockingQueue;
        this.driver = driver;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                AreaUrl areaUrl = (AreaUrl) blockingQueue.take();
                driver.get(areaUrl.getUrl());
                logger.info(driver.getCurrentUrl());
                List<WebElement> p = driver.findElement(By.className("pri_k")).findElements(By.tagName("p"));
                List<Map<String,Object>> vegetablesList = p.stream().map(line->line.getText().split("\n")).map(row-> {
                    Date date = null;
                    Vegetables vegetables = new Vegetables();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat df1 = new SimpleDateFormat("yyyy-M-d");
                    DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
                    try {
                        date = df1.parse(row[0].replace("[", "").replace("]", ""));
                        vegetables.setUpdateTime(df.format(date));
                        vegetables.setvName(row[1]);
                        String vegeName = changeToPinYin.getStringPinYin(row[1]);
                        StringBuffer sb = new StringBuffer();
                        sb.append(areaUrl.getArea()).append("_").append(vegeName).append("_").append(df2.format(date));
                        vegetables.setRowKey(sb.toString());
                        vegetables.setvNameEn(vegeName);
                        vegetables.setvMarket(row[2]);
                        vegetables.setlPrice(row[3].replace("¥", ""));
                        vegetables.sethPrice(row[4].replace("¥", ""));
                        vegetables.setvPrice(row[5].replace("¥", ""));
                        vegetables.setInsertTime(df.format(new Date()));
                        vegetables.setArea(areaUrl.getArea());
                        vegetables.setSource("vegnet.com.cn");
                    } catch (ParseException e1) {
                        logger.error("parse html error", e1);
                    }
                    return Util.transBean2Map(vegetables);
                }).collect(Collectors.toList());

                hbaseFactory.batchInsert(TABLENAME ,FAMILYNAME,vegetablesList);
                //Util.saveData(JSONObject.toJSONString(vegetablesList),"d:\\documents\\vegetables.txt");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
