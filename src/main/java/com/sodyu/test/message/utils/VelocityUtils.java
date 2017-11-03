package com.sodyu.test.message.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.slf4j.Logger;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yuhp on 2017/10/25.
 */
public class VelocityUtils {
    private static Logger log = org.slf4j.LoggerFactory.getLogger(VelocityUtils.class);

    static {
        try {
            Properties p = new Properties();
            p.load(VelocityUtils.class.getClassLoader().getResourceAsStream("velocity.properties"));
            Velocity.init(p);
        } catch (Exception e) {
            log.error("初始化velocity出错！", e);
        }
    }

    public static String mergeTemplate(Map<String, Object> contextmap,
                                       String templatepath) {
        StringWriter sw = new StringWriter(1000);
        try {
            Template template = Velocity.getTemplate(templatepath, "UTF-8");
            Context context = new VelocityContext(contextmap);
            template.merge(context, sw);
            sw.flush();
        } catch (Exception ex) {
            log.error("初始化velocity模板{}出错!", templatepath, ex);
        }
        return sw.toString();
    }

}
