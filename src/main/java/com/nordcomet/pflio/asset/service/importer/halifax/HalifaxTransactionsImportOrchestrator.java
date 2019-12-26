package com.nordcomet.pflio.asset.service.importer.halifax;

import com.nordcomet.pflio.asset.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class HalifaxTransactionsImportOrchestrator {

    private final HalifaxTransactionReader halifaxTransactionReader;
    private final HalifaxPortfolioReportFetcher halifaxPortfolioReportFetcher;
    private final HalifaxTransactionMapper halifaxTransactionMapper;
    private final TransactionService transactionService;

    @Autowired
    public HalifaxTransactionsImportOrchestrator(HalifaxTransactionReader halifaxTransactionReader, HalifaxPortfolioReportFetcher halifaxPortfolioReportFetcher, HalifaxTransactionMapper halifaxTransactionMapper, TransactionService transactionService) {
        this.halifaxTransactionReader = halifaxTransactionReader;
        this.halifaxPortfolioReportFetcher = halifaxPortfolioReportFetcher;
        this.halifaxTransactionMapper = halifaxTransactionMapper;
        this.transactionService = transactionService;
    }

    public void importTransactions() {
        File file = halifaxPortfolioReportFetcher.fetchReport();
        halifaxTransactionReader.readTransactions(file).stream()
                .map(halifaxTransactionMapper::toTransactionSaveRequest)
                .forEachOrdered(transactionService::save);
    }

}
