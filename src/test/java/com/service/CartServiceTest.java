package com.service;

import com.domain.*;
import com.repository.OrderRepository;
import com.sun.xml.bind.v2.TODO;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @BeforeEach
    void insertOrders(){
        String sql = "drop table if exists orders cascade; create table orders (id serial primary key, user_id integer not null, status integer not null, total_price integer not null, order_date date, destination_name varchar(100), destination_email varchar(100), destination_zipcode varchar(7), destination_address varchar(200), destination_tel varchar(15), delivery_time timestamp, payment_method integer) ;";
        jdbcTemplate.execute(sql);
        //userId=1,status=0の人のデータ挿入(OrderID=1)
        String insertSql = "insert into orders(user_id, status, total_price, order_date, destination_name, destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method) values(1, 0, 2000,'2000-07-21','東京','test@sample.com','1111111','東京都新宿1-1-1','123-4567-8910','2020-07-21 20:00:00','2');";
        template.update(insertSql, new MapSqlParameterSource());
        //statusが0ではない人のデータ挿入
        String insertSql3 = "insert into orders(user_id, status, total_price, order_date, destination_name, destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method) values(2, 1, 2000,'2000-07-22','東京','test@sample.com','1111111','東京都新宿1-1-1','123-4567-8910','2020-07-23 20:00:00','2');";
        template.update(insertSql3, new MapSqlParameterSource());
    }

    @BeforeEach
    void insertOrderItems(){
        String sql = "drop table if exists order_items cascade; create table order_items (id serial primary key, item_id integer not null, order_id integer not null, quantity integer not null, size varchar(1));";
        jdbcTemplate.execute(sql);
        //同じOrderIdの人が複数注文した時のリストの取り出しをチェックしたいので、３つほどデータを挿入(orderID=1,userID=1)
        String insertSql = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES (1, 1, 1, 'M');";
        template.update(insertSql, new MapSqlParameterSource());
        String insertSql2 = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES (2, 1, 1, 'M')";
        template.update(insertSql2, new MapSqlParameterSource());
        String insertSql3 = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES (3, 1, 1, 'M')";
        template.update(insertSql3, new MapSqlParameterSource());
    }

    @BeforeEach
    void insertTopping(){
        String sql = "drop table if exists toppings cascade; create table toppings (id integer primary key, name text not null, price_m integer not null, price_l integer not null) ;";
        jdbcTemplate.execute(sql);
        String insertSql = "insert into toppings values(1, 'チャーシュー', 200, 300);";
        template.update(insertSql, new MapSqlParameterSource());
        String insertSql2="insert into toppings values(2, '煮たまご', 200, 300)";
        template.update(insertSql2, new MapSqlParameterSource());
        String insertSlq3="insert into toppings values(3, 'メンマ', 200, 300)";
        template.update(insertSlq3,new MapSqlParameterSource());

    }

    @BeforeEach
    void insertOrderTopping(){
        String sql = "drop table if exists order_toppings cascade; create table order_toppings (id serial primary key, topping_id integer not null, order_item_id integer not null)";
        jdbcTemplate.execute(sql);
        //上のorder_itemsの数に合わせてorder_toppingのデータも挿入(orderID=1,userId=1)
        String insertSql = "insert into order_toppings(topping_id, order_item_id) VALUES (1,1)";
        template.update(insertSql, new MapSqlParameterSource());
        String insertSql2 = "insert into order_toppings(topping_id, order_item_id) VALUES (1,2)";
        template.update(insertSql2, new MapSqlParameterSource());
        String insertSql3 = "insert into order_toppings(topping_id, order_item_id) VALUES (1,3)";
        template.update(insertSql3, new MapSqlParameterSource());
        String insertSql4 = "insert into order_toppings(topping_id, order_item_id) VALUES (2,3)";
        template.update(insertSql4, new MapSqlParameterSource());
        String insertSql5 = "insert into order_toppings(topping_id, order_item_id) VALUES (3,3)";
        template.update(insertSql5, new MapSqlParameterSource());
    }

    @BeforeEach
    void insertItems(){
        String sql = "drop table if exists items cascade; create table items (id integer primary key, name text not null, description text not null, price_m integer not null, price_l integer not null, image_path text not null, deleted boolean default false not null) ;";
        jdbcTemplate.execute(sql);
        String insertSql = "insert into items values(1, 'とんこつラーメン', '創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。', 700, 800, '1.jpg');";
        template.update(insertSql, new MapSqlParameterSource());
        String insertSql2 = "insert into items values(2, '赤ラーメン', '自家製の香味油と辛みそを加えることで、一杯のラーメンの中でいくつもの味の奥行きと調和を楽しめます。白丸が正統派のとんこつラーメンならば、赤丸新味は豚骨ラーメンの可能性を広げた“革新派”。 コクと深みを追求した、自信作です。', 750, 850, '2.jpg');";
        template.update(insertSql2, new MapSqlParameterSource());
        String insertSql3 = "insert into items values(3, 'からか麺', '博多絹ごしとんこつをベースに、豆板醤や甜麺醤などを独自に配合した肉味噌を大胆にトッピング。山椒などのスパイスを効かせた自家製ラー油が全体をピリリとまとめあげ、中太のストレート麺がうま味と辛味を余すところなくすくいあげます。1989年に大名本店で登場以来、進化を続ける根強い人気の一杯です。', 800, 900, '3.jpg');";
        template.update(insertSql3, new MapSqlParameterSource());
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

    private static final RowMapper<OrderItem> ORDER_ITEM_ROW_MAPPER = (rs, i) -> {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getInt("id"));
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

    @Test
    void cart_status0() throws Exception {
        Order order = cartService.cart(1);
        System.out.println(order);
        String strDate = "2000-07-21";
        String strDate2 = "2020-07-21 20:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(strDate);
        Date date2 = dateFormat2.parse(strDate2);
        assertEquals(1,(int)order.getUserId());
        assertEquals(0,(int)order.getStatus());
        Timestamp ts = new Timestamp(date2.getTime());
        assertEquals(2000, (int)order.getTotalPrice());
        assertEquals(date,order.getOrderDate());
        assertEquals("東京", order.getDestinationName());
        assertEquals("test@sample.com", order.getDestinationEmail());
        assertEquals("1111111", order.getDestinationZipcode());
        assertEquals("東京都新宿1-1-1", order.getDestinationAddress());
        assertEquals("123-4567-8910", order.getDestinationTel());
        assertEquals(ts, order.getDeliveryTime());
        assertEquals(2, (int)order.getPaymentMethod());
    }

    @Test
    void cart_notStatus0() throws Exception {
        Order order = cartService.cart(2);
        assertEquals(2,(int)order.getUserId());
        assertEquals(0,(int)order.getStatus());
        assertEquals(0,(int)order.getTotalPrice());
        assertEquals(2,(int)order.getUserId());
    }

    @Test
    void showCart() throws Exception {
        List<Order> orderList = cartService.showCart(1,0);
        System.out.println(orderList);
        String strDate = "2000-07-21";
        String strDate2 = "2020-07-21 20:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(strDate);
        Date date2 = dateFormat2.parse(strDate2);
        Timestamp ts = new Timestamp(date2.getTime());
        assertEquals(3250, (int)orderList.get(0).getTotalPrice());
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

        assertEquals(2,(int)orderList.get(0).getOrderItemList().get(1).getItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(1).getOrderId());
        assertEquals(1, (int)orderList.get(0).getOrderItemList().get(1).getQuantity());
        assertEquals('M', (char)orderList.get(0).getOrderItemList().get(1).getSize());
        assertEquals("赤ラーメン", orderList.get(0).getOrderItemList().get(1).getItem().getName());

        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(2).getOrderId());
        assertEquals(1, (int)orderList.get(0).getOrderItemList().get(2).getQuantity());
        assertEquals('M', (char)orderList.get(0).getOrderItemList().get(2).getSize());
        assertEquals("からか麺", orderList.get(0).getOrderItemList().get(2).getItem().getName());

        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getToppingId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getOrderItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getTopping().getId());
        assertEquals("チャーシュー", orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getTopping().getName());

        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(1).getOrderToppingList().get(0).getToppingId());
        assertEquals(2,(int)orderList.get(0).getOrderItemList().get(1).getOrderToppingList().get(0).getOrderItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(1).getOrderToppingList().get(0).getTopping().getId());
        assertEquals("チャーシュー", orderList.get(0).getOrderItemList().get(1).getOrderToppingList().get(0).getTopping().getName());

        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(0).getToppingId());
        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(0).getOrderItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(0).getTopping().getId());
        assertEquals("チャーシュー", orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(0).getTopping().getName());

        assertEquals(2,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(1).getToppingId());
        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(1).getOrderItemId());
        assertEquals(2,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(1).getTopping().getId());
        assertEquals("煮たまご", orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(1).getTopping().getName());

        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(2).getToppingId());
        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(2).getOrderItemId());
        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(2).getTopping().getId());
        assertEquals("メンマ", orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(2).getTopping().getName());




    }

    @Test
    void delete() {
        cartService.delete(1,1);

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
    void addCartItem() {
//        OrderItem orderItem = new OrderItem();
//        Item item = new Item();
//        item.setName("とんこつラーメン");
//        item.setPriceM(700);
//
//        orderItem.setItemId(1);
//        orderItem.setOrderId(1);
//        orderItem.setSize('M');
//        orderItem.setQuantity(1);
//        orderItem.setTotalPrice(700);
//        orderItem.setItem(item);
//
//
//        List<OrderTopping> orderToppingList = new LinkedList<>();
//        OrderTopping orderTopping = new OrderTopping();
//        orderTopping.setToppingId(1);
//        orderTopping.setOrderItemId(1);
//        orderTopping.setPrice(200);
//        Topping topping = new Topping();
//        topping.setName("チャーシュー");
//        topping.setPriceM(200);
//        orderTopping.setTopping(topping);
//        orderToppingList.add(orderTopping);
//
//        orderItem.setOrderToppingList(orderToppingList);
//        orderItem.setReviewPoint(1);
//        cartService.addCartItem(orderItem);
//
//        String SqlOfOrderItem = "SELECT*FROM order_items WHERE id=(SELECT max(id) FROM order_items)";
//        List<OrderItem> orderItemList = template.query(SqlOfOrderItem, ORDER_ITEM_ROW_MAPPER);
//        System.out.println(orderItemList);
//        assertEquals(1, (int) orderItemList.get(0).getItemId());
//        assertEquals(1, (int) orderItemList.get(0).getOrderId());
//        assertEquals('M', (char) orderItemList.get(0).getSize());
//        assertEquals(1,(int)orderItemList.get(0).getQuantity());
//        assertEquals("とんこつラーメン", orderItemList.get(0).getItem().getName());
//        assertEquals(700, (int) orderItemList.get(0).getItem().getPriceM());
//        assertEquals(700, (int) orderItemList.get(0).getTotalPrice());
//        assertEquals(1, (int) orderItemList.get(0).getOrderToppingList().get(0).getToppingId());
//        assertEquals(1, (int) orderItemList.get(0).getOrderToppingList().get(0).getOrderItemId());
//        assertEquals(200, (int) orderItemList.get(0).getOrderToppingList().get(0).getPrice());
//        assertEquals("チャーシュー", orderItemList.get(0).getOrderToppingList().get(0).getTopping().getName());
//        assertEquals(200, (int) orderItemList.get(0).getOrderToppingList().get(0).getTopping().getPriceM());
//        assertEquals(1, (int) orderItemList.get(0).getReviewPoint());
//
    }

    @Test
    void addCartTopping() {

    }
}