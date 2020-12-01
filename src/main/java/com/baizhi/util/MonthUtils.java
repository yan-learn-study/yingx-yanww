package com.baizhi.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yww
 * @Description
 * @Date 2020/11/27 17:31
 */
public class MonthUtils {

    public static ArrayList<Integer> getMonths() {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(Integer.valueOf(getLast12Months(5)));
        integers.add(Integer.valueOf(getLast12Months(4)));
        integers.add(Integer.valueOf(getLast12Months(3)));
        integers.add(Integer.valueOf(getLast12Months(2)));
        integers.add(Integer.valueOf(getLast12Months(1)));
        integers.add(Integer.valueOf(new SimpleDateFormat("MM").format(new Date())));
        return integers;
    }

    private static String getLast12Months(int i) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        return sdf.format(m);
    }
}
