package com.repository;

import com.domain.Item;
import com.domain.ItemPaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemJpaRepository extends JpaRepository<ItemPaging,Integer> {
    Page<ItemPaging> findAllByOrderByPriceM(Pageable pageable);
}
