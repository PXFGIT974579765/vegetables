package com.bigdata.controller;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class HbaseDemo {

    /*@Autowired
    private HbaseFactory hbaseFactory;

    @GetMapping("hbase")
    public void Hbase(){
        try {
            Map map = hbaseFactory.get("nongye","123456");

            //hbaseFactory.addOneRecord("nongye","123456","message1","a","1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Autowired
    private SolrClient client;

    //待索引、查询字段
    public static String[] docs = {"Solr是一个独立的企业级搜索应用服务器",
            "它对外提供类似于Web-service的API接口",
            "用户可以通过http请求",
            "向搜索引擎服务器提交一定格式的XML文件生成索引",
            "也可以通过Http Get操作提出查找请求",
            "并得到XML格式的返回结果"};

    @GetMapping("/solr")
    public void testSolr() throws IOException, SolrServerException {
        int i=0;
        List<SolrInputDocument> solrDocs = new ArrayList<SolrInputDocument>();
        for (String content : docs) {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", i++);
            doc.addField("content_test", content);
            solrDocs.add(doc);
        }
        try {
            client.add(solrDocs);
            client.commit();
        } catch (SolrServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
