package com.service;

import com.domain.Item;
import com.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    public List<Item> findByItem(String name){
        return itemRepository.findByItem(name);
    }
}
