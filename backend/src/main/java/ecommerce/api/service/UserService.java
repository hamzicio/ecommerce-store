package ecommerce.api.service;


import ecommerce.api.entity.ProductCategory;
import ecommerce.api.entity.User;
import ecommerce.api.form.Statistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;


public interface UserService {
    User findOne(String email);

    Page<User> findAllPageable(Pageable pageable);


    Statistics getStats();

    Collection<User> findByRole(String role);

    User save(User user);

    User update(User user);
}
