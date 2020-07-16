package com.repository;

import com.domain.Order;
import com.domain.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@Repository
public class OrderItemRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private SimpleJdbcInsert insert;

    @PostConstruct
    public void init(){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(
                ((JdbcTemplate) template.getJdbcOperations())
        );

        SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("order_items");

        insert = withTableName.usingGeneratedKeyColumns("id");
    }



    public Integer insertOrderItem(OrderItem orderItem) {

        SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);

        Number id = insert.executeAndReturnKey(param);

        return id.intValue();
    }
}
