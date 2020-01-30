package com.nordcomet.portfolio.data.transaction;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepo extends CrudRepository<Transaction, Integer> {

    Optional<Transaction> findFirstByAssetIdOrderByTimestampDesc(Integer assetId);

    List<Transaction> findAllByAssetId(Integer assetId);

    List<Transaction> findTop10ByOrderByTimestampDesc();

}
