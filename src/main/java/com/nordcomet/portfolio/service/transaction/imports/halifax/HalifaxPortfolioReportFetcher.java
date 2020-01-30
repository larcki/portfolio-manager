package com.nordcomet.portfolio.service.transaction.imports.halifax;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class HalifaxPortfolioReportFetcher {

    File fetchReport() {
        return new File(getClass().getClassLoader().getResource("halifax-transactions.html").getFile());
    }
}
