package com.nordcomet.pflio.asset;

import com.nordcomet.pflio.asset.model.AssetDto;
import com.nordcomet.pflio.asset.model.TransactionDto;
import com.nordcomet.pflio.asset.service.AssetService;
import com.nordcomet.pflio.asset.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class AssetController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AssetService assetService;

    @PostMapping("/api/transaction")
    public void saveTransaction(TransactionDto transactionDto) {
        transactionService.save(transactionDto);
    }

    @RequestMapping("/api/assets")
    public List<AssetDto> getAssets() {
        return assetService.getAssetList();
    }

    @RequestMapping("/api/assets/{assetId}")
    public AssetInfoDto getAssets(@PathVariable("assetId") Integer assetId) {
        return assetService.getInfo(assetId);
    }

}
