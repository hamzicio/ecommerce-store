package ecommerce.api.repository;

import ecommerce.api.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    // Some category
    List<ProductCategory> findByDeletedFalseAndCategoryIdInOrderByCategoryIdAsc(List<Integer> categoryTypes);
    // All category
    List<ProductCategory> findByDeletedFalseOrderByCategoryName();
    // One category
    ProductCategory findByCategoryId(Integer categoryType);

    Optional<ProductCategory> findByCategoryName(String productCategory);

    Page<ProductCategory> findAllByDeletedFalse(Pageable page);

}
