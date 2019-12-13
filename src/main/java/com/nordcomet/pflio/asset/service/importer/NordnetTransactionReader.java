package com.nordcomet.pflio.asset.service.importer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class NordnetTransactionReader {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public List<NordnetTransaction> readTransactions(File file) {

        try {
            FileInputStream excelFile = new FileInputStream(file);
            Workbook workbook = new HSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);

            return StreamSupport.stream(datatypeSheet.spliterator(), false)
                    .skip(3)
                    .map(this::rowToTransaction)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private NordnetTransaction rowToTransaction(Row row) {
        return NordnetTransaction.builder()
                .date(toLocalDate(row.getCell(0)))
                .assetName(row.getCell(1).getStringCellValue())
                .isin(row.getCell(3).getStringCellValue())
                .currency(row.getCell(5).getStringCellValue())
                .quantity(toBigDecimal(row.getCell(6)))
                .purchasePriceInCurrency(toBigDecimal(row.getCell(7)))
                .purchasePriceInEur(toBigDecimal(row.getCell(8)))
                .purchaseValueInCurrency(toBigDecimal(row.getCell(9)))
                .purchaseValueInEur(toBigDecimal(row.getCell(10)))
                .marketValueInCurrency(toBigDecimal(row.getCell(11)))
                .marketValueInEur(toBigDecimal(row.getCell(12)))
                .build();
    }

    private BigDecimal toBigDecimal(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return new BigDecimal(cell.getNumericCellValue()).setScale(12, RoundingMode.HALF_UP);
        }
        return new BigDecimal(cell.getStringCellValue());
    }

    private LocalDate toLocalDate(Cell cell) {
        return LocalDate.parse(cell.getStringCellValue(), formatter);
    }

}
