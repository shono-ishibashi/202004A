package com.repository;

import com.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ItemRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Item> ITEM_ROW_MAPPER
            = (rs,i) -> {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setPriceM(rs.getInt("price_m"));
        item.setPriceL(rs.getInt("price_l"));
        item.setImagePath(rs.getString("image_path"));
        item.setDeleted(rs.getBoolean("deleted"));

        return item;
    };


    /**
     * 商品一覧を表示する。
     *
     * @return itemList
     */
    public List<Item> findAll(){
        String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items ORDER BY price_m";

        List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);

        return itemList;
    }


    /**
     *
     * 検索された商品を表示する。
     * @param name
     * @return itemList
     */
    public List<Item> findByNameLike(String name){
        String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items WHERE name LIKE :name ORDER BY price_m";

        SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+name+"%");

        List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);

        return itemList;
    }

    /**
     *
     * 主キーでitemを検索するメソッド
     *
     * @param id 検索条件の主キー
     * @return 検索結果のitemのlist
     */


    public Item load(Long id) {
        String sql = "SELECT * FROM items WHERE id = :id";

        SqlParameterSource param = new MapSqlParameterSource().addValue("id" ,id);

        Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);

        return item;

    }


}
