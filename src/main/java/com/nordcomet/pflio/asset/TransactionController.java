package com.nordcomet.pflio.asset;

import com.nordcomet.pflio.asset.model.TransactionDto;
import com.nordcomet.pflio.asset.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction")
    public void saveTransaction(TransactionDto transactionDto) {
        logger.info("Incoming request, period {} chartType {} tags {} assets {}", transactionDto);

        transactionService.save(transactionDto);
    }
}
