package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransactionRepo extends CrudRepository<Transaction, Integer> {

    Optional<Transaction> findFirstByAssetIdOrderByTimestampDesc(Integer assetId);
}
