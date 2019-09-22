package com.nordcomet.pflio.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChartDaysResolver {

    private int maxDays = 100;

    ChartDaysResolver(int maxDays) {
        this.maxDays = maxDays;
    }

    public ChartDaysResolver() {
    }

    public List<LocalDate> resolveDays(int daysSinceExclusive) {
        List<LocalDate> allDates = LocalDate.now()
                .minusDays(daysSinceExclusive - 1)
                .datesUntil(LocalDate.now().plusDays(1))
                .collect(Collectors.toList());

        if (daysSinceExclusive <= maxDays) {
            return allDates;
        }

        BigDecimal likelihood = new BigDecimal(maxDays - 1).divide(new BigDecimal(daysSinceExclusive), 6, RoundingMode.HALF_UP);

        List<LocalDate> adjustedDates = new ArrayList<>();

        BigDecimal fractionPoint = likelihood;
        for (int i = 0; i < allDates.size(); i++) {
            fractionPoint = fractionPoint.add(likelihood);
            if (i == 0 || i == allDates.size() - 1 || fractionPoint.compareTo(BigDecimal.ONE) >= 0) {
                adjustedDates.add(allDates.get(i));
                fractionPoint = fractionPoint.remainder(BigDecimal.ONE);
            }
        }
        return adjustedDates;

    }

}
