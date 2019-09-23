package com.nordcomet.pflio.service;

import com.nordcomet.pflio.DataRandomiser;
import com.nordcomet.pflio.model.Asset;
import com.nordcomet.pflio.model.Tag;
import com.nordcomet.pflio.model.Tags;
import com.nordcomet.pflio.model.Transaction;
import com.nordcomet.pflio.repo.AssetRepo;
import com.nordcomet.pflio.view.ChartView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.nordcomet.pflio.DataRandomiser.randomAsset;
import static com.nordcomet.pflio.model.Tags.BOND;
import static com.nordcomet.pflio.model.Tags.STOCK;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ChartServiceTest {

    @Autowired
    private ChartService underTest;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AssetRepo assetRepo;

    @Test
    void name() {
        Asset bond = randomAsset();
        bond.setTags(List.of(new Tag(BigDecimal.ONE, BOND)));
        assetRepo.save(bond);
        Asset stock = randomAsset();
        stock.setTags(List.of(new Tag(BigDecimal.ONE, BOND)));
        assetRepo.save(stock);
        transactionService.save(randomTransaction(bond, LocalDateTime.now().minusDays(10)));
        transactionService.save(randomTransaction(stock, LocalDateTime.now().minusDays(10)));

        transactionService.save(randomTransaction(bond, LocalDateTime.now().minusDays(8)));
        transactionService.save(randomTransaction(stock, LocalDateTime.now().minusDays(8)));

        transactionService.save(randomTransaction(bond, LocalDateTime.now().minusDays(6)));
        transactionService.save(randomTransaction(stock, LocalDateTime.now().minusDays(6)));

        transactionService.save(randomTransaction(bond, LocalDateTime.now().minusDays(4)));
        transactionService.save(randomTransaction(stock, LocalDateTime.now().minusDays(4)));

        transactionService.save(randomTransaction(bond, LocalDateTime.now().minusDays(2)));
        transactionService.save(randomTransaction(stock, LocalDateTime.now().minusDays(2)));

        int sinceDays = 10;
        List<Tags> tags = List.of(STOCK, BOND);
        ChartView result = underTest.getStackedValueChart(tags, sinceDays);
        System.out.println(result);

    }

    private Transaction randomTransaction(Asset asset, LocalDateTime timestamp) {
        Transaction transaction = DataRandomiser.randomTransaction(asset);
        transaction.setTimestamp(timestamp);
        return transaction;
    }
}