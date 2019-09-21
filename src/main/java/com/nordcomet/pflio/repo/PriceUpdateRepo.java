package com.nordcomet.pflio.repo;

import com.nordcomet.pflio.model.PriceUpdate;
import org.springframework.data.repository.CrudRepository;

public interface PriceUpdateRepo extends CrudRepository<PriceUpdate, Integer> {

    PriceUpdate findFirstByAssetIdOrderByTimestampDesc(Integer assetId);

}
