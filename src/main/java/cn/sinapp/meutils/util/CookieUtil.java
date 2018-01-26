package cn.sinapp.meutils.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;


/**
 * @author zhangdong zhangdong147896325@163.com
 * 
 *         2012-6-13 下午4:59:08
 */
public class CookieUtil {

    private static final DateFormatSymbols LOCALE_US = DateFormatSymbols.getInstance(Locale.US);

    private static final SimpleTimeZone ZERO_TIME_ZONE = new SimpleTimeZone(0, "GMT");

    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            return null;
        }

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(key)) {
                return cookies[i].getValue();
            }
        }

        return null;
    }

    public static void saveCookie(HttpServletResponse response, String key, String value) {
        saveCookie(response, key, value, -1, "/");
    }

    public static void saveCookie(HttpServletResponse response, String key, String value,
            String path, boolean httpOnly) {
        if (httpOnly) {
            saveCookie(response, key, value, -1, path, true);
        } else {
            saveCookie(response, key, value, -1, path);
        }
    }

    public static void saveCookie(HttpServletResponse response, String key, String value,
            String path) {
        saveCookie(response, key, value, -1, path);
    }

    public static void saveCookie(HttpServletResponse response, String key, String value,
            int second, String path) {
        value = StringUtils.remove(value, '\n');
        value = StringUtils.remove(value, '\r');
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        cookie.setMaxAge(second);
        cookie.setDomain(getDomain());
        response.addCookie(cookie);
    }

    public static void saveCookie(HttpServletResponse response, String key, String value,
            int second, String path, String domain) {
        value = StringUtils.remove(value, '\n');
        value = StringUtils.remove(value, '\r');
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        cookie.setMaxAge(second);
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    public static void saveCookie(HttpServletResponse response, String key, String value,
            int second, String path, boolean httpOnly) {
        if (httpOnly) {
            // 这里就要手工拼凑cookie了

            SimpleDateFormat COOKIE_EXPIRES_HEADER_FORMAT = new SimpleDateFormat(
                    "EEEEEE, dd-MMM-yyyy HH:mm:ss zzz");
            COOKIE_EXPIRES_HEADER_FORMAT.setTimeZone(ZERO_TIME_ZONE);
            COOKIE_EXPIRES_HEADER_FORMAT.setDateFormatSymbols(LOCALE_US);
            Date d = new Date();
            d.setTime(d.getTime() + TimeUnit.SECONDS.toMillis(second));
            StringBuilder sb = new StringBuilder();

            // key=value
            sb.append(key).append("=").append(value).append(";");

            // domain=.hongxiujie.com
            sb.append(" Domain=").append(getDomain()).append(";");

            // path=
            if (!StringUtils.isBlank(path)) {
                sb.append(" Path=").append(path).append(";");
            }

            // expireds=
            if (second > 0) {
                String cookieLifeTime = COOKIE_EXPIRES_HEADER_FORMAT.format(d);
                sb.append(" Expires=").append(cookieLifeTime).append(";");
            }
            sb.append(" HTTPOnly");
            response.addHeader("Set-Cookie", sb.toString());
        } else {
            saveCookie(response, key, value);
        }
    }

    public static void saveCookie(HttpServletResponse response, String key, String value,
            int second, String path, boolean httpOnly, String domain) {
        if (httpOnly) {
            // 这里就要手工拼凑cookie了

            SimpleDateFormat COOKIE_EXPIRES_HEADER_FORMAT = new SimpleDateFormat(
                    "EEEEEE, dd-MMM-yyyy HH:mm:ss zzz");
            COOKIE_EXPIRES_HEADER_FORMAT.setTimeZone(ZERO_TIME_ZONE);
            COOKIE_EXPIRES_HEADER_FORMAT.setDateFormatSymbols(LOCALE_US);
            Date d = new Date();
            d.setTime(d.getTime() + TimeUnit.SECONDS.toMillis(second));
            StringBuilder sb = new StringBuilder();

            // key=value
            sb.append(key).append("=").append(value).append(";");

            // domain=.hongxiujie.com
            sb.append(" Domain=").append(domain).append(";");

            // path=
            if (!StringUtils.isBlank(path)) {
                sb.append(" Path=").append(path).append(";");
            }

            // expireds=
            if (second > 0) {
                String cookieLifeTime = COOKIE_EXPIRES_HEADER_FORMAT.format(d);
                sb.append(" Expires=").append(cookieLifeTime).append(";");
            }
            sb.append(" HTTPOnly");
            response.addHeader("Set-Cookie", sb.toString());
        } else {
            saveCookie(response, key, value);
        }
    }

    public static void clearCookie(HttpServletResponse response, String key, int second, String path) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath(path);
        cookie.setMaxAge(second);
        cookie.setDomain(getDomain());
        response.addCookie(cookie);
    }

    public static void clearCookie(HttpServletResponse response, String key, int second,
            String path, String domain) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath(path);
        cookie.setMaxAge(second);
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    //TODO 这货是暂时的的，一定要改
    //实在不好意思，因为之前没有传入参数，实在无法改
    private static String getDomain() {
    	return "";
    }

}
