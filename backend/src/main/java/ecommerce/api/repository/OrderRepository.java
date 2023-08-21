package ecommerce.api.repository;


import ecommerce.api.entity.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {
    CustomerOrder findByOrderId(Long orderId);


    Page<CustomerOrder> findAllByOrderStatusOrderByCreateTimeDesc(Integer orderStatus, Pageable pageable);


    Page<CustomerOrder> findAllByBuyerEmailOrderByOrderStatusAscCreateTimeDesc(String buyerEmail, Pageable pageable);

    Page<CustomerOrder> findAllByOrderByOrderStatusAscCreateTimeDesc(Pageable pageable);

    Page<CustomerOrder> findAllByBuyerPhoneOrderByOrderStatusAscCreateTimeDesc(String buyerPhone, Pageable pageable);
}
