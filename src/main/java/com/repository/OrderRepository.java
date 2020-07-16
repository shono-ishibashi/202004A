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
        orderItem.setId(rs.getInt("order_item_id"));
        orderItem.setItemId(rs.getInt("item_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setSize(rs.getString("size").charAt(0));

        List<OrderTopping> orderToppingList = new ArrayList<>();

        orderItem.setOrderToppingList(orderToppingList);

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

    public List<Order> findByUserIdAndStatus(Integer userId ,Integer status) {
        String sql = "SELECT * FROM orders WHERE user_id = :userId and status = :status;";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId",userId)
                .addValue("status",status);

        List<Order> orderList = template.query(sql,param,ORDER_ROW_MAPPER);

        return orderList;
    }

    public List<Order> findByUserIdJoinOrderItems(Integer userId,Integer status){
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
                "     ,oi.id as order_item_id" +
                "     ,oi.order_id as order_id" +
                "     ,oi.item_id as item_id" +
                "     ,oi.quantity as quantity" +
                "     ,oi.size as size" +
                "     ,items.name as itemName" +
                "     ,items.description as itemDescription" +
                "     ,items.price_m as itemPriceM" +
                "     ,items.price_l as itemPriceL" +
                "     ,items.image_path as itemImagePath" +
                "     ,items.deleted as itemDeleted" +
                "     ,ot.id as orderTopping" +
                "     ,ot.topping_id as orderToppingId" +
                "     ,ot.order_item_id as orderItemId" +
                "     ,toppings.id as toppingId" +
                "     ,toppings.name as toppingName" +
                "     ,toppings.price_m as toppingPriceM" +
                "     ,toppings.price_l as toppingPriceL" +
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
                " AND o.status = :status" +
                " AND items.deleted = false" +
                " ORDER BY o.order_date ;";

        SqlParameterSource param = new MapSqlParameterSource().addValue("userId",userId).addValue("status",status);

        List<Order> orderList = template.query(sql,param,ORDER_ROW_MAPPER);

        return orderList;
    }

    public Integer insert(Order order){
        //orderのinsert
        String sql =
                "INSERT INTO orders (" +
                "user_id, " +
                "status, " +
                "total_price, " +
                "order_date, " +
                "destination_name, " +
                "destination_email, " +
                "destination_zipcode, " +
                "destination_address, " +
                "destination_tel, " +
                "delivery_time, " +
                "payment_method) " +
                "VALUES (" +
                ":userId, " +
                ":status, " +
                ":totalPrice, " +
                ":orderDate , " +
                ":destinationName, " +
                ":destinationEmail, " +
                ":destinationZipcode , " +
                ":destinationAddress, " +
                ":destinationTel ," +
                ":deliveryTime, " +
                ":paymentMethod" +
                ");";

        String orderItemInsert =
                "INSERT INTO order_items (" +
                "id, " +
                "item_id, " +
                "order_id, " +
                "quantity, " +
                "size " +
                ")VALUES(" +
                ":id, " +
                ":itemId, " +
                ":quantity, " +
                ":size  " +
                ")";

        String orderToppingInsert =
                "INSERT INTO order_toppings (" +
                "id, " +
                "topping_id, " +
                "order_item_id " +
                ")VALUES(" +
                ":id, " +
                ":toppingId, " +
                ":orderItemId " +
                ")";


        SqlParameterSource orderPram = new BeanPropertySqlParameterSource(order);



        return null;
    }

    public void delete(Integer orderId, Integer itemId, Integer orderItemId){
        String sql = "BEGIN;" +
                "DELETE FROM order_items WHERE order_id = :orderId AND item_id = :itemId;" +
                "DELETE FROM order_toppings WHERE order_item_id = :orderItemId;" +
                "COMMIT;";

        SqlParameterSource param = new MapSqlParameterSource().addValue("orderId",orderId).addValue("itemId", itemId).addValue("orderItemId",orderItemId);

        template.update(sql,param);

    }
}
