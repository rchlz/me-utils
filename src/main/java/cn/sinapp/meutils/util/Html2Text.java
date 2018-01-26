/**
 * 
 */
package cn.sinapp.meutils.util;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * HTML转换为普通文本工具类
 * @author zhangdong zhangdong147896325@163.com
 */
public class Html2Text {

    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Html2Text.class);

    static String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script>]*?>[\\s\\S]*?<\\/script> }

    static String regEx_option = "<[\\s]*?option[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?option[\\s]*?>"; //定义option的正则表达式{或<script>]*?>[\\s\\S]*?<\\/script> }

    static String regEx_select = "<[\\s]*?select[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?select[\\s]*?>"; //定义select的正则表达式{或<script>]*?>[\\s\\S]*?<\\/script> }

    static String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style>]*?>[\\s\\S]*?<\\/style> }

    static String regEx_html_exp = "<![^>].*?\\s*?-\\s*?-\\s*?>";// "<[\\s]*?\\![^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?[\\s]*?>"; //定义style的正则表达式{或<style>]*?>[\\s\\S]*?<\\/style> }

    static String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式    

    static Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);

    static Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);

    static Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);

    static Pattern p_option = Pattern.compile(regEx_option, Pattern.CASE_INSENSITIVE);

    static Pattern p_select = Pattern.compile(regEx_select, Pattern.CASE_INSENSITIVE);

    static Pattern p_html_exp = Pattern.compile(regEx_html_exp, Pattern.CASE_INSENSITIVE);

    /**
     * 剔除标签 (同时剔除select标签的全部)
     * 
     * @param htmlStr 待处理的HTML代码
     * @return 返回处理后的HTML字符串
     */
    public static String html2Text(String htmlStr) {

        htmlStr = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(htmlStr);

        try {
            htmlStr = p_option.matcher(htmlStr).replaceAll(""); //过滤select标签    
            htmlStr = p_select.matcher(htmlStr).replaceAll(""); //过滤option标签    
            htmlStr = p_script.matcher(htmlStr).replaceAll(""); //过滤script标签    
            htmlStr = p_style.matcher(htmlStr).replaceAll(""); //过滤style标签    
            htmlStr = p_html_exp.matcher(htmlStr).replaceAll(""); //过滤注释
            htmlStr = p_html.matcher(htmlStr).replaceAll(""); //过滤html标签    
        } catch (Exception e) {
            logger.error("html2Text(String)", e); //$NON-NLS-1$
        }

        return htmlStr;//返回文本字符串    
    }
}
