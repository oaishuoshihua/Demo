package com.sodyu.test.message.controller;

import com.sodyu.test.message.utils.VelocityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuhp on 2017/10/25.
 */
@Controller
public class AnalysisMessageController extends BaseController {
    @RequestMapping(value="/activity")
    public @ResponseBody String activity(String message){

        List<String> test=new ArrayList<String>();
        test.add("ni");
        test.add("hao");
        test.add("a");
        Map<String,Object> h=new HashMap<String,Object>();
        h.put("viplist",test);
        String c= VelocityUtils.mergeTemplate(h, "templates/test.vm");
        System.out.println(c);
        return c;
    }

    @RequestMapping(value="/scenicSpot")
    public void scenicSpot(String message){

    }
}
