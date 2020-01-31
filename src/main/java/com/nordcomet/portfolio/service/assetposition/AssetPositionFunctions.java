package com.nordcomet.portfolio.service.assetposition;

import com.nordcomet.portfolio.data.assetposition.AssetPosition;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

public class AssetPositionFunctions {

    public static Predicate<AssetPosition> beforeEndOfDay(LocalDateTime time) {
        return it -> it.getTimestamp().isBefore(startOfNextDay(time));
    }

    public static LocalDateTime startOfNextDay(LocalDateTime time) {
        return time.toLocalDate().plus(1, ChronoUnit.DAYS).atStartOfDay();
    }

}
