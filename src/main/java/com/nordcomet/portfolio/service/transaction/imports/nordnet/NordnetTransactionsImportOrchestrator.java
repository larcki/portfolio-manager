package com.nordcomet.portfolio.service.transaction.imports.nordnet;

import com.nordcomet.portfolio.service.transaction.TransactionSaveRequest;
import com.nordcomet.portfolio.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.stream.Collectors;

@Service
public class NordnetTransactionsImportOrchestrator {

    private final NordnetTransactionReader nordnetTransactionReader;
    private final TransactionService transactionService;
    private final NordnetTransactionMapper nordnetTransactionsToTransactionRequestMapper;
    private final NordnetPortfolioReportFetcher nordnetPortfolioReportFetcher;

    @Autowired
    public NordnetTransactionsImportOrchestrator(NordnetTransactionReader nordnetTransactionReader,
                                                 TransactionService transactionService,
                                                 NordnetTransactionMapper nordnetTransactionsToTransactionRequestMapper,
                                                 NordnetPortfolioReportFetcher nordnetPortfolioReportFetcher) {
        this.nordnetTransactionReader = nordnetTransactionReader;
        this.transactionService = transactionService;
        this.nordnetTransactionsToTransactionRequestMapper = nordnetTransactionsToTransactionRequestMapper;
        this.nordnetPortfolioReportFetcher = nordnetPortfolioReportFetcher;
    }

    public void importTransactions() {
        File portfolioReport = nordnetPortfolioReportFetcher.fetchReport();

        nordnetTransactionReader.readTransactions(portfolioReport).stream()
                .map(nordnetTransactionsToTransactionRequestMapper::toTransactionSaveRequest)
                .collect(Collectors.groupingBy(TransactionSaveRequest::getAssetId))
                .entrySet().parallelStream()
                .forEach(entry -> entry.getValue().forEach(transactionService::save));

    }

}
