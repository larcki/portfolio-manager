package com.nordcomet.portfolio.controller;

import com.nordcomet.portfolio.DataRandomiser;
import com.nordcomet.portfolio.data.asset.AssetRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
@Disabled
class TransactionControllerTest {

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAcceptRequest() throws Exception {
        Integer asset = assetRepo.save(DataRandomiser.randomAsset()).getId();

        String request = "{\n" +
                "    \"totalAmount\": {\n" +
                "        \"amount\": \"1\",\n" +
                "        \"currency\": \"EUR\"\n" +
                "    },\n" +
                "    \"unitPrice\": {\n" +
                "        \"amount\": \"1\",\n" +
                "        \"currency\": \"EUR\"\n" +
                "    },\n" +
                "    \"quantityChange\": \"1\",\n" +
                "    \"exchangeRate\": \"1\",\n" +
                "    \"timestamp\": \"2019-10-20T11:32:32.573Z\",\n" +
                "    \"assetId\": "+asset+"\n" +
                "}";

        this.mockMvc.perform(
                post("/api/transaction")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}