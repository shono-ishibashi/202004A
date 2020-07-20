package com.service;

import com.domain.Item;
import com.domain.ItemPaging;
import com.domain.Topping;
import com.repository.ItemJpaRepository;
import com.repository.ItemRepository;
import com.repository.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemJpaRepository itemJpaRepository;

    @Autowired
    private ToppingRepository toppingRepository;

    public Page<ItemPaging> findAllPage(Pageable pageable){
        return itemJpaRepository.findAllByOrderByPriceM(pageable);
    }

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    public List<Item> findByItem(String name){
        return itemRepository.findByNameLike(name);
    }


    public Item load(Long id){
        return itemRepository.load(id);
    }

    public List<Topping> toppingFindAll(){
        return toppingRepository.findAll();
    }

    public List<Item> findByGenre(Integer genre){
        if(genre == 0){
            return itemRepository.findAll();
        }else {
            return itemRepository.findByGenre(genre);
        }
    }
}
