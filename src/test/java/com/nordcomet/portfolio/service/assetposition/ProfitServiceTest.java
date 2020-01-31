package com.nordcomet.portfolio.service.assetposition;

import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.data.assetposition.AssetPosition;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.*;

class ProfitServiceTest {

    private final AssetRepo assetRepo = mock(AssetRepo.class);
    private final AssetPositionService assetPositionService = mock(AssetPositionService.class);
    private final ProfitService underTest = new ProfitService(assetRepo, assetPositionService);

    @Test
    void getPortfolioProfitSince_shouldCalculateProfitItems() {
        LocalDateTime since = now().minusDays(1);
        Asset asset = Asset.builder().id(1).build();
        when(assetRepo.findAll()).thenReturn(Set.of(asset));
        when(assetPositionService.findAssetPositionsByDateFor(anySet(), eq(since))).thenReturn(List.of(AssetPosition.builder()
                .asset(asset)
                .totalValue(new BigDecimal("120"))
                .totalPurchaseAmount(new BigDecimal("95"))
                .build()));
        when(assetPositionService.findAssetPositionsByDateFor(anySet(), not(eq(since)))).thenReturn(List.of(AssetPosition.builder()
                .asset(asset)
                .totalValue(new BigDecimal("125"))
                .totalPurchaseAmount(new BigDecimal("97"))
                .build()));

        Profit result = underTest.getPortfolioProfitSince(since);

        assertThat(result.getValue(), comparesEqualTo(new BigDecimal("5")));
        assertThat(result.getProfit(), comparesEqualTo(new BigDecimal("3")));
        assertThat(result.getPurchaseAmount(), comparesEqualTo(new BigDecimal("2")));
        assertThat(result.getProfitPercentage(), comparesEqualTo(new BigDecimal("0.12")));
    }

}