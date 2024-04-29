package com.example.katabforbank;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

@SpringBootTest
@AutoConfigureMockMvc
class KataBforbankApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository repository;

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void whenAddPot_thenPotShouldBeAdded(Long id) throws Exception {
        int amount = new Random().nextInt((50 - 2) + 1) + 2;
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

        mockMvc.perform(patch("/customers/" + id + "/pot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/hal+json")
                        .content(String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.potValue", is(customer.getPot() + amount)))
                .andExpect(jsonPath("$.passCount", is(customer.getPassCount() + 1)));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void givenCustomerId_WhenGetCustomer_thenStatus200(Long id) throws Exception {
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

        mockMvc.perform(get("/customers/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", is(customer.getId().intValue())))
                .andExpect(jsonPath("$.fullName", is(customer.getFullName())));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void givenCustomerId_WhenGetPot_thenStatus200(Long id) throws Exception {
        Customer customer = repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));

        mockMvc.perform(get("/customers/" + id + "/pot")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.potAvailability", is(customer.getPotAvailability().name())))
                .andExpect(jsonPath("$.potOwner", is(customer.getFullName())));
    }
}
