package com.service;

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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void insertOrdersOFStatus0(){
        String sql = "drop table if exists orders cascade; create table orders (id serial primary key, user_id integer not null, status integer not null, total_price integer not null, order_date date, destination_name varchar(100), destination_email varchar(100), destination_zipcode varchar(7), destination_address varchar(200), destination_tel varchar(15), delivery_time timestamp, payment_method integer) ;";
        jdbcTemplate.execute(sql);
        //userId=1,status=1の人のデータ挿入(OrderID=1)
        String insertSql = "insert into orders(user_id, status, total_price, order_date, destination_name, destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method) values(1, 1, 2000,'2000-07-24','東京','test@sample.com','1111111','東京都新宿1-1-1','123-4567-8910','2020-07-25 20:00:00','2');";
        template.update(insertSql, new MapSqlParameterSource());
        //同じUserId,Order by order_dateでチェックしたいため、一番目と同じuserID=1とstatus=2の人のデータを挿入(OrderID=2)
        String insertSql2 = "insert into orders(user_id, status, total_price, order_date, destination_name, destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method) values(1, 2, 2000,'2000-07-27','東京','abc@sample.com','1234567','東京都品川区1-1-1','111-1111-1111','2020-07-28 20:00:00','1');";
        template.update(insertSql2, new MapSqlParameterSource());
        //updateメソッドのチェックするためstatus=0のデータを挿入
        String insertSql3 = "insert into orders(user_id, status, total_price, order_date, destination_name, destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method) values(2, 0, 2000,'2000-07-22','東京','test@sample.com','1111111','東京都新宿1-1-1','123-4567-8910','2020-07-23 20:00:00','1');";
        template.update(insertSql3, new MapSqlParameterSource());
    }

    @BeforeEach
    void insertOrderItems (){
        String sql = "drop table if exists order_items cascade; create table order_items (id serial primary key, item_id integer not null, order_id integer not null, quantity integer not null, size varchar(1));";
        jdbcTemplate.execute(sql);
        //orderId=1,userID=1
        String insertSql = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES (1, 1, 1, 'M');";
        template.update(insertSql, new MapSqlParameterSource());
        String insertSql2 = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES (2, 1, 2, 'M')";
        template.update(insertSql2, new MapSqlParameterSource());
        String insertSql3 = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES (3, 1, 3, 'M')";
        template.update(insertSql3, new MapSqlParameterSource());
        //orderID=2,userID=1
        String insertSql4 = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES (2, 2, 2, 'L')";
        template.update(insertSql4, new MapSqlParameterSource());
    }

    @BeforeEach
    void insertItems(){
        String sql = "drop table if exists items cascade; create table items (id integer primary key, name text not null, description text not null, price_m integer not null, price_l integer not null, image_path text not null, deleted boolean default false not null) ;";
        jdbcTemplate.execute(sql);
        String insertSql = "insert into items values(1, 'とんこつラーメン', '創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。', 700, 800, '1.jpg');";
        template.update(insertSql, new MapSqlParameterSource());
        String insertSql2 = "insert into items values(2, '赤ラーメン', '自家製の香味油と辛みそを加えることで、一杯のラーメンの中でいくつもの味の奥行きと調和を楽しめます。白丸が正統派のとんこつラーメンならば、赤丸新味は豚骨ラーメンの可能性を広げた“革新派”。 コクと深みを追求した、自信作です。', 750, 850, '2.jpg');";
        template.update(insertSql2, new MapSqlParameterSource());
        String insertSql3 = "insert into items values(3, 'からか麺', '博多絹ごしとんこつをベースに、豆板醤や甜麺醤などを独自に配合した肉味噌を大胆にトッピング。山椒などのスパイスを効かせた自家製ラー油が全体をピリリとまとめあげ、中太のストレート麺がうま味と辛味を余すところなくすくいあげます。1989年に大名本店で登場以来、進化を続ける根強い人気の一杯です。', 800, 900, '3.jpg')";
        template.update(insertSql3, new MapSqlParameterSource());

    }

    @BeforeEach
    void insertTopping() {
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
        //複数トッピングをオーダーした場合のチェックをするため
        String insertSql3 = "insert into order_toppings(topping_id, order_item_id) VALUES (1,3)";
        template.update(insertSql3, new MapSqlParameterSource());
        String insertSql4 = "insert into order_toppings(topping_id, order_item_id) VALUES (2,3)";
        template.update(insertSql4, new MapSqlParameterSource());
        String insertSql5 = "insert into order_toppings(topping_id, order_item_id) VALUES (3,3)";
        template.update(insertSql5, new MapSqlParameterSource());
        //orderID=2, userID=1
        String insertSql6 = "insert into order_toppings(topping_id, order_item_id) VALUES (1,4)";
        template.update(insertSql6, new MapSqlParameterSource());
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
    void getOrderHistoryList() throws ParseException {
        List<Order> orderList = orderService.getOrderHistoryList(1);
        System.out.println(orderList);


        //orderID=1,Status=1
        String strDate = "2000-07-24";
        String strDate2 = "2020-07-25 20:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(strDate);
        Date date2 = dateFormat2.parse(strDate2);
        Timestamp ts = new Timestamp(date2.getTime());
        assertEquals(1,(int)orderList.get(0).getUserId());
        assertEquals(1,(int)orderList.get(0).getStatus());
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
        assertEquals(200,(int)orderList.get(0).getOrderItemList().get(0).getOrderToppingList().get(0).getPrice());


        //複数注文していた場合に正しく抽出できるかチェック。変化しないところは省く。(orderId=1)
        assertEquals(2,(int)orderList.get(0).getOrderItemList().get(1).getItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(1).getOrderId());
        assertEquals(2, (int)orderList.get(0).getOrderItemList().get(1).getQuantity());
        assertEquals('M', (char)orderList.get(0).getOrderItemList().get(1).getSize());
        assertEquals("赤ラーメン", orderList.get(0).getOrderItemList().get(1).getItem().getName());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(1).getOrderToppingList().get(0).getToppingId());
        assertEquals(2,(int)orderList.get(0).getOrderItemList().get(1).getOrderToppingList().get(0).getOrderItemId());
        assertEquals(200,(int)orderList.get(0).getOrderItemList().get(1).getOrderToppingList().get(0).getPrice());

        //トッピングが複数ある時にあっているかチェック。1つ目。変更しないところは省く。(orderId=1)
        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getItemId());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(2).getOrderId());
        assertEquals(3, (int)orderList.get(0).getOrderItemList().get(2).getQuantity());
        assertEquals('M', (char)orderList.get(0).getOrderItemList().get(2).getSize());
        assertEquals("からか麺", orderList.get(0).getOrderItemList().get(2).getItem().getName());
        assertEquals(1,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(0).getToppingId());
        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(0).getOrderItemId());
        assertEquals(200,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(0).getPrice());

        //トッピング2つ目
        assertEquals(2,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(1).getToppingId());
        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(1).getOrderItemId());
        assertEquals("煮たまご", orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(1).getTopping().getName());
        assertEquals(200,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(1).getPrice());


        //トッピング3つ目
        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(2).getToppingId());
        assertEquals(3,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(2).getOrderItemId());
        assertEquals("メンマ", orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(2).getTopping().getName());
        assertEquals(200,(int)orderList.get(0).getOrderItemList().get(2).getOrderToppingList().get(2).getPrice());


        //被っている部分を省略し、order_date順で抽出できているかチェック
        //orderID=1,status=2
        String strDate1_1 = "2000-07-27";
        String strDate1_2 = "2020-07-28 20:00:00";
        Date date1_1 = dateFormat.parse(strDate1_1);
        Date date1_2 = dateFormat2.parse(strDate1_2);
        Timestamp timestamp = new Timestamp(date1_2.getTime());
        assertEquals(1,(int)orderList.get(1).getUserId());
        assertEquals(2,(int)orderList.get(1).getStatus());
        assertEquals(date1_1,orderList.get(1).getOrderDate());
        assertEquals("1234567", orderList.get(1).getDestinationZipcode());
        assertEquals("東京都品川区1-1-1", orderList.get(1).getDestinationAddress());
        assertEquals("111-1111-1111", orderList.get(1).getDestinationTel());
        assertEquals(timestamp, orderList.get(1).getDeliveryTime());
        assertEquals(1, (int)orderList.get(1).getPaymentMethod());
        assertEquals(2,(int)orderList.get(1).getOrderItemList().get(0).getItemId());
        assertEquals(2,(int)orderList.get(1).getOrderItemList().get(0).getOrderId());
        assertEquals(2, (int)orderList.get(1).getOrderItemList().get(0).getQuantity());
        assertEquals('L', (char)orderList.get(1).getOrderItemList().get(0).getSize());
        assertEquals(1,(int)orderList.get(1).getOrderItemList().get(0).getOrderToppingList().get(0).getToppingId());
        assertEquals(4,(int)orderList.get(1).getOrderItemList().get(0).getOrderToppingList().get(0).getOrderItemId());
        assertEquals(300, (int)orderList.get(1).getOrderItemList().get(0).getOrderToppingList().get(0).getPrice());

    }

    @Test
    void upDate() throws ParseException{
        Order order = new Order();
        order.setUserId(2);
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
        order.setPaymentMethod(1);
        orderService.UpDate(order);

        String sql = "SELECT*FROM orders WHERE id = 3";
        List<Order> orderList = template.query(sql,ORDER_ROW_MAPPER);
        assertEquals(1,(int)orderList.get(0).getStatus());
    }
}