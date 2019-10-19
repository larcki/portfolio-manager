package com.nordcomet.pflio.asset.service;

import com.nordcomet.pflio.asset.model.Account;
import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DataGeneratorService {

    @Autowired
    private AssetRepo assetRepo;

    @Transactional
    public void generate(Account account, List<Asset> assets) {

        assets.forEach(asset -> {
            assetRepo.save(asset);
        });

    }

}
