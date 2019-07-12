import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.Math.abs;
import static java.util.Calendar.*;
import static java.util.TimeZone.getTimeZone;

public class SprintUtil {
    private static SimpleDateFormat TIME = new SimpleDateFormat("yyyy-MM-dd");
    private static final long _2_WEEKS = 1209600000L;
    private static final long _1_DAY = 86400000L;

    static {
        TIME.setTimeZone(getTimeZone("TIME"));
    }
    
    private static long sprintBase() {
        Calendar cal = getInstance();
        cal.set(YEAR, 2019);
        cal.set(MONTH, JANUARY);
        cal.set(DAY_OF_MONTH, 1);
        cal.set(AM_PM, AM);
        cal.set(HOUR, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private static long sprintEnd(long base, long time) {
        return sprintStart(base, time) + _2_WEEKS - _1_DAY;
    }

    private static long sprintStart(long base, long time) {
        long r = abs(base - time) % _2_WEEKS;
        return (time + (base > time ? r : -r)) - (base > time && r > 0 ? _2_WEEKS : 0);

    }

    public static long[] nextSprint(long end) {
        long base = sprintBase();
        long n = sprintEnd(base, end) + _1_DAY;
        long e = sprintEnd(base, n);
        return new long[]{n, e};
    }

    public static long[][] splitTime(String start, String end) throws ParseException {
        long base = sprintBase();
        long startDate = sprintStart(base, TIME.parse(start).getTime());
        long endDate = sprintEnd(base, TIME.parse(end).getTime());
        long count = (endDate - startDate) / _2_WEEKS + 1;
        long[][] dates = new long[(int) count][2];
        for (int i = 0; i < count; i++) {
            dates[i][0] = startDate;
            dates[i][1] = startDate + _2_WEEKS - _1_DAY;
            startDate += _2_WEEKS;
        }

        return dates;
    }
}
