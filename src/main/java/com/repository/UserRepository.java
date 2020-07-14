package com.repository;

import com.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * User domain を 操作するためのrepository
 *
 * @author shono ishibashi
 *
 */
@Repository
public class UserRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private final static RowMapper<User> USER_ROW_MAPPER = (rs ,i ) ->{
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

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        SqlParameterSource param = new MapSqlParameterSource().addValue("email",email);

        List<User> user = template.query(sql, param,USER_ROW_MAPPER);

        if(user.isEmpty()){
            return null;
        }else {
            return user.get(0);
        }
    }

    public void insert(User user) {
        String sql = "INSERT INTO users (name, email, password, zipcode, address, telephone) " +
                " VALUES (:name, :email, :password, :zipcode, :address, :telephone)";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("email",user.getEmail())
                .addValue("password",user.getPassword())
                .addValue("zipcode",user.getZipcode())
                .addValue("address",user.getAddress())
                .addValue("telephone",user.getTelephone());

        template.update(sql,param);
    }

}
