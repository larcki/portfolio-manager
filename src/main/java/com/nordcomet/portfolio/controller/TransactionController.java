package com.nordcomet.portfolio.controller;

import com.nordcomet.portfolio.service.transaction.TransactionResponse;
import com.nordcomet.portfolio.service.transaction.TransactionSaveRequest;
import com.nordcomet.portfolio.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public void saveTransaction(@RequestBody TransactionSaveRequest saveRequest) {
        transactionService.save(saveRequest);
    }

    @RequestMapping
    public List<TransactionResponse> getTransactions() {
        return transactionService.getTransactions();
    }

}
