package ecommerce.api.service;

import ecommerce.api.entity.Cart;
import ecommerce.api.entity.CartItem;
import ecommerce.api.entity.User;

import java.util.Collection;


public interface CartService {
    Cart getCart(User user);

    void mergeLocalCart(Collection<CartItem> cartItems, User user);

    void delete(String itemId, User user);

    void checkout(User user);
}
