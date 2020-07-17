package com.repository;

import com.domain.*;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

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

    public List<Order> findByUserIdAndStatus(Integer userId, Integer status) {
        String sql = "SELECT o.id as id " +
                ", o.user_id as user_id " +
                ", o.status as status " +
                ", o.total_price as total_price" +
                ", o.order_date as order_date" +
                ", o.destination_name as destination_name" +
                ", o.destination_email as destination_email" +
                ", o.destination_zipcode as destination_zipcode" +
                ", o.destination_address as destination_address" +
                ", o.destination_tel as destination_tel" +
                ", o.delivery_time as delivery_time" +
                ", o.payment_method as payment_method" +
                " FROM orders as o " +
                " WHERE user_id = :userId " +
                " and status = :status;";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("status", status);

        List<Order> orderList = template.query(sql, param, ORDER_ROW_MAPPER);

        return orderList;
    }


    public Integer createCart(Order order) {

        SqlParameterSource param = new BeanPropertySqlParameterSource(order);

        Number id = insert.executeAndReturnKey(param);

        return id.intValue();
    }

    public List<Order> findByUserIdJoinOrderItems(Integer userId, Integer status) {
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
                " LEFT OUTER JOIN order_items AS oi" +
                " ON o.id = oi.order_id" +
                " LEFT OUTER JOIN items" +
                " ON oi.item_id = items.id" +
                " LEFT OUTER JOIN order_toppings as ot" +
                " ON ot.order_item_id = oi.id" +
                " LEFT OUTER JOIN toppings" +
                " ON toppings.id = ot.topping_id" +
                " WHERE o.user_id = :userId " +
                " AND o.status = :status" +
                " AND items.deleted = false" +
                " ORDER BY o.order_date ;";

        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);

        List<Order> orderList = template.query(sql, param, ORDER_JOIN_ROW_MAPPER);

        return orderList;
    }
    public List<Order> findByUserIdJoinOrderItemsForOrderHistory(Integer userId){
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
                " LEFT JOIN order_items AS oi" +
                " ON o.id = oi.order_id" +
                " LEFT JOIN items" +
                " ON oi.item_id = items.id" +
                " LEFT JOIN order_toppings as ot" +
                " ON ot.order_item_id = oi.id" +
                " LEFT JOIN toppings" +
                " ON toppings.id = ot.topping_id" +
                " WHERE o.user_id = :userId " +
                " AND o.status = 1" +
                " OR o.status = 2" +
                " OR o.status = 3" +
                " OR o.status = 4" +
                " AND items.deleted = false" +
                " ORDER BY o.order_date ;";

        SqlParameterSource param = new MapSqlParameterSource().addValue("userId",userId);

        List<Order> orderList = template.query(sql,param,ORDER_ROW_MAPPER);

        return orderList;
    }
  
   /**
     *お客様情報を更新する処理
     *
     */
    public void UpDate(Order order){
        SqlParameterSource param = new BeanPropertySqlParameterSource(order);

        if( order.getStatus() == 1 ){
            String upDateSqlCash ="UPDATE orders SET  status=1,  " +
                    "order_date=:orderDate, destination_name=:destinationName, destination_email=:destinationEmail, " +
                    "destination_zipcode=:destinationZipcode, destination_address=:destinationAddress, " +
                    "destination_tel=:destinationTel, delivery_time=:deliveryTime, payment_method=:paymentMethod  " +
                    "WHERE user_id=:userId ";

            template.update(upDateSqlCash,param);
        }
        String upDateSqlCredit ="UPDATE orders SET status=2,  " +
                "order_date=:orderDate, destination_name=:destinationName, destination_email=:destinationEmail, " +
                "destination_zipcode=:destinationZipcode, destination_address=:destinationAddress, " +
                "destination_tel=:destinationTel, delivery_time=:deliveryTime, payment_method=:paymentMethod " +
                "WHERE user_id=:userId ";

        template.update(upDateSqlCredit,param);
    }


    public void updateUserId(Integer userId,Integer temporaryId) {
        String sql = "UPDATE orders SET user_id = :userId WHERE user_id = :temporaryId ";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId",userId)
                .addValue("temporaryId",temporaryId);

        template.update(sql,param );

    }

    public void delete(Integer orderId, Integer orderItemId){
        String sql = "BEGIN;" +
                "DELETE FROM order_items WHERE order_id = :orderId AND id = :orderItemId;" +
                "DELETE FROM order_toppings WHERE order_item_id = :orderItemId;" +
                "COMMIT;";

        SqlParameterSource param = new MapSqlParameterSource().addValue("orderId",orderId).addValue("orderItemId",orderItemId);

        template.update(sql,param);
    }

    public void updateTotalPrice(Integer orderId, Integer totalPrice){

        String sql = "UPDATE orders SET total_price = :totalPrice WHERE id = :orderId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("totalPrice",totalPrice)
                .addValue("orderId",orderId);

        template.update(sql,param);

    }
}
