package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.PriceUpdate;
import org.springframework.data.repository.CrudRepository;

public interface PriceUpdateRepo extends CrudRepository<PriceUpdate, Integer> {

    PriceUpdate findFirstByAssetIdOrderByTimestampDesc(Integer assetId);

}
