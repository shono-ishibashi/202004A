package com.controller;

import com.domain.Order;
import com.domain.OrderItem;
import com.domain.OrderTopping;
import com.form.CartAddForm;
import com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.UUID;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/noodle/cart")
public class AddCartController {

    @Autowired
    private CartService cartService;

    @Autowired
    CartController cartController;

    @Autowired
    private HttpSession session;


    /**
     *
     * 新しくカートを作成するメソッド
     *
     * @param cartAddForm 入力フォームで入力したデータ
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public String cartAdd(CartAddForm cartAddForm, Model model) throws Exception {

    Integer userId = (Integer) session.getAttribute("userId");

    if(isNull(userId)){
        userId = UUID.randomUUID().hashCode();
        session.setAttribute("userId", userId);
    }else{
        session.setAttribute("userId", userId);
    }


    Order cart = cartService.cart(userId);

    OrderItem orderItem = new OrderItem();

    //ここで、orderItemテーブルに新しく挿入して、generatedIdより、orderToppingをinsertする。

    orderItem.setItemId(cartAddForm.getId());
    orderItem.setOrderId(cart.getId());
    orderItem.setQuantity(cartAddForm.getQuantity());
    orderItem.setSize(cartAddForm.getSize().charAt(0));

    Integer orderItemId = cartService.addCartItem(orderItem);

    for(Integer toppingId : cartAddForm.getToppingList()){
        OrderTopping orderTopping = new OrderTopping();
        orderTopping.setToppingId(toppingId);
        orderTopping.setOrderItemId(orderItemId);
        cartService.addCartTopping(orderTopping);
    }

    return cartController.showCart(model);
    }
}
