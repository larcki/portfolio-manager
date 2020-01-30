package com.nordcomet.portfolio.service.transaction.imports.fidelity;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FidelityPortfolioReportFetcher {

    public File fetchReport() {
        return new File(getClass().getClassLoader().getResource("fidelity-transactions.csv").getFile());
    }
}
