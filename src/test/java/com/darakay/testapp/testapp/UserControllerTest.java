package com.darakay.testapp.testapp;

import com.darakay.testapp.testapp.dto.TransactionDto;
import com.darakay.testapp.testapp.dto.TransactionResult;
import com.darakay.testapp.testapp.dto.UserTransaction;
import com.darakay.testapp.testapp.entity.*;
import com.darakay.testapp.testapp.exception.TransactionNotFountException;
import com.darakay.testapp.testapp.repos.AccountRepository;
import com.darakay.testapp.testapp.repos.TransactionRepository;
import com.darakay.testapp.testapp.repos.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String CONTROLLER_URI = "/users";

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user2", password = "qwerty")
    public void getTransactions() throws Exception {
        String uri = CONTROLLER_URI + "/2000/transaction";

        MvcResult result = mockMvc.perform(get(uri)).andReturn();

        List<UserTransaction> userTransactions =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<UserTransaction>>(){});

        assertThat(userTransactions).asList().contains(
                UserTransaction.builder().accountId(2).otherId(1)
                        .sum(2000).date("2019-07-10 08:20:32").type("transaction").build(),
                UserTransaction.builder().accountId(2).otherId(1)
                        .sum(500).date("2019-07-11 09:20:32").type("transaction").build());

    }

    @Test
    @WithMockUser(username = "user2", password = "qwerty")
    public void getTransactionsSortedByDate() throws Exception {
        String uri = CONTROLLER_URI + "/2000/transaction?sortedBy=date";

        MvcResult result = mockMvc.perform(get(uri)).andReturn();

        List<UserTransaction> userTransactions =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<UserTransaction>>(){});

        assertThat(userTransactions).asList().containsSequence(
                UserTransaction.builder().accountId(2).otherId(1)
                        .sum(2000).date("2019-07-10 08:20:32").type("transaction").build(),
                UserTransaction.builder().accountId(2).otherId(1)
                        .sum(500).date("2019-07-11 09:20:32").type("transaction").build());

    }

    @Test
    @WithMockUser(username = "user2", password = "qwerty")
    public void getTransactionsSortedByTransactionSum() throws Exception {
        String uri = CONTROLLER_URI + "/2000/transaction?sortedBy=sum";

        MvcResult result = mockMvc.perform(get(uri)).andReturn();

        List<UserTransaction> userTransactions =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<UserTransaction>>(){});

        assertThat(userTransactions).asList().containsSequence(
                UserTransaction.builder().accountId(2).otherId(1)
                        .sum(500).date("2019-07-11 09:20:32").type("transaction").build(),
                UserTransaction.builder().accountId(2).otherId(1)
                        .sum(2000).date("2019-07-10 08:20:32").type("transaction").build());

    }

    @Test
    @WithMockUser(username = "user2", password = "qwerty")
    public void getTransactions_() throws Exception {
        String uri = CONTROLLER_URI + "/2000/transaction?sortedBy=sum&limit=1&offset=2";

        MvcResult result = mockMvc.perform(get(uri)).andReturn();

        List<UserTransaction> userTransactions =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<UserTransaction>>() {
                        });

        assertThat(userTransactions).asList().containsSequence(
                UserTransaction.builder().accountId(2).otherId(1)
                        .sum(2000).date("2019-07-10 08:20:32").type("transaction").build());

    }
}