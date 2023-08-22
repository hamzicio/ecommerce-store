package ecommerce.api.service.impl;


import ecommerce.api.entity.CartItem;
import ecommerce.api.repository.CartRepository;
import ecommerce.api.repository.OrderRepository;
import ecommerce.api.repository.CartItemRepository;
import ecommerce.api.repository.UserRepository;
import ecommerce.api.entity.Cart;
import ecommerce.api.entity.CustomerOrder;
import ecommerce.api.entity.User;
import ecommerce.api.enums.ResultEnum;
import ecommerce.api.exception.EcommerceException;
import ecommerce.api.service.CartService;
import ecommerce.api.service.ProductService;
import ecommerce.api.service.UserService;
import ecommerce.api.utility.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    MailConstructor mailConstructor;
    @Autowired
    ProductService productService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserService userService;


    @Override
    public Cart getCart(User user) {
        Optional<Cart> userCart =  cartRepository.findByUser(user);
       return userCart.get();
    }

    @Override
    public void mergeLocalCart(Collection<CartItem> cartItems, User user) {
        Optional<Cart> userCart =  cartRepository.findByUser(user);
        System.out.println(userCart.get().getCartItems().size());
        if (userCart.isPresent()) {
            Cart finalUserCart = userCart.get();
            cartItems.forEach(cartItem -> {
                Set<CartItem> set = finalUserCart.getCartItems();
                Optional<CartItem> old = set.stream().filter(e -> e.getProductId().equals(cartItem.getProductId())).findFirst();
                CartItem prod;
                if (old.isPresent()) {
                    prod = old.get();
                    prod.setCount(cartItem.getCount() + prod.getCount());
                } else {
                    prod = cartItem;
                    prod.setCart(finalUserCart);
                    finalUserCart.getCartItems().add(prod);
                }
                cartItemRepository.save(prod);
            });
            cartRepository.save(finalUserCart);
        }
        else {
            Cart newUserCart = new Cart(user);
            cartItems.forEach(cartItem -> {
                    CartItem prod = cartItem;
                    prod.setCart(newUserCart);
                newUserCart.getCartItems().add(prod);
                prod.setCart(newUserCart);
                cartItemRepository.save(prod);

            });
           cartRepository.save(newUserCart);
        }


    }

    @Override
    @Transactional
    public void delete(String itemId, User user) {
        if(itemId.equals("") || user == null) {
            throw new EcommerceException(ResultEnum.ORDER_STATUS_ERROR);
        }

        Optional<Cart> cart= cartRepository.findByUser(user);

        if(cart.isPresent()) {

            var op = cart.get().getCartItems().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
            op.ifPresent(productInOrder -> {
                productInOrder.setCart(null);
                cartItemRepository.deleteById(productInOrder.getId());
            });
        }
    }

    @Override
    public void checkout(User user) {
        // Creat an order
        Optional<Cart> cart= cartRepository.findByUser(user);
        if(cart.isPresent()) {
            CustomerOrder order = new CustomerOrder(user);
            order.setOrderAmount(cart.get().getCartItems().stream().map(item -> item.getProductPrice().multiply(new BigDecimal(item.getCount())))
                    .reduce(BigDecimal::add)
                    .orElse(new BigDecimal(0)));
            Set<CartItem> cartItems= new HashSet<>();
            cartItems.addAll(cart.get().getCartItems());
            order.setProducts(cartItems);
            CustomerOrder order1= orderRepository.save(order);

            try {
                mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
            // clear cart's foreign key & set order's foreign key& decrease stock
            cart.get().getCartItems().forEach(cartItem -> {
                cartItem.setCart(null);
                cartItem.setCustomerOrder(order1);
                productService.decreaseStock(cartItem.getProductId(), cartItem.getCount());
                cartItemRepository.save(cartItem);
            });

        }
    }
}
