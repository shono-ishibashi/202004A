package com.service;

import com.domain.Item;
import com.domain.Topping;
import com.repository.ItemRepository;
import com.repository.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ToppingRepository toppingRepository;

    /**
     * 商品一覧を表示する。
     * @return
     */
    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    /**
     *
     *商品の値段の安い順に表示する。
     * @return
     */
    public List<Item> findAllByCheapPric(){
        return itemRepository.findAllByCheapPrice();
    }

    /**
     * 商品の値段お高い順で表示する。
     * @return
     */
    public List<Item> findAllByExpensivePrice(){
        return itemRepository.findAllByExpensivePrice();
    }

    /**
     * 商品を人気順で表示する。
     * @return
     */
    public List<Item> findAllByPopularItem(){
        return itemRepository.findAllByPopularItem();
    }

    /**
     * 商品の曖昧検索をする。
     * @param name
     * @return
     */
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




    public StringBuilder getNoodleAutoCompleteList(List<Item> itemList){
        StringBuilder noodleAutoCompleteList = new StringBuilder();
        for (int i = 0; i < itemList.size(); i++) {
            if (i != 0) {
                noodleAutoCompleteList.append(",");
            }
            Item item = itemList.get(i);
            noodleAutoCompleteList.append("\"");
            noodleAutoCompleteList.append(item.getName());
            noodleAutoCompleteList.append("\"");
        }


        return noodleAutoCompleteList;
    }



}
