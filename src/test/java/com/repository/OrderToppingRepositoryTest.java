package com.repository;

import com.domain.OrderTopping;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderToppingRepositoryTest {

    @Autowired
    private OrderToppingRepository orderToppingRepository;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeInsert(){
        String sql = "drop table if exists order_toppings cascade; create table order_toppings (id serial primary key, topping_id integer not null, order_item_id integer not null)";
        jdbcTemplate.execute(sql);
        String insertSql = "INSERT INTO order_toppings(topping_id, order_item_id) VALUES (1,2)";
        template.update(sql, new MapSqlParameterSource()) ;
    }

    @AfterEach
    void dropTable(){
        String dropSql = "DROP TABLE order_toppings";
        jdbcTemplate.execute(dropSql);
    }

    private static final RowMapper<OrderTopping> ORDER_TOPPING_ROW_MAPPER
            =(rs,i) -> {
        OrderTopping orderTopping = new OrderTopping();
        orderTopping.setId(rs.getInt("id"));
        orderTopping.setToppingId(rs.getInt("topping_id"));
        orderTopping.setOrderItemId(rs.getInt("order_item_id"));
        return orderTopping;
    };

    @Test
    void insertOrderTopping() {
        OrderTopping orderTopping = new OrderTopping();
        orderTopping.setOrderItemId(1);
        orderTopping.setToppingId(3);
        orderToppingRepository.insertOrderTopping(orderTopping);

        String sql = "SELECT*FROM order_toppings WHERE id = (SELECT max(id) FROM order_toppings)";
        List<OrderTopping> orderToppingList = template.query(sql,ORDER_TOPPING_ROW_MAPPER);
        assertEquals(1, (int) orderToppingList.get(0).getOrderItemId());
        assertEquals(3, (int) orderToppingList.get(0).getToppingId());
    }
}