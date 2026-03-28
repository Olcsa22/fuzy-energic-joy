package com.oliver.fuzzy.energic.joy.util;

import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void getStartOfDayDaysAgo_withZero_shouldReturnStartOfToday() throws ValidationException {
        Date result = DateUtil.getDateDaysBeforeNow(0);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date expected = cal.getTime();

        assertEquals(expected, result);
    }

    @Test
    void getStartOfDayDaysAgo_withPositiveDays_shouldReturnStartOfPastDay() throws ValidationException {
        Random random = new Random();
        int days = random.nextInt(0,31);

        Date result = DateUtil.getDateDaysBeforeNow(days);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -days);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date expected = cal.getTime();

        assertEquals(expected, result);
    }

    @Test
    void getDateDaysBeforeNow_withNegativeDays_shouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> DateUtil.getDateDaysBeforeNow(-1));
    }

    @Test
    void getDateDaysBeforeNow_with30Days_shouldReturnDateOneMonthAgo() throws ValidationException {
        Date result = DateUtil.getDateDaysBeforeNow(30);
        Date now = new Date();

        assertTrue(result.before(now));
        long diffMillis = now.getTime() - result.getTime();
        long diffDays = diffMillis / (1000 * 60 * 60 * 24);
        assertTrue(diffDays >= 29 && diffDays <= 31);
    }
}
