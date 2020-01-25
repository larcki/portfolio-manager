package com.nordcomet.pflio.asset.service.importer.fidelity;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FidelityPortfolioReportFetcher {

    public File fetchReport() {
        return new File(getClass().getClassLoader().getResource("fidelity-transactions.csv").getFile());
    }
}
