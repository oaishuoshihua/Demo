package com.sodyu.test.message.controller;

import com.alibaba.fastjson.JSON;
import com.sodyu.test.message.service.BackupIndexService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by yuhp on 2017/10/27.
 */
@Controller
@RequestMapping("/backup")
public class BackupIndexController {

    @Autowired
    private BackupIndexService service;


    @RequestMapping("/index")
    public String index(HttpServletRequest request,HttpServletResponse response){
        return "backupIndex";
    }



    @RequestMapping("/getSnapshot")
    public @ResponseBody String getAllSnapshot(@RequestParam String cluster){
        if(StringUtils.isBlank(cluster)){
            return null;
        }
        String result=service.getAllSnapshot(cluster);
        return result;
    }

    @RequestMapping("/closeIndex")
    public @ResponseBody String close(@RequestParam String cluster,@RequestParam String indexName){
        if(StringUtils.isBlank(cluster)||StringUtils.isBlank(indexName)){
            return null;
        }
        String status=service.close(cluster,indexName);
        String result=BackupIndexService.SUCCESS_STATUS.equalsIgnoreCase(status)? JSON.toJSONString(BackupIndexService.SUCCESS_STATUS):null;
        return result;
    }

    @RequestMapping("/backupIndex")
    public @ResponseBody String backupIndex(@RequestParam String cluster,@RequestParam String snapshotName,@RequestParam String indexName){
        if(StringUtils.isBlank(cluster)||StringUtils.isBlank(snapshotName)||StringUtils.isBlank(indexName)){
            return null;
        }
        String status=service.backup(cluster, snapshotName, indexName);
        String result=BackupIndexService.SUCCESS_STATUS.equalsIgnoreCase(status)? JSON.toJSONString(BackupIndexService.SUCCESS_STATUS):null;
        return result;
    }

    @RequestMapping("/restoreIndex")
    public @ResponseBody String restoreIndex(@RequestParam String cluster,@RequestParam String snapshotName,@RequestParam String indexName){
        if(StringUtils.isBlank(cluster)||StringUtils.isBlank(snapshotName)||StringUtils.isBlank(indexName)){
            return null;
        }
        String status=service.restore(cluster, snapshotName, indexName);
        String result=BackupIndexService.SUCCESS_STATUS.equalsIgnoreCase(status)? JSON.toJSONString(BackupIndexService.SUCCESS_STATUS):null;
        return result;
    }


}
