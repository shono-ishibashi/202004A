package com.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.print.Pageable;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
class ShowListControllerTest {

    private MockMvc mockMvc;

    @Autowired
    ShowListController showListController;

    @BeforeEach
    void beforeDo(){
        mockMvc = MockMvcBuilders.standaloneSetup(showListController).build();
    }

    @AfterEach
    void afterDo(){

    }

    @Test
    void showList() throws Exception {

    }

    @Test
    void searchNoodle() {
    }

    @Test
    void searchByGenre() {
    }
}