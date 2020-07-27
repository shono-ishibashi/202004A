package com.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShowDetailControllerTest {

    @Autowired
    private ShowDetailController showDetailController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(showDetailController).build();
    }

    @Test
    void setUpCartAddForm() {
    }

    @Test
    void showDetail() throws Exception{
        mockMvc.perform(get("/noodle/show-detail?id=1"))
                .andExpect(status().isOk()).andExpect(view().name("item_detail"));

    }

    @AfterEach
    void tearDown() {
    }




}