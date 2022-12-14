package com.gaia.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j2;

/**
 * Created by kzyuan on 2017/6/21.
 */

@Log4j2
public class SystemUtils {
    /**
     * 获取访问者IP * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。 *
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。 * * @param request
     *
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    /**
     * 获取来访者的浏览器版本 * * @param request
     *
     * @return
     */
    public static String getRequestBrowserInfo(HttpServletRequest request) {
        String browserVersion = null;
        String header = request.getHeader("user-agent");
        log.info("user-agent>>>>>>>>>>>>" + header);
        if (header == null || header.equals("")) {
            return "";
        }
        if (header.indexOf("MSIE") > 0) {
            browserVersion = "IE";
        } else if (header.indexOf("Firefox") > 0) {
            browserVersion = "Firefox";
        } else if (header.indexOf("Chrome") > 0) {
            browserVersion = "Chrome";
        } else if (header.indexOf("Safari") > 0) {
            browserVersion = "Safari";
        } else if (header.indexOf("Camino") > 0) {
            browserVersion = "Camino";
        } else if (header.indexOf("Konqueror") > 0) {
            browserVersion = "Konqueror";
        }
        return browserVersion;

    }

    /**
     * 获取系统版本信息 * * @param request
     *
     * @return
     */
    public static String getRequestSystemInfo(HttpServletRequest request) {
        String systenInfo = null;
        String header = request.getHeader("user-agent");
        if (header == null || header.equals("")) {
            return "";
        }
        //得到用户的操作系统
        if (header.indexOf("NT 6.0") > 0) {
            systenInfo = "Windows Vista/Server 2008";
        } else if (header.indexOf("NT 5.2") > 0) {
            systenInfo = "Windows Server 2003";
        } else if (header.indexOf("NT 5.1") > 0) {
            systenInfo = "Windows XP";
        } else if (header.indexOf("NT 6.0") > 0) {
            systenInfo = "Windows Vista";
        } else if (header.indexOf("NT 6.1") > 0) {
            systenInfo = "Windows 7";
        } else if (header.indexOf("NT 6.2") > 0) {
            systenInfo = "Windows Slate";
        } else if (header.indexOf("NT 6.3") > 0) {
            systenInfo = "Windows 9";
        } else if (header.indexOf("NT 10.0") > 0) {
            systenInfo = "Windows 10";
        } else if (header.indexOf("NT 5") > 0) {
            systenInfo = "Windows 2000";
        } else if (header.indexOf("NT 4") > 0) {
            systenInfo = "Windows NT4";
        } else if (header.indexOf("Me") > 0) {
            systenInfo = "Windows Me";
        } else if (header.indexOf("98") > 0) {
            systenInfo = "Windows 98";
        } else if (header.indexOf("95") > 0) {
            systenInfo = "Windows 95";
        } else if (header.indexOf("Mac") > 0) {
            systenInfo = "Mac";
        } else if (header.indexOf("Unix") > 0) {
            systenInfo = "UNIX";
        } else if (header.indexOf("Linux") > 0) {
            systenInfo = "Linux";
        } else if (header.indexOf("SunOS") > 0) {
            systenInfo = "SunOS";
        }
        return systenInfo;
    }

    /**
     * 获取来访者的主机名称 * * @param ip
     *
     * @return
     */
    public static String getHostName(String ip) {
        InetAddress inet = null;
        try {
            inet = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            log.error("UnknownHostException", e);
        }
        return inet.getHostName();

    }

    public static void getClient(HttpServletRequest request) {
        String ipaddr = getIpAddr(request);
        //客户端信息
        log.debug("ip地址>>>" + getIpAddr(request)+"   "+"浏览器>>>" + getRequestBrowserInfo(request)+"   "+"系统>>>" + getRequestSystemInfo(request)+"   "+"主机名>>>" + getHostName(ipaddr));
       /* log.debug("浏览器>>>>>>>>>" + getRequestBrowserInfo(request));
        log.debug("系统>>>>>>>>>>>" + getRequestSystemInfo(request));
        log.debug("主机名>>>>>>>>>" + getHostName(ipaddr));*/
        //post参数



        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            log.debug("key>>>>>>>>>>>" + entry.getKey());
            for (String string : entry.getValue()) {
                log.debug("value>>>>>>>>" + string);
            }
            log.debug("----------------");
        }
    }

    /**
     * 执行参考
     */
    public void transfer(HttpServletRequest request) {
        SystemUtils systemUtils = new SystemUtils();
        String ipaddr = systemUtils.getIpAddr(request);
        log.info(systemUtils.getIpAddr(request));
        log.info(systemUtils.getRequestBrowserInfo(request));
        log.info(systemUtils.getRequestSystemInfo(request));
        log.info(systemUtils.getHostName(ipaddr));
    }
}
