package com.nordcomet.pflio.asset;

import com.nordcomet.pflio.asset.model.AssetDto;
import com.nordcomet.pflio.asset.model.AssetSelectionDto;
import com.nordcomet.pflio.asset.model.PortfolioSummary;
import com.nordcomet.pflio.asset.model.TransactionSaveRequest;
import com.nordcomet.pflio.asset.service.AssetPositionService;
import com.nordcomet.pflio.asset.service.AssetService;
import com.nordcomet.pflio.asset.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class AssetController {

    private final TransactionService transactionService;
    private final AssetService assetService;
    private final AssetPositionService assetPositionService;

    @Autowired
    public AssetController(TransactionService transactionService, AssetService assetService, AssetPositionService assetPositionService) {
        this.transactionService = transactionService;
        this.assetService = assetService;
        this.assetPositionService = assetPositionService;
    }

    @PostMapping("/api/transaction")
    public void saveTransaction(@RequestBody TransactionSaveRequest saveRequest) {
        transactionService.save(saveRequest);
    }

    @RequestMapping("/api/transaction")
    public List<TransactionResponse> getTransactions() {
        return transactionService.getTransactions();
    }

    @RequestMapping("/api/assets")
    public List<AssetDto> getAssets() {
        return assetService.getAssetList();
    }

    @RequestMapping("/api/assets-min")
    public List<AssetSelectionDto> getAssetsForSelection() {
        return assetService.getAssetForSelection();
    }

    @RequestMapping("/api/summary")
    public PortfolioSummary getSummary() {
        return assetPositionService.getPortfolioSummary();
    }

    @RequestMapping("/api/assets/{assetId}")
    public AssetInfoDto getAssets(@PathVariable("assetId") Integer assetId) {
        return assetService.getInfo(assetId);
    }

}
