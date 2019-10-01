package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AccountRepo extends CrudRepository<Account, UUID> {
}
