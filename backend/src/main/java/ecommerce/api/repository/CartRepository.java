package ecommerce.api.repository;

import ecommerce.api.entity.Cart;
import ecommerce.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser(User user);
}
