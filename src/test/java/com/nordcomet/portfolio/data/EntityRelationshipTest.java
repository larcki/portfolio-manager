package com.nordcomet.portfolio.data;

import com.nordcomet.portfolio.data.account.Account;
import com.nordcomet.portfolio.data.account.AccountRepo;
import com.nordcomet.portfolio.data.asset.Asset;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import com.nordcomet.portfolio.data.fee.Fee;
import com.nordcomet.portfolio.data.fee.FeeRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.nordcomet.portfolio.DataRandomiser.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class EntityRelationshipTest {

    @Autowired
    private FeeRepo feeRepo;

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Test
    void shouldSaveFee() {
        Asset asset = randomAsset();
        assetRepo.save(asset);

        Fee fee = randomFee(asset);
        feeRepo.save(fee);

        assertEquals(feeRepo.count(), 1);
    }

    @Test
    void shouldSaveAccount() {
        Account account = randomAccount();
        accountRepo.save(account);

        Asset asset = randomAsset(account);
        assetRepo.save(asset);
    }

}