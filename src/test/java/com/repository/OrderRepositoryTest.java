package com.repository;

import com.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void insertOrdersOFStatus0(){
        String sql = "drop table if exists orders cascade; create table orders (id serial primary key, user_id integer not null, status integer not null, total_price integer not null, order_date date, destination_name varchar(100), destination_email varchar(100), destination_zipcode varchar(7), destination_address varchar(200), destination_tel varchar(15), delivery_time timestamp, payment_method integer) ;";
        jdbcTemplate.execute(sql);
        String insertSql = "insert into orders(user_id, status, total_price, order_date, destination_name, destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method) values(1,0, 2000,'2000-07-21','東京','test@sample.com','1111111','東京都新宿1-1-1','123-4567-8910','2020-07-21 20:00:00','2');";
        template.update(insertSql, new MapSqlParameterSource());
    }

    @BeforeEach
    void insertOrdersOFStatus1(){
        String insertSql = "insert into orders(user_id, status, total_price, order_date, destination_name, destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method) values(2, 1,2000,'2000-07-21','東京','test@sample.com','1111111','東京都新宿1-1-1','123-4567-8910','2020-07-21 20:00:00','2');";
        template.update(insertSql, new MapSqlParameterSource());
    }

    @BeforeEach
    void insertOrderItem(){
        String sql = "drop table if exists order_items cascade; create table order_items (id serial primary key, item_id integer not null, order_id integer not null, quantity integer not null, size varchar(1));";
        jdbcTemplate.execute(sql);
        String insertSql = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES (1, 1, 1, 'M');";
        template.update(insertSql, new MapSqlParameterSource());
    }

    @BeforeEach
    void insertItems(){
        String sql = "drop table if exists items cascade; create table items (id integer primary key, name text not null, description text not null, price_m integer not null, price_l integer not null, image_path text not null, deleted boolean default false not null) ;";
        jdbcTemplate.execute(sql);
        String insertSql = "insert into items values(1, 'とんこつラーメン', '創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。', 700, 800, '1.jpg');";
        template.update(insertSql, new MapSqlParameterSource());
    }

    @BeforeEach
    void insertTopping(){
        String sql ="drop table if exists toppings cascade; create table toppings (id integer primary key, name text not null, price_m integer not null, price_l integer not null) ;";
        jdbcTemplate.execute(sql);
        String insertSql = "insert into toppings values(1, 'チャーシュー', 200, 300);";
        template.update(insertSql, new MapSqlParameterSource());
    }

    @BeforeEach
    void insetOrderTopping(){
        String sql = "drop table if exists order_toppings cascade; create table order_toppings (id serial primary key, topping_id integer not null, order_item_id integer not null)";
        jdbcTemplate.execute(sql);
        String insertSql = "insert into order_toppings(topping_id, order_item_id) VALUES (1,1)";
        template.update(insertSql, new MapSqlParameterSource());
    }


    @AfterEach
    void dropOrders(){
        String sql = "DROP TABLE orders";
        jdbcTemplate.execute(sql);
    }

    @AfterEach
    void dropOrdersItem(){
        String sql = "DROP TABLE order_items";
        jdbcTemplate.execute(sql);
    }

    @AfterEach
    void dropItems(){
        String sql = "DROP TABLE items";
        jdbcTemplate.execute(sql);
    }

    @AfterEach
    void DropOrderTopping(){}
    void dropTable(){
        String dropSql = "DROP TABLE order_toppings";
        jdbcTemplate.execute(dropSql);
    }


    @AfterEach
    void dropToppings(){
        String sql = "DROP TABLE toppings";
        jdbcTemplate.execute(sql);
    }

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

    private static final RowMapper<OrderItem> ORDER_ITEM_ROW_MAPPER = (rs, i) -> {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getInt("orderItemId"));
        orderItem.setItemId(rs.getInt("item_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setSize(rs.getString("size").charAt(0));
        return orderItem;

    };

    private static final RowMapper<OrderTopping> ORDER_TOPPING_ROW_MAPPER = (rs, i) -> {
        OrderTopping orderTopping = new OrderTopping();
        orderTopping.setId(rs.getInt("orderToppingId"));
        orderTopping.setToppingId(rs.getInt("orderToppingId"));
        orderTopping.setOrderItemId(rs.getInt("orderItemId"));

        return orderTopping;
    };

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
        orderItemList.add(orderItem);
        order.setOrderItemList(orderItemList);


        return order;
    };

        @Test
    void findByUserIdAndStatus() throws ParseException {
        List<Order> orderList = orderRepository.findByUserIdAndStatus(1,0);
        String strDate = "2000-07-21";
        String strDate2 = "2020-07-21 20:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(strDate);
        Date date2 = dateFormat2.parse(strDate2);
        Timestamp ts = new Timestamp(date2.getTime());
        assertEquals(2000, (int)orderList.get(0).getTotalPrice());
        assertEquals(date,orderList.get(0).getOrderDate());
        assertEquals("東京", orderList.get(0).getDestinationName());
        assertEquals("test@sample.com", orderList.get(0).getDestinationEmail());
        assertEquals("1111111", orderList.get(0).getDestinationZipcode());
        assertEquals("東京都新宿1-1-1", orderList.get(0).getDestinationAddress());
        assertEquals("123-4567-8910", orderList.get(0).getDestinationTel());
        assertEquals(ts, orderList.get(0).getDeliveryTime());
        assertEquals(2, (int)orderList.get(0).getPaymentMethod());
    }



    @Test
    void createCart() throws ParseException{
        Order order = new Order();
        order.setUserId(3);
        order.setStatus(0);
        order.setTotalPrice(3000);
        String strDate = "2000-07-22";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(strDate);
        order.setOrderDate(date);
        order.setDestinationName("東京");
        order.setDestinationEmail("test@sample.com");
        order.setDestinationZipcode("1111111");
        order.setDestinationAddress("東京都新宿1-1-1");
        order.setDestinationTel("123-4567-8910");
        String strDate2 = "2020-07-21 20:00:00";
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = dateFormat2.parse(strDate2);
        Timestamp ts = new Timestamp(date2.getTime());
        order.setDeliveryTime(ts);
        order.setPaymentMethod(2);
        Integer id = orderRepository.createCart(order);

        assertEquals(3,id.intValue());



    }

    @Test
    void findByUserIdJoinOrderItems() throws  ParseException{
            List<Order> orderList = orderRepository.findByUserIdJoinOrderItems(1,0);
        System.out.println(orderList);
        String strDate = "2000-07-21";
        String strDate2 = "2020-07-21 20:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(strDate);
        Date date2 = dateFormat2.parse(strDate2);
        Timestamp ts = new Timestamp(date2.getTime());
        assertEquals(2000, (int)orderList.get(0).getTotalPrice());
        assertEquals(date,orderList.get(0).getOrderDate());
        assertEquals("東京", orderList.get(0).getDestinationName());
        assertEquals("test@sample.com", orderList.get(0).getDestinationEmail());
        assertEquals("1111111", orderList.get(0).getDestinationZipcode());
        assertEquals("東京都新宿1-1-1", orderList.get(0).getDestinationAddress());
        assertEquals("123-4567-8910", orderList.get(0).getDestinationTel());
        assertEquals(ts, orderList.get(0).getDeliveryTime());
        assertEquals(2, (int)orderList.get(0).getPaymentMethod());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderId());
        assertEquals(1, (int)orderList.get(0).getOrderItemList().get(0).getQuantity());
        assertEquals('M', (char)orderList.get(0).getOrderItemList().get(0).getSize());
        assertEquals("とんこつラーメン", orderList.get(0).getOrderItemList().get(0).getItem().getName());
        assertEquals("創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。",orderList.get(0).getOrderItemList().get(0).getItem().getDescription());
        assertEquals(700, (int)orderList.get(0).getOrderItemList().get(0).getItem().getPriceM());
        assertEquals(800, (int)orderList.get(0).getOrderItemList().get(0).getItem().getPriceL());
        assertEquals("1.jpg", orderList.get(0).getOrderItemList().get(0).getItem().getImagePath());
        assertEquals(false,orderList.get(0).getOrderItemList().get(0).getItem().getDeleted());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getToppingId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getOrderItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getTopping().getId());
        assertEquals("チャーシュー", orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getTopping().getName());
        assertEquals(200,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getTopping().getPriceM());
        assertEquals(300,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getTopping().getPriceL());
    }

    @Test
    void findByUserIdJoinOrderItemsForOrderHistory() throws ParseException{
            List<Order> orderList = orderRepository.findByUserIdJoinOrderItemsForOrderHistory(2);
        String strDate = "2000-07-21";
        String strDate2 = "2020-07-21 20:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(strDate);
        Date date2 = dateFormat2.parse(strDate2);
        Timestamp ts = new Timestamp(date2.getTime());
        assertEquals(2000, (int)orderList.get(0).getTotalPrice());
        assertEquals(date,orderList.get(0).getOrderDate());
        assertEquals("東京", orderList.get(0).getDestinationName());
        assertEquals("test@sample.com", orderList.get(0).getDestinationEmail());
        assertEquals("1111111", orderList.get(0).getDestinationZipcode());
        assertEquals("東京都新宿1-1-1", orderList.get(0).getDestinationAddress());
        assertEquals("123-4567-8910", orderList.get(0).getDestinationTel());
        assertEquals(ts, orderList.get(0).getDeliveryTime());
        assertEquals(2, (int)orderList.get(0).getPaymentMethod());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderId());
        assertEquals(1, (int)orderList.get(0).getOrderItemList().get(0).getQuantity());
        assertEquals('M', (char)orderList.get(0).getOrderItemList().get(0).getSize());
        assertEquals("とんこつラーメン", orderList.get(0).getOrderItemList().get(0).getItem().getName());
        assertEquals("創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。",orderList.get(0).getOrderItemList().get(0).getItem().getDescription());
        assertEquals(700, (int)orderList.get(0).getOrderItemList().get(0).getItem().getPriceM());
        assertEquals(800, (int)orderList.get(0).getOrderItemList().get(0).getItem().getPriceL());
        assertEquals("1.jpg", orderList.get(0).getOrderItemList().get(0).getItem().getImagePath());
        assertEquals(false,orderList.get(0).getOrderItemList().get(0).getItem().getDeleted());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getToppingId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getOrderItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getTopping().getId());
        assertEquals("チャーシュー", orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getTopping().getName());
        assertEquals(200,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getTopping().getPriceM());
        assertEquals(300,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getTopping().getPriceL());
    }

    @Test
    void upDate() throws ParseException{
            Order order = new Order();
            order.setUserId(1);
            order.setStatus(0);
            order.setTotalPrice(2000);
            String strDate = "2000-07-21";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(strDate);
            order.setOrderDate(date);
            order.setDestinationName("東京");
            order.setDestinationEmail("test@sample.com");
            order.setDestinationZipcode("1111111");
            order.setDestinationAddress("東京都新宿1-1-1");
            order.setDestinationTel("123-4567-8910");
            String strDate2 = "2020-07-21 20:00:00";
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date2 = dateFormat2.parse(strDate2);
            Timestamp ts = new Timestamp(date2.getTime());
            order.setDeliveryTime(ts);
            order.setPaymentMethod(2);
            orderRepository.UpDate(order);

            String sql = "SELECT*FROM orders WHERE id = (SELECT max(id) FROM orders)";
            List<Order> orderList = template.query(sql,ORDER_ROW_MAPPER);
            assertEquals(2,(int)orderList.get(0).getStatus());

    }

    @Test
    void updateUserId() {
            orderRepository.updateUserId(2,1);

            String sql = "SELECT*FROM orders WHERE id = (SELECT max(id) FROM orders)";
            List<Order> orderList = template.query(sql,ORDER_ROW_MAPPER);
            assertEquals(2,(int)orderList.get(0).getUserId());
    }

    @Test
    void delete() {
            orderRepository.delete(1,1);

            String deleteSqlOfOrderItem="SELECT*FROM order_items WHERE order_id =1 AND id = 1";
            List<OrderItem> itemList = template.query(deleteSqlOfOrderItem,ORDER_ITEM_ROW_MAPPER);
            if(itemList.isEmpty()){
                itemList.add(null);
            }
            System.out.println(itemList.get(0));
            assertNull(itemList.get(0));

            String deleteSqlOFOrderTopping = " SELECT*FROM order_toppings WHERE order_item_id = 1";
            List<OrderTopping> orderToppingList = template.query(deleteSqlOFOrderTopping,ORDER_TOPPING_ROW_MAPPER);
            if(orderToppingList.isEmpty()){
                orderToppingList.add(null);
            }
            System.out.println(orderToppingList.get(0));
            assertNull(orderToppingList.get(0));


            System.out.println("***deleteTest ended. ***");

    }

    @Test
    void updateTotalPrice() {
            orderRepository.updateTotalPrice(1,5000);

            String sql = "SELECT*FROM orders WHERE id = (SELECT max(id) FROM orders)";
            List<Order> orderList = template.query(sql,ORDER_ROW_MAPPER);
            assertEquals(5000,(int)orderList.get(0).getTotalPrice());
    }
}