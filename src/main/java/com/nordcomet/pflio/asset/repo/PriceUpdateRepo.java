package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.PriceUpdate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PriceUpdateRepo extends CrudRepository<PriceUpdate, Integer> {

    PriceUpdate findFirstByAssetIdOrderByTimestampDesc(Integer assetId);

    List<PriceUpdate> findAllByAssetId(Integer assetId);

}
