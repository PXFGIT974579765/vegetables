package com.bigdata.controller;


import com.bigdata.task.VegenetSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VegnetController{

    @Autowired
    private VegenetSchedule vegenetSchedule;

    @GetMapping("vegetable")
    public void getvege(){
        vegenetSchedule.index();
    }


}