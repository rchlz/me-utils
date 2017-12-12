package cn.sinapp.meutils.util;

import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * ip相关的工具。
 * 
 * @author zhangdong zhangdong147896325@163.com
 * 
 *         2012-6-11 下午2:06:43
 */
public class IPUtil {

    public static final Pattern IP_PATTERN = Pattern.compile("([0-9]{1,3}\\.){3}[0-9]{1,3}");

    /**
     * 将ip从long转化为常用的Str格式
     * 
     * @param ipaddress
     * @return
     */
    public static String iplongToIp(long ipaddress) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((ipaddress >>> 24)));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x000000FF)));
        return sb.toString();
    }

    /**
     * string ip to long
     * 
     * @param ipaddress
     * @return
     */
    public static long ipStrToLong(String ipaddress) {
        try {
            long[] ip = new long[4];
            int position1 = ipaddress.indexOf(".");
            int position2 = ipaddress.indexOf(".", position1 + 1);
            int position3 = ipaddress.indexOf(".", position2 + 1);
            ip[0] = Long.parseLong(ipaddress.substring(0, position1));
            ip[1] = Long.parseLong(ipaddress.substring(position1 + 1, position2));
            ip[2] = Long.parseLong(ipaddress.substring(position2 + 1, position3));
            ip[3] = Long.parseLong(ipaddress.substring(position3 + 1));
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        } catch (Throwable e) {
            return 0;
        }
    }

    /**
     * 是否是本地IP
     * 
     * @param strIp
     * @return
     */
    public static boolean isLocal(String strIp) {
        if ("127.0.0.1".equals(strIp)) return true;
        long l = ipStrToLong(strIp);
        if (l >= 3232235520L) return l <= 3232301055L;
        return (l >= 167772160L) && (l <= 184549375L);
    }

    /**
     * 判断是否合法ip格式
     * 
     * @param ip
     * @return
     */
    public static boolean isValidIP(String ip) {
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        return IP_PATTERN.matcher(ip).matches();
    }

    /**
     * 获得X-Forwarded-For中的ip数据
     * 
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getIpFromXForwardedFor(HttpServletRequest request) {
        String ip = null;

        Enumeration<String> xffe = request.getHeaders("X-Forwarded-For");
        if (xffe == null) return null;
        while (xffe.hasMoreElements()) {
            String xffIp = xffe.nextElement();
            if (isValidIP(xffIp) && !xffIp.startsWith("10.")) { // 过滤掉10.开头的IP
                ip = xffIp;
            }
            //对opera mini 4.2.1 的支持
            if (xffIp.indexOf(',') != -1) {
                String[] ips = StringUtils.split(xffIp, ",\n\r ");
                if (ips.length > 1) {
                    ip = ips[0];
                    if (isValidIP(ip) && !ip.startsWith("10.")) {
                        return ip;
                    }
                }
            }
        }

        return ip;
    }

    /**
     * get client ip
     * 
     * @param request
     * @return
     */
    public static String getClientIP(HttpServletRequest request) {
        // return BizFilterUtil.getClientIP(request);

        String ip = getIpFromXForwardedFor(request);
        if (isValidIP(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIP(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIP(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("202.102.249.59:" + Long.toString(IPUtil.ipStrToLong("202.102.249.59")));
        System.out.println("202.102.249.59 is local ip:"
                + Boolean.toString(IPUtil.isLocal("202.102.249.59")));
        System.out.println("127.0.0.1:" + Long.toString(IPUtil.ipStrToLong("127.0.0.1")));
        System.out
                .println("127.0.0.1 is local ip:" + Boolean.toString(IPUtil.isLocal("127.0.0.1")));
        System.out.println("10.9.1.134:" + Long.toString(IPUtil.ipStrToLong("10.9.1.134")));
        System.out.println("10.9.1.134 is local ip:"
                + Boolean.toString(IPUtil.isLocal("10.9.1.134")));
        System.out.println(IPUtil.iplongToIp(3232240842l));
    }
}
