package com.nordcomet.portfolio.service.transaction.imports.nordnet;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class NordnetPortfolioReportFetcher {

    public File fetchReport() {
        return new File(getClass().getClassLoader().getResource("PortfolioReport.xls").getFile());
    }
}
