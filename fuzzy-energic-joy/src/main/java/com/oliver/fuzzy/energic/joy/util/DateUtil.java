package com.oliver.fuzzy.energic.joy.util;

import com.oliver.fuzzy.energic.joy.exception.ValidationException;

import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    private DateUtil(){
        throw new RuntimeException("Can not instantiate utility class");
    }

    /**
     * Returns a {@link Date} representing a point in time a given number of days
     * before the current date.
     *
     * @param days the number of days to subtract from the current date; must be non-negative
     * @return a {@link Date} instance representing the date {@code days} days ago from now
     * @throws ValidationException if {@code days} is negative
     */
    public static Date getDateDaysBeforeNow(int days) throws ValidationException {
        if (days < 0) {
            throw new ValidationException(Constants.Validaiton.DAYS_NEGATIVE);
        }

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -days);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

}
