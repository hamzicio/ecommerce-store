package ecommerce.api.service.impl;

import ecommerce.api.entity.Cart;
import ecommerce.api.repository.CartRepository;
import ecommerce.api.repository.CartItemRepository;
import ecommerce.api.entity.CartItem;
import ecommerce.api.entity.User;
import ecommerce.api.service.ProductInOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class ProductInOrderServiceImpl implements ProductInOrderService {

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Override
    @Transactional
    public void update(String itemId, Integer quantity, User user) {
        Optional<Cart> cart= cartRepository.findByUser(user);
        if(cart.isPresent()) {
            var op = cart.get().getCartItems().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
            op.ifPresent(productInOrder -> {
                productInOrder.setCount(quantity);
                cartItemRepository.save(productInOrder);
            });
        }
    }

    @Override
    public CartItem findOne(String itemId, User user) {
        Optional<Cart> cart= cartRepository.findByUser(user);
        AtomicReference<CartItem> res = new AtomicReference<>();
        if(cart.isPresent()) {
            var op = cart.get().getCartItems().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
            op.ifPresent(res::set);
        }
            return res.get();
    }
}
