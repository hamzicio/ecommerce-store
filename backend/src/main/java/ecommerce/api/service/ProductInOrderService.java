package ecommerce.api.service;

import ecommerce.api.entity.CartItem;
import ecommerce.api.entity.User;


public interface ProductInOrderService {
    void update(String itemId, Integer quantity, User user);
    CartItem findOne(String itemId, User user);
}
