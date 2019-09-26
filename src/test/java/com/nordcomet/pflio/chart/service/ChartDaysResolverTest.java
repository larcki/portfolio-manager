package com.nordcomet.pflio.chart.service;

import com.nordcomet.pflio.chart.service.ChartDaysResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Comparator.comparing;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ChartDaysResolverTest {

    @ParameterizedTest()
    @CsvSource({
            "100, 365, 101",
            "100, 180, 100",
            "100, 30, 30",
            "100, 5, 5",
    })
    void shouldLimitDays(int max, int sinceDays, int expected) {
        ChartDaysResolver chartDaysResolver = new ChartDaysResolver(max);
        List<LocalDate> days = chartDaysResolver.resolveDays(sinceDays);
        assertThat(days.size(), is(expected));
    }

    @Test
    void shouldSkipDaysEvenlyAndIncludeFirstAndLast() {
        ChartDaysResolver chartDaysResolver = new ChartDaysResolver(10);
        List<LocalDate> days = chartDaysResolver.resolveDays(18);
        days.forEach(System.out::println);
        assertThat(days.size(), is(10));
        assertThat(days.get(0), is(LocalDate.now().minusDays(17)));
        assertThat(days.get(1), is(LocalDate.now().minusDays(15)));
        assertThat(days.get(2), is(LocalDate.now().minusDays(13)));
        assertThat(days.get(3), is(LocalDate.now().minusDays(11)));
        assertThat(days.get(4), is(LocalDate.now().minusDays(9)));
        assertThat(days.get(5), is(LocalDate.now().minusDays(7)));
        assertThat(days.get(6), is(LocalDate.now().minusDays(5)));
        assertThat(days.get(7), is(LocalDate.now().minusDays(3)));
        assertThat(days.get(8), is(LocalDate.now().minusDays(1)));
        assertThat(days.get(9), is(LocalDate.now().minusDays(0)));
    }

    @ParameterizedTest()
    @CsvSource({
            "100, 365",
            "100, 180",
            "14, 66321",
            "10, 55",
    })
    void shouldSkipDaysEvenly(int max, int daysSince) {
        ChartDaysResolver chartDaysResolver = new ChartDaysResolver(max);
        List<LocalDate> days = chartDaysResolver.resolveDays(daysSince);
        List<Long> daysSkipped = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            LocalDate date = days.get(i);
            if (i > 1 && i < days.size() - 1) {
                long between = DAYS.between(days.get(i - 1), date);
                daysSkipped.add(between);
            }
        }
        long intervalVariation = daysSkipped.stream().max(comparing(Long::valueOf)).get() - daysSkipped.stream().min(comparing(Long::valueOf)).get();
        System.out.println(intervalVariation);
        assertThat(intervalVariation <= 1, is(true));

    }

}