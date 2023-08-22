package ecommerce.api.service.impl;


import ecommerce.api.entity.CartItem;
import ecommerce.api.entity.User;
import ecommerce.api.exception.EcommerceException;
import ecommerce.api.repository.OrderRepository;
import ecommerce.api.repository.CartItemRepository;
import ecommerce.api.repository.ProductInfoRepository;
import ecommerce.api.repository.UserRepository;
import ecommerce.api.service.ProductService;
import ecommerce.api.entity.CustomerOrder;
import ecommerce.api.entity.ProductInfo;
import ecommerce.api.enums.OrderStatusEnum;
import ecommerce.api.enums.ResultEnum;
import ecommerce.api.service.OrderService;
import ecommerce.api.utility.MailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductInfoRepository productInfoRepository;
    @Autowired
    ProductService productService;
    @Autowired
    CartItemRepository cartItemRepository;

    @Override
    public Page<CustomerOrder> findAll(Pageable pageable) {
        return orderRepository.findAllByOrderByOrderStatusAscCreateTimeDesc(pageable);
    }

    @Override
    public Page<CustomerOrder> findByStatus(Integer status, Pageable pageable) {
        return orderRepository.findAllByOrderStatusOrderByCreateTimeDesc(status, pageable);
    }

    @Override
    public Page<CustomerOrder> findByBuyerEmail(String email, Pageable pageable) {
        return orderRepository.findAllByBuyerEmailOrderByOrderStatusAscCreateTimeDesc(email, pageable);
    }

    @Override
    public Page<CustomerOrder> findByBuyerPhone(String phone, Pageable pageable) {
        return orderRepository.findAllByBuyerPhoneOrderByOrderStatusAscCreateTimeDesc(phone, pageable);
    }

    @Override
    public CustomerOrder findOne(Long orderId) {
        CustomerOrder customerOrder = orderRepository.findByOrderId(orderId);
        if(customerOrder == null) {
            throw new EcommerceException(ResultEnum.ORDER_NOT_FOUND);
        }
        return customerOrder;
    }

    @Override
    @Transactional
    public CustomerOrder finish(Long orderId) {
        CustomerOrder customerOrder = findOne(orderId);
        if(!customerOrder.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new EcommerceException(ResultEnum.ORDER_STATUS_ERROR);
        }

        customerOrder.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderRepository.save(customerOrder);
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional
    public CustomerOrder cancel(Long orderId) {
        CustomerOrder customerOrder = findOne(orderId);
        if(!customerOrder.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new EcommerceException(ResultEnum.ORDER_STATUS_ERROR);
        }

        customerOrder.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderRepository.save(customerOrder);
        User user= userRepository.findByEmail(customerOrder.getBuyerEmail());
        try {
            mailSender.send(mailConstructor.constructOrderCancelEmail(user, customerOrder, Locale.ENGLISH));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        // Restore Stock
        Iterable<CartItem> products = customerOrder.getProducts();
        for(CartItem cartItem : products) {
            ProductInfo productInfo = productInfoRepository.findByProductId(cartItem.getProductId());
            if(productInfo != null) {
                productService.increaseStock(cartItem.getProductId(), cartItem.getCount());
            }
        }
        return orderRepository.findByOrderId(orderId);

    }
}
