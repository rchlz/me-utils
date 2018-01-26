package cn.sinapp.meutils.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 校验工具类，主要用于校验手机号、邮箱地址、汉字、字母等的合法性判断
 * @author guoweiwei gww0426@163.com
 *
 */
public class ValidateUtil {

    private static final String mobilePattern = "^1(3|4|5|7|8)[0-9]{9}$";

    private static final String emailPattern = "[\\w\\.\\-]+@([\\w\\-]+\\.)+[a-zA-Z]+";

    private static final String chinesePattern = "[\u4e00-\u9fa5]+";

    /**
     * 检查手机号的合法性
     * 
     * @param mobile 手机号
     * @return 手机合法则为true
     */
    public static boolean checkMobile(String mobile) {
        return isValidMobile(mobile);
    }

    public static boolean isValidMobile(String mobile) {
        boolean returnValue = false;

        if (mobile != null && mobile.length() > 0) {
            Pattern p = Pattern.compile(mobilePattern);
            Matcher m = p.matcher(mobile);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 验证邮箱是否合法
     * 
     * @param email 邮箱地址
     * @return 邮箱合法则为true
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isNotEmpty(email)) {
            Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        } else {
            return false;
        }

    }

    /**
     * 检查文件是否是可允许的图片
     * 
     * @param fileName 文件名
     * @return 图片被允许返回true
     */
    public static boolean checkImageType(String fileName) {

        if (!StringUtils.isBlank(fileName)) {
            Map<String, String> imageTypes = new HashMap<String, String>();
            imageTypes.put("jpg", "1");
            imageTypes.put("jpeg", "1");
            imageTypes.put("png", "1");
            imageTypes.put("gif", "1");
            imageTypes.put("bmp", "1");
            String fileSuffix = StringUtils
                    .substringAfterLast(StringUtils.lowerCase(fileName), ".");
            int typeLimited = 0;
            if (!StringUtils.isBlank(fileSuffix)) {
                typeLimited = NumberUtils.toInt(imageTypes.get(fileSuffix));
                if (typeLimited == 1) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 判断字符串是否含有中文字
     * 
     * @param s String
     * @return 包含中文字符返回true,否则返回false
     */
    public static boolean chineseValid(String s) {
        int length = s.length();
        for (int i = 0; i < length; i++) {
            if (!s.substring(i).matches(chinesePattern)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符是否中文字
     * @param word char
     * @return 如果是中文字返回true
     */
    public boolean checkChinese(char word) {
        if ((word >= 0x4e00) && (word <= 0x9fbb)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 中文转unicode
     * 
     * @param s 需要转换的中文汉字
     * @return 反回unicode编码
     */
    public static String chineseToUnicode(String s) {

        if (StringUtils.isBlank(s)) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int length = s.length();

        for (int i = 0; i < length; i++) {
            char chr = s.charAt(i);

            if (!isLetter(chr)) {
                sb.append("\\u").append(Integer.toHexString(chr));
            } else {
                sb.append(chr);
            }

        }
        return sb.toString();
    }

    /**
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
     * 
     * @param c 需要判断的字符
     * @return 返回true,Ascill字符
     */
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     * 
     * @param s 需要得到长度的字符串
     * @return i得到的字符串长度
     */
    public static int length(String s) {
        if (s == null) {
            return 0;
        }
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
     * 
     * 
     * @param origin 原始字符串
     * @param len 截取长度(一个汉字长度按2算的)
     * @param postfix 后缀
     * @return 返回的字符串
     */
    public static String subString(String origin, int len, String postfix) {
        if (StringUtils.isBlank(origin) || (len < 1)) {
            return "";
        }

        if (len > length(origin)) {
            return origin;
        }

        StringBuffer buff = new StringBuffer();
        int index = 0;
        char c;
        while (len > 0) {
            c = origin.charAt(index);

            if (isLetter(c)) {
                len--;
            } else {
                len--;
                if (len < 1) {
                    break;
                }
                len--;
            }
            buff.append(c);
            index++;
        }

        return buff.append(postfix).toString();
    }
}
