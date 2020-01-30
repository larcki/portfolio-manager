package com.nordcomet.portfolio.data.fee;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FeeRepo extends CrudRepository<Fee, Integer> {

    Optional<Fee> findFeeByAssetId(Integer assetId);
}
