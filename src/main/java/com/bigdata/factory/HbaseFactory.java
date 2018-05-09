package com.bigdata.factory;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Async
public class HbaseFactory {

    private static Logger logger = LoggerFactory.getLogger(HbaseFactory.class);

    @Autowired
    private HbaseTemplate htemplate;

/**
     * 写数据
     * @param tableName
     * @param action
     * @return*/


    public Boolean execute(String tableName,String key,String familyName,String qualifier,String value, TableCallback<Boolean> action) {
        return htemplate.execute(tableName, (table)->{
                boolean flag = false;
                try{
                    byte[] rowkey = key.getBytes();
                    Put put = new Put(rowkey);
                    put.add(Bytes.toBytes(familyName),Bytes.toBytes(qualifier), Bytes.toBytes(value));
                    table.put(put);
                    flag = true;
                }catch(Exception e){
                    e.printStackTrace();
                }
                return flag;
        });
    }


    public Boolean batchInsert(String tableName,String familyName,List<Map<String,Object>> colVals) {
        return htemplate.execute(tableName, (table)->{
                boolean flag = false;
                try {
                    List<Put> putList = new ArrayList<Put>();
                    for (Map<String, Object> colVal : colVals) {
                        Put p = null;
                        if (colVal.get("rowKey") != null) {
                            p = new Put(Bytes.toBytes(colVal.get("rowKey").toString()));
                        } else {
                            logger.error("rowKey 为空");
                            return false;
                        }
                        for (String col : colVal.keySet()) {
                            if (!col.equals("rowKey") && !col.equals("family")) {
                                String val = colVal.get(col).toString();
                                if (StringUtils.isNotBlank(val)) {
                                    p.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(col), Bytes.toBytes(val));
                                }
                            }
                        }
                        putList.add(p);
                    }
                    if (!putList.isEmpty()) {
                        table.put(putList);
                    } else {
                       logger.info("putList 为空");
                        return false;
                    }
                    logger.info("插入数据完成");
                    flag = true;

                } catch (Exception e) {
                    logger.error("insert into hbase error",e);
                }
                return flag;
        });
    }



/**
     * 通过表名和key获取一行数据
     * @param tableName
     * @param rowName
     * @return
     */

    public Map<String, Object> get(String tableName, String rowName) {
        return htemplate.get(tableName, rowName,(result, rowNum) ->{
                List<Cell> ceList =   result.listCells();
                Map<String,Object> map = new HashMap<String, Object>();
                if(ceList!=null&&ceList.size()>0){
                    for(Cell cell:ceList){
                        map.put(Bytes.toString(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength())+
                                        "_"+Bytes.toString( cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength()),
                                Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                    }
                }
                return  map;
        });
    }

/**
     * 通过表名  key 和 列族 和列 获取一个数据
     * @param tableName
     * @param rowName
     * @param familyName
     * @param qualifier
     * @return*/


    public String get(String tableName ,String rowName, String familyName, String qualifier) {
        return htemplate.get(tableName, rowName,familyName,qualifier ,(result, rowNum) ->{
                List<Cell> ceList =   result.listCells();
                String res = "";
                if(ceList!=null&&ceList.size()>0){
                    for(Cell cell:ceList){
                        res = Bytes.toString( cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    }
                }
                return res;
        });
    }


/**
     * 通过表名，开始行键和结束行键获取数据
     * @param tableName
     * @param startRow
     * @param stopRow
     * @return
     */

    public List<Map<String,Object>> find(String tableName , String startRow,String stopRow) {
        Scan scan = new Scan();
        if(startRow==null){
            startRow="";
        }
        if(stopRow==null){
            stopRow="";
        }
        scan.setStartRow(Bytes.toBytes(startRow));
        scan.setStopRow(Bytes.toBytes(stopRow));
        PageFilter filter = new PageFilter(5);
         scan.setFilter(filter);

        return     htemplate.find(tableName, scan,(result, rowNum) -> {
            List<Cell> ceList = result.listCells();
            Map<String, Object> map = new HashMap<String, Object>();
            String row = "";
            if (ceList != null && ceList.size() > 0) {
                for (Cell cell : ceList) {
                    row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                    String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    String family = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
                    String quali = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                    map.put(family + "_" + quali, value);
                }
                map.put("row", row);
            }
            return map;
        });
    }

}
