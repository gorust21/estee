package com.gaia.util;

import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作类
 *
 * @author hongyang.jiang
 */
@Log4j2
public class DateUtil {
    private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

    private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat sdfTimes = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 获取YYYY格式
     */
    public static String getYear() {
        return sdfYear.format(new Date());
    }

    Timestamp timestamp = new Timestamp(new Date().getTime());

    /**
     * 获取YYYY-MM-DD格式
     */
    public static String getDay() {
        return sdfDay.format(new Date());
    }

    /**
     * 获取YYYYMMDD格式
     */
    public static String getDays() {
        return sdfDays.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD hh:mm:ss格式
     */
    public static String getTime() {
        return sdfTime.format(new Date());
    }
    /**
     * 获取YYYYMMDDHHMMSS格式
     */
    public static String getTimes() {
        return sdfTimes.format(new Date());
    }


    /**
     * @Description:(日期比较，如果s>=e 返回true 否则返回false)
     */
    public static boolean compareDate(String s, String e) {
        if (fomatDate(s) == null || fomatDate(e) == null) {
            return false;
        }
        return fomatDate(s).getTime() >= fomatDate(e).getTime();
    }

    /**
     * 格式化日期
     */
    public static Date fomatDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化返回值日期
     */
    public static Date returnDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //1、日期转字符串
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String dateStringParse = sdf.format(date);
            Date dateParse = sdf.parse(dateStringParse);
            return dateParse;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
       /* try {
            String string = date.getTime() + "";
            log.debug(string);
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String current = fmt.format(string);
            return fmt.parse(current);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }*/
    }

    /**
     * 校验日期是否合法
     */
    public static boolean isValidDate(String s) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    /**
     * 年数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * 功能描述：时间相减得到天数
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day = (beginDate.getTime()-endDate.getTime() ) / (24 * 60 * 60 * 1000);
        // System.out.println("相隔的天数="+day);
        return day;
    }

    /**
     * 得到当前n天之后的日期
     */
    public static String getAfterDayDate(Integer days) {
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdfd.format(date);
        return dateStr;
    }

    /**
     * 得到当前n月之后的日期
     */
    public static String getAfterMonthDate(Integer month) {
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.MONTH, month); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdfd.format(date);
        return dateStr;
    }

    /**
     * 得到指定日期n天之后的日期
     */
    public static String getAfterDay(String time, Integer days) {
        try {
            SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
            Date date_ = sdfd.parse(time);// 将字符串转化为时间格式
            Calendar canlendar = Calendar.getInstance(); // java.util包
            canlendar.setTime(date_);
            canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
            Date date = canlendar.getTime();
            //SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = sdfd.format(date);
            return dateStr;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到指定日期n月之后的日期
     */
    public static String getAfterMonth(String time, Integer month) {
        try {
            SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
            Date date_ = sdfd.parse(time);// 将字符串转化为时间格式
            Calendar canlendar = Calendar.getInstance(); // java.util包
            canlendar.setTime(date_);
            canlendar.add(Calendar.MONTH, month); // 日期加 如果不够减会将月变动
            Date date = canlendar.getTime();
            String dateStr = sdfd.format(date);
            return dateStr;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 得到n天之后是周几
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
     */
    public static String date2Str(Date date) {
        return date2Str(date, "yyyy-MM-dd");
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
     */
    public static Date str2Date(String date) {
        if (date!=null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new Date();
        } else {
            return null;
        }
    }

    /**
     * 按照参数format的格式，日期转字符串
     */
    public static String date2Str(Date date, String format) {
        if (null == format) {
            format = "yyyy-MM-dd";
        }
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 把时间根据时、分、秒转换为时间段
     */
    public static String getTimes(String StrDate) {
        String resultTimes = "";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now;

        try {
            now = new Date();
            Date date = df.parse(StrDate);
            long times = now.getTime() - date.getTime();
            long day = times / (24 * 60 * 60 * 1000);
            long hour = (times / (60 * 60 * 1000) - day * 24);
            long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

            StringBuffer sb = new StringBuffer();
            // sb.append("发表于：");
            if (day > 0) {
                sb.append(day + "天前");
            } else if (hour > 0) {
                sb.append(hour + "小时前");
            } else if (min > 0) {
                sb.append(min + "分钟前");
            } else {
                sb.append(sec + "刚刚");
            }

            resultTimes = sb.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return resultTimes;
    }

    public static Long countSecond(String applyTime) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Date date_ = df.parse(applyTime);
            long currentTime = System.currentTimeMillis();
            //从对象中拿到时间
            long createTime = df.parse(applyTime).getTime();
            long diff = (currentTime - createTime) / 1000 / 60;
            log.debug("当前系统时间为：" + currentTime + "下单时间为：" + createTime + "两个时间差为：" + diff);
            return diff;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @return
     */
    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
}
