package com.ozge.wallet.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Gün 0: bu test baştan yeşil olmalı. Yeşilse kurulum tamam demektir.
 * Önce: docker compose up -d postgres
 */
@SpringBootTest
@AutoConfigureMockMvc
class AccountApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void opensAccountAndReadsItBack() throws Exception {
        var body = """
                {"ownerName": "Özge", "currency": "EUR", "initialBalance": 1000.00}
                """;

        var response = mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andReturn();

        var id = com.jayway.jsonpath.JsonPath.read(response.getResponse().getContentAsString(), "$.id").toString();

        mockMvc.perform(get("/api/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName").value("Özge"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void returns404ForUnknownAccount() throws Exception {
        mockMvc.perform(get("/api/accounts/{id}", 999_999_999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidCurrency() throws Exception {
        var body = """
                {"ownerName": "Özge", "currency": "euro", "initialBalance": 10}
                """;

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
