package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.Fee;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FeeRepo extends CrudRepository<Fee, UUID> {
}
