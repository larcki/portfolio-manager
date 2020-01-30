package com.nordcomet.portfolio.data.asset;

import com.nordcomet.portfolio.data.assetposition.AssetPosition;
import com.nordcomet.portfolio.data.assetposition.AssetPositionRepo;
import com.nordcomet.portfolio.service.asset.AssetDto;
import com.nordcomet.portfolio.service.asset.AssetService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.nordcomet.portfolio.DataRandomiser.randomAsset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AssetServiceTest {

    private final AssetRepo assetRepo = mock(AssetRepo.class);
    private final AssetPositionRepo assetPositionRepo = mock(AssetPositionRepo.class);
    private final AssetService underTest = new AssetService(assetRepo, assetPositionRepo);

    @Test
    void shouldReturnAssetListWhenAssetPositionExists() {
        Asset asset = randomAsset();
        when(assetRepo.findAll()).thenReturn(Set.of(asset));

        BigDecimal quantity = new BigDecimal("3");
        BigDecimal unitPrice = new BigDecimal("2");
        BigDecimal totalValue = new BigDecimal("6");
        BigDecimal totalPurchaseAmount = new BigDecimal("5");
        AssetPosition latestAssetPosition = new AssetPosition(asset, quantity, unitPrice, totalValue, totalPurchaseAmount, LocalDateTime.now());
        when(assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId())).thenReturn(Optional.of(latestAssetPosition));

        List<AssetDto> result = underTest.getAssetList();

        assertEquals(1, result.size());
        AssetDto actual = result.get(0);
        assertEquals(asset.getName(), actual.getName());
        assertEquals(new BigDecimal("6.00").toString(), actual.getTotalValue());
        assertEquals(new BigDecimal("20.0").toString(), actual.getPerformancePercentage());
        assertEquals(new BigDecimal("1.00").toString(), actual.getPerformance());
    }

    @Test
    void shouldReturnAssetWithZeroValuesWhenAssetPositionNotPresent() {
        Asset asset = randomAsset();
        when(assetRepo.findAll()).thenReturn(Set.of(asset));
        when(assetPositionRepo.findFirstByAssetIdOrderByTimestampDesc(asset.getId())).thenReturn(Optional.empty());

        List<AssetDto> result = underTest.getAssetList();

        assertEquals(1, result.size());
        AssetDto actual = result.get(0);
        assertEquals(asset.getName(), actual.getName());
        assertEquals(new BigDecimal("0.00").toString(), actual.getTotalValue());
        assertEquals(new BigDecimal("0.0").toString(), actual.getPerformancePercentage());
        assertEquals(new BigDecimal("0.00").toString(), actual.getPerformance());

    }
}