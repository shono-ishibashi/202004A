package com.repository;

import com.domain.Item;
import com.domain.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public class ReviewPointRepository {
    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<OrderItem> ORDER_ITEM_ROW_MAPPER
            = (rs,i) -> {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getInt("id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setReviewPoint(rs.getInt("point"));
        orderItem.setItemId(rs.getInt("item_id"));

        return orderItem;
    };

    private static final RowMapper<Item> ITEM_ROW_MAPPER
            = (rs,i) -> {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setReviewPoint(rs.getDouble("point"));
        item.setName(rs.getString("name"));
        return item;
    };


    public void postReviewPointForOrderItem(Integer orderItemId, Integer point){
        String updateSql = "UPDATE order_items SET point = :point WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("point", point)
                                                               .addValue("id", orderItemId);
        template.update(updateSql,param);
    }

    public List<OrderItem> getOrderItemByItemId(Integer itemId){
        String sql = "SELECT id, item_id, order_id, point from order_items WHERE item_id = :itemId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);
        List<OrderItem> orderItemList = template.query(sql, param, ORDER_ITEM_ROW_MAPPER);
        return  orderItemList;
    }

    public Item getItem(Integer orderItemId){
        String sql = "SELECT i.id, i.point, i.name FROM items AS i " +
                     "JOIN order_items AS oi ON i.id = oi.item_id " +
                     "WHERE oi.id = :orderItemId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("orderItemId", orderItemId);
        Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);
        return item;
    }

    public void postReviewPointForItem(Item item){
        String sql = "UPDATE items SET point = :point, review_counts = :reviewCounts WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("point", item.getReviewPoint())
                                                              .addValue("reviewCounts", item.getReviewCounts())
                                                               .addValue("id", item.getId());
        template.update(sql, param);
    }
}
