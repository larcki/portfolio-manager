package com.nordcomet.portfolio.data.account;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepo extends CrudRepository<Account, Integer> {

    Optional<Account> findAccountById(Integer id);

}
