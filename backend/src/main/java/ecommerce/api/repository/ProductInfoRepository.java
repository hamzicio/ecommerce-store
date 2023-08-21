package ecommerce.api.repository;

import ecommerce.api.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
    ProductInfo findByProductId(Long id);
    // onsale product
    Page<ProductInfo> findAllByProductStatusAndDeletedFalseOrderByProductIdAsc(Integer productStatus, Pageable pageable);

    // product in one category
    Page<ProductInfo> findAllByProductCategory_CategoryIdAndDeletedFalseOrderByProductIdAsc(Integer categoryType, Pageable pageable);

    Page<ProductInfo> findAllByAndDeletedFalseOrderByProductId(Pageable pageable);

}
