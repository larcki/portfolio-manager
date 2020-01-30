package com.nordcomet.portfolio.service.priceupdate.imports.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.of;

@Service
@Slf4j
public class PageDocumentProvider {

    private static final int TIMEOUT_MS = 120000;

    Optional<Document> getPage(String url) {
        try {
            log.info("Connecting {}", url);
            return of(Jsoup.connect(url).timeout(TIMEOUT_MS).get());
        } catch (IOException e) {
            log.error("Failed to fetch page from " + url, e);
            return Optional.empty();
        }
    }

}
