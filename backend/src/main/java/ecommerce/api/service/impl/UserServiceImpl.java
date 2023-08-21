package ecommerce.api.service.impl;


import ecommerce.api.exception.EcommerceException;
import ecommerce.api.form.Statistics;
import ecommerce.api.repository.*;
import ecommerce.api.service.UserService;
import ecommerce.api.entity.Cart;
import ecommerce.api.entity.User;
import ecommerce.api.enums.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Service
@DependsOn("passwordEncoder")
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductInfoRepository productInfoRepository;
    @Autowired
    ProductCategoryRepository productCategoryRepository;
    @Autowired
    OrderRepository orderRepository;

    @Override
    public User findOne(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<User> findAllPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Statistics getStats() {
        Statistics statistics= new Statistics();
        statistics.setUsers(userRepository.count());
        statistics.setOrders(orderRepository.count());
        statistics.setProducts(productInfoRepository.count());
        statistics.setCategories(productCategoryRepository.count());
        return statistics;
    }

    @Override
    public Collection<User> findByRole(String role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    @Transactional
    public User save(User user) {
        //register
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User savedUser = userRepository.save(user);
            // initial Cart
            Cart savedCart = cartRepository.save(new Cart(savedUser));
            return savedUser;

        } catch (Exception e) {
            throw new EcommerceException(ResultEnum.VALID_ERROR);
        }

    }

    @Override
    @Transactional
    public User update(User user) {
        User oldUser = userRepository.findByEmail(user.getEmail());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        oldUser.setName(user.getName());
        oldUser.setPhone(user.getPhone());
        oldUser.setAddress(user.getAddress());
        return userRepository.save(oldUser);
    }

}
