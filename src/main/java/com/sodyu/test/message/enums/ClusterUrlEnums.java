package com.sodyu.test.message.enums;

/**
 * Created by yuhp on 2017/10/27.
 */
public enum  ClusterUrlEnums {
    ONE("http://192.168.197.132:9200","one"),
    TWO("http://192.168.197.132:9200","two");
    String url;
    String type;
    ClusterUrlEnums(String url,String type){
        this.type=type;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
