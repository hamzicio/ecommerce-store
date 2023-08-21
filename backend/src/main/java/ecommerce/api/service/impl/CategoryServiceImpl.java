package ecommerce.api.service.impl;


import ecommerce.api.repository.ProductCategoryRepository;
import ecommerce.api.entity.ProductCategory;
import ecommerce.api.enums.ResultEnum;
import ecommerce.api.exception.EcommerceException;
import ecommerce.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    ProductCategoryRepository productCategoryRepository;



    @Override
    public List<ProductCategory> findAll() {
        List<ProductCategory> res = productCategoryRepository.findByDeletedFalseOrderByCategoryName();
      //  res.sort(Comparator.comparing(ProductCategory::getCategoryType));
        return res;
    }

    @Override
    public Page<ProductCategory> findAllPageable(Pageable pageable) {
        return productCategoryRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public ProductCategory addNewCategory(ProductCategory productCategory) {
        Optional<ProductCategory> productCategoryExists = productCategoryRepository.findByCategoryName(productCategory.getCategoryName());

        if(productCategoryExists.isPresent()){
            throw new EcommerceException(ResultEnum.CATEGORY_EXISTS);
        }
        productCategory.setDeleted(false);

        return productCategoryRepository.save(productCategory);
    }

    @Override
    public ProductCategory findByCategoryType(Integer categoryType) {
        System.out.println(categoryType);
        ProductCategory res = productCategoryRepository.findByCategoryId(categoryType);
        if(res == null) throw new EcommerceException(ResultEnum.CATEGORY_NOT_FOUND);
        return res;
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        List<ProductCategory> res = productCategoryRepository.findByDeletedFalseAndCategoryIdInOrderByCategoryIdAsc(categoryTypeList);
       //res.sort(Comparator.comparing(ProductCategory::getCategoryType));
        return res;
    }

    @Override
    @Transactional
    public ProductCategory save(ProductCategory productCategory) {
        productCategory.setDeleted(false);
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public void delete(Integer id) {
        ProductCategory productCategory= productCategoryRepository.findByCategoryId(id);
        if(productCategory == null) throw new EcommerceException(ResultEnum.CATEGORY_NOT_FOUND);

        productCategory.setDeleted(true);
        productCategoryRepository.save(productCategory);

    }


}
