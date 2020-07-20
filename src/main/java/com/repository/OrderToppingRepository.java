package com.repository;

import com.domain.OrderItem;
import com.domain.OrderTopping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
    import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
    import org.springframework.jdbc.core.namedparam.SqlParameterSource;
    import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
    import org.springframework.stereotype.Repository;

    import javax.annotation.PostConstruct;

@Repository
public class OrderToppingRepository {
    @Autowired
    private NamedParameterJdbcTemplate template;

    public void insertOrderTopping(OrderTopping orderTopping) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(orderTopping);
        String sql = "INSERT INTO order_toppings(topping_id, order_item_id) " +
                "VALUES (:toppingId,:orderItemId)";

        template.update(sql,param );
    }
}
