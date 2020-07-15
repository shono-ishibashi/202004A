package com.repository;

import com.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Order> ORDER_ROW_MAPPER = (rs,i) -> {
        Order order = new Order();

        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setStatus(rs.getInt("status"));
        order.setTotalPrice(rs.getInt("total_price"));
        order.setOrderDate(rs.getDate("order_date").toLocalDate());
        order.setDestinationName(rs.getString("destination_name"));
        order.setDestinationEmail(rs.getString("destination_email"));
        order.setDestinationZipcode(rs.getString("destination_zipcode"));
        order.setDestinationAddress(rs.getString("destination_address"));
        order.setDestinationTel(rs.getString("destination_tel"));
        order.setDeliveryTime(rs.getTimestamp("delivery_time"));
        order.setPaymentMethod(rs.getInt("payment_method"));

        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getInt("order_item_id"));
        orderItem.setItemId(rs.getInt("item_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setSize(rs.getString("size").charAt(0));

        Item item = new Item();
        item.setId(rs.getInt("item_id"));
        item.setName(rs.getString("itemName"));
        item.setDescription(rs.getString("itemDescription"));
        item.setPriceL(rs.getInt("itemPriceL"));
        item.setPriceM(rs.getInt("itemPriceM"));
        item.setImagePath(rs.getString("itemImagePath"));

        Topping topping = new Topping();
        topping.setId(rs.getInt("toppingId"));
        topping.setName(rs.getString("toppingName"));
        topping.setPriceM(rs.getInt("toppingPriceM"));
        topping.setPriceL(rs.getInt("toppingPriceL"));

        OrderTopping orderTopping = new OrderTopping();
        orderTopping.setId(rs.getInt("orderToppingId"));
        orderTopping.setToppingId(rs.getInt("orderToppingId"));
        orderTopping.setOrderItemId(rs.getInt("orderItemId"));



        orderTopping.setTopping(topping);
        orderItem.setItem(item);
        orderItem.getOrderToppingList().add(orderTopping);

        order.getOrderItemList().add(orderItem);


        return order;
    };

    public List<Order> findByUserIdAndStatus0(Integer userId ,Integer status) {
        String sql = "SELECT * FROM orders WHERE user_id = :userId and status = :status;";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId",userId)
                .addValue("status",status);

        List<Order> orderList = template.query(sql,param,ORDER_ROW_MAPPER);

        return orderList;
    }

    public List<Order> findByUserIdJoinOrderItems(Integer userId){
        String sql = "SELECT o.id as i" +
                "     , o.user_id as user_i" +
                "     , o.status as statu" +
                "     , o.total_price as total_pric" +
                "     , o.order_date as order_dat" +
                "     , o.destination_name as destination_nam" +
                "     , o.destination_email as destination_emai" +
                "     , o.destination_zipcode as destination_zipcod" +
                "     , o.destination_address as destination_addres" +
                "     , o.destination_tel as destination_te" +
                "     , o.delivery_time as delivery_tim" +
                "     , o.payment_method as payment_metho" +
                "     ,oi.id as order_item_i" +
                "     ,oi.item_id as item_i" +
                "     ,oi.quantity as quantit" +
                "     ,oi.size as siz" +
                "     ,items.name as itemNam" +
                "     ,items.description as itemDescriptio" +
                "     ,items.price_m as price" +
                "     ,items.price_l as price" +
                "     ,items.image_path as itemImagePat" +
                "     ,items.deleted as itemDelete" +
                "     ,ot.id as orderToppin" +
                "     ,ot.topping_id as orderToppingI" +
                "     ,ot.order_item_id as orderItemI" +
                "     ,toppings.id as toppingI" +
                "     ,toppings.name as toppingNam" +
                "     ,toppings.price_m as toppingPrice" +
                "     ,toppings.price_l as toppingPrice" +
                " FROM orders AS o" +
                " JOIN order_items AS oi" +
                " ON o.id = oi.order_id" +
                " JOIN items" +
                " ON oi.item_id = items.id" +
                " JOIN order_toppings as ot" +
                " ON ot.order_item_id = oi.id" +
                " JOIN toppings" +
                " ON toppings.id = ot.topping_id" +
                " WHERE o.user_id = :userId " +
                " AND items.deleted = false" +
                " ORDER BY o.order_date ;";

        SqlParameterSource param = new MapSqlParameterSource().addValue("userId",userId);

        return template.query(sql,param,ORDER_ROW_MAPPER);
    }

    public Integer insert(Order order){
        String sql = "INSERT INTO orders (user_id, status, total_price, order_date, destination_name, destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method) " +
                "VALUES (:userId, :status, :totalPrice, :orderDate , :destinationName, :destinationEmail, :destinationZipcode , :destinationAddress, :destinationTel ,:deliveryTime, :paymentMethod)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(order);

        Number id = template.update(sql,param);
        template.update(sql,param);

        return id.intValue();
    }
}
