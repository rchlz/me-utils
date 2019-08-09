package cn.sinapp.meutils.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.util.TextUtils;

/**
 * 
 * @author zhuguli
 *
 */
public class HtmlUtil {
    //private static Long      beginDebugTime = 0L;

    private static final Log logger         = LogFactory.getLog(HtmlUtil.class);


    /**
     * 取得指定URL的Web内容，加入取内容超时处理
     * @author 郑卿
     * @param theURL url地址
     * @param connTimeout 连接超时时间(毫秒)
     * @param requTimeout 获取超时时间(毫秒)
     * @return 指定URL的Web内容
     */
    public static String getWebContentWithTimeout(String theURL, int connTimeout, int requTimeout) {
        String sTotalString = "";
        URL l_url = null;
        HttpURLConnection l_connection = null;
        java.io.InputStream l_urlStream = null;
        BufferedReader l_reader = null;
        try {
            l_url = new URL(theURL);
            l_connection = (HttpURLConnection) l_url.openConnection();
            l_connection.setConnectTimeout(connTimeout);

            // 加入取内容超时处理
            l_connection.setReadTimeout(requTimeout);

            l_connection.connect();
            l_urlStream = l_connection.getInputStream();
            l_reader = new BufferedReader(new InputStreamReader(l_urlStream));
            int buffer_size = 1024;
            char[] buffer = new char[buffer_size];
            StringBuffer sb = new StringBuffer();
            int readcount = 0;
            while ((readcount = l_reader.read(buffer, 0, buffer_size)) > 0) {
                sb.append(buffer, 0, readcount);
            }
            sTotalString = sb.toString();
            l_reader.close();
            l_urlStream.close();
            l_connection.disconnect();
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("error: exception in WebUtil: " + e.toString() + ":" + theURL);
            }
        } finally {
            if (l_reader != null) {
                try {
                    l_reader.close();
                } catch (Exception e) {
                }
            }
            if (l_urlStream != null) {
                try {
                    l_urlStream.close();
                } catch (Exception e) {
                }
            }
            if (l_connection != null) {
                try {
                    l_connection.disconnect();
                } catch (Exception e) {
                }
            }
        }
        return sTotalString;
    }

    /**
     * 对输入进行htmlEncode 07.07.30 优化性能
     * 
     * @author RobinChang
     * @param desc 输入的内容
     * @return 编码过的内容
     */
    public static String formatDesc(String desc) {
        int spaceCount = 0; //用于判断是否是连续空格
        StringBuffer result = new StringBuffer("");
        if (desc != null) {
            char[] c = desc.toCharArray();
            for (int i = 0; i < c.length; i++) {
                switch (c[i]) {
                    case 10:
                        spaceCount = 0;
                        break;// \r换行
                    case 13:
                        spaceCount = 0;
                        result.append("<br/>");
                        break;// \n回车
                    case 32:
                        if (spaceCount == 0) {
                            result.append(" ");
                        } else if (spaceCount == 1) {
                            result.deleteCharAt(result.length() - 1);
                            result.append("&nbsp;&nbsp;");
                        } else {
                            result.append("&nbsp;");
                        }

                        spaceCount++;

                        break; // 空格
                    case 34:
                        spaceCount = 0;
                        result.append("&quot;");
                        break;// "
                    case 38:
                        spaceCount = 0;
                        result.append("&amp");
                        break;// &
                    case 60:
                        spaceCount = 0;
                        result.append("&lt;");
                        break; // <
                    case 62:
                        spaceCount = 0;
                        result.append("&gt;");
                        break; // >
                    default:
                        spaceCount = 0;
                        result.append(c[i]);
                }
            }
            c = null;
        }
        return result.toString();
    }

    /**
     * 对输入进行htmlEncode 07.07.30 优化性能
     * 
     * @author RobinChang
     * @param desc 输入的内容
     * @return 编码过的内容
     */
    public static String formatDescEx(String desc) {
        return formatDesc(desc);
        /*
         * desc = TextUtils.htmlEncode(desc); desc = desc.replaceAll("\n",
         * "<br/>"); return desc;
         */
    }

    /**
     * format content for html output
     * @param desc 描述内容
     * @return 处理后的描述内容
     */
    public static String formatDescNoEncodeScript(String desc) {
        desc = desc.replaceAll("\n", "<br/>");
        if (desc.endsWith("<br/>"))
            desc = desc.substring(0, desc.length() - 5);
        desc = desc.replaceAll("<script>", "&lt;script&gt;");
        desc = desc.replaceAll("</script>", "&lt;/script&gt;");
        return desc;
    }

    public static String formatbanjiaDescNoEncodeScript(String desc) {
        desc = desc.replaceAll("<br/>", "\r\n");
        desc = desc.replaceAll("&nbsp;", " ");
        if (desc.endsWith("<br/>"))
            desc = desc.substring(0, desc.length() - 5);
        desc = desc.replaceAll("<script>", "&lt;script&gt;");
        desc = desc.replaceAll("</script>", "&lt;/script&gt;");
        return desc;
    }

    /**
     * format content for html output
     * @param desc 描述内容
     * @return 处理后的描述内容
     */
    public static String formatDescNoEncode(String desc) {
        desc = desc.replaceAll("\n", "<br/>");
        if (desc.endsWith("<br/>"))
            desc = desc.substring(0, desc.length() - 5);
        return desc;
    }

    public static String replaceParameterValue(final String querystring, String parameter,
                                               String value) throws UnsupportedEncodingException {
        // 如果参数名为null或"" 则直接返回
        if (parameter == null || "".equals(parameter.trim())) {
            return "";
        }
        String result = (querystring != null) ? querystring : "";

        // 目的是否是删除
        boolean isDel = (value == null || "".equals(value));
        // 如果输入querystring 为空字符串,且value有值,则直接返回
        if ("".equals(result.trim())) {
            return (!isDel) ? parameter + "=" + URLEncoder.encode(value, "UTF-8") : "";
        }

        // 计算方便,先前后追加&
        if (!result.startsWith("&")) {
            result = "&" + result;
        }

        if (!result.endsWith("&")) {
            result += "&";
        }

        int iStartPos = result.indexOf("&" + parameter + "=");
        // 如果查到参数则对"&"进行修正
        if (iStartPos > -1) {
            iStartPos++;
        }

        int iEndPos = -1;
        // 如果找到起点则,则计算结束点
        if (iStartPos > -1) {
            iEndPos = result.indexOf("&", iStartPos + 1);
        }

        if (isDel) {
            // 删除 只处理iStartPos > -1的情况 即找到的情况
            if (iStartPos > -1 && iEndPos > -1 && iEndPos > iStartPos) {
                result = result.substring(0, iStartPos) + result.substring(iEndPos + 1);
            }
        } else {
            String replaceValue = parameter + "=" + URLEncoder.encode(value, "UTF-8");
            // 如果原先没有该值则添加,否则覆盖
            if (iStartPos == -1) {
                result += replaceValue;
            } else {
                result = result.substring(0, iStartPos) + replaceValue + "&"
                        + result.substring(iEndPos + 1);
            }
        }

        // 如果去掉开始及结束的&
        if (result.startsWith("&")) {
            result = result.substring(1);
        }
        if (result.endsWith("&")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 用来替换在querystring 中的参数的值
     * @param querystring 查询字符串
     * @param parameters 没有此参数则给加上
     * @param values 为空(null or "")时从querystring 中删除此参数
     * @return 处理后的字符串
     * @throws UnsupportedEncodingException  不支持的编码
     */
    public static String replaceParameterValue(final String querystring, List<String> parameters,
                                               List<String> values) throws UnsupportedEncodingException {
        String result = querystring;
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                String name = parameters.get(i);
                // 有问题 如果输入的是 Character类型将报错
                String value = values.get(i).toString();
                result = replaceParameterValue(result, name, value);
            }
        }
        return result;
    }

    /**
     * 用来替换在querystring 中的参数的值, 并判断在queryString前是否加?
     * @param querystring 查询字符串
     * @param parameters 没有此参数则给加上
     * @param values 为空(null or "")时从querystring 中删除此参数
     * @return 处理后的字符串
     * @throws UnsupportedEncodingException 不支持的编码
     */
    public static String replaceParameterValueEx(final String querystring, List<String> parameters,
                                                 List<String> values) throws UnsupportedEncodingException {
        String result = replaceParameterValue(querystring, parameters, values);
        if (result != null && result.trim().length() > 0)
            result = "?" + result;
        return result;
    }

    /**
     * 显示一个url ， 没有http://的，自动加上
     * 
     * @param url url地址
     * @return 处理后的网址
     */
    public static String formatHomepage(String url) {
        String result = "";
        if (url != null && url.trim().length() > 0) {
            if (!url.startsWith("http://"))
                result = "http://" + url;
            else
                result = url;
        }
        return result;
    }

    /**
     * 把html代码用 document.write输出，静态页面or php使用script src=... 调用时会用到
     * @param html html内容
     * @return 包含document.write的html内容
     */
    public static String html2Script(String html) {
        String script = "";
        if (html != null) {
            String[] result = html.split("\n");
            for (String one : result) {
                one = one.replaceAll("\"", "'").trim();
                script += "document.write(\"" + one + "\");\r\n";
            }
        }
        return script;
    }

    public static String formatDescOld(String desc) {
        desc = TextUtils.htmlEncode(desc);
        desc = desc.replaceAll("\r\n", "<br/>");
        desc = desc.replaceAll(" ", "&nbsp;");
        return desc;
    }

    /**
     * 把特殊字符转换成htmlcode:&lt;,&gt;,&amp;,",bbs中标题需要htmlcode
     * 
     * @param s output
     * @return string
     * @author Lazyman Tong
     */
    public static String dhtmlSpecialchars(String s) {
        return dhtmlSpecialchars(s, false);
    }

    /**
     * 把特殊字符转换成htmlcode:&lt;,&gt;,&amp;,",bbs中标题需要htmlcode
     * 
     * @param s output
     * @param isurl url不对 &amp; 进行转换
     * @return string
     * @author Lazyman Tong
     */
    public static String dhtmlSpecialchars(String s, boolean isurl) {
        StringBuffer str = new StringBuffer();
        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            if (c < '\200') {
                switch (c) {
                    case 34: // '"'
                        str.append("&quot;");
                        break;

                    case 38: // '&'
                        if (isurl == false) {
                            str.append("&amp;");
                        }
                        break;

                    case 39:// ' ' '
                        str.append("&#039;");
                        break;

                    case 60: // '<'
                        str.append("&lt;");
                        break;

                    case 62: // '>'
                        str.append("&gt;");
                        break;

                    default:
                        str.append(c);
                        break;
                }
                continue;
            }
            if (c < '\377') {
                String hexChars = "0123456789ABCDEF";
                int a = c % 16;
                int b = (c - a) / 16;
                String hex = "" + hexChars.charAt(b) + hexChars.charAt(a);
                str.append("&#x" + hex + ";");
            } else {
                str.append(c);
            }
        }
        return str.toString();
    }

    /**
     * 把特殊字符转换成htmlcode:&lt;,&gt;,&amp;,",bbs中标题需要htmlcode
     * 
     * @param str 待处理的字符串
     * @return 处理后的字符串
     */
    public static String dhtmlSpecialchars1(String str) {
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("\"", "&quot;");
        str = str.replaceAll("'", "&#039;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        return str;
    }
}