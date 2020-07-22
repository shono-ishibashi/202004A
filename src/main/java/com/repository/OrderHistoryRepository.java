package com.repository;

import com.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderHistoryRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private SimpleJdbcInsert insert;

    private static final RowMapper<Order> ORDER_JOIN_ROW_MAPPER = (rs, i) -> {
        Order order = new Order();

        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setStatus(rs.getInt("status"));
        order.setTotalPrice(rs.getInt("total_price"));
        order.setOrderDate(rs.getDate("order_date"));
        order.setDestinationName(rs.getString("destination_name"));
        order.setDestinationEmail(rs.getString("destination_email"));
        order.setDestinationZipcode(rs.getString("destination_zipcode"));
        order.setDestinationAddress(rs.getString("destination_address"));
        order.setDestinationTel(rs.getString("destination_tel"));
        order.setDeliveryTime(rs.getTimestamp("delivery_time"));
        order.setPaymentMethod(rs.getInt("payment_method"));

        List<OrderItem> orderItemList = new ArrayList<>();

        order.setOrderItemList(orderItemList);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getInt("orderItemId"));
        orderItem.setItemId(rs.getInt("item_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setSize(rs.getString("size").charAt(0));
        orderItem.setReviewPoint(rs.getInt("point"));

        List<OrderTopping> orderToppingList = new ArrayList<>();

        orderItem.setOrderToppingList(orderToppingList);

        Item item = new Item();
        item.setId(rs.getInt("item_id"));
        item.setName(rs.getString("itemName"));
        item.setDescription(rs.getString("itemDescription"));
        item.setPriceL(rs.getInt("itemPriceL"));
        item.setPriceM(rs.getInt("itemPriceM"));
        item.setImagePath(rs.getString("itemImagePath"));
        item.setReviewPoint(rs.getDouble("itemPoint"));

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
        orderItemList.add(orderItem);
        order.setOrderItemList(orderItemList);


        return order;
    };

    private static final RowMapper<Order> ORDER_ROW_MAPPER = (rs, i) -> {
        Order order = new Order();

        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setStatus(rs.getInt("status"));
        order.setTotalPrice(rs.getInt("total_price"));
        order.setOrderDate(rs.getDate("order_date"));
        order.setDestinationName(rs.getString("destination_name"));
        order.setDestinationEmail(rs.getString("destination_email"));
        order.setDestinationZipcode(rs.getString("destination_zipcode"));
        order.setDestinationAddress(rs.getString("destination_address"));
        order.setDestinationTel(rs.getString("destination_tel"));
        order.setDeliveryTime(rs.getTimestamp("delivery_time"));
        order.setPaymentMethod(rs.getInt("payment_method"));

        return order;
    };

    @PostConstruct
    public void init(){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(
                ((JdbcTemplate) template.getJdbcOperations())
        );

        SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("orders");

        insert = withTableName.usingGeneratedKeyColumns("id");
    }

    public List<Order> findByUserIdJoinOrderItemsForOrderList(Integer userId){
        String sql = "SELECT o.id as id" +
                "     , o.user_id as user_id" +
                "     , o.status as status" +
                "     , o.total_price as total_price" +
                "     , o.order_date as order_date" +
                "     , o.destination_name as destination_name" +
                "     , o.destination_email as destination_email" +
                "     , o.destination_zipcode as destination_zipcode" +
                "     , o.destination_address as destination_address" +
                "     , o.destination_tel as destination_tel" +
                "     , o.delivery_time as delivery_time" +
                "     , o.payment_method as payment_method" +
                "     ,oi.id as orderItemId" +
                "     ,oi.order_id as order_id" +
                "     ,oi.item_id as item_id" +
                "     ,oi.quantity as quantity" +
                "     ,oi.size as size" +
                "     ,oi.point as point" +
                "     ,items.name as itemName" +
                "     ,items.description as itemDescription" +
                "     ,items.price_m as itemPriceM" +
                "     ,items.price_l as itemPriceL" +
                "     ,items.image_path as itemImagePath" +
                "     ,items.deleted as itemDeleted" +
                "     ,items.point as itempoint" +
                "     ,ot.id as orderTopping" +
                "     ,ot.topping_id as orderToppingId" +
                "     ,ot.order_item_id as orderItemId" +
                "     ,toppings.id as toppingId" +
                "     ,toppings.name as toppingName" +
                "     ,toppings.price_m as toppingPriceM" +
                "     ,toppings.price_l as toppingPriceL" +
                " FROM orders AS o" +
                " LEFT OUTER JOIN order_items AS oi" +
                " ON o.id = oi.order_id" +
                " LEFT OUTER JOIN items" +
                " ON oi.item_id = items.id" +
                " LEFT OUTER JOIN order_toppings as ot" +
                " ON ot.order_item_id = oi.id" +
                " LEFT OUTER JOIN toppings" +
                " ON toppings.id = ot.topping_id" +
                " WHERE o.user_id = :userId " +
                " AND NOT o.status = 0" +
                " AND items.deleted = false" +
                " ORDER BY o.order_date";

        SqlParameterSource param = new MapSqlParameterSource().addValue("userId",userId);

        List<Order> orderList = template.query(sql,param,ORDER_JOIN_ROW_MAPPER);

        return orderList;
    }
    public List<Order> findByUserIdJoinOrderItemsForOrderHistoryDetail(Integer orderId){
        String sql = "SELECT o.id as id" +
                "     , o.user_id as user_id" +
                "     , o.status as status" +
                "     , o.total_price as total_price" +
                "     , o.order_date as order_date" +
                "     , o.destination_name as destination_name" +
                "     , o.destination_email as destination_email" +
                "     , o.destination_zipcode as destination_zipcode" +
                "     , o.destination_address as destination_address" +
                "     , o.destination_tel as destination_tel" +
                "     , o.delivery_time as delivery_time" +
                "     , o.payment_method as payment_method" +
                "     ,oi.id as orderItemId" +
                "     ,oi.order_id as order_id" +
                "     ,oi.item_id as item_id" +
                "     ,oi.quantity as quantity" +
                "     ,oi.size as size" +
                "     ,oi.point as point" +
                "     ,items.name as itemName" +
                "     ,items.description as itemDescription" +
                "     ,items.price_m as itemPriceM" +
                "     ,items.price_l as itemPriceL" +
                "     ,items.image_path as itemImagePath" +
                "     ,items.deleted as itemDeleted" +
                "     ,items.point as itemPoint" +
                "     ,ot.id as orderTopping" +
                "     ,ot.topping_id as orderToppingId" +
                "     ,ot.order_item_id as orderItemId" +
                "     ,toppings.id as toppingId" +
                "     ,toppings.name as toppingName" +
                "     ,toppings.price_m as toppingPriceM" +
                "     ,toppings.price_l as toppingPriceL" +
                " FROM orders AS o" +
                " LEFT OUTER JOIN order_items AS oi" +
                " ON o.id = oi.order_id" +
                " LEFT OUTER JOIN items" +
                " ON oi.item_id = items.id" +
                " LEFT OUTER JOIN order_toppings as ot" +
                " ON ot.order_item_id = oi.id" +
                " LEFT OUTER JOIN toppings" +
                " ON toppings.id = ot.topping_id" +
                " WHERE o.id = :orderId" +
                " AND items.deleted = false" +
                " ORDER BY o.order_date ;";

        SqlParameterSource param = new MapSqlParameterSource().addValue("orderId",orderId);

        List<Order> orderList = template.query(sql,param,ORDER_JOIN_ROW_MAPPER);

        return orderList;
    }
}

