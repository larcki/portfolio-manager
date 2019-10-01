package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.Fee;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FeeRepo extends CrudRepository<Fee, Integer> {

    Optional<Fee> findFeeByAssetId(Integer assetId);
}
