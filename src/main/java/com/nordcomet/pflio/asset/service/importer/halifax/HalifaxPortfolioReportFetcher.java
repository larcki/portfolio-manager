package com.nordcomet.pflio.asset.service.importer.halifax;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class HalifaxPortfolioReportFetcher {

    public File fetchReport() {
        return new File(getClass().getClassLoader().getResource("halifax-transactions.html").getFile());
    }
}
