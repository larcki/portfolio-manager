package com.nordcomet.pflio.asset.service.importer.fidelity;

import com.nordcomet.pflio.asset.model.TransactionSaveRequest;
import com.nordcomet.pflio.asset.repo.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FidelityTransactionMapper {

    private final AssetRepo assetRepo;

    @Autowired
    public FidelityTransactionMapper(AssetRepo assetRepo) {
        this.assetRepo = assetRepo;
    }

    public TransactionSaveRequest toTransactionSaveRequest(FidelityTransaction fidelityTransaction) {
        return TransactionSaveRequest.builder()
                .build();
    }

    private Integer findAssetId(String isin) {
        return assetRepo.findAssetByIsin(isin).orElseThrow(() -> new IllegalArgumentException("Unable to find asset with isin " + isin)).getId();
    }

}
