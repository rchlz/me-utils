/**
 * 
 */
package cn.sinapp.meutils.util;

/**
 * 数字之间的转化工具
 * 
 * @author zhangdong zhangdong147896325@163.com
 * 
 *         2012-6-18 上午11:00:49
 */
public class NumberUtil {

    public static int long2Int(long l) {
        return Integer.valueOf(Long.valueOf(l).toString());
    }

    public static int long2Int(Long l) {
        if (l == null) return 0;
        else return Integer.valueOf(l.toString());
    }

    public static void main(String[] args) {

        System.out.println(NumberUtil.long2Int(1l));

        System.out.println(NumberUtil.long2Int(null));

        System.out.println(NumberUtil.long2Int(Long.valueOf("123")));

    }

}
