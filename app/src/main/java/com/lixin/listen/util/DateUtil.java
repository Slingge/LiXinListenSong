package com.lixin.listen.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wbq on 2014/12/9.
 */
public class DateUtil {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat ALL_SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT_NO_SECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat MESSAGE_DATE_FORMAT = new SimpleDateFormat("MM.dd HH:mm");
    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("MM-dd");

    public static Date toDate(Object o) {
        if (o != null) {
            if (o instanceof Number) {
                return new Date(((Number) o).longValue());
            } else if (o instanceof String) {
                return toDate((String) o);
            }
        }
        return null;
    }

    public static Date toDate(String o, String parseStr) {
        if (o == null) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(parseStr);
            return simpleDateFormat.parse(o);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date toDate(String o) {
        if (o == null) {
            return null;
        }
        try {
            return SIMPLE_DATE_FORMAT.parse(o);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date toDateByAllDate(String o) {
        if (o == null) {
            return null;
        }
        try {
            return ALL_SIMPLE_DATE_FORMAT.parse(o);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toDateStr(Object o) {
        Date date = toDate(o);
        if (date != null) {
            return ALL_SIMPLE_DATE_FORMAT.format(date);
        }
        return "";
    }

    public static String toDateStrNoSecond(Object o) {
        Date date = toDate(o);
        if (date != null) {
            return SIMPLE_DATE_FORMAT_NO_SECOND.format(date);
        }
        return "";
    }

    public static String toDateStrSimple(Object o) {
        Date date = toDate(o);
        if (date != null) {
            return SIMPLE_DATE_FORMAT.format(date);
        }
        return "";
    }

    public static String toMessageDateFormat(long time) {
        Date date = new Date(time);
        if (date != null) {
            return MESSAGE_DATE_FORMAT.format(date);
        }
        return "";
    }

    public static String toDayFormat(Object o){
        Date date = toDate(o);
        if (date != null) {
            return DAY_FORMAT.format(date);
        }
        return "";
    }

}
