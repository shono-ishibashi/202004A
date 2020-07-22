package com.service;

import com.domain.User;
import com.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeInsert(){
        String createSql = "create table users (id serial primary key, name varchar(100) not null, email varchar(100) not null unique, password text not null, zipcode varchar(7) not null, address varchar(200) not null, telephone varchar(15) not null)";

        jdbcTemplate.execute(createSql);

        String sql = "insert into users(name, email, password, zipcode, address, telephone) " +
                "values('テストユーザ', 'test@test.co.jp'," +
                " 'a232fe3fd81a175aa15541dc2051fe2cb003cdae810371104fbfc100eb34cd6f'," +
                "'1111111', 'テスト住所', 'テスト電話番号');";

        template.update(sql, new MapSqlParameterSource());
    }

    @AfterEach
    void dropTable(){
        String dropSql = "DROP TABLE users";
        jdbcTemplate.execute(dropSql);
    }



    private final static RowMapper<User> USER_ROW_MAPPER = (rs , i ) ->{
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setZipcode(rs.getString("zipcode"));
        user.setAddress(rs.getString("address"));
        user.setTelephone(rs.getString("telephone"));

        return user;
    };
    @Test
    void insertUser() {
        User user = new User();
        user.setName("テストゆうた");
        user.setEmail("yuta@gmail.com");
        user.setPassword("yutayuta");
        user.setAddress("神奈川県相模原市南区2-81-2");
        user.setTelephone("042-456-7777");
        user.setZipcode("7777777");
        userService.insertUser(user);

        String sql = "SELECT * from users WHERE name = 'テストゆうた'";
        List<User> list = template.query(sql, USER_ROW_MAPPER);
        assertEquals("テストゆうた", list.get(0).getName());
        assertEquals("yuta@gmail.com", list.get(0).getEmail());
        assertEquals("神奈川県相模原市南区2-81-2", list.get(0).getAddress());
        assertEquals("7777777", list.get(0).getZipcode());
        assertEquals("042-456-7777", list.get(0).getTelephone());
        assertNotEquals("yutayuta", list.get(0).getPassword());
    }

    @Test
    void findUserByEmail() {
    }
}