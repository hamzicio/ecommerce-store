package ecommerce.api.service;


import ecommerce.api.entity.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface OrderService {
    Page<CustomerOrder> findAll(Pageable pageable);

    Page<CustomerOrder> findByStatus(Integer status, Pageable pageable);

    Page<CustomerOrder> findByBuyerEmail(String email, Pageable pageable);

    Page<CustomerOrder> findByBuyerPhone(String phone, Pageable pageable);

    CustomerOrder findOne(Long orderId);


    CustomerOrder finish(Long orderId);

    CustomerOrder cancel(Long orderId);

}
