package com.nordcomet.pflio.repo;

import com.nordcomet.pflio.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransactionRepo extends CrudRepository<Transaction, Integer> {

    Optional<Transaction> findFirstByAssetIdOrderByTimestampDesc(Integer assetId);
}
