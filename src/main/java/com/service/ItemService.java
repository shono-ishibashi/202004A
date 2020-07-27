package com.service;

import com.domain.Item;
import com.domain.ItemPaging;
import com.domain.Topping;
import com.repository.ItemJpaRepository;
import com.repository.ItemRepository;
import com.repository.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    /**
     * 商品一覧を表示する。
     * @return
     */

    public List<Item> findAll(){
        return itemRepository.findAll();
    }


    /**
     * 商品の値段お高い順で表示する。
     * @return
     */
    public Page<ItemPaging> findAllByPriceDesc(Pageable pageable){
        return itemJpaRepository.findAllByOrderByPriceMDesc(pageable);
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
    public Page<ItemPaging> findByName(String name, Integer sortNum,Pageable pageable) throws Exception {

        if(sortNum == 1){
            return itemJpaRepository.findByNameContainingOrderByPriceM(name,pageable);
        }else if(sortNum == 2){
            return itemJpaRepository.findByNameContainingOrderByPriceMDesc(name,pageable);
        }else if(sortNum == 3){
            return itemJpaRepository.findAllByOrderByPriceM(pageable);
        }else {
            throw new Exception();
        }
    }


    public Item load(Long id){
        return itemRepository.load(id);
    }

    public List<Topping> toppingFindAll(){
        return toppingRepository.findAll();
    }

    public Page<ItemPaging> findByGenre(Integer genre,Pageable pageable){
        if(genre == 0){
            return itemJpaRepository.findAllByOrderByPriceM(pageable);
        }else {
            return itemJpaRepository.findByGenreOrderByPriceM(genre,pageable);
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


        System.out.println(noodleAutoCompleteList);
        return noodleAutoCompleteList;
    }
}
