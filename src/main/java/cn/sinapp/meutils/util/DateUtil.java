package cn.sinapp.meutils.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期处理工具类
 * @author youfly.yanghf
 */
public class DateUtil {

    /** 日期+时间的格式 */
    final static public String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 日期的格式 */
    final static public String DATE_FORMAT = "yyyy-MM-dd";

    /** 小时:分的格式 */
    final static public String HHMM_FORMAT = "HH:mm";

    final static public String HHMMSS_FORMAT = "HH:mm";

    final static public String DATE_TIME_HHMM = "yyyy-MM-dd HH:mm";

    final static SimpleDateFormat formatMM = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 判断date至今是否超过3个月时间，是返回true，否则返回false
     * @param date 日期
     * @return 超过3个月返回true，否则返回false
     */
    public static boolean isOver3Month(Date date) {

        if (date == null) throw new IllegalArgumentException();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 3);
        Date target = c.getTime();

        Date now = new Date();

        return now.getTime() - target.getTime() >= 0 ? true : false;

    }

    /**
     * 判断date至今是否超过指定的天数，是返回true，否则返回false
     * 
     * @author guoweiwei
     * @param date 日期
     * @param day 天数
     * @return 超过指定天数返回true，否则返回false
     */
    public static boolean isOver(Date date, int day) {

        if (date == null) throw new IllegalArgumentException();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        Date target = c.getTime();

        Date now = new Date();

        return now.getTime() - target.getTime() >= 0 ? true : false;

    }

    public static void main(String[] args) throws ParseException {

        String s = "2009-01-01";
        System.out.println(DateUtil.parse(s, DateUtil.DATE_FORMAT));
        System.out.println(DateUtil.getTime(s));
    }

    /**
     * 将日期字符串解析成指定格式的Date对象
     * 
     * @param dateTime 日期字符串
     * @param format 指定格式
     * @return （正确格式）日期对象
     */
    public static Date parse(String dateTime, String format) {
        if (dateTime == null || dateTime.length() <= 0) return null;
        String sDateTime = ((dateTime.indexOf('.') > 0)) ? dateTime.substring(0,
                dateTime.indexOf('.')) : dateTime;

        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(sDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将日期类解析成指定格式的日期字符串
     * 
     * @param date 日期
     * @param format 指定格式
     * @return 格式化后的日期字符串
     */
    public static String format(Date date, String format) {
        if (date == null) return null;
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 将日期字符串解析成"yyyy-MM-dd HH:mm:ss"格式的Date对象
     * 
     * @param dateTime 日期字符串
     * @return （正确格式）日期对象
     */
    public static Date parseDateTime(String dateTime) {
        return parse(dateTime, DATE_TIME_FORMAT);
    }

    /**
     * 将日期类解析成"yyyy-MM-dd HH:mm:ss"格式的日期字符串
     * 
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    public static String formatDateTime(Date date) {
        return format(date, DATE_TIME_FORMAT);
    }

    /**
     * 将日期类解析成"yyyy-MM-dd HH:mm"
     * 
     * @param date 日期对象
     * @return "yyyy-MM-dd HH:mm" 格式的日期字符串
     * @author haimin.xuhm
     */
    public static String formatDateHHmm(Date date) {
        return format(date, DATE_TIME_HHMM);
    }

    /**
     * 将日期字符串解析成"yyyy-MM-dd"格式的Date对象
     * 
     * @param dateTime 日期字符串
     * @return （正确格式）日期对象
     */
    public static Date parseDate(String dateTime) {
        return parse(dateTime, DATE_FORMAT);
    }

    /**
     * 将日期类解析成"yyyy-MM-dd"格式的日期字符串
     * 
     * @param date 日期
     * @return "yyyy-MM-dd"格式的日期字符串
     */
    public static String formatDate(Date date) {
        return format(date, DATE_FORMAT);
    }

    /**
     * 将日期类解析成"HH:mm"格式的日期字符串
     * 
     * @param date 日期
     * @return "HH:mm"格式的日期字符串
     */
    public static String formatHHmm(Date date) {
        return format(date, HHMM_FORMAT);
    }

    /**
     * 将日期类解析成"HH:mm:ss"格式的日期字符串
     * 
     * @param date 日期
     * @return "HH:mm:ss"格式的日期字符串
     */
    public static String formatHHmmss(Date date) {
        return format(date, HHMMSS_FORMAT);
    }

    /**
     * 格式化日期类型，返回中文显示的日期时间 返回String 格式如下： 2007年9月4日 16:32:35
     * 
     * @param date 日期
     * @return 中文显示的日期时间，如2007年9月4日 16:32:35
     */
    public static String formatDateTimeChinaLocale(Date date) {
        if (date == null) return null;
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT,
                Locale.CHINA);
        return df.format(date);
    }

    /**
     * 格式化日期类型，返回中文显示的日期时间 返回String 格式如下： 2007年9月4日
     * 
     * @param date 日期
     * @return 中文显示的日期时间 返回String 格式如下： 2007年9月4日
     */
    public static String formatDateChinaLocale(Date date) {
        if (date == null) return null;
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.CHINA);
        return df.format(date);
    }

    /**
     * 日期格式合法性校验
     * 
     * @param year 年
     * @param month 月
     * @param monthDay 日
     * @return 合法日期返回true
     */
    private static boolean checkDay(int year, int month, int monthDay) {
        if (month < 0 || month > 11 || monthDay < 1 || monthDay > 31) {
            throw new RuntimeException("The day<" + year + "," + (month + 1) + "," + monthDay
                    + "> is ERROR-DAY!");
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(year, month, 1);
        int maxDay = getMaxDaysOfMonth(cal.getTime());
        if (monthDay > maxDay) {
            throw new RuntimeException("The day<" + year + "," + (month + 1) + "," + monthDay
                    + "> is ERROR-DAY!");
        }
        return true;
    }

    /** String转换成integer
     * @param str 字符串
     * @return 转换后的整形
     */
    public static int str2Int(String str) {
        if (StringUtil.isEmpty(str.trim())) {
            return -1;
        }
        try {
            return Integer.valueOf(str).intValue();
        } catch (Exception e) {}
        return -1;
    }

    /**
     * 将“2010-01-02”格式的日期
     * zhangzhiwei.tw
     * 
     * @param date 日期
     * @return long型日期
     */
    public static long getTime(String date) {

        Date d = null;
        try {
            if (date.length() > 10) {
                d = DateUtil.parse(date, DateUtil.DATE_TIME_FORMAT);
            } else {
                d = DateUtil.parse(date, DateUtil.DATE_FORMAT);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        return d.getTime();
    }

    /**
     * 返回日期，格式为yyyy-MM-dd
     * 
     * @param year 年份
     * @param month 0-11
     * @param day 1-31
     * @return Date对象
     */
    public static Date toDate(int year, int month, int day) {
        checkDay(year, month, day);

        Calendar cal = GregorianCalendar.getInstance();
        // 同步设置时分秒的方法，并未设置毫秒级别，会导致时间并不一致
        // cal.set(year, month, day, 0, 0, 0);
        cal.set(year, month, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * 返回四位年份,如果传入的参数为null，将抛出空指针异常。
     * 
     * @param date 日期
     * @return 年份
     */
    public static int getYear(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 返回月数(0-11) 如果传入的参数为null，将抛出空指针异常。
     * 
     * @param date 日期
     * @return 月份
     */
    public static int getMonth(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    /**
     * 返回月几(1-31) 如果传入的参数为null，将抛出空指针异常。
     * 
     * @param date 日期
     * @return 月几(1-31)
     */
    public static int getDayOfMonth(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回周几 returned value (<tt>1</tt> = Sunday, <tt>2</tt> = Monday,
     * <tt>3</tt> = Tuesday, <tt>4</tt> =
     * Wednesday, <tt>6</tt> = Thursday, <tt>6</tt> = Friday, <tt>7</tt> =
     * Saturday) 如果传入的参数为null，将抛出空指针异常。
     * 
     * @param date 日期
     * @return 周几(1-7)
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        int result = cal.get(Calendar.DAY_OF_WEEK);
        return result;
    }

    /**
     * 返回周几 returned value (<tt>7</tt> = Sunday, <tt>1</tt> = Monday,
     * <tt>2</tt> = Tuesday, <tt>3</tt> =
     * Wednesday, <tt>4</tt> = Thursday, <tt>5</tt> = Friday, <tt>6</tt> =
     * Saturday) 如果传入的参数为null，将抛出空指针异常。
     * 
     * @param date 日期
     * @return 周几(1-7)
     */
    private static int getDayOfWeekForChina(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        int result = cal.get(Calendar.DAY_OF_WEEK);
        if (result == 1) {
            return 7;
        } else {
            return result - 1;
        }
    }

    /**
     * 返回小时(0-23) 如果传入的参数为null，将抛出空指针异常。
     * 
     * @param date 日期
     * @return 小时(0-23)
     */
    public static int getHours(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 返回分钟（0-59） 如果传入的参数为null，将抛出空指针异常。
     * 
     * @param date 日期
     * @return 分钟（0-59）
     */
    public static int getMinutes(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }

    /**
     * 某年某月某日的所在周的第一天(周一)。 如果传入的参数为null则返回null。
     * 
     * @param date 日期
     * @return 所在周的第一天(周一)
     */
    public static Date getFirstDayOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK, 1 - getDayOfWeekForChina(date));

        return cal.getTime();
    }

    /**
     * 某年某月某日的所在周的周末一天(周日)。 如果传入的参数为null则返回null。
     * 
     * @param date 日期
     * @return 所在周的周末一天(周日)
     */
    public static Date getLastDayOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK, 7 - getDayOfWeekForChina(date));

        return cal.getTime();
    }

    /**
     * 参数date所在月份的第一天。 如果传入的参数为null则返回null。
     * 
     * @param date 日期
     * @return 所在月份的第一天
     */
    public static Date getFirstDayOfMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 参数Date所在月份的最后一天。 如果传入的参数为null则返回null。
     * 
     * @param date 日期
     * @return 所在月份的最后一天
     */
    public static Date getLastDayOfMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, getMaxDaysOfMonth(date));
        return cal.getTime();
    }

    /**
     * 参数date所在月份的最大天数。 如果传入的参数为null，则返回-1。
     * 
     * @param date 日期
     * @return 所在月份的最大天数
     */
    public static int getMaxDaysOfMonth(Date date) {
        if (date == null) {
            return -1;
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 比较日期：年月日。
     * 
     * @param d0 日期1
     * @param d1 日期2
     * @return the value <code>0</code> if the argument d1 is equal to d0;
     *         a value less than <code>0</code> if d0 is
     *         before d1 argument; and a value greater than <code>0</code>
     *         if d0 is after d1 argument.
     */
    public static int compareDay(Date d0, Date d1) {
        return d0.compareTo(d1);
    }

    /**
     * d0是否在[d1,d2]的日期区间中
     * 
     * @param d0 当前日期
     * @param d1 日期1
     * @param d2 日期2
     * @return 时间范围内则为true
     */
    public static boolean isDayInRange(Date d0, Date d1, Date d2) {
        return (compareDay(d0, d1) >= 0 && compareDay(d1, d2) <= 0) ? true : false;
    }

    /**
     * 返回N个月之后（之前）的某天。 例外情况：如果找到的天数大于此月的最后一天，则返回此月的最后一天。
     * 比如：输入(2007-1-31,1)，则返回2007-2-28
     * 
     * @param date 日期
     * @param monthNumber 月数
     * @return N个月之后（之前）的某天
     */
    public static Date addMonths(Date date, int monthNumber) {
        if (monthNumber == 0) return date;
        if (date == null) return null;

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, monthNumber);

        return cal.getTime();
    }

    /**
     * 返回N天之后（之前）的某天。
     * 
     * @param date 日期
     * @param days 天数
     * @return N天之后（之前）的某天
     */
    public static Date addDays(Date date, int days) {
        if (days == 0) return date;
        if (date == null) return null;

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, days);

        return cal.getTime();
    }

    /**
     * 截去time部分，使日期的时间部分变成变成00:00:00,如果传入的参数为null，则返回null。
     * 
     * @param date 日期
     * @return 新的处理后的Date
     */
    public static Date truncateDate(Date date) {
        if (date == null) {
            return null;
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * 将date转为date对应的那一天的最后时间，使日期的时间部分变成变成23:59:59.999，如果传入的参数是null，则返回null
     * 。
     * 
     * @param date 日期
     * @return date对应的那一天的最后时间
     */
    public static Date getLastDateTimeOfDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 比较2个日期类型，返回2个日期相差的天数，正数表示前一个日期比后一个日期晚n天，负数表示前一日期比后一日期早n天
     * 
     * @param d1 待比较日期1
     * @param d2 待比较日期2
     * @return 相差天数
     */
    public static int compareDays(Date d1, Date d2) {
        if (d1 == null || d2 == null) throw new RuntimeException("Not compare between d1<" + d1
                + "> and d2<" + d2 + ">!");
        d1 = truncateDate(d1);
        d2 = truncateDate(d2);

        int i = (int) ((d1.getTime() - d2.getTime()) / (24 * 3600 * 1000));
        return i;
    }

    /**
     * 返回给定时间在当年处于第几周，其中 getFirstDayOfWeek() 为 SUNDAY 例如，1998 年 1 月 1
     * 日是星期四，getFirstDayOfWeek() 为 SUNDAY 那么 1998
     * 年的第一个星期开始于 1998 年 1 月 4 日 结束于 1998 年 1 月 10 日；1998 年头三天是 1997 年第 53
     * 个星期的一部分。
     * 
     * @param date 日期
     * @return 给定时间在当年处于第几周
     */
    public static int getWeekOfYear(Date date) {
        if (date == null) {
            throw new RuntimeException("this date is null!");
        }
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获得当前日期第一个时刻（二十四小时制） 00:00:00
     * 
     * @param date
     *        当前日期
     * @return 当前日期第一个时刻（二十四小时制） 00:00:00
     */
    public static Date getFirstTimeOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得当前日期最后一个时刻（二十四小时制） 23:59:59
     * 
     * @param date
     *        当前日期
     * @return 当前日期最后一个时刻（二十四小时制） 23:59:59
     */
    public static Date getLastTimeOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得指定格式的当前日期字符串
     * 
     * @param date 日期
     * @param format 格式化字符串
     * @return 指定格式的当前日期字符串
     */
    public static String date(Date date, String format) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(date);
        } else {
            return "";
        }
    }

    /**
     * 本地时间换算其它时区时间
     * 
     * @param date 日期时间串："yyyy-MM-dd HH:mm:ss"或"yyyy-MM-dd"格式
     * @param timeZone 时区：如北京时间“ETC/GMT-8”，UTC时间“GMT”
     * @return 其它时区的时间
     */
    public static Date strToDateTime(String date, String timeZone) {
        Date d = null;
        try {
            if (date.length() > 10) {
                SimpleDateFormat f = new SimpleDateFormat(DATE_TIME_FORMAT);
                f.setTimeZone(TimeZone.getTimeZone(timeZone));
                d = f.parse(date);
            } else {
                SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);
                f.setTimeZone(TimeZone.getTimeZone(timeZone));
                d = f.parse(date);
            }
        } catch (Exception e) {}
        return d;
    }

    public static String formatEx(Date date) {
        return formatEx(date, null);
    }

    static final long SECOND = 1000;

    static final long MINUTE = SECOND * 60;

    static final long HOUR = MINUTE * 60;

    static final long DAY = HOUR * 24;

    static final long MONTH = DAY * 30;

    static final long YEAR = MONTH * 12;

    public static String formatEx(Date date, Date compare) {

        if (date == null) return StringUtils.EMPTY;

        String hit = StringUtils.EMPTY;
        String unit = StringUtils.EMPTY;

        int cnt = -1;
        long diff = (compare == null ? System.currentTimeMillis() : compare.getTime())
                - date.getTime();

        if (diff > 0) {
            hit = "前";
        } else {
            hit = "后";
            diff = -diff;
        }

        if (diff > YEAR) {
            //            unit = "年";
            //            cnt = (int) (diff / YEAR);
            return formatMM.format(date);
        } else if (diff > MONTH) {

            //            cnt = (int) (diff / MONTH);
            //
            //            if (cnt >= 6) {
            //                unit = "半年";
            //                cnt = -1;
            //            } else {
            //                unit = "个月";
            //            }
            return formatMM.format(date);
        } else if (diff > DAY) {

            //            cnt = (int) (diff / DAY);
            //            
            //            if (cnt >= 15) {
            //                unit = "半个月";
            //                cnt = -1;
            //            } else if (cnt >= 7) {
            //                unit = "一星期";
            //                cnt = -1;
            //            } else {
            //                unit = "天";
            //            }
            return formatMM.format(date);
        } else if (diff > HOUR) {

            cnt = (int) (diff / HOUR);
            //           
            //            if (cnt >= 12) {
            //                unit = "半天";
            //                cnt = -1;
            //            } else {
            unit = "小时";
            //            }            
        } else if (diff > MINUTE) {

            cnt = (int) (diff / MINUTE);
            //            
            //            if (cnt >= 30) {
            //                unit = "半小时";
            //                cnt = -1;
            //            } else {
            unit = "分钟";
            //            }   
        } else {

            hit = StringUtils.EMPTY;
            unit = "刚刚";
            cnt = -1;
        }

        return (cnt > 0 ? String.valueOf(cnt) : StringUtils.EMPTY) + unit + hit;
    }

    public static String formatExDay(Date date, Date compare) {

        if (date == null) return StringUtils.EMPTY;

        String hit = StringUtils.EMPTY;
        String unit = StringUtils.EMPTY;

        int cnt = -1;
        long diff = (compare == null ? System.currentTimeMillis() : compare.getTime())
                - date.getTime();

        if (diff > 0) {
            hit = "前";
        } else {
            hit = "后";
            diff = -diff;
        }

        if (diff > YEAR) {
            unit = "年";
            cnt = (int) (diff / YEAR);
        } else if (diff > MONTH) {

            cnt = (int) (diff / MONTH);

            if (cnt >= 6) {
                unit = "半年";
                cnt = -1;
            } else {
                unit = "个月";
            }
        } else if (diff > DAY) {
            cnt = (int) (diff / DAY);
            unit = "天";
        } else {
            hit = StringUtils.EMPTY;
            unit = "1天内";
            cnt = -1;
        }

        return (cnt > 0 ? String.valueOf(cnt) : StringUtils.EMPTY) + unit + hit;
    }

    /**
     * 两个日期是否同一天
     * 
     * @param date1 日期1
     * @param date2 日期2
     * @return 如果是同一天返回true,否则返回false
     */
    public static boolean sameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))
                && (cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE));
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat fullSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat HourSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static synchronized String getDateText(Date date) {
        return sdf.format(date);
    }

    public static String getDateTimeText(Date date) {
        return fullSdf.format(date);
    }

    //    public static Date parseDate(String dateTxt) throws Exception {
    //        try {
    //            return sdf.parse(dateTxt);
    //        } catch (ParseException e) {
    //            throw new Exception(e);
    //        }
    //    }
    //
    //    public static Date parseDatetime(String dateTimeTxt) throws Exception {
    //        try {
    //            return fullSdf.parse(dateTimeTxt);
    //        } catch (ParseException e) {
    //            throw new Exception(e);
    //        }
    //    }

    public static int getDay(Date date) {
        return getCal(date).get(Calendar.DAY_OF_MONTH);
    }

    private static Calendar getCal(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * 获取time当天0点时刻的Date对象
     * 
     * @param time 时间
     * @return 当天0点时刻的Date对象
     * @throws Exception 异常
     */
    public static Date getBeginDay(Date time) throws Exception {
        return toDate(getDateText(time));
    }

    /**
     * 获取当前时间的Ｎ天的0点时间
     * 
     * @param currentDate 当前时间
     * @param offSet 偏移数
     * @return 当前时间的Ｎ天的0点时间
     */
    public static synchronized Date getDay0(Date currentDate, int offSet) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(currentDate);
        cal.add(GregorianCalendar.DATE, offSet);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 将yyyy-MM-dd 格式的ＳＴＲＩＮＧ 转化为ＤＡＴＥ
     * 
     * @param queryDateStr 日期字符串
     * @return Date 日期
     */
    public static Date toDate(String queryDateStr) {

        if (!StringUtils.isEmpty(queryDateStr)) {
            try {
                return sdf.parse(queryDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将yyyy-MM-dd hh:mm:ss格式的 String转化为Date
     * 
     * @param queryDateStr 时间字符串
     * @return Date 时间
     */
    public static Date toDateFull(String queryDateStr) {

        if (!StringUtils.isEmpty(queryDateStr)) {
            try {
                return fullSdf.parse(queryDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将yyyy-MM-dd hh:mm格式的 String转化为Date
     * 
     * @param queryDateStr 时间字符串
     * @return Date 时间
     */
    public static Date toDateHour(String queryDateStr) {

        if (!StringUtils.isEmpty(queryDateStr)) {
            try {
                return HourSdf.parse(queryDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Date getWeekBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        // System.out.println("@@@@@@@@@@ week number" + weekNumber);
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();// 该周的第一天
    }

    public static Date getWeekEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        // System.out.println("@@@@@@@@@@ week number" + weekNumber);
        calendar.set(Calendar.WEEK_OF_YEAR, weekNumber);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return calendar.getTime();// 该周的最后一天
    }

    public static Date getMonthBegin(Date currentDate) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(currentDate);
        cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getMonthEnd(Date currentDate) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(currentDate);
        cal.set(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH) + 1, 1);
        return cal.getTime();
    }

    public static String formatDate(String queryDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(queryDate)) {
            queryDate = sdf.format(new Date());
        }

        try {
            Date dtTmp = sdf.parse(queryDate);
            queryDate = sdf.format(dtTmp);
        } catch (ParseException e) {}

        return queryDate;
    }

    /**
     * 获得用于显示的日期
     * 
     * @param queryDate 时间字符串
     * @param offSet 偏移数
     * @return 用于显示的日期
     */
    public static String getQueryDateForView(String queryDate, int offSet) {
        Date dtTmp = DateUtil.toDate(queryDate);
        // 取得上一天的时间
        dtTmp = DateUtil.getDay0(dtTmp, offSet);
        queryDate = DateUtil.getDateText(dtTmp);

        return queryDate;
    }

    /**
     * 获取月的天数
     * 
     * @param date 日期
     * @return 月的天数
     */
    public static int getDayCountByMonth(Date date) {
        int year = getYear(date);
        int month = getMonth(date);
        return getDayCountByMonth(year, month);
    }

    public static int getDayCountByMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return cal.getMaximum(Calendar.DAY_OF_MONTH);
    }

}
