package ecommerce.api.service;

import ecommerce.api.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CategoryService {

    List<ProductCategory> findAll();

    Page<ProductCategory> findAllPageable(Pageable pageable);

    ProductCategory addNewCategory(ProductCategory productCategory);

    ProductCategory findByCategoryType(Integer categoryType);

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);

    void delete(Integer id);


}
