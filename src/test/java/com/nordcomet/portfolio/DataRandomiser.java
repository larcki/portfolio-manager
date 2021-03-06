package com.nordcomet.portfolio;

import com.nordcomet.portfolio.common.Currency;
import com.nordcomet.portfolio.common.Money;
import com.nordcomet.portfolio.data.account.Account;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetClass;
import com.nordcomet.portfolio.data.assetposition.AssetPosition;
import com.nordcomet.portfolio.data.fee.Fee;
import com.nordcomet.portfolio.data.priceupdate.PriceUpdate;
import com.nordcomet.portfolio.data.transaction.Transaction;
import com.nordcomet.portfolio.service.transaction.TransactionSaveRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static com.nordcomet.portfolio.data.asset.AssetClassType.BOND;
import static com.nordcomet.portfolio.data.asset.AssetClassType.STOCK;
import static com.nordcomet.portfolio.data.asset.Region.DEVELOPED;
import static com.nordcomet.portfolio.data.asset.Region.EMERGING;

public class DataRandomiser {

    public static Asset randomAsset(Account account) {
        return randomAssetBuilder()
                .account(account)
                .build();
    }

    public static Asset.AssetBuilder randomAssetBuilder() {
        return Asset.builder()
                .account(randomAccount())
                .name(randomString())
                .assetClasses(randomAssetClasses())
                .isin(randomString())
                .quoteCurrency(Currency.EUR)
                .baseCurrency(Currency.EUR);
    }

    public static Asset randomAsset() {
        return randomAssetBuilder().build();
    }

    private static Set<AssetClass> randomAssetClasses() {
        return Set.of(
                new AssetClass(DEVELOPED, STOCK, new BigDecimal("0.6")),
                new AssetClass(EMERGING, STOCK, new BigDecimal("0.2")),
                new AssetClass(null, BOND, new BigDecimal("0.2"))
        );
    }

    public static PriceUpdate randomPriceUpdate(Asset asset) {
        PriceUpdate priceUpdate = new PriceUpdate();
        priceUpdate.setPrice(randomPrice());
        priceUpdate.setTimestamp(LocalDateTime.now());
        priceUpdate.setAsset(asset);
        return priceUpdate;
    }

    public static AssetPosition randomAssetPosition(Asset asset) {
        return new AssetPosition(
                asset,
                randomQuantity(),
                randomPrice(),
                randomPrice(),
                randomPrice(), LocalDateTime.now()
        );
    }

    public static Transaction randomTransaction(Asset asset) {
        return randomBaseTransaction(asset).build();
    }

    public static Transaction.TransactionBuilder randomBaseTransaction(Asset asset) {
        Money unitPrice = Money.of(randomPrice(), Currency.EUR);
        BigDecimal quantityChange = randomQuantity();
        return Transaction.builder()
                .asset(asset)
                .unitPrice(unitPrice)
                .quantityChange(quantityChange)
                .totalAmount(Money.of(unitPrice.getAmount().multiply(quantityChange), Currency.EUR))
                .timestamp(LocalDateTime.now())
                .exchangeRate(BigDecimal.ONE);
    }

    public static TransactionSaveRequest randomTransactionDto(Asset asset, LocalDateTime timestamp) {
        BigDecimal quantityChange = randomQuantity();
        BigDecimal unitPrice = randomAmount();
        BigDecimal totalPrice = quantityChange.multiply(unitPrice).add(randomAmount(0, 1)).setScale(4, RoundingMode.HALF_UP);
        return TransactionSaveRequest.builder()
                .assetId(asset.getId())
                .quantityChange(quantityChange)
                .unitPrice(Money.of(unitPrice, Currency.GBP))
                .totalAmount(Money.of(totalPrice, Currency.GBP))
                .exchangeRate(BigDecimal.ONE)
                .timestamp(timestamp)
                .build();
    }

    public static TransactionSaveRequest randomTransactionDto(Asset asset) {
        return randomTransactionDto(asset, LocalDateTime.now());
    }

    public static BigDecimal randomPrice() {
        return adjust(new BigDecimal("10"), -1, 3).setScale(4, RoundingMode.HALF_UP);
    }

    private static BigDecimal randomQuantity() {
        return new BigDecimal(randomInt(1, 3)).setScale(4, RoundingMode.HALF_UP);
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static int randomInt(int from, int to) {
        return from + new Random().nextInt(to);
    }

    public static int randomInt() {
        return new Random().nextInt(100000);
    }

    public static Transaction transaction(Asset asset, int daysOfData, int daysOffset) {
        return randomBaseTransaction(asset)
                .timestamp(LocalDateTime.now().minusDays(daysOfData).plusDays(daysOffset))
                .build();
    }

    public static double random(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * new Random().nextDouble();
    }

    public static BigDecimal adjust(BigDecimal price, double from, double to) {
        return price.add(randomAmount(from, to));
    }

    private static BigDecimal randomAmount(double from, double to) {
        return BigDecimal.valueOf(random(from, to)).setScale(4, RoundingMode.HALF_UP);
    }

    private static BigDecimal randomAmount() {
        return BigDecimal.valueOf(random(0, 100)).setScale(4, RoundingMode.HALF_UP);
    }

    public static PriceUpdate priceUpdate(Asset asset, int daysOfData, int daysOffset) {
        PriceUpdate priceUpdate = randomPriceUpdate(asset);
        priceUpdate.setTimestamp(LocalDateTime.now().minusDays(daysOfData).plusDays(daysOffset));
        return priceUpdate;
    }

    public static Account randomAccount() {
        return Account.builder()
                .name(randomString())
                .defaultCurrency("EUR")
                .build();
    }

    public static Fee randomFee(Asset asset) {
        return Fee.builder()
                .amount(Money.of(randomAmount(0.1, 1), Currency.EUR))
                .asset(asset)
                .timestamp(LocalDateTime.now())
                .build();
    }

    static boolean probabilityOf(Double value) {
        return Math.random() <= value;
    }

}
