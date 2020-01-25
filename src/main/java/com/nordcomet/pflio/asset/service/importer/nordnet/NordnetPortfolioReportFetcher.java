package com.nordcomet.pflio.asset.service.importer.nordnet;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class NordnetPortfolioReportFetcher {

    public File fetchReport() {
        return new File(getClass().getClassLoader().getResource("PortfolioReport.xls").getFile());
    }
}
