package com.service;

import com.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;




}
