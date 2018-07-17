package com.cnr.basemodule.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author xingzhiqiao 2015-11-10
 */
public class MSDateUtils {
    public static final SimpleDateFormat md = new SimpleDateFormat("MM-dd",
            Locale.getDefault());

    public static final SimpleDateFormat ymd = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault());

    public static final SimpleDateFormat mdSlash = new SimpleDateFormat(
            "MM/dd", Locale.getDefault());

    public static final SimpleDateFormat mdChinese = new SimpleDateFormat(
            "MM月d日", Locale.getDefault());

    public static final SimpleDateFormat ymdhhmmss = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm:ss", Locale.getDefault());

    public static final SimpleDateFormat ymdhhmm = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm", Locale.getDefault());

    public static final SimpleDateFormat mdhhmm = new SimpleDateFormat(
            "MM-dd HH:mm", Locale.getDefault());

    public static final SimpleDateFormat ym = new SimpleDateFormat(
            "yyyy-MM", Locale.getDefault());

    public static final SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm",
            Locale.getDefault());

    private static SimpleDateFormat simpleDateFormat;

    /**
     * parse string time to long
     *
     * @param date
     * @return
     * @throws ParseException 加同步的原因是 多线程操作会影响到日期的解析
     */
    public static synchronized long parseDate(String date,
                                              SimpleDateFormat format) throws ParseException {
        long ldate = 0;
        Date d = new Date();
        d = format.parse(date);
        ldate = d.getTime();
        return ldate;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return ymdhhmmss.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseYMDHMDate(String sdate) {
        try {
            return ymdhhmm.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getMonthAndDay(String sdate) {
        try {
            return ymd.parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 获取小时和分钟
     *
     * @param milliseconds
     * @return
     */
    public static String getHourAndMinute(long milliseconds) {
        Date date = new Date(milliseconds);
        return hhmm.format(date);
    }

    public static String friendlyTime(long milliseconds) {
        // Date date = new Date(milliseconds*1000);

        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        // String curDate = ymd.format(cal.getTime());
        Date time = new Date(milliseconds);
        // String paramDate = ymd.format(time);
        // if (curDate.equals(paramDate)) {
        // int hour = (int) ((cal.getTimeInMillis() - time.getTime()) /
        // 3600000);
        // int minute = (int) ((cal.getTimeInMillis() - time.getTime()) /
        // 60000);
        // if (minute == 0) {
        // ftime = Math.max(
        // (cal.getTimeInMillis() - time.getTime()) / 1000, 1)
        // + "秒前";
        // return ftime;
        // }
        // if (hour == 0)
        // ftime = Math.max(
        // (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
        // + "分钟前";
        // else
        // ftime = hour + "小时前";
        // return ftime;
        // }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                final long t = (cal.getTimeInMillis() - time.getTime()) / 60000;
                if (t < 1) {
                    ftime = "刚刚";
                } else {
                    ftime = t + "分钟前";
                }
            } else {
                ftime = hour + "小时前";
            }
        } else if (days == 1) {
            ftime = "昨天" + " " + hhmm.format(time);
        } else if (days >= 2) {
            // ftime = "前天";
            ftime = ymdhhmm.format(time);
        }
        // else if (days > 2 && days <= 10) {
        // ftime = days + "天前";
        // } else if (days > 10) {
        // ftime = ymdhhmm.format(time);
        // }
        return ftime;

    }


    /**
     * 获取剩余时间
     *
     * @param time
     * @return
     */
    public static String getResidueTime(int time) {
        time = time / 1000;
        int minute = time / 60;
        int second = time % 60;

        String s = "直播" + minute + "分" + second + "秒后开始";
        return s;

    }


    /**
     * 返回 yyyy-MM-dd HH:mm:ss格式的时间字符串
     *
     * @param milliseconds
     * @return
     */
    public static String friendlyTime3(String milliseconds) {
        if (TextUtils.isEmpty(milliseconds)) {
            return "";
        }
        return friendlyTime(Long.valueOf(milliseconds));
    }


    /**
     * 获取年月 yyyy-mm
     *
     * @param date
     * @return
     */
    public static String getYearAndMonth(long date) {
        return ym.format(new Date(date));
    }


    /**
     * 以友好的方式显示时间 <li>如果是同一天,如果是一小时内 比如说 现在是12:00,参数是11:59
     * 则显示一分钟前.如果超过一小时比如说现在是12点,参数是10:59则显示一小时前 <li>如果不是同一天，
     *
     * @param sdate
     * @return
     */
    public static String friendlyTime(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = ymd.format(cal.getTime());
        String paramDate = ymd.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days > 2) {
            // ftime = "前天";
            ftime = ymdhhmm.format(time);
        }
        // else if (days > 2 && days <= 10) {
        // ftime = days + "天前";
        // } else if (days > 10) {
        // ftime = ymdhhmm.format(time);
        // }
        return ftime;
    }

    /**
     * 获取当前的某个字段
     *
     * @param field {@link Calendar#YEAR}
     */
    public static int getCurrentDataByFiled(int field) {
        Calendar cal = Calendar.getInstance();
        return cal.get(field);
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = ymd.format(today);
            String timeDate = ymd.format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 当月生日
     */
    public static boolean isBirthdayOnMonth(String birday) {
        if (TextUtils.isEmpty(birday)) {
            return false;
        }
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

        Date date = new Date(Long.parseLong(birday));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int tarMonth = cal.get(Calendar.MONTH);

        return currentMonth == tarMonth;
    }

    /**
     * 将整形的时间转为Calendar
     */
    public static Calendar getCalendarFromDate(String str) {
        if (TextUtils.isEmpty(str)) {
            return Calendar.getInstance();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(Long.valueOf(str)));
        return cal;
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentDate(SimpleDateFormat pattern) {
        long t = System.currentTimeMillis();
        String st = formatDate(t, pattern);
        return st;
    }

    /**
     * 格式化时间值
     *
     * @param time 时间值 是以Long类型传过来的
     * @return
     */
    public static String formatDate(String time, SimpleDateFormat pattern) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        return formatDate(Long.valueOf(time), pattern);
    }

    /**
     * 格式化时间值
     *
     * @param lDate
     * @return
     */
    public static String formatDate(long lDate, SimpleDateFormat pattern) {
        if (lDate == 0)
            return "";
        Date date = new Date(lDate);
        return pattern.format(date);
    }

    public static String getTime(long date) {
        Date date2 = new Date(date);
        simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        return simpleDateFormat.format(date2);
    }


    public static String getNextMonday(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, week);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date time = calendar.getTime();
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd",
                Locale.getDefault());
        String format = simpleDateFormat.format(time);

        return format;
    }

    public static String getMonthWeek(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, week);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date time = calendar.getTime();
        simpleDateFormat = new SimpleDateFormat("yyyy.MM", Locale.getDefault());
        String format = simpleDateFormat.format(time);

        return format;
    }

    public static String getNextSunDay(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, week + 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date time = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd",
                Locale.getDefault());
        String format = sdf.format(time);

        return format;
    }

    public static long getLongTime(String date) {
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd",
                Locale.getDefault());
        try {
            return simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 格式化音乐时间
     */
    public static String getMusicMinutes(long time) {
        StringBuilder sb = new StringBuilder();

        String hours;
        String minutes;
        String seconds;

        int hour = (int) (time / 3600);
        time = time - hour * 3600;
        int minute = (int) (time / 60);
        int second = (int) (time - minute * 60);

        if (hour > 0) {
            hours = hour + "";
            sb.append(hours).append(":");
        } else {
            hours = "";
        }
        if (minute < 10) {
            minutes = "0" + minute;
        } else {
            minutes = "" + minute;
        }
        sb.append(minutes).append(":");
        if (second < 10) {
            seconds = "0" + second;
        } else {
            seconds = "" + second;
        }
        sb.append(seconds);
        return sb.toString();
    }

    public static String getTime(Date date) {
        return ymd.format(date);
    }

    /**
     * 获得生日的月份
     *
     * @param birthday
     * @return
     */
    public static String getBirthDayMonth(String birthday) {
        Date date = new Date(Long.parseLong(birthday));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String tarMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
        return tarMonth;
    }

    /**
     * 获得生日为当月的哪一天
     *
     * @param birthday
     * @return
     */
    public static String getBirthDayDate(String birthday) {
        Date date = new Date(Long.parseLong(birthday));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String tarDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        return tarDay;
    }

    /**
     * 获得月份和日期
     *
     * @param date
     * @return
     */
    public static String getMonthAndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        @SuppressLint("WrongConstant") String month = Integer.toString(calendar.get(Calendar.MONDAY) + 1);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        return month + "月" + day + "日";
    }

    /**
     * add by wangfangmin int转化为字符串显示的日期
     *
     * @param date 与当天相差的天数
     * @return 日期的字符串表示
     */
    public static String diffDateToStr(int date) {
        // 创建 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, date);
        String formatDayStr = mdSlash.format(calendar.getTime());
        // 0今天 -1昨天 -2前天 1明天 2后天
        switch (date) {
            case -2:
                return "前天	" + formatDayStr;
            case -1:
                return "昨天	" + formatDayStr;
            case 0:
                return "今天	" + formatDayStr;
            case 1:
                return "明天	" + formatDayStr;
            case 2:
                return "后天	" + formatDayStr;
            default:
                return mdChinese.format(calendar.getTime());
        }
    }

    /**
     * mm/dd
     * 获取当前的月份和日期
     *
     * @return
     */
    public static String getMdSlash(Date date) {
        return mdSlash.format(date);
    }

    /**
     * 通过day获取当前日期
     *
     * @param day 与当前日期的差值，以0为基础，昨天-1，明天1以此类推
     * @return
     */
    public static String getDateByDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        Date date = calendar.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        return format.format(date);
    }

    /**
     * 通过某个位置设置日期
     *
     * @param day 0代表今天
     * @return
     */
    public static String getDateByPosition(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        Date date = calendar.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        if (day > -2 && day < 2) {
            switch (day) {
                case 0:
                    return "今天";
                case -1:
                    return "昨天";
                case 1:
                    return "明天";

                default:
                    break;
            }
        } else {
            return format.format(date);
        }
        return "";
    }

    /**
     * 通过day获取当前日期
     *
     * @param day 与当前日期的差值，以0为基础，昨天-1，明天1以此类推
     * @return
     */
    public static String getWeekByDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return getWeekDay(week);
    }

    public static String getWeekDay(int week) {
        String weekString = "";
        switch (week) {
            case 1:
                weekString = "一";
                break;
            case 2:
                weekString = "二";
                break;
            case 3:
                weekString = "三";
                break;
            case 4:
                weekString = "四";
                break;
            case 5:
                weekString = "五";
                break;
            case 6:
                weekString = "六";
                break;
            case 7:
                weekString = "日";
                break;
            default:
                break;
        }
        return weekString;
    }

    /**
     * 将时间转化为00:00:00型
     *
     * @param time
     * @return
     */
    public static String getPlayTime(long time) {
        int ss = 1000;
        int mi = ss * 60;

        long hour = time / (mi * 60);
        long minute = (time / mi) - (hour * 60);
        long second = (time - minute * mi - hour * mi * 60) / ss;

        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        return strHour + ":" + strMinute + ":" + strSecond;
    }
}
