package ecommerce.api.service;


import ecommerce.api.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {

    ProductInfo findOne(Long productId);

    // All selling products
    Page<ProductInfo> findUpAll(Pageable pageable);
    // All products
    Page<ProductInfo> findAll(Pageable pageable);
    // All products in a category
    Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable);

    // increase stock
    void increaseStock(Long productId, int amount);

    //decrease stock
    void decreaseStock(Long productId, int amount);

    ProductInfo offSale(Long productId);

    ProductInfo onSale(Long productId);

    ProductInfo update(ProductInfo productInfo);
    ProductInfo save(ProductInfo productInfo);

    void delete(Long productId);


}
