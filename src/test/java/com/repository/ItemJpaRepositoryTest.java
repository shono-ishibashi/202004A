package com.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemJpaRepositoryTest {

    @Autowired ItemJpaRepository itemJpaRepository;

    @Test
    void findAllByOrderByPriceM() {

    }

    @Test
    void findByGenreOrderByPriceM() {
    }

    @Test
    void findByGenreOrderByPriceMDesc() {
    }

    @Test
    void findByNameContainingOrderByPriceM() {
    }

    @Test
    void findByNameContainingOrderByPriceMDesc() {
    }

    @Test
    void findAllByOrderByPriceMDesc() {
    }
}