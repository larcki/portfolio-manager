package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.Account;
import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Fee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.nordcomet.pflio.DataRandomiser.*;
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