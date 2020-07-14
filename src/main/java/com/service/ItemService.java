package com.service;

import com.domain.Item;
import com.domain.Topping;
import com.repository.ItemRepository;
import com.repository.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ToppingRepository toppingRepository;


    public Item load (Long id) {
        return itemRepository.load(id);
    }

    public List<Topping> toppingFindAll(){
        return toppingRepository.findAll();
    }

}
