package com.iitr.vishal.expensetracker.Common;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by Divya on 17-03-2018.
 */

public class Formatter {
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private static final String[] MonthArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_00_000L, "L");
        suffixes.put(1_00_00_000L, "Cr");
    }

    public static String moneyFormatter(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return moneyFormatter(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + moneyFormatter(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        //return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
        return (truncated / 10d) + suffix;
    }

    public static String monthFormatter(String yearMonth) {
        String[] yearMonthArray = yearMonth.split("-");
        int month = Integer.parseInt(yearMonthArray[1]);
        return MonthArray[month - 1] + "'" + yearMonthArray[0].substring(2);
    }
}
