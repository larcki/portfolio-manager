package com.nordcomet.pflio.asset.service.importer;

import com.nordcomet.pflio.asset.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

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
                .forEachOrdered(transactionService::save);

    }

}
