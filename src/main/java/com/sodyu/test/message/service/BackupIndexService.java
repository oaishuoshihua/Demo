package com.sodyu.test.message.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sodyu.test.message.enums.ClusterUrlEnums;
import com.sodyu.test.message.utils.HttpClientUtil;
import com.sodyu.test.message.utils.JSONUtil;
import com.sun.org.apache.bcel.internal.generic.FNEG;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuhp on 2017/10/27.
 */
@Component
public class BackupIndexService {

    private static final Logger logger = LoggerFactory.getLogger(BackupIndexService.class);
    private static final String URL_SPLIT = "/";
    private static final String COMMAND_CLOSE = "_close";
    private static final String COMMAND_RESTORE = "_restore";
    private static final String COMMAND_ALL = "_all";
    private static final String BACKUP_REPERTORY = "_snapshot/my_backup";
    public static final String SUCCESS_STATUS = "SUCCESS";
    public static final String FAIL_STATUS = "FAIL";


    /**
     * 关闭索引
     *
     * @param indexName
     * @param cluster
     * @return
     */
    public String close(String cluster, String indexName) {
        String resultmsg = null;
        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(cluster)) {
            return resultmsg;
        }
        StringBuilder url = new StringBuilder();

        if (cluster.equalsIgnoreCase(ClusterUrlEnums.ONE.getType())) {
            url.append(ClusterUrlEnums.ONE.getUrl());
        } else if (cluster.equalsIgnoreCase(ClusterUrlEnums.TWO.getType())) {
            url.append(ClusterUrlEnums.TWO.getUrl());
        }
        url.append(URL_SPLIT).append(indexName).append(URL_SPLIT).append(COMMAND_CLOSE);
        try {
            HttpResponse response = HttpClientUtil.execute(HttpMethod.POST, url.toString(), null, null);
            if (response != null && response.getStatusLine() != null) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JSONObject jsonObject=JSONUtil.getJSONObject(msg);
                    resultmsg=jsonObject!=null&&Boolean.TRUE.toString().equalsIgnoreCase(JSONUtil.getString(jsonObject, "acknowledged"))?SUCCESS_STATUS:FAIL_STATUS;
                } else {
                    resultmsg = response.getStatusLine().toString();
                }
            } else {
                resultmsg = "response is  " + response.toString();
            }
        } catch (Exception e) {
            logger.error("关闭索引{}出错", indexName, e);
        }

        return resultmsg;
    }

    /**
     * 备份索引
     *
     * @param indexName
     * @param snapshotName
     * @param cluster
     * @return
     */
    public String backup(String cluster, String snapshotName,String indexName) {
        String resultmsg = null;
        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(cluster) || StringUtils.isBlank(snapshotName)) {
            return resultmsg;
        }
        StringBuilder url = new StringBuilder();

        if (cluster.equalsIgnoreCase(ClusterUrlEnums.ONE.getType())) {
            url.append(ClusterUrlEnums.ONE.getUrl());
        } else if (cluster.equalsIgnoreCase(ClusterUrlEnums.TWO.getType())) {
            url.append(ClusterUrlEnums.TWO.getUrl());
        }
        url.append(URL_SPLIT).append(BACKUP_REPERTORY).append(URL_SPLIT).append(snapshotName);
        Map<String, String> params = new HashMap<String, String>();
        params.put("indices", indexName);
        try {
            HttpResponse response = HttpClientUtil.execute(HttpMethod.PUT, url.toString(), params, null);
            if (response != null) {
                if (response.getStatusLine() != null) {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
                        JSONObject jsonObject=JSONUtil.getJSONObject(msg);
                        resultmsg=jsonObject!=null&&Boolean.TRUE.toString().equalsIgnoreCase(JSONUtil.getString(jsonObject, "accepted"))?SUCCESS_STATUS:FAIL_STATUS;
                    } else {
                        resultmsg = response.getStatusLine().toString();
                    }
                } else {
                    resultmsg = "response.getStatusLine() is null " + response.toString();
                }
            }
        } catch (Exception e) {
            logger.error("备份索引{}出错", indexName, e);
        }

        return resultmsg;
    }

    /**
     * 恢复索引
     *
     * @param indexName
     * @param snapshotName
     * @param cluster
     * @return
     */
    public String restore( String cluster, String snapshotName,String indexName) {
        String resultmsg = null;
        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(cluster) || StringUtils.isBlank(snapshotName)) {
            return resultmsg;
        }
        StringBuilder url = new StringBuilder();

        if (cluster.equalsIgnoreCase(ClusterUrlEnums.ONE.getType())) {
            url.append(ClusterUrlEnums.ONE.getUrl());
        } else if (cluster.equalsIgnoreCase(ClusterUrlEnums.TWO.getType())) {
            url.append(ClusterUrlEnums.TWO.getUrl());
        }
        url.append(URL_SPLIT).append(BACKUP_REPERTORY).append(URL_SPLIT).append(snapshotName).append(URL_SPLIT).append(COMMAND_RESTORE);
        Map<String, String> params = new HashMap<String, String>();
        params.put("indices", indexName);
        Map<String,String> headers=new HashMap<String, String>();
        headers.put("content-Type","application/json");
        try {
            HttpResponse response = HttpClientUtil.execute(HttpMethod.POST, url.toString(), params, headers);
            if (response != null) {
                if (response.getStatusLine() != null) {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
                        JSONObject jsonObject=JSONUtil.getJSONObject(msg);
                        resultmsg=jsonObject!=null&&Boolean.TRUE.toString().equalsIgnoreCase(JSONUtil.getString(jsonObject, "accepted"))?SUCCESS_STATUS:FAIL_STATUS;
                    } else {
                        resultmsg = response.getStatusLine().toString();
                    }
                } else {
                    resultmsg = "response.getStatusLine() is null " + response.toString();
                }
            }
        } catch (Exception e) {
            logger.error("恢复索引{}出错", indexName, e);
        }

        return resultmsg;
    }

    /**
     * 获取某一集群下所有快照
     *
     * @param cluster
     * @return
     */
    public String getAllSnapshot(String cluster) {
        String resultmsg = null;
        if (StringUtils.isBlank(cluster)) {
            return resultmsg;
        }
        StringBuilder url = new StringBuilder();
        if (cluster.equalsIgnoreCase(ClusterUrlEnums.ONE.getType())) {
            url.append(ClusterUrlEnums.ONE.getUrl());
        } else if (cluster.equalsIgnoreCase(ClusterUrlEnums.TWO.getType())) {
            url.append(ClusterUrlEnums.TWO.getUrl());
        }
        url.append(URL_SPLIT).append(BACKUP_REPERTORY).append(URL_SPLIT).append(COMMAND_ALL);
        try {
            HttpResponse response = HttpClientUtil.execute(HttpMethod.GET, url.toString(), null, null);
            if (response != null && response.getStatusLine() != null && response.getStatusLine().getStatusCode() == 200) {
                String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
                List<String> result = getSnapshotList(msg);
                resultmsg = (result == null || result.size() == 0) ? null : JSON.toJSONString(result);
            }
        } catch (Exception e) {
            logger.error("获取集群{}快照版本出错", cluster, e);
        }

        return resultmsg;
    }


    private static List<String> getSnapshotList(String msg) {
        List<String> list = new ArrayList<String>();
        if (StringUtils.isBlank(msg)) {
            return list;
        }
        JSONArray snapshots = JSONUtil.getJSONArray(JSONUtil.getJSONObject(msg), "snapshots");
        for (int i = 0; snapshots != null && i < snapshots.size(); i++) {
            JSONObject snapshot = snapshots.getJSONObject(i);
            if (snapshot != null) {
                list.add(JSONUtil.getString(snapshot, "snapshot"));
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String resultmsg = JSON.toJSONString(getSnapshotList("fff"));
        System.out.println(resultmsg);
    }
}
