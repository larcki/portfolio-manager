package com.nordcomet.pflio.asset.service.importer.fidelity;

import com.nordcomet.pflio.asset.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FidelityTransactionsImportOrchestrator {

    private final FidelityTransactionReader fidelityTransactionReader;
    private final FidelityPortfolioReportFetcher fidelityPortfolioReportFetcher;
    private final FidelityTransactionMapper fidelityTransactionMapper;
    private final TransactionService transactionService;

    @Autowired
    public FidelityTransactionsImportOrchestrator(FidelityTransactionReader fidelityTransactionReader, FidelityPortfolioReportFetcher fidelityPortfolioReportFetcher, FidelityTransactionMapper fidelityTransactionMapper, TransactionService transactionService) {
        this.fidelityTransactionReader = fidelityTransactionReader;
        this.fidelityPortfolioReportFetcher = fidelityPortfolioReportFetcher;
        this.fidelityTransactionMapper = fidelityTransactionMapper;
        this.transactionService = transactionService;
    }

    public void importTransactions() {
        File file = fidelityPortfolioReportFetcher.fetchReport();
        fidelityTransactionReader.readTransactions(file).stream()
                .map(fidelityTransactionMapper::toTransactionSaveRequest)
                .forEachOrdered(transactionService::save);
    }

}
