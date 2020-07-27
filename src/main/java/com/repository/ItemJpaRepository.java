package com.repository;

import com.domain.ItemPaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemJpaRepository extends JpaRepository<ItemPaging,Integer> {
    Page<ItemPaging> findAllByOrderByPriceM(Pageable pageable);


    Page<ItemPaging> findByGenreOrderByPriceM(Integer genre,Pageable pageable);
    Page<ItemPaging> findByGenreOrderByPriceMDesc(Integer genre,Pageable pageable);


    Page<ItemPaging> findByNameContainingOrderByPriceM(String name, Pageable pageable);
    Page<ItemPaging> findByNameContainingOrderByPriceMDesc(String name, Pageable pageable);

    Page<ItemPaging> findAllByOrderByPriceMDesc(Pageable pageable);

    //Page<ItemPaging> findAllByOrderByPointDesc(Pageable pageable);
}
